package com.example.tester.service;

import com.example.tester.entity.User;
import com.example.tester.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    // Giữ nguyên trả về String như ý bạn muốn
    public String login(String username, String password) {

        if (username == null || username.trim().isEmpty())
            return "Username không được để trống";

        if (password == null || password.trim().isEmpty())
            return "Password không được để trống";

        // Logic check độ dài giữ nguyên
        if (username.length() < 3 || username.length() > 20)
            return "Báo lỗi độ dài Username";

        if (password.length() < 6 || password.length() > 20)
            return "Báo lỗi độ dài Password";

        if (password.contains(" "))
            return "Password không được chứa khoảng trắng";

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty())
            return "Tài khoản không hợp lệ";

        User user = userOptional.get();

        // --- CHỈ THÊM ĐOẠN CHECK NÀY ---
        // Kiểm tra xem tài khoản có bị khóa không (đồng bộ logic)
        if (!user.isActive()) {
            return "Tài khoản đã bị khóa";
        }
        // -------------------------------

        if (!user.getPassword().equals(password))
            return "Sai mật khẩu";

        return "Đăng nhập thành công";
    }
}