package com.example.kiosk_backend.controller;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kiosk_backend.dto.PhoneRequest;
import com.example.kiosk_backend.service.SmsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://kiosktest.shop") // ✅ 여기만 수정
public class AuthController {

    private final SmsService smsService;

    // ✅ 인증번호 임시 저장소 (전화번호 → 인증번호)
    // TODO: 실서비스에서는 Redis 등의 외부 저장소 사용 권장
    private final Map<String, String> codeStorage = new ConcurrentHashMap<>();

    // ✅ 인증번호 전송 API
    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestBody PhoneRequest request) {
        String phone = request.getPhone();
        String code = String.format("%06d", new Random().nextInt(999999));

        smsService.sendSms(phone, code);
        codeStorage.put(phone, code); // 인증번호 저장

        return ResponseEntity.ok("인증번호 문자 전송 완료");
    }

    // ✅ 인증번호 검증 API
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody PhoneRequest request) {
        String phone = request.getPhone();
        String inputCode = request.getCode();

        String savedCode = codeStorage.get(phone);

        if (savedCode != null && savedCode.equals(inputCode)) {
            return ResponseEntity.ok("✅ 인증 성공");
        } else {
            return ResponseEntity.badRequest().body("❌ 인증 실패: 잘못된 코드");
        }
    }
}
