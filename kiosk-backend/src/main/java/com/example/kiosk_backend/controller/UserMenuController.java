package com.example.kiosk_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kiosk_backend.dto.MenuDto;
import com.example.kiosk_backend.service.UserMenuService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserMenuController {

    private final UserMenuService userMenuService;

    // ✅ 인증 없이 유저가 볼 수 있는 메뉴 리스트
    @GetMapping("/menus")
    public List<MenuDto> getMenusForKiosk() {
        return userMenuService.getAllMenus();
    }
}
