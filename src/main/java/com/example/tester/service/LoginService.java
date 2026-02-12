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

        // [1] Decision: Kiểm tra Username rỗng/null
        if (username == null || username.trim().isEmpty()) 
            return "Username không được để trống"; // [2] Return (End)

        // [3] Decision: Kiểm tra Password rỗng/null
        if (password == null || password.trim().isEmpty()) 
            return "Password không được để trống"; // [4] Return (End)

        // [5] Decision: Kiểm tra độ dài Username (<3 hoặc >20)
        if (username.length() < 3 || username.length() > 20) 
            return "Báo lỗi độ dài Username"; // [6] Return (End)

        // [7] Decision: Kiểm tra độ dài Password (<6 hoặc >20)
        if (password.length() < 6 || password.length() > 20) 
            return "Báo lỗi độ dài Password"; // [8] Return (End)

        // [9] Decision: Kiểm tra khoảng trắng trong Password
        if (password.contains(" ")) 
            return "Password không được chứa khoảng trắng"; // [10] Return (End)

        // [11] Process: Gọi Database tìm user
        Optional<User> userOptional = userRepository.findByUsername(username);

        // [12] Decision: Kiểm tra user có tồn tại không (Empty?)
        if (userOptional.isEmpty()) 
            return "Tài khoản không hợp lệ"; // [13] Return (End)

        // [14] Process: Lấy đối tượng User ra khỏi Optional
        User user = userOptional.get();

        // [15] Decision: Kiểm tra Active (Tài khoản bị khóa?)
        if (!user.isActive()) {
            return "Tài khoản đã bị khóa"; // [16] Return (End)
        }

        // [17] Decision: Kiểm tra Mật khẩu (So sánh password)
        if (!user.getPassword().equals(password)) 
            return "Sai mật khẩu"; // [18] Return (End)

        // [19] Return: Đăng nhập thành công (End Success)
        return "Đăng nhập thành công";
    }
}