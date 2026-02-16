# Change Password - Phân Tích & JUnit Test

## 👤 Thành viên nhóm
**Nhóm 3 – Change Password (Long-Kỳ)**

---

## 📋 Mục tiêu
Viết testcase Black box, bảng quyết định và JUnit test cho chức năng **Change Password** (12 testcase)

---

## 1️⃣ PHƯƠNG THỨC CẦN TEST

```java
public void changePassword(int userId, String oldPassword, String newPassword) 
        throws UserException
```

### Logic xử lý:
1. Kiểm tra user tồn tại?
2. Kiểm tra mật khẩu cũ có khớp không?
3. Kiểm tra mật khẩu mới có hợp lệ không (không null, không rỗng, >= 6 ký tự)?
4. Kiểm tra mật khẩu mới khác mật khẩu cũ?
5. Cập nhật password nếu tất cả điều kiện đúng

---

## 2️⃣ BẢNG QUYẾT ĐỊNH BLACK BOX TESTING

| TC | User Exists | Old Pwd Match | New Pwd Valid | New≠Old | oldPassword Validation | newPassword Validation | **Expected Result** |
|----|---|---|---|---|---|---|---|
| **001** | ✅ Yes | ✅ Yes | ✅ Yes (≥6) | ✅ Yes | Pass | Pass | **✅ SUCCESS** |
| **002** | ✅ Yes | ❌ No | ✅ Yes (≥6) | ✅ Yes | ❌ FAIL | — | **❌ FAIL** |
| **003** | ❌ No | — | — | — | — | — | **❌ FAIL** |
| **004** | ✅ Yes | ✅ Yes | ❌ No (empty) | ✅ Yes | Pass | ❌ FAIL | **❌ FAIL** |
| **005** | ✅ Yes | ✅ Yes | ❌ No (<6 chr) | ✅ Yes | Pass | ❌ FAIL | **❌ FAIL** |
| **006** | ✅ Yes | ✅ Yes | ✅ Yes (≥6) | ❌ No | Pass | Pass | **❌ FAIL** |
| **007** | ✅ Yes | ✅ Yes | ✅ Yes (≥6) | ✅ Yes | Pass | Pass | **✅ SUCCESS** |
| **008** | ✅ Yes | ❌ null | ✅ Yes (≥6) | ✅ Yes | ❌ FAIL | — | **❌ FAIL** |
| **009** | ✅ Yes | ✅ Yes | ❌ null | — | Pass | ❌ FAIL | **❌ FAIL** |
| **010** | ✅ Yes | ❌ whitespace | ✅ Yes (≥6) | ✅ Yes | ❌ FAIL | — | **❌ FAIL** |
| **011** | ✅ Yes | ✅ Yes | ❌ whitespace | — | Pass | ❌ FAIL | **❌ FAIL** |
| **012** | ✅ Yes | ✅ Yes | ✅ Yes (=6) | ✅ Yes | Pass | Pass | **✅ SUCCESS** |

---

## 3️⃣ PHÂN TÍCH WHITE BOX TESTING

### Control Flow & Branch Coverage Analysis

#### **Nhánh 1: User không tồn tại**
```java
User user = getUserById(userId);
if (user == null) {
    throw new UserException("Người dùng không tồn tại");
}
```
- **Test Cover**: TC-CP003
- **Condition**: `getUserById(userId) == null`

---

#### **Nhánh 2: Mật khẩu cũ không khớp (validateOldPassword)**
```java
UserValidator.validateOldPassword(oldPassword, user.getPassword());
// Inside validator:
if (oldPassword == null || oldPassword.trim().isEmpty()) {
    throw new UserException("Mật khẩu cũ không được để trống");
}
if (!oldPassword.equals(currentPassword)) {
    throw new UserException("Mật khẩu cũ không chính xác");
}
```
- **Test Cover**: 
  - TC-CP002: oldPassword sai
  - TC-CP008: oldPassword = null
  - TC-CP010: oldPassword = "   " (whitespace)

---

#### **Nhánh 3: Mật khẩu mới không hợp lệ - Null/Empty (validatePassword)**
```java
UserValidator.validatePassword(newPassword);
// Inside validator:
if (password == null || password.trim().isEmpty()) {
    throw new UserException("Password không được để trống");
}
```
- **Test Cover**:
  - TC-CP004: newPassword = ""
  - TC-CP009: newPassword = null
  - TC-CP011: newPassword = "   " (whitespace)

---

#### **Nhánh 4: Mật khẩu mới quá ngắn (validatePassword)**
```java
if (password.length() < 6) {
    throw new UserException("Password phải có tối thiểu 6 ký tự");
}
```
- **Test Cover**:
  - TC-CP005: newPassword = "pass" (4 ký tự < 6)
  - TC-CP012: newPassword = "pass11" (6 ký tự = boundary - **Boundary Value Testing**)

---

#### **Nhánh 5: Mật khẩu mới giống mật khẩu cũ**
```java
if (oldPassword.equals(newPassword)) {
    throw new UserException("Mật khẩu mới phải khác mật khẩu cũ");
}
```
- **Test Cover**: TC-CP006

---

