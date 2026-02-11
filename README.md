# User Management System - Testing Project

## Cấu trúc Project

```
Kiemthu/
├── pom.xml                                  # Maven configuration
├── README.md                                # Tài liệu này
└── src/
    ├── main/java/com/usermanagement/
    │   ├── model/
    │   │   └── User.java                   # User model class
    │   ├── service/
    │   │   └── UserService.java            # Business logic
    │   ├── validator/
    │   │   └── UserValidator.java          # Input validation
    │   └── exception/
    │       └── UserException.java          # Custom exception
    └── test/java/com/usermanagement/
        └── service/
            └── UserServiceTest.java        # Unit tests
```

## Các thành phần chính

### 1. **User Model** (`User.java`)
- Đại diện cho đối tượng người dùng
- Các trường: id, username, password, email, fullName, phone, isActive, createdAt, updatedAt
- Các methods: getters, setters, toString(), equals(), hashCode()

### 2. **UserService** (`UserService.java`)
Cung cấp các chức năng:
- **register(username, password, email, fullName)** - Đăng ký tài khoản mới
- **login(username, password)** - Đăng nhập
- **changePassword(userId, oldPassword, newPassword)** - Đổi mật khẩu
- **deleteUser(userId)** - Xóa tài khoản
- **updateUser(userId, fullName, phone, email)** - Cập nhật thông tin
- **getUserById(userId)** - Lấy thông tin user
- **getAllUsers()** - Lấy tất cả users

### 3. **UserValidator** (`UserValidator.java`)
Kiểm tra tính hợp lệ của dữ liệu:
- **validateUsername()** - Username: 3-20 ký tự, chứa chữ, số, underscore
- **validatePassword()** - Password: tối thiểu 6 ký tự
- **validateEmail()** - Email: format chuẩn
- **validatePhone()** - Phone: 10 chữ số
- **validateFullName()** - FullName: tối đa 100 ký tự
- **validateOldPassword()** - Kiểm tra mật khẩu cũ

### 4. **UserException** (`UserException.java`)
Custom exception cho hệ thống

## Dependencies

- **JUnit 4** - Viết test cases
- **JUnit 5 (Jupiter)** - Parametrized tests (tuỳ chọn)
- **Mockito** - Mocking objects cho unit tests
- **JaCoCo** - Code coverage analysis
- **SLF4J** - Logging

## Chạy Project

### Cài đặt dependencies:
```bash
mvn clean install
```

### Chạy Unit Tests:
```bash
mvn test
```

### Tạo Code Coverage Report:
```bash
mvn clean test jacoco:report
```
Report sẽ tại: `target/site/jacoco/index.html`

### Biên dịch code:
```bash
mvn compile
```

## Test Cases

### Black Box Testing (Kiểm thử chức năng)
- **Đăng ký**: test valid/invalid username, password, email
- **Đăng nhập**: test with valid/invalid credentials, inactive account
- **Đổi mật khẩu**: test correct/incorrect old password, password strength
- **Xóa user**: test delete existing/non-existing user

### White Box Testing (Kiểm thử logic)
- **Branch coverage**: Kiểm tra tất cả các nhánh if/else
- **Path coverage**: Kiểm tra các đường chạy khác nhau
- **Statement coverage**: Đảm bảo mọi statement được thực thi

## Tính năng chính

✅ Đăng ký tài khoản với validation  
✅ Đăng nhập và xác thực  
✅ Đổi mật khẩu an toàn  
✅ Xóa tài khoản  
✅ Cập nhật thông tin user  
✅ Kiểm tra xung đột username/email  
✅ In-memory database (mock data)  
✅ Comprehensive unit tests  
✅ Code coverage report  

## Notes
- Project sử dụng in-memory Map để mô phỏng database
- Không sử dụng database thực tế
- Tất cả dữ liệu giả lập cho mục đích testing
