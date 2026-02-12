TESTER
 ├── .mvn
 ├── .vscode
 ├── src
 │   ├── main
 │   │   ├── java
 │   │   │   └── com
 │   │   │       └── example
 │   │   │           └── tester
 │   │   │               ├── config
 │   │   │               │   └── DataInitializer.java
 │   │   │               ├── controller
 │   │   │               │   └── LoginController.java
 │   │   │               ├── entity
 │   │   │               │   └── User.java
 │   │   │               ├── exception
 │   │   │               │   └── UserException.java
 │   │   │               ├── repository
 │   │   │               │   └── UserRepository.java
 │   │   │               ├── service
 │   │   │               │   └── LoginService.java
 │   │   │               ├── validator
 │   │   │               │   └── UserValidator.java
 │   │   │               └── TesterApplication.java
 │   │   └── resources
 │   │       ├── static
 │   │       ├── templates
 │   │       │   └── login.html
 │   │       └── application.properties
 │   └── test
 │       └── java
 │           └── com
 │               └── example
 │                   └── tester
 │                       ├── service
 │                       │   └── LoginServiceTest.java
 │                       └── TesterApplicationTests.java
 ├── target
 └── .gitattributes






 Bảng Danh Sách Các Mục Cần Test

 Junit test 
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
        user = new User();
        user.setUsername("userA");
        user.setPassword("123456");
        // QUAN TRỌNG: Phải set Active = true để test các case thông thường
        user.setActive(true);
    }
    
    // ... Các test case check rỗng/độ dài giữ nguyên như cũ ...

    @Test
    void testUserInactive() {
        // Case mới: Test tài khoản bị khóa
        user.setActive(false);
        when(userRepository.findByUsername("userA")).thenReturn(Optional.of(user));

        assertEquals("Tài khoản đã bị khóa",
                loginService.login("userA", "123456"));
    }

    @Test
    void testLoginSuccess() {
        when(userRepository.findByUsername("userA")).thenReturn(Optional.of(user));

        assertEquals("Đăng nhập thành công",
                loginService.login("userA", "123456"));
    }
}