#### **Nhánh 6: Success Path - Cập nhật password**
```java
user.setPassword(newPassword);
user.setUpdatedAt(System.currentTimeMillis());
// return (no exception)
```
- **Test Cover**:
  - TC-CP001: Normal flow
  - TC-CP007: From 6-char password to 12-char password
  - TC-CP012: Boundary length (6 ký tự)

---

## 4️⃣ DANH SÁCH 12 TESTCASE

### ✅ Success Cases (3 test)

| TC | Test Name | Old Pwd | New Pwd | Description |
|---|---|---|---|---|
| **TC-CP001** | `testChangePasswordSuccessWithValidData` | oldpassword | newpassword | ChangePassword thành công với dữ liệu hợp lệ |
| **TC-CP007** | `testChangePasswordNormalLength` | pass12 | newpassword123 | ChangePassword từ 6 ký tự → 12 ký tự |
| **TC-CP012** | `testChangePasswordSuccessWithBoundaryLength` | oldpassword | pass11 | ChangePassword với boundary length (6 ký tự) |

### ❌ Failure Cases (9 test)

| TC | Test Name | Description | Exception | Error Message |
|---|---|---|---|---|
| **TC-CP002** | `testChangePasswordFailsWithWrongOldPassword` | Old password sai | UserException | "Mật khẩu cũ không chính xác" |
| **TC-CP003** | `testChangePasswordFailsWithNonExistentUser` | User ID không tồn tại (ID=999) | UserException | "Người dùng không tồn tại" |
| **TC-CP004** | `testChangePasswordFailsWithEmptyNewPassword` | New password rỗng ("") | UserException | "Password không được để trống" |
| **TC-CP005** | `testChangePasswordFailsWithNewPasswordTooShort` | New password < 6 ký tự ("pass") | UserException | "Password phủ có tối thiểu 6 ký tự" |
| **TC-CP006** | `testChangePasswordFailsWhenNewPasswordSameAsOld` | New password = Old password | UserException | "Mật khẩu mới phải khác mật khẩu cũ" |
| **TC-CP008** | `testChangePasswordFailsWithNullOldPassword` | Old password = null | UserException | "Mật khẩu cũ không được để trống" |
| **TC-CP009** | `testChangePasswordFailsWithNullNewPassword` | New password = null | UserException | "Password không được để trống" |
| **TC-CP010** | `testChangePasswordFailsWithWhitespaceOldPassword` | Old password = "   " | UserException | "Mật khẩu cũ không được để trống" |
| **TC-CP011** | `testChangePasswordFailsWithWhitespaceNewPassword` | New password = "   " | UserException | "Password không được để trống" |

---

## 5️⃣ COVERAGE METRICS

### Statement Coverage
- ✅ **100%** - Tất cả statements trong `changePassword()` được execute

### Branch Coverage
- ✅ **100%** - Tất cả if/else branches được cover:
  - User exists / Not exists
  - Old password match / Not match / Null / Whitespace
  - New password valid / Null / Empty / Too short / Whitespace
  - New password same as old / Different
  - Success path

### Condition Coverage
- oldPassword == null → TC-CP008, TC-CP010
- oldPassword.equals(currentPassword) → TC-CP002, TC-CP001
- newPassword == null → TC-CP009
- newPassword.isEmpty() → TC-CP004, TC-CP011
- newPassword.length() < 6 → TC-CP005, TC-CP012
- oldPassword.equals(newPassword) → TC-CP006

---

## 6️⃣ CHẠY TEST

### Chấy tất cả test Change Password:
```bash
mvn test -Dtest=UserServiceTest#testChangePassword*
```

### Chạy một testcase cụ thể:
```bash
mvn test -Dtest=UserServiceTest#testChangePasswordSuccessWithValidData
```

### Chạy test với code coverage:
```bash
mvn clean test jacoco:report
```

---

## 7️⃣ KẾT QUẢ TEST

```
Tatal Tests: 48 (tất cả UserServiceTest)
Change Password Tests: 12
Status: ✅ ALL PASSED (12/12)
```

### Test Results:
- ✅ TC-CP001: PASS
- ✅ TC-CP002: PASS
- ✅ TC-CP003: PASS
- ✅ TC-CP004: PASS
- ✅ TC-CP005: PASS
- ✅ TC-CP006: PASS
- ✅ TC-CP007: PASS
- ✅ TC-CP008: PASS
- ✅ TC-CP009: PASS
- ✅ TC-CP010: PASS
- ✅ TC-CP011: PASS
- ✅ TC-CP012: PASS

---

## 📌 GHI CHÚ QUAN TRỌNG

### Black Box vs White Box Testing:
- **12 testcase** cover cả **Black Box** (Test các tính năng) và **White Box** (Test tất cả nhánh logic)
- **3 testcase success** + **9 testcase failure** = Balanced test suite

### Boundary Value Testing:
- TC-CP012 test **boundary** của password length (= 6 ký tự, không < 6)

### Validator Enhancement:
- Cải tiến `UserValidator.validateOldPassword()` để check null/whitespace trước khi so sánh

---

## 📂 File Location
- Source: `/src/main/java/com/usermanagement/service/UserService.java`
- Tests: `/src/test/java/com/usermanagement/service/UserServiceTest.java` (TC-CP001 to TC-CP012)
- Validator: `/src/main/java/com/usermanagement/validator/UserValidator.java`

---

**Hoàn thành: 100% ✅**
