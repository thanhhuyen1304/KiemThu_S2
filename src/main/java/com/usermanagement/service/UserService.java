package com.usermanagement.service;

import com.usermanagement.exception.UserException;
import com.usermanagement.model.User;
import com.usermanagement.validator.UserValidator;

import java.util.*;

/**
 * UserService - Xử lý logic cho các chức năng User Management
 */
public class UserService {
    
    // Giả lập database trong memory
    private Map<Integer, User> userDatabase = new HashMap<>();
    private int nextUserId = 1;

    /**
     * Đăng ký người dùng mới
     * - Kiểm tra username chưa tồn tại
     * - Kiểm tra email chưa tồn tại
     * - Kiểm tra dữ liệu hợp lệ
     */
    public User register(String username, String password, String email, String fullName) 
            throws UserException {
        
        // Validate inputs
        UserValidator.validateUsername(username);
        UserValidator.validatePassword(password);
        UserValidator.validateEmail(email);
        
        if (fullName != null) {
            UserValidator.validateFullName(fullName);
        }

        // Kiểm tra username chưa tồn tại
        if (userExists(username)) {
            throw new UserException("Username '" + username + "' đã tồn tại");
        }

        // Kiểm tra email chưa tồn tại
        if (emailExists(email)) {
            throw new UserException("Email '" + email + "' đã được đăng ký");
        }

        // Tạo user mới
        User newUser = new User(nextUserId++, username, password, email, fullName, "", true);
        userDatabase.put(newUser.getId(), newUser);

        return newUser;
    }

    /**
     * Đăng nhập
     * - Kiểm tra username tồn tại
     * - Kiểm tra password chính xác
     * - Kiểm tra account active
     */
    public User login(String username, String password) throws UserException {
        
        if (username == null || username.trim().isEmpty()) {
            throw new UserException("Username không được để trống");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new UserException("Password không được để trống");
        }

        // Tìm user theo username
        User user = findUserByUsername(username);
        
        if (user == null) {
            throw new UserException("Username hoặc password không chính xác");
        }

        // Kiểm tra account active
        if (!user.isActive()) {
            throw new UserException("Tài khoản đã bị vô hiệu hóa");
        }

        // Kiểm tra password
        if (!user.getPassword().equals(password)) {
            throw new UserException("Username hoặc password không chính xác");
        }

        return user;
    }

    /**
     * Đổi mật khẩu
     * - Kiểm tra user tồn tại
     * - Kiểm tra mật khẩu cũ chính xác
     * - Kiểm tra mật khẩu mới hợp lệ
     */
    public void changePassword(int userId, String oldPassword, String newPassword) 
            throws UserException {
        
        User user = getUserById(userId);
        
        if (user == null) {
            throw new UserException("Người dùng không tồn tại");
        }

        // Kiểm tra mật khẩu cũ
        UserValidator.validateOldPassword(oldPassword, user.getPassword());

        // Kiểm tra mật khẩu mới hợp lệ
        UserValidator.validatePassword(newPassword);

        // Không được đặt mật khẩu mới giống cũ
        if (oldPassword.equals(newPassword)) {
            throw new UserException("Mật khẩu mới phải khác mật khẩu cũ");
        }

        // Cập nhật password
        user.setPassword(newPassword);
        user.setUpdatedAt(System.currentTimeMillis());
    }

    /**
     * Xóa người dùng
     * - Kiểm tra user ID không được null hoặc rỗng
     * - Kiểm tra user tồn tại
     * @return Thông báo thành công
     */
    public String deleteUser(Integer userId) throws UserException {
        
        if (userId == null) {
            throw new UserException("User ID không được để trống");
        }
        
        if (userId <= 0) {
            throw new UserException("User ID không hợp lệ");
        }
        
        User user = getUserById(userId);
        
        if (user == null) {
            throw new UserException("Người dùng không tồn tại. Không thể xóa");
        }

        userDatabase.remove(userId);
        return "Xóa người dùng thành công";
    }

    /**
     * Cập nhật thông tin người dùng
     */
    public void updateUser(int userId, String fullName, String phone, String email) 
            throws UserException {
        
        User user = getUserById(userId);
        
        if (user == null) {
            throw new UserException("Người dùng không tồn tại");
        }

        // Validate inputs
        if (fullName != null && !fullName.isEmpty()) {
            UserValidator.validateFullName(fullName);
            user.setFullName(fullName);
        }

        if (phone != null && !phone.isEmpty()) {
            UserValidator.validatePhone(phone);
            user.setPhone(phone);
        }

        if (email != null && !email.isEmpty()) {
            UserValidator.validateEmail(email);
            // Kiểm tra email mới chưa được dùng
            if (!email.equals(user.getEmail()) && emailExists(email)) {
                throw new UserException("Email '" + email + "' đã được đăng ký");
            }
            user.setEmail(email);
        }

        user.setUpdatedAt(System.currentTimeMillis());
    }

    /**
     * Lấy thông tin user theo ID
     */
    public User getUserById(int userId) {
        return userDatabase.get(userId);
    }

    /**
     * Lấy thông tin user theo username
     */
    public User findUserByUsername(String username) {
        for (User user : userDatabase.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Lấy tất cả users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(userDatabase.values());
    }

    /**
     * Kiểm tra username đã tồn tại
     */
    private boolean userExists(String username) {
        return findUserByUsername(username) != null;
    }

    /**
     * Kiểm tra email đã được đăng ký
     */
    private boolean emailExists(String email) {
        for (User user : userDatabase.values()) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Xóa tất cả users (dùng cho testing)
     */
    public void clearAllUsers() {
        userDatabase.clear();
        nextUserId = 1;
    }

    /**
     * Lấy số lượng users
     */
    public int getUserCount() {
        return userDatabase.size();
    }
}
