package com.example.kiosk_backend.service;

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

    private final String apiKey = "EGXANOOMILTPOQ8B7AJFAEJLQNZYEW8D";
    private final String apiSecret = "NCS7CM6AWJRBUA47";
    private final String senderPhone = "01093043225"; // 발신번호 (CoolSMS에 등록된 번호)

    public void sendSms(String to, String code) {
        String messageText = "[키오스크] 인증번호는 " + code + " 입니다.";

        RestTemplate restTemplate = new RestTemplate();

        // 요청 본문 구성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("type", "sms");
        body.add("from", senderPhone);
        body.add("to", to);
        body.add("text", messageText);
        body.add("app_version", "kiosk-backend 1.0");

        // 요청 헤더 구성
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(apiKey, apiSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        // 요청 전송
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
