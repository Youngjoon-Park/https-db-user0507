package com.example.kiosk_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoReadyResponse {

    private String tid;

    @JsonProperty("next_redirect_mobile_url")
    private String nextRedirectMobileUrl;

    @JsonProperty("next_redirect_pc_url")
    private String nextRedirectPcUrl; // ✅ 필드로 선언

    // 👉 직접 설정하는 경우를 위한 수동 setter도 존재해야 함 (롬복 @Setter로 자동 생성됨)
}
