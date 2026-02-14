package com.usermanagement.service;

import com.usermanagement.exception.UserException;
import com.usermanagement.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests cho UserService
 * Covering 4 main functions: Login, Register, Change Password, Delete User
 * Using Black Box Testing và White Box Testing
 */
public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }

    @AfterEach
    public void tearDown() {
        userService.clearAllUsers();
    }

    // ============= LOGIN TESTS =============

    /**
     * TC-L001: Đăng nhập thành công với username và password hợp lệ
     */
    @Test
    public void testLoginSuccessWithValidCredentials() throws UserException {
        // Arrange
        userService.register("testuser", "password123", "test@example.com", "Test User");
        
        // Act
        User loginUser = userService.login("testuser", "password123");
        
        // Assert
        assertNotNull(loginUser);
        assertEquals("testuser", loginUser.getUsername());
        assertEquals("test@example.com", loginUser.getEmail());
    }

    /**
     * TC-L002: Đăng nhập thất bại với username không tồn tại
     */
    @Test
    public void testLoginFailsWithNonExistentUsername() {
        // Arrange
        try {
            // Act
            userService.login("nonexistent", "password123");
            
            // Assert - Không nên chạy đến đây
            fail("Should throw UserException for non-existent username");
        } catch (UserException e) {
            assertEquals("Username hoặc password không chính xác", e.getMessage());
        }
    }

    /**
     * TC-L003: Đăng nhập thất bại với password sai
     */
    @Test
    public void testLoginFailsWithWrongPassword() throws UserException {
        // Arrange
        userService.register("testuser", "password123", "test@example.com", "Test User");
        
        try {
            // Act
            userService.login("testuser", "wrongpassword");
            
            // Assert - Không nên chạy đến đây
            fail("Should throw UserException for wrong password");
        } catch (UserException e) {
            assertEquals("Username hoặc password không chính xác", e.getMessage());
        }
    }

    /**
     * TC-L004: Đăng nhập thất bại với username rỗng
     */
    @Test
    public void testLoginFailsWithEmptyUsername() {
        try {
            // Act
            userService.login("", "password123");
            
            // Assert
            fail("Should throw UserException for empty username");
        } catch (UserException e) {
            assertEquals("Username không được để trống", e.getMessage());
        }
    }

    /**
     * TC-L005: Đăng nhập thất bại với username null
     */
    @Test
    public void testLoginFailsWithNullUsername() {
        try {
            // Act
            userService.login(null, "password123");
            
            // Assert
            fail("Should throw UserException for null username");
        } catch (UserException e) {
            assertEquals("Username không được để trống", e.getMessage());
        }
    }

    /**
     * TC-L006: Đăng nhập thất bại với password rỗng
     */
    @Test
    public void testLoginFailsWithEmptyPassword() {
        try {
            // Act
            userService.login("testuser", "");
            
            // Assert
            fail("Should throw UserException for empty password");
        } catch (UserException e) {
            assertEquals("Password không được để trống", e.getMessage());
        }
    }

    /**
     * TC-L007: Đăng nhập thất bại với password null
     */
    @Test
    public void testLoginFailsWithNullPassword() {
        try {
            // Act
            userService.login("testuser", null);
            
            // Assert
            fail("Should throw UserException for null password");
        } catch (UserException e) {
            assertEquals("Password không được để trống", e.getMessage());
        }
    }

    /**
     * TC-L008: Đăng nhập thất bại với tài khoản đã bị vô hiệu hóa
     */
    @Test
    public void testLoginFailsWithInactiveAccount() throws UserException {
        // Arrange
        userService.register("testuser", "password123", "test@example.com", "Test User");
        User user = userService.findUserByUsername("testuser");
        user.setActive(false);
        
        try {
            // Act
            userService.login("testuser", "password123");
            
            // Assert
            fail("Should throw UserException for inactive account");
        } catch (UserException e) {
            assertEquals("Tài khoản đã bị vô hiệu hóa", e.getMessage());
        }
    }

    /**
     * TC-L009: Đăng nhập với username có khoảng trắng
     */
    @Test
    public void testLoginWithUsernameContainingSpaces() {
        try {
            // Act
            userService.login("test user", "password123");
            
            // Assert
            fail("Should throw UserException");
        } catch (UserException e) {
            assertNotNull(e.getMessage());
        }
    }

    // ============= REGISTER TESTS =============

    /**
     * TC-R001: Đăng ký thành công với thông tin hợp lệ
     */
    @Test
    public void testRegisterSuccessWithValidData() throws UserException {
        // Act
        User newUser = userService.register("newuser", "password123", "new@example.com", "New User");
        
        // Assert
        assertNotNull(newUser);
        assertEquals("newuser", newUser.getUsername());
        assertEquals("new@example.com", newUser.getEmail());
        assertEquals("New User", newUser.getFullName());
        assertTrue(newUser.isActive());
    }

    /**
     * TC-R002: Đăng ký thất bại với username đã tồn tại
     */
    @Test
    public void testRegisterFailsWithDuplicateUsername() throws UserException {
        // Arrange
        userService.register("existing", "password123", "existing@example.com", "Existing User");
        
        try {
            // Act
            userService.register("existing", "password456", "different@example.com", "Different User");
            
            // Assert
            fail("Should throw UserException for duplicate username");
        } catch (UserException e) {
            assertTrue(e.getMessage().contains("đã tồn tại"));
        }
    }

    /**
     * TC-R003: Đăng ký thất bại với email đã được đăng ký
     */
    @Test
    public void testRegisterFailsWithDuplicateEmail() throws UserException {
        // Arrange
        userService.register("user1", "password123", "same@example.com", "User 1");
        
        try {
            // Act
            userService.register("user2", "password123", "same@example.com", "User 2");
            
            // Assert
            fail("Should throw UserException for duplicate email");
        } catch (UserException e) {
            assertTrue(e.getMessage().contains("đã được đăng ký"));
        }
    }

    /**
     * TC-R004: Đăng ký thất bại với username rỗng
     */
    @Test
    public void testRegisterFailsWithEmptyUsername() {
        try {
            // Act
            userService.register("", "password123", "test@example.com", "Test User");
            
            // Assert
            fail("Should throw UserException for empty username");
        } catch (UserException e) {
            assertEquals("Username không được để trống", e.getMessage());
        }
    }

    /**
     * TC-R005: Đăng ký thất bại với username quá ngắn (< 3 ký tự)
     */
    @Test
    public void testRegisterFailsWithUsernameTooShort() {
        try {
            // Act
            userService.register("ab", "password123", "test@example.com", "Test User");
            
            // Assert
            fail("Should throw UserException for username too short");
        } catch (UserException e) {
            assertTrue(e.getMessage().contains("độ dài từ 3 đến 20 ký tự"));
        }
    }

    /**
     * TC-R006: Đăng ký thất bại với username quá dài (> 20 ký tự)
     */
    @Test
    public void testRegisterFailsWithUsernameTooLong() {
        try {
            // Act
            userService.register("abcdefghijklmnopqrstu", "password123", 
                                "test@example.com", "Test User");
            
            // Assert
            fail("Should throw UserException for username too long");
        } catch (UserException e) {
            assertTrue(e.getMessage().contains("độ dài từ 3 đến 20 ký tự"));
        }
    }

    /**
     * TC-R007: Đăng ký thất bại với username chứa ký tự đặc biệt
     */
    @Test
    public void testRegisterFailsWithUsernameContainingSpecialChars() {
        try {
            // Act
            userService.register("user@name", "password123", "test@example.com", "Test User");
            
            // Assert
            fail("Should throw UserException for special characters");
        } catch (UserException e) {
            assertTrue(e.getMessage().contains("chỉ chứa chữ, số và underscore"));
        }
    }

    /**
     * TC-R008: Đăng ký thất bại với password rỗng
     */
    @Test
    public void testRegisterFailsWithEmptyPassword() {
        try {
            // Act
            userService.register("validuser", "", "test@example.com", "Test User");
            
            // Assert
            fail("Should throw UserException for empty password");
        } catch (UserException e) {
            assertEquals("Password không được để trống", e.getMessage());
        }
    }

    /**
     * TC-R009: Đăng ký thất bại với password quá ngắn (< 6 ký tự)
     */
    @Test
    public void testRegisterFailsWithPasswordTooShort() {
        try {
            // Act
            userService.register("validuser", "pass", "test@example.com", "Test User");
            
            // Assert
            fail("Should throw UserException for password too short");
        } catch (UserException e) {
            assertTrue(e.getMessage().contains("tối thiểu 6 ký tự"));
        }
    }

    /**
     * TC-R010: Đăng ký thất bại với email rỗng
     */
    @Test
    public void testRegisterFailsWithEmptyEmail() {
        try {
            // Act
            userService.register("validuser", "password123", "", "Test User");
            
            // Assert
            fail("Should throw UserException for empty email");
        } catch (UserException e) {
            assertEquals("Email không được để trống", e.getMessage());
        }
    }

    /**
     * TC-R011: Đăng ký thất bại với email không hợp lệ
     */
    @Test
    public void testRegisterFailsWithInvalidEmail() {
        try {
            // Act
            userService.register("validuser", "password123", "invalidemail", "Test User");
            
            // Assert
            fail("Should throw UserException for invalid email");
        } catch (UserException e) {
            assertEquals("Email không hợp lệ", e.getMessage());
        }
    }

    /**
     * TC-R012: Đăng ký thất bại với email không có @
     */
    @Test
    public void testRegisterFailsWithEmailMissingAtSymbol() {
        try {
            // Act
            userService.register("validuser", "password123", "invalidemail.com", "Test User");
            
            // Assert
            fail("Should throw UserException for invalid email");
        } catch (UserException e) {
            assertEquals("Email không hợp lệ", e.getMessage());
        }
    }

    /**
     * TC-R013: Đăng ký thành công với fullName null
     */
    @Test
    public void testRegisterSuccessWithNullFullName() throws UserException {
        // Act
        User newUser = userService.register("validuser", "password123", "test@example.com", null);
        
        // Assert
        assertNotNull(newUser);
        assertEquals("validuser", newUser.getUsername());
    }

    /**
     * TC-R014: Đăng ký thất bại với fullName quá dài
     */
    @Test
    public void testRegisterFailsWithFullNameTooLong() {
        try {
            // Act
            String longName = "a".repeat(101); // 101 characters
            userService.register("validuser", "password123", "test@example.com", longName);
            
            // Assert
            fail("Should throw UserException for fullName too long");
        } catch (UserException e) {
            assertTrue(e.getMessage().contains("không vượt quá 100 ký tự"));
        }
    }

    /**
     * TC-R015: Username hợp lệ ở độ dài ranh giới (3 ký tự)
     */
    @Test
    public void testRegisterSuccessWithMinLengthUsername() throws UserException {
        // Act
        User newUser = userService.register("abc", "password123", "test@example.com", "Test User");
        
        // Assert
        assertNotNull(newUser);
        assertEquals("abc", newUser.getUsername());
    }

    /**
     * TC-R016: Username hợp lệ ở độ dài ranh giới (20 ký tự)
     */
    @Test
    public void testRegisterSuccessWithMaxLengthUsername() throws UserException {
        // Act
        User newUser = userService.register("abcdefghij1234567890", "password123", 
                                           "test@example.com", "Test User");
        
        // Assert
        assertNotNull(newUser);
        assertEquals("abcdefghij1234567890", newUser.getUsername());
    }

    /**
     * TC-R017: Password hợp lệ ở độ dài ranh giới (6 ký tự)
     */
    @Test
    public void testRegisterSuccessWithMinLengthPassword() throws UserException {
        // Act
        User newUser = userService.register("validuser", "pass12", "test@example.com", "Test User");
        
        // Assert
        assertNotNull(newUser);
        assertEquals("validuser", newUser.getUsername());
    }

    // ============= CHANGE PASSWORD TESTS =============
    
    /**
     * ===== PHÂN TÍCH CHỨC NĂNG CHANGE PASSWORD =====
     * 
     * PHƯƠNG THỨC: changePassword(int userId, String oldPassword, String newPassword)
     * 
     * LỐC LOGIC:
     * 1. Kiểm tra user tồn tại? -> Nếu không -> Exception "Người dùng không tồn tại"
     * 2. Kiểm tra oldPassword == currentPassword? -> Nếu không -> Exception "Mật khẩu cũ không chính xác"
     * 3. Kiểm tra newPassword != null && !empty? -> Nếu không -> Exception "Password không được để trống"
     * 4. Kiểm tra newPassword.length() >= 6? -> Nếu không -> Exception "Password phải có tối thiểu 6 ký tự"
     * 5. Kiểm tra oldPassword != newPassword? -> Nếu không -> Exception "Mật khẩu mới phải khác mật khẩu cũ"
     * 6. Cập nhật password và updatedAt timestamp
     * 
     * ===== BẢNG QUYẾT ĐỊNH BLACK BOX TESTING =====
     * 
     * |TC  |UserExists|OldPwdMatch|NewPwdValid|NewPwd≠Old|OldPwdValidation|NewPwdValidation|Result   |
     * |----|----------|-----------|-----------|---------|-----------------|-----------------|---------|
     * |001 |Yes       |Yes        |Yes(≥6)    |Yes      |Pass             |Pass             |SUCCESS  |
     * |002 |Yes       |No         |Yes(≥6)    |Yes      |FAIL             |-                |FAIL     |
     * |003 |No        |-          |-          |-        |FAIL             |FAIL             |FAIL     |
     * |004 |Yes       |Yes        |No(empty)  |Yes      |Pass             |FAIL             |FAIL     |
     * |005 |Yes       |Yes        |No(<6 chr) |Yes      |Pass             |FAIL             |FAIL     |
     * |006 |Yes       |Yes        |Yes(≥6)    |No       |Pass             |Pass             |FAIL     |
     * |007 |Yes       |Yes        |Yes(≥6)    |Yes      |Pass             |Pass             |SUCCESS  |
     * |008 |Yes       |null       |Yes(≥6)    |Yes      |FAIL             |-                |FAIL     |
     * |009 |Yes       |Yes        |null       |-        |Pass             |FAIL             |FAIL     |
     * |010 |Yes       |whitespace |Yes(≥6)    |Yes      |FAIL             |-                |FAIL     |
     * |011 |Yes       |Yes        |whitespace |-        |Pass             |FAIL             |FAIL     |
     * |012 |Yes       |Yes        |Yes(=6)    |Yes      |Pass             |Pass             |SUCCESS  |
     * 
     * ===== PHÂN TÍCH WHITE BOX TESTING (Control Flow & Branch Coverage) =====
     * 
     * Nhánh 1: getUserById(userId) == null
     *   - LineĐT: if (user == null) throw UserException("Người dùng không tồn tại") 
     *   - TC: TC-CP003 (UserID 999 không tồn tại)
     * 
     * Nhánh 2: validateOldPassword() - Kiểm tra oldPassword == currentPassword
     *   - Condition: oldPassword.equals(currentPassword)
     *   - TC: TC-CP002 (oldPassword sai), TC-CP008 (oldPassword null), TC-CP010 (whitespace)
     * 
     * Nhánh 3: validatePassword(newPassword) - Kiểm tra không null/empty
     *   - Condition: password == null || password.trim().isEmpty()
     *   - TC: TC-CP004 (empty), TC-CP009 (null), TC-CP011 (whitespace)
     * 
     * Nhánh 4: validatePassword(newPassword) - Kiểm tra độ dài >= 6
     *   - Condition: password.length() < 6
     *   - TC: TC-CP005 (4 ký tự < 6), TC-CP012 (6 ký tự = boundary)
     * 
     * Nhánh 5: oldPassword.equals(newPassword)
     *   - Condition: oldPassword == newPassword
     *   - TC: TC-CP006 (mật khẩu mới giống cũ)
     * 
     * Nhánh 6: Success path - Cập nhật password
     *   - TC: TC-CP001, TC-CP007, TC-CP012
     */

    /**
     * TC-CP001: Đổi mật khẩu thành công với dữ liệu hợp lệ
     */
    @Test
    public void testChangePasswordSuccessWithValidData() throws UserException {
        // Arrange
        User user = userService.register("testuser", "oldpassword", "test@example.com", "Test User");
        
        // Act
        userService.changePassword(user.getId(), "oldpassword", "newpassword");
        
        // Assert
        User updatedUser = userService.getUserById(user.getId());
        assertEquals("newpassword", updatedUser.getPassword());
    }

    /**
     * TC-CP002: Đổi mật khẩu thất bại với mật khẩu cũ sai
     */
    @Test
    public void testChangePasswordFailsWithWrongOldPassword() throws UserException {
        // Arrange
        User user = userService.register("testuser", "oldpassword", "test@example.com", "Test User");
        
        try {
            // Act
            userService.changePassword(user.getId(), "wrongoldpassword", "newpassword");
            
            // Assert
            fail("Should throw UserException for wrong old password");
        } catch (UserException e) {
            assertEquals("Mật khẩu cũ không chính xác", e.getMessage());
        }
    }

    /**
     * TC-CP003: Đổi mật khẩu thất bại khi user không tồn tại
     */
    @Test
    public void testChangePasswordFailsWithNonExistentUser() {
        try {
            // Act
            userService.changePassword(999, "oldpassword", "newpassword");
            
            // Assert
            fail("Should throw UserException for non-existent user");
        } catch (UserException e) {
            assertEquals("Người dùng không tồn tại", e.getMessage());
        }
    }

    /**
     * TC-CP004: Đổi mật khẩu thất bại khi mật khẩu mới rỗng
     */
    @Test
    public void testChangePasswordFailsWithEmptyNewPassword() throws UserException {
        // Arrange
        User user = userService.register("testuser", "oldpassword", "test@example.com", "Test User");
        
        try {
            // Act
            userService.changePassword(user.getId(), "oldpassword", "");
            
            // Assert
            fail("Should throw UserException for empty new password");
        } catch (UserException e) {
            assertEquals("Password không được để trống", e.getMessage());
        }
    }

    /**
     * TC-CP005: Đổi mật khẩu thất bại khi mật khẩu mới quá ngắn
     */
    @Test
    public void testChangePasswordFailsWithNewPasswordTooShort() throws UserException {
        // Arrange
        User user = userService.register("testuser", "oldpassword", "test@example.com", "Test User");
        
        try {
            // Act
            userService.changePassword(user.getId(), "oldpassword", "pass");
            
            // Assert
            fail("Should throw UserException for new password too short");
        } catch (UserException e) {
            assertTrue(e.getMessage().contains("tối thiểu 6 ký tự"));
        }
    }

    /**
     * TC-CP006: Đổi mật khẩu thất bại khi mật khẩu mới giống mật khẩu cũ
     */
    @Test
    public void testChangePasswordFailsWhenNewPasswordSameAsOld() throws UserException {
        // Arrange
        User user = userService.register("testuser", "password123", "test@example.com", "Test User");
        
        try {
            // Act
            userService.changePassword(user.getId(), "password123", "password123");
            
            // Assert
            fail("Should throw UserException when new password same as old");
        } catch (UserException e) {
            assertEquals("Mật khẩu mới phải khác mật khẩu cũ", e.getMessage());
        }
    }

    /**
     * TC-CP007: Đổi mật khẩu thành công từ mật khẩu 6 ký tự lên 12 ký tự
     */
    @Test
    public void testChangePasswordNormalLength() throws UserException {
        // Arrange
        User user = userService.register("testuser", "pass12", "test@example.com", "Test User");
        
        // Act
        userService.changePassword(user.getId(), "pass12", "newpassword123");
        
        // Assert
        User updatedUser = userService.getUserById(user.getId());
        assertEquals("newpassword123", updatedUser.getPassword());
    }

    /**
     * TC-CP008: Đổi mật khẩu thất bại khi mật khẩu cũ là null
     */
    @Test
    public void testChangePasswordFailsWithNullOldPassword() throws UserException {
        // Arrange
        User user = userService.register("testuser", "oldpassword", "test@example.com", "Test User");
        
        try {
            // Act
            userService.changePassword(user.getId(), null, "newpassword");
            
            // Assert
            fail("Should throw UserException for null old password");
        } catch (UserException e) {
            assertEquals("Mật khẩu cũ không được để trống", e.getMessage());
        }
    }

    /**
     * TC-CP009: Đổi mật khẩu thất bại khi mật khẩu mới là null
     */
    @Test
    public void testChangePasswordFailsWithNullNewPassword() throws UserException {
        // Arrange
        User user = userService.register("testuser", "oldpassword", "test@example.com", "Test User");
        
        try {
            // Act
            userService.changePassword(user.getId(), "oldpassword", null);
            
            // Assert
            fail("Should throw UserException for null new password");
        } catch (UserException e) {
            assertEquals("Password không được để trống", e.getMessage());
        }
    }

    /**
     * TC-CP010: Đổi mật khẩu thất bại khi mật khẩu cũ chỉ có khoảng trắng
     */
    @Test
    public void testChangePasswordFailsWithWhitespaceOldPassword() throws UserException {
        // Arrange
        User user = userService.register("testuser", "oldpassword", "test@example.com", "Test User");
        
        try {
            // Act
            userService.changePassword(user.getId(), "   ", "newpassword");
            
            // Assert
            fail("Should throw UserException for whitespace-only old password");
        } catch (UserException e) {
            assertEquals("Mật khẩu cũ không được để trống", e.getMessage());
        }
    }

    /**
     * TC-CP011: Đổi mật khẩu thất bại khi mật khẩu mới chỉ có khoảng trắng
     */
    @Test
    public void testChangePasswordFailsWithWhitespaceNewPassword() throws UserException {
        // Arrange
        User user = userService.register("testuser", "oldpassword", "test@example.com", "Test User");
        
        try {
            // Act
            userService.changePassword(user.getId(), "oldpassword", "   ");
            
            // Assert
            fail("Should throw UserException for whitespace-only new password");
        } catch (UserException e) {
            assertEquals("Password không được để trống", e.getMessage());
        }
    }

    /**
     * TC-CP012: Đổi mật khẩu thành công với mật khẩu mới ở độ dài ranh giới (6 ký tự)
     */
    @Test
    public void testChangePasswordSuccessWithBoundaryLength() throws UserException {
        // Arrange
        User user = userService.register("testuser", "oldpassword", "test@example.com", "Test User");
        String boundaryPassword = "pass11"; // Exactly 6 characters
        
        // Act
        userService.changePassword(user.getId(), "oldpassword", boundaryPassword);
        
        // Assert
        User updatedUser = userService.getUserById(user.getId());
        assertEquals(boundaryPassword, updatedUser.getPassword());
    }

    // ============= DELETE USER TESTS =============

    /**
     * TC-DU001: Xóa người dùng thành công
     */
    @Test
    public void testDeleteUserSuccessWithValidId() throws UserException {
        // Arrange
        User user = userService.register("testuser", "password123", "test@example.com", "Test User");
        int userId = user.getId();
        
        // Verify user exists
        assertNotNull(userService.getUserById(userId));
        
        // Act
        userService.deleteUser(userId);
        
        // Assert
        assertNull(userService.getUserById(userId));
    }

    /**
     * TC-DU002: Xóa người dùng thất bại khi user không tồn tại
     */
    @Test
    public void testDeleteUserFailsWithNonExistentUser() {
        try {
            // Act
            userService.deleteUser(999);
            
            // Assert
            fail("Should throw UserException for non-existent user");
        } catch (UserException e) {
            assertTrue(e.getMessage().contains("không tồn tại"));
        }
    }

    /**
     * TC-DU003: Xóa người dùng thất bại khi xóa lần thứ hai
     */
    @Test
    public void testDeleteUserFailsWhenDeletedTwice() throws UserException {
        // Arrange
        User user = userService.register("testuser", "password123", "test@example.com", "Test User");
        int userId = user.getId();
        
        // Act & Assert - Delete first time
        userService.deleteUser(userId);
        
        // Try delete second time
        try {
            userService.deleteUser(userId);
            fail("Should throw UserException when user already deleted");
        } catch (UserException e) {
            assertTrue(e.getMessage().contains("không tồn tại"));
        }
    }

    /**
     * TC-DU004: Xóa người dùng không ảnh hưởng tới người dùng khác
     */
    @Test
    public void testDeleteUserDoesNotAffectOtherUsers() throws UserException {
        // Arrange
        User user1 = userService.register("user1", "password123", "user1@example.com", "User 1");
        User user2 = userService.register("user2", "password123", "user2@example.com", "User 2");
        int user1Id = user1.getId();
        int user2Id = user2.getId();
        
        // Act
        userService.deleteUser(user1Id);
        
        // Assert
        assertNull(userService.getUserById(user1Id));
        assertNotNull(userService.getUserById(user2Id));
        assertEquals(1, userService.getUserCount());
    }

    /**
     * TC-DU005: Xóa người dùng sau khi đăng nhập
     */
    @Test
    public void testDeleteUserAfterLogin() throws UserException {
        // Arrange
        User user = userService.register("testuser", "password123", "test@example.com", "Test User");
        int userId = user.getId();
        
        // Login
        User loginUser = userService.login("testuser", "password123");
        assertEquals(userId, loginUser.getId());
        
        // Act
        userService.deleteUser(userId);
        
        // Assert
        assertNull(userService.getUserById(userId));
    }

    // ============= INTEGRATION TESTS =============

    /**
     * TC-INT001: Luồng đầy đủ - Đăng ký -> Đăng nhập -> Đổi password -> Đăng nhập lại
     */
    @Test
    public void testCompleteUserFlow() throws UserException {
        // Arrange & Act & Assert
        
        // Step 1: Register
        User registeredUser = userService.register("testuser", "password123", 
                                                   "test@example.com", "Test User");
        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        
        // Step 2: Login with original password
        User loginUser = userService.login("testuser", "password123");
        assertNotNull(loginUser);
        assertEquals("testuser", loginUser.getUsername());
        
        // Step 3: Change password
        userService.changePassword(loginUser.getId(), "password123", "newpassword456");
        
        // Step 4: Login with old password should fail
        try {
            userService.login("testuser", "password123");
            fail("Should fail login with old password");
        } catch (UserException e) {
            assertEquals("Username hoặc password không chính xác", e.getMessage());
        }
        
        // Step 5: Login with new password should succeed
        User loginUserAfterPasswordChange = userService.login("testuser", "newpassword456");
        assertNotNull(loginUserAfterPasswordChange);
        assertEquals("testuser", loginUserAfterPasswordChange.getUsername());
    }

    /**
     * TC-INT002: Đăng ký 3 người dùng, kiểm tra số lượng
     */
    @Test
    public void testMultipleUserRegistration() throws UserException {
        // Act
        userService.register("user1", "password123", "user1@example.com", "User 1");
        userService.register("user2", "password123", "user2@example.com", "User 2");
        userService.register("user3", "password123", "user3@example.com", "User 3");
        
        // Assert
        assertEquals(3, userService.getUserCount());
        assertEquals(3, userService.getAllUsers().size());
    }

    /**
     * TC-INT003: Đăng ký từ chối username trùng nhưng email khác
     */
    @Test
    public void testRegisterRejectsDuplicateUsernameWithDifferentEmail() throws UserException {
        // Arrange
        userService.register("testuser", "password123", "test1@example.com", "Test User 1");
        
        // Act & Assert
        try {
            userService.register("testuser", "password456", "test2@example.com", "Test User 2");
            fail("Should throw UserException for duplicate username");
        } catch (UserException e) {
            assertTrue(e.getMessage().contains("đã tồn tại"));
        }
    }

    /**
     * TC-INT004: Kiểm tra username case-sensitive
     */
    @Test
    public void testUsernameIsCaseSensitive() throws UserException {
        // Arrange
        userService.register("TestUser", "password123", "test@example.com", "Test User");
        
        // Act & Assert
        try {
            userService.login("testuser", "password123");
            fail("Should fail - username should be case-sensitive");
        } catch (UserException e) {
            assertEquals("Username hoặc password không chính xác", e.getMessage());
        }
    }
}
