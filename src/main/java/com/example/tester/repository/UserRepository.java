package com.example.tester.repository;

import com.example.tester.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username); // Dùng để check khi đăng ký
    boolean existsByEmail(String email);       // Dùng để check email trùng
}