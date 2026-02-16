package com.usermanagement.validator;

import com.usermanagement.exception.UserException;

import java.util.regex.Pattern;

/**
 * UserValidator - Kiểm tra tính hợp lệ của dữ liệu User
 */
public class UserValidator {
    
    // Regex patterns
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_]{3,20}$");

    private UserValidator() {
        // Private constructor để tránh instantiation
    }

    /**
     * Kiểm tra username hợp lệ
     * - Độ dài: 3-20 ký tự
     * - Chỉ chứa chữ, số, underscore
     */
    public static void validateUsername(String username) throws UserException {
        if (username == null || username.trim().isEmpty()) {
            throw new UserException("Username không được để trống");
        }

        if (username.length() < 3 || username.length() > 20) {
            throw new UserException("Username phải có độ dài từ 3 đến 20 ký tự");
        }

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new UserException("Username chỉ chứa chữ, số và underscore");
        }
    }

    /**
     * Kiểm tra password hợp lệ
     * - Độ dài: tối thiểu 6 ký tự
     * - Không được để trống
     */
    public static void validatePassword(String password) throws UserException {
        if (password == null || password.trim().isEmpty()) {
            throw new UserException("Password không được để trống");
        }

        if (password.length() < 6) {
            throw new UserException("Password phải có tối thiểu 6 ký tự");
        }
    }

    /**
     * Kiểm tra email hợp lệ
     */
    public static void validateEmail(String email) throws UserException {
        if (email == null || email.trim().isEmpty()) {
            throw new UserException("Email không được để trống");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new UserException("Email không hợp lệ");
        }
    }

    /**
     * Kiểm tra phone hợp lệ (số điện thoại Việt Nam)
     * - Độ dài: 10 chữ số
     * - Chỉ chứa chữ số
     */
    public static void validatePhone(String phone) throws UserException {
        if (phone == null || phone.trim().isEmpty()) {
            throw new UserException("Số điện thoại không được để trống");
        }

        if (!phone.matches("^[0-9]{10}$")) {
            throw new UserException("Số điện thoại phải có 10 chữ số");
        }
    }

    /**
     * Kiểm tra fullName hợp lệ
     */
    public static void validateFullName(String fullName) throws UserException {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new UserException("Tên đầy đủ không được để trống");
        }

        if (fullName.length() > 100) {
            throw new UserException("Tên đầy đủ không vượt quá 100 ký tự");
        }
    }

    /**
     * Kiểm tra xem password cũ có khớp không
     */
    public static void validateOldPassword(String oldPassword, String currentPassword) 
            throws UserException {
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            throw new UserException("Mật khẩu cũ không được để trống");
        }
        
        if (!oldPassword.equals(currentPassword)) {
            throw new UserException("Mật khẩu cũ không chính xác");
        }
    }
}
