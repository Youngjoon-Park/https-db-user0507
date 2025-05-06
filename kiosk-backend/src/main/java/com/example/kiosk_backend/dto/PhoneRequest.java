package com.example.kiosk_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneRequest {
    private String phone;
    private String code; // ✅ 인증번호 추가
}
