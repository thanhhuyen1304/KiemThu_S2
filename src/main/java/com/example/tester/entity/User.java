package com.example.tester.entity;

import lombok.Data; // Lombok tự tạo getter/setter
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private String username;
    private String password;

    // --- CÁC TRƯỜNG CẦN THÊM CHO ĐỒNG BỘ ---
    private String email;
    private String fullName;
    private String phone;
    
    private boolean isActive = true; // Quan trọng: Mặc định là true (hoạt động)
    
}