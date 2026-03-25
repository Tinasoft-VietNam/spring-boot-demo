# Postman Collection - Hướng Dẫn Chi Tiết

## Tổng Quan

Postman collection này cung cấp toàn bộ các endpoint cho hệ thống OAuth2 multi-service:

- **User Service** (Port 9001): Authentication, User, Role, Permission, Profile management
- **Business Service** (Port 9002): Customer & Chemical management
- **Kong Gateway** (Port 8000): API routing

## Cách Import Collection

### 1. **Import vào Postman**

```
1. Mở Postman
2. Click "Import" (phía trên bên trái)
3. Chọn tab "File"
4. Chọn file: oauth2-complete.postman_collection.json
5. Click "Import"
```

### 2. **Thiết Lập Environment**

Collection đã có sẵn variables:

- `baseUrl`: http://localhost:8000
- `accessToken`: (tự động lưu khi login)
- `refreshToken`: (tự động lưu khi login)
- `adminAccessToken`: (token cho admin user)
- `userAccessToken`: (token cho regular user)

## Workflow Cơ Bản

### Step 1: Đăng Nhập (Authentication)

#### Đăng nhập Admin:

1. Vào folder **Authentication**
2. Click **Login - Admin**
3. Click **Send**
4. Admin token sẽ tự động lưu vào `{{accessToken}}`

**Request:**

```json
{
    "email": "admin@users.local",
    "password": "Admin@123"
}
```

**Response (thành công 200):**

```json
{
    "code": "00",
    "message": "Login successfully",
    "data": {
        "accessToken": "eyJhbGciOiJIUzI1NiIs...",
        "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
    }
}
```

#### Đăng nhập User thường:

1. Click **Login - User**
2. Click **Send**
3. User token sẽ lưu vào `{{userAccessToken}}`

---

### Step 2: Làm Việc với User Service

#### User Management

```
GET    /api/user                  → Lấy danh sách users
GET    /api/user/1                → Lấy user theo ID
POST   /api/user/search           → Tìm kiếm users (phân trang, sắp xếp)
POST   /api/user                  → Tạo user mới
PUT    /api/user/3                → Cập nhật user
DELETE /api/user/3                → Xóa user
GET    /api/user/current          → Lấy user hiện tại
```

**Example: Tạo User Mới**

```json
{
    "name": "New User",
    "email": "newuser@test.com",
    "password": "Pass@123",
    "roleId": 2,
    "avatar": "https://via.placeholder.com/150",
    "gender": 1
}
```

#### Role Management

```
GET    /api/role                  → Lấy danh sách roles
GET    /api/role/1                → Lấy role theo ID
POST   /api/role                  → Tạo role mới
PUT    /api/role/3                → Cập nhật role
DELETE /api/role/3                → Xóa role
```

#### Permission Management

```
GET    /api/permission            → Lấy danh sách permissions
```

#### Profile

```
GET    /api/profile               → Lấy profile của user hiện tại
PATCH  /api/profile               → Cập nhật profile
```

---

### Step 3: Làm Việc với Business Service

#### Customer Management (Port 9002)

```
GET    /api/customer/get-all                    → Lấy tất cả customers
GET    /api/customer/detail/1                   → Lấy customer theo ID
POST   /api/customer/search                     → Tìm kiếm customers
POST   /api/customer/create                     → Tạo customer mới
PUT    /api/customer/update/1                   → Cập nhật customer
DELETE /api/customer/delete/1                   → Xóa customer
```

**Example: Tạo Customer**

```json
{
    "name": "Acme Corporation",
    "email": "contact@acme.com",
    "phone_number": "+1-555-123-4567",
    "birthday": "1990-01-15",
    "address": "123 Business Street, New York, NY 10001"
}
```

#### Chemical Management (Port 9002)

```
GET    /api/chemical/get-all                    → Lấy tất cả chemicals
GET    /api/chemical/detail/1                   → Lấy chemical theo ID
POST   /api/chemical/search                     → Tìm kiếm chemicals
POST   /api/chemical/create                     → Tạo chemical mới
PUT    /api/chemical/update/1                   → Cập nhật chemical
DELETE /api/chemical/delete/1                   → Xóa chemical
```

**Example: Tạo Chemical**

```json
{
    "name": "Sodium Chloride",
    "description": "Common table salt",
    "cas_number": "7647-14-5",
    "file_path": "/docs/nacl.pdf",
    "flash_point": -1.0,
    "storage_temperature_min": 15.0,
    "storage_temperature_max": 25.0,
    "storage_conditions": "Cool, dry place",
    "type": "Inorganic",
    "unit_of_measure": "kg"
}
```

---

## Test Cases

Collection bao gồm test cases để verify security:

### Test: Without Token (Expect 401)

```
GET /api/customer/get-all
```

Kết quả: `401 Unauthorized`

### Test: Invalid Token (Expect 401)

```
GET /api/customer/get-all
Authorization: Bearer invalid.token.here
```

Kết quả: `401 Unauthorized`

### Test: Admin Can Access

```
GET /api/customer/get-all
Authorization: Bearer {{adminAccessToken}}
```

Kết quả: `200 OK` + danh sách customers

### Test: User Can Access

