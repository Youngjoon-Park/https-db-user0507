// 📁 src/main/java/com/example/kiosk_backend/controller/PaymentController.java
package com.example.kiosk_backend.controller;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.kiosk_backend.dto.KakaoReadyResponse;
import com.example.kiosk_backend.dto.PaymentRequest;
import com.example.kiosk_backend.entity.Order;
import com.example.kiosk_backend.repository.OrderRepository;
import com.example.kiosk_backend.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;

    // ✅ 1. 결제 준비 요청 API
    @PostMapping("/api/payment/ready")
    public ResponseEntity<?> readyToPay(@RequestBody PaymentRequest paymentRequest) {
        try {
            if (paymentRequest == null || paymentRequest.getOrderId() == null) {
                throw new IllegalArgumentException("orderId가 요청 본문에 포함되지 않았습니다.");
            }

            Long orderId = paymentRequest.getOrderId();

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 주문 ID가 존재하지 않습니다. id=" + orderId));

            // 총 결제 금액 계산
            int totalAmount = order.getItems().stream()
                    .mapToInt(item -> item.getMenu().getPrice() * item.getQuantity())
                    .sum();

            log.info("✅ 서버에서 계산한 totalAmount: {}", totalAmount);

            // 카카오페이 결제 준비 요청
            KakaoReadyResponse response = paymentService.kakaoPayReady(orderId, totalAmount);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // ← 이 한 줄 추가
            // 전체 스택트레이스 로깅
            log.error("❌ 결제 준비 중 예외 발생", e);
            // 클라이언트에는 예외 메시지를 그대로 내려줌 (카카오 에러 본문 포함)
            return ResponseEntity
                    .status(500)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // ✅ 2. 결제 승인 요청 API
    @PostMapping("/api/payment/approve")
    public ResponseEntity<?> approvePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            String pgToken = paymentRequest.getPgToken();
            Long orderId = paymentRequest.getOrderId();

            if (pgToken == null || orderId == null) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("error", "필수 값 누락(pgToken 또는 orderId)"));
            }

            paymentService.kakaoPayApprove(pgToken, orderId);
            return ResponseEntity.ok(Collections.singletonMap("message", "결제 승인 완료"));

        } catch (Exception e) {
            log.error("❌ 결제 승인 중 예외 발생", e);
            return ResponseEntity
                    .status(500)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
