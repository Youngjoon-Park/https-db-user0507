package com.example.kiosk_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kiosk_backend.dto.PhoneRequest;
import com.example.kiosk_backend.service.SmsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://kiosktest.shop")
public class AuthController {

    private final SmsService smsService;

    // ✅ 인증번호 전송
    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestBody PhoneRequest request) {
        smsService.sendVerificationCode(request.getPhone());
        return ResponseEntity.ok("인증번호 문자 전송 완료");
    }

    // ✅ 인증번호 검증
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody PhoneRequest request) {
        boolean isValid = smsService.verifyCode(request.getPhone(), request.getCode());

        if (isValid) {
            return ResponseEntity.ok("✅ 인증 성공");
        } else {
            return ResponseEntity.badRequest().body("❌ 인증 실패: 잘못된 코드");
        }
    }
}