```
GET /api/customer/get-all
Authorization: Bearer {{userAccessToken}}
```

Kết quả: `200 OK` + danh sách customers

---

## Pre-request Scripts

Một số request đã có **Pre-request Script** để tự động thiết lập headers:

**Auto-Token Setup** (có sẵn):

```javascript
// Login requests tự động:
// 1. Capture access token từ response
// 2. Lưu vào environment variable {{accessToken}}
// 3. Sử dụng cho request tiếp theo
```

---

## Workflow: Tạo CRUD Customer

### 1. Đăng Nhập

- Folder: **Authentication**
- Request: **Login - Admin**
- Token lưu tự động → `{{accessToken}}`

### 2. Xem Danh Sách

- Folder: **Business Service - Customer**
- Request: **Get All Customers**
- Header: `Authorization: Bearer {{accessToken}}`

### 3. Tạo Customer Mới

- Request: **Create Customer**

```json
{
    "name": "Tech Solutions Inc",
    "email": "info@techsolutions.com",
    "phone_number": "+1-555-456-7890",
    "address": "789 Tech Park, San Jose, CA 95110"
}
```

### 4. Lấy Customer Chi Tiết

- Request: **Get Customer By ID**
- Thay {id} bằng ID mới tạo

### 5. Cập Nhật Customer

- Request: **Update Customer**

```json
{
    "name": "Tech Solutions Inc - Updated",
    "email": "newemail@techsolutions.com"
}
```

### 6. Xóa Customer

- Request: **Delete Customer**

---

## Xem Token Detail

### 1. Decode JWT Token

- Copy access token từ response
- Vào https://jwt.io
- Paste token vào **Encoded** section
- Xem claims: email, role, authorities, exp

### 2. Token Payload Example

```json
{
    "sub": "admin@users.local",
    "role": "ADMIN",
    "authorities": [
        "USER_READ",
        "USER_CREATE",
        "USER_UPDATE",
        "USER_DELETE",
        "USER_MANAGE"
    ],
    "token_type": "access",
    "exp": 1704067200,
    "iat": 1704063600
}
```

---

## Common Errors & Solutions

### 1. **401 Unauthorized**

**Nguyên nhân:** Token không hợp lệ hoặc hết hạn
**Giải pháp:**

- Đăng nhập lại (Login - Admin)
- Token sẽ tự động update
- Thử request lại

### 2. **403 Forbidden**

**Nguyên nhân:** User không có quyền (hiện tại tất cả user có quyền)
**Giải pháp:**

- Đảm bảo token hợp lệ
- Kiểm tra JWT scope

### 3. **Connection Refused**

**Nguyên nhân:** Services không chạy
**Giải pháp:**

```bash
# Terminal 1: User Service
cd user-service
./mvnw.cmd spring-boot:run

# Terminal 2: Business Service
cd business-service
./mvnw.cmd spring-boot:run

# Terminal 3: Kong
docker-compose -f kong-gateway/docker-compose.yml up -d
```

### 4. **CORS Error**

**Nguyên nhân:** CORS không được cấu hình
**Giải pháp:** Services đã được cấu hình CORS, nếu vẫn lỗi kiểm tra `SecurityConfigurations.java`

---

## Response Format

### Success Response (200)

```json
{
    "code": "00",
    "message": "Success",
    "data": {
        // ... response data ...
    }
}
```

### Pagination Response

```json
{
    "code": "00",
    "message": "Success",
    "data": {
        "content": [
            /* items */
        ],
        "page": 0,
        "size": 10,
        "totalElements": 42,
        "totalPages": 5
    }
}
```

### Error Response (4xx/5xx)

```json
{
    "code": "01",
    "message": "Error message here",
    "data": null
}
```

---

## Test Scenarios

### Scenario 1: Full RBAC Flow

```
1. Login as Admin
2. Create new User
3. Create new Role
4. Assign Role to User
5. Login as new User
6. Access Customer endpoints
7. Verify audit trail (created_by = authenticated email)
```

### Scenario 2: Multi-Service Flow

```
1. Login from User Service
2. Get user profile
3. Use token to access Business Service Customer endpoints
4. Create/Update/Delete customer
5. Verify audit trail captures email
```

### Scenario 3: Security Testing

```
1. Try access without token → 401
2. Try access with invalid token → 401
3. Try access with expired token → 401
4. Try access with valid token → 200
```

---

## Useful Links

- **JWT Decoder**: https://jwt.io
- **Base64 Encoder**: https://www.base64encode.org
- **Postman Docs**: https://learning.postman.com
- **Spring Security OAuth2**: https://spring.io/projects/spring-security

---

## Tips & Tricks

### 1. **Nhanh Import Environment Variables**

```
Environments → Chọn Environment → Pre-request Script:
Tự động set baseUrl từ config file
```

### 2. **Batch Testing**

```
Postman → Collection → Run
Chọn collection → Run all requests
Xem kết quả từng request
```

### 3. **Export Test Results**

```
Run → Finish → Export Results
Lưu thành file JSON hoặc HTML
```

### 4. **Mock Server**

Nếu services chưa sẵn sàng:

```
Postman → Mock Servers → Create mock server
Tự động tạo endpoints từ collection
```
