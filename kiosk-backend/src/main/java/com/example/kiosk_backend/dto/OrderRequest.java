// 📁 src/main/java/com/example/kiosk_backend/dto/OrderRequest.java
package com.example.kiosk_backend.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private String phoneNumber; // ✅ 인증 후 저장용 (선택 사항)
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private Long menuId;
        private int quantity;
    }
}
