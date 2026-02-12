package com.example.tester.entity;

import jakarta.persistence.*;
import lombok.Data; // Lombok tự tạo getter/setter

@Entity
@Table(name = "users")
@Data 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    // --- CÁC TRƯỜNG CẦN THÊM CHO ĐỒNG BỘ ---
    private String email;
    private String fullName;
    private String phone;
    
    private boolean isActive = true; // Quan trọng: Mặc định là true (hoạt động)
    
    // Nếu không dùng Lombok, bạn phải tự viết getter/setter cho isActive 
    // public boolean isActive() { return isActive; }
    // public void setActive(boolean active) { this.isActive = active; }
}