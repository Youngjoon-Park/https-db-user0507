package com.example.kiosk_backend.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsService {

    private final String apiKey = "";
    private final String apiSecret = "NCS7CM6AWJRBUA47";
    private final String senderPhone = "01093043225";

    // ✅ 전화번호 → 인증정보 저장
    private static class AuthInfo {
        String code;
        LocalDateTime createdAt;

        AuthInfo(String code, LocalDateTime createdAt) {
            this.code = code;
            this.createdAt = createdAt;
        }
    }

    private final Map<String, AuthInfo> codeStore = new ConcurrentHashMap<>();

    // 인증번호 발송
    public String sendVerificationCode(String phone) {
        AuthInfo existing = codeStore.get(phone);
        LocalDateTime now = LocalDateTime.now();

        if (existing != null && now.minusMinutes(1).isBefore(existing.createdAt)) {
            return "❗ 1분 이내 재요청은 제한됩니다.";
        }

        String code = generateCode();
        codeStore.put(phone, new AuthInfo(code, now));
        sendSms(phone, code);
        return "✅ 인증번호 전송 완료 (테스트용 코드: " + code + ")";
    }

    // 인증번호 확인
    public boolean verifyCode(String phone, String inputCode) {
        AuthInfo info = codeStore.get(phone);
        if (info == null)
            return false;

        if (LocalDateTime.now().isAfter(info.createdAt.plusMinutes(5))) {
            return false; // 유효시간 초과
        }

        return info.code.equals(inputCode);
    }

    // 랜덤 6자리 코드 생성
    private String generateCode() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    // SMS 전송
    private void sendSms(String to, String code) {
        String messageText = "[키오스크] 인증번호는 " + code + " 입니다.";

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("type", "sms");
        body.add("from", senderPhone);
        body.add("to", to);
        body.add("text", messageText);
        body.add("app_version", "kiosk-backend 1.0");

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey, apiSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.coolsms.co.kr/messages/v4/send",
                    entity,
                    String.class);
            System.out.println("✅ 문자 전송 결과: " + response.getBody());
        } catch (Exception e) {
            System.out.println("❌ 문자 전송 실패: " + e.getMessage());
        }
    }
}
