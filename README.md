# BusinessService - Clean Architecture

Dự án này được tổ chức theo chuẩn **Clean Architecture** để đáp ứng việc mở rộng theo mô hình Microservices. 

Kiến trúc này đảm bảo nguyên lý thiết kế: Dependency Rule (Hướng phụ thuộc). Mọi sự phụ thuộc trong Source Code đều phải trỏ vào trong (hướng về tầng `domain` và `application`). Các vòng tròn bên ngoài (như Web, DB) phụ thuộc vào vòng trong, nhưng không có sự phụ thuộc ngược lại.

## Cấu trúc thư mục

```text
src/main/java/com/example/businessservice
├── domain/               # (Lõi nghiệp vụ) Chứa các Domain Entities nguyên thủy nhất, không phụ thuộc framework
│   └── entity/           # Các lớp đại diện cho đối tượng thực tế (VD: Chemical, Customer, Order)
│
├── application/          # (Use cases) Chứa logic nghiệp vụ ứng dụng, điều phối các Entity
│   ├── repository/       # Interface của các Repository (Để Repository Implementation ở Infrastructure gọi đến)
│   └── service/          # Định nghĩa Interface của các Services
│       └── implementations/ # Triển khai chi tiết Service, gọi Repository Interface để lấy Domain Entity
│
├── presentation/         # (Tầng giao tiếp Web) Nơi nhận Request từ bên ngoài (HTTP/REST) 
│   ├── controller/       # Các REST APIs Controller, định tuyến Request tới Application Service
│   ├── dto/              # Các đối tượng Data Transfer Object (Request/Response)
│   │   ├── request/
│   │   └── response/
│   └── mapper/           # Web Mappers: Chuyển đổi giữa DTO (Web) và Domain Entity
│
├── infrastructure/       # (Tầng cơ sở hạ tầng) Mọi Framework (Spring, DB, Security) nằm ở đây
│   ├── config/           # Cấu hình Spring Boot, Exception Handlers, Security
│   │   ├── common/       
│   │   └── security/     
│   └── persistence/      # Data Access/Database
│       ├── entity/       # JPA Entities (Được annotate bằng @Entity mapping với Database)
│       └── mapper/       # Persistence Mappers: Chuyển đổi giữa JPA Entity và Domain Entity
│
└── utils/                # Các classes tiện ích (Helper/Utility) chia sẻ chung
```

## Các thành phần chính và Vai trò

### 1. `domain`
- **Mục đích**: Các file Java thuần túy, định nghĩa cấu trúc dữ liệu cốt lõi phản ánh thế giới thực.
- **Quy tắc**: TUYỆT ĐỐI không chứa các annotations của Spring Web (`@RestController`) hay Database (`@Entity`, `@Table`). Chỉ chứa các field, constructor hoặc logic nghiệp vụ cơ bản.

### 2. `application`
- **Mục đích**: Hiện thực hóa các tính năng nghiệp vụ (Use Cases).
- **Quy tắc**: Chỉ nhận vào và trả về các `Domain Entity`. Không được phép thao tác trực tiếp với các `JpaEntity` hay `DTO` của tầng ngoài. Gọi DB thông qua các Interface khai báo ở `application/repository`.

### 3. `presentation`
- **Mục đích**: Giao tiếp với HTTP Client, Mobile, Web.
- **Quy tắc**: Nhận vào HTTP Request. Tại Controller, dùng `Web Mapper` (nằm trong `presentation/mapper`) để chuyển đổi `RequestDTO` thành `Domain Entity`. Lưu/Xử lý qua Application Service. Sau đó, dùng Web Mapper biến đổi `Domain Entity` thành `ResponseDTO` rồi trả về JSON.

### 4. `infrastructure`
- **Mục đích**: Giao tiếp với công nghệ bên ngoài (DB MySQL/Postgres, Redis, Kafka, JWT).
- **Quy tắc**: Tầng duy nhất biết về DB Tables (qua `@Entity`). Tầng này sẽ implementation interface của `application/repository`. Khi được Application Service gọi, cấu trúc Repository Implementation ở đây sẽ truy xuất `JpaEntity` từ DB, rồi dùng `Persistence Mapper` map ngược thành `Domain Entity` để trả lại về Service.

## Luồng dữ liệu (Data Flow) ví dụ khi tạo (Create)

1. Client gửi JSON lên **Controller** (Presentation layer).
2. JSON được Spring ép kiểu thành `CreateRequestDTO`.
3. **Controller** dùng `WebMapper` map `CreateRequestDTO` -> `DomainEntity`.
4. **Controller** gọi `Service` (Application layer), truyền vào `DomainEntity`.
5. **Service** thực hiện validation nghiệp vụ, sau đó gọi `Repository Interface` truyền vào `DomainEntity`.
6. Thực tế, framework chạy `RepositoryImpl` (Infrastructure layer) hiện thực hóa Interface.
7. `RepositoryImpl` nhận `DomainEntity`, dùng `PersistenceMapper` map sang `JpaEntity`.
8. `RepositoryImpl` lưu `JpaEntity` vào DB.
9. Kết quả thành công trả vòng ngược lại cho tới Controller, map ra `ResponseDTO` và báo success.


## Link SWAGGER

http://localhost:8080/swagger-ui/index.html#/