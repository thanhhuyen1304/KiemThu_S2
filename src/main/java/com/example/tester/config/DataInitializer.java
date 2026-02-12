package com.example.tester.config;

import com.example.tester.entity.User;
import com.example.tester.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Kiểm tra nếu chưa có user admin thì tạo mới
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("123456");
                
                // --- CẬP NHẬT CÁC TRƯỜNG MỚI ---
                admin.setActive(true); // Quan trọng: Phải set true để đăng nhập được
                admin.setFullName("Administrator"); // Tùy chọn
                admin.setEmail("admin@example.com"); // Tùy chọn
                admin.setPhone("0123456789"); // Tùy chọn
                
                userRepository.save(admin);
                System.out.println(">> Đã tạo user mẫu: admin / 123456");
            }
        };
    }
}