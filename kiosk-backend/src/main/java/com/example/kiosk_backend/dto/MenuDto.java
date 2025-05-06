// 📁 src/main/java/com/example/kiosk_backend/dto/MenuDto.java
package com.example.kiosk_backend.dto;

import com.example.kiosk_backend.entity.AdminMenu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuDto {
    private Long id;
    private String name;
    private int price;
    private String image;

    // entity → dto 변환
    public static MenuDto from(AdminMenu menu) {
        return new MenuDto(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getImageFilename());
    }
}
