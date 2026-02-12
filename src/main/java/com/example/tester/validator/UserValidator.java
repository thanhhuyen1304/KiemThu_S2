package com.example.tester.validator;

import com.example.tester.exception.UserException;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class UserValidator {

    // Regex: Chữ cái, số, gạch dưới, 3-20 ký tự
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{3,20}$";
    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PHONE_PATTERN = "^\\d{10}$";

    public void validateUsername(String username) {
        if (username == null || !Pattern.matches(USERNAME_PATTERN, username)) {
            throw new UserException("Username không hợp lệ (3-20 ký tự, chữ, số, gạch dưới)");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new UserException("Password phải có tối thiểu 6 ký tự");
        }
        if (password.contains(" ")) {
            throw new UserException("Password không được chứa khoảng trắng");
        }
    }

    public void validateEmail(String email) {
        if (email == null || !Pattern.matches(EMAIL_PATTERN, email)) {
            throw new UserException("Định dạng Email không hợp lệ");
        }
    }

    public void validatePhone(String phone) {
        if (phone != null && !Pattern.matches(PHONE_PATTERN, phone)) {
            throw new UserException("Số điện thoại phải gồm 10 chữ số");
        }
    }

    public void validateFullName(String fullName) {
        if (fullName != null && fullName.length() > 100) {
            throw new UserException("Họ tên không được quá 100 ký tự");
        }
    }
}