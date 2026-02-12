package com.example.tester.service;

import com.example.tester.entity.User;
import com.example.tester.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        // Khởi tạo dữ liệu mẫu "Sạch" (Happy Path) cho mỗi lần chạy test
        user = new User();
        user.setUsername("userA");
        user.setPassword("123456"); // Mật khẩu đúng độ dài
        user.setActive(true);       // Mặc định tài khoản đang hoạt động
    }

    // ==========================================
    // NHÓM 1: KIỂM TRA VALIDATE DỮ LIỆU ĐẦU VÀO
    // ==========================================

    @Test
    void testUsernameEmpty() {
        // Case 1: Username null
        assertEquals("Username không được để trống", loginService.login(null, "123456"));
        // Case 2: Username rỗng
        assertEquals("Username không được để trống", loginService.login("", "123456"));
        // Case 3: Username chỉ có khoảng trắng (trim check)
        assertEquals("Username không được để trống", loginService.login("   ", "123456"));
    }

    @Test
    void testPasswordEmpty() {
        // Case 1: Password null
        assertEquals("Password không được để trống", loginService.login("userA", null));
        // Case 2: Password rỗng
        assertEquals("Password không được để trống", loginService.login("userA", ""));
    }

    @Test
    void testUsernameLength() {
        // Case 1: Quá ngắn (< 3 ký tự)
        assertEquals("Báo lỗi độ dài Username", loginService.login("ab", "123456"));
        // Case 2: Quá dài (> 20 ký tự)
        String longUsername = "nguyenvana12345678901"; // 21 ký tự
        assertEquals("Báo lỗi độ dài Username", loginService.login(longUsername, "123456"));
    }

    @Test
    void testPasswordLength() {
        // Case 1: Quá ngắn (< 6 ký tự)
        assertEquals("Báo lỗi độ dài Password", loginService.login("userA", "12345"));
        // Case 2: Quá dài (> 20 ký tự)
        String longPassword = "matkhauquadaivuotqua20kytu";
        assertEquals("Báo lỗi độ dài Password", loginService.login("userA", longPassword));
    }

    @Test
    void testPasswordContainsSpace() {
        // Case: Password chứa khoảng trắng
        assertEquals("Password không được chứa khoảng trắng", loginService.login("userA", "123 456"));
    }

    // ==========================================
    // NHÓM 2: KIỂM TRA LOGIC NGHIỆP VỤ & DB
    // ==========================================

    @Test
    void testUserNotFound() {
        // Giả lập Repository trả về Empty (không tìm thấy user)
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        
        assertEquals("Tài khoản không hợp lệ", loginService.login("unknown", "123456"));
    }

    @Test
    void testUserInactive() {
        // Case: Tìm thấy user nhưng tài khoản bị khóa
        user.setActive(false); // Ghi đè trạng thái active thành false
        when(userRepository.findByUsername("userA")).thenReturn(Optional.of(user));

        assertEquals("Tài khoản đã bị khóa", loginService.login("userA", "123456"));
    }

    @Test
    void testWrongPassword() {
        // Case: Tìm thấy user, active = true, nhưng sai pass
        when(userRepository.findByUsername("userA")).thenReturn(Optional.of(user));

        // Pass trong DB là "123456" (do setup), truyền vào "wrongPass"
        assertEquals("Sai mật khẩu", loginService.login("userA", "wrongPass"));
    }

    // ==========================================
    // NHÓM 3: HAPPY PATH (THÀNH CÔNG)
    // ==========================================

    @Test
    void testLoginSuccess() {
        // Case: Mọi thứ đều đúng
        when(userRepository.findByUsername("userA")).thenReturn(Optional.of(user));

        assertEquals("Đăng nhập thành công", loginService.login("userA", "123456"));
    }
}