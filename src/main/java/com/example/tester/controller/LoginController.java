package com.example.tester.controller;

import com.example.tester.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    // 1. Hiển thị trang login khi vào trang chủ
    @GetMapping("/")
    public String showLoginForm() {
        return "login"; // Trả về file login.html
    }

    // 2. Xử lý khi bấm nút Đăng nhập
    @PostMapping("/login")
    public String processLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model) {

        // Gọi service kiểm tra
        String resultMessage = loginService.login(username, password);

        // Đẩy kết quả ra giao diện
        model.addAttribute("message", resultMessage);
        
        // Giữ lại username để người dùng không phải nhập lại nếu sai
        model.addAttribute("username", username);

        return "login"; // Trả về lại trang login kèm thông báo
    }
}