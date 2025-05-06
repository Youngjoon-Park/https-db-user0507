// 📁 src/main/java/com/example/kiosk_backend/service/UserMenuService.java
package com.example.kiosk_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.kiosk_backend.dto.MenuDto;
import com.example.kiosk_backend.entity.AdminMenu;
import com.example.kiosk_backend.repository.AdminMenuRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserMenuService {

    private final AdminMenuRepository adminMenuRepository;

    public List<MenuDto> getAllMenus() {
        List<AdminMenu> menus = adminMenuRepository.findAll();
        return menus.stream()
                .map(MenuDto::from)
                .collect(Collectors.toList());
    }
}
