# Student Management System (Backend)
* **Dự án **Spring Boot** này cho phép quản lý **Students, Courses, Instructors và Enrollments**, với các tính năng **CRUD đầy đủ** và các ràng buộc business logic:
* Không thể xóa **Student** nếu còn **Enrollment** liên quan.
* Khi xóa **Course** → tất cả **Enrollment liên quan sẽ bị xóa theo**.
* Khi xóa **Instructor** → các **Course** liên quan sẽ **set instructor_id = null**.
* Sử dụng **DTO** để tránh expose trực tiếp Entity ra API.

## Công nghệ
* **Java 17**
* **Spring Boot 3.x**
* **Spring Data JPA**
* **Maven**
* **PostgreSQL / MySQL** (tùy config)
* **Postman** (để test API)

## Cấu trúc project
```
StudentManaSystem/
├─ src/main/java/com/StudentManaSystem/
│  ├─ controller/   # REST API
│  ├─ service/      # Business logic
│  ├─ repository/   # JPA repository
│  ├─ dto/          # DTO cho request/response
│  └─ entity/       # Entity mapping DB
├─ src/main/resources/
│  └─ application.properties
├─ pom.xml
└─ .gitignore
```

## Cài đặt & chạy project
1. **Clone repo**:
```bash
git clone https://github.com/username/StudentManaSystem.git
```
2. **Cấu hình database** trong `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/studentdb
spring.datasource.username=postgres
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
```
3. **Chạy project**:
```bash
./mvnw spring-boot:run
```

## Lưu ý & Quan hệ (CRUD summary)
* **Student ↔ Course**: N-N qua Enrollment (Student có nhiều Enrollment, Course có nhiều Enrollment)
* **Instructor ↔ Course**: 1-N (Instructor giảng dạy nhiều Course)
* **Xóa Student** → nếu còn Enrollment → trả về 400
* **Xóa Course** → Enrollment liên quan bị xóa theo
* **Xóa Instructor** → Course liên quan set instructor_id = null
* **DTO** dùng để tránh expose trực tiếp Entity ra API
**CRUD chung:**
* `GET` → lấy danh sách hoặc theo id
* `POST` → tạo mới
* `PUT` → cập nhật
* `DELETE` → xóa

## Hướng phát triển tiếp theo / Nâng cao
* **Paging, Sorting, Filtering**: Hỗ trợ phân trang, sắp xếp và lọc dữ liệu theo nhiều tiêu chí, giúp API trả về kết quả linh hoạt hơn.
* **Sử dụng các annotation để gọn code**:
  * `@Autowired` thay cho inject thủ công
  * Annotation từ Lombok như `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` để giảm boilerplate
* **Lưu lịch sử thao tác Enrollment**: Tạo entity riêng để ghi lại các thay đổi của Enrollment, bao gồm thông tin student, course, thời gian và loại hành động (tạo, cập nhật, xóa).
* **Mapper nâng cao**: Sử dụng các công cụ mapping giữa Entity và DTO để giảm code thủ công và giữ code gọn gàng.
* **Exception Handling**: Xử lý lỗi toàn cục, trả về response thống nhất cho client.
* **ResponseApi**: Chuẩn hóa response API, bao gồm trạng thái, dữ liệu trả về và thông báo, giúp client dễ dàng xử lý.
* **Security & Authorization**: Thêm phân quyền CRUD theo vai trò, ví dụ admin, instructor, student, để bảo vệ API.

## License

MIT © MinhNam
