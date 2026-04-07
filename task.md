## 1. Project Setup

- [ ] 1.1 Tạo cấu trúc dự án Backend (Spring Boot, Java) và Frontend với dependencies (Web, JPA, H2, Lombok).
- [ ] 1.2 Tạo cấu trúc thư mục theo kiến trúc MVC phân lớp (Controller - Service - Repository).

## 2. API & Data Layer

- [ ] 2.1 Cấu hình kết nối H2 Database trong `application.properties` để chạy in-memory.
- [ ] 2.2 Tạo Entity `Product` (id, name, price, stock, category, description).
- [ ] 2.3 Tạo Interface `ProductRepository` extends `JpaRepository`.

## 3. Service Layer

- [ ] 3.1 Tạo Interface `ProductService` với method get danh sách sản phẩm và get chi tiết sản phẩm theo ID.
- [ ] 3.2 Tạo class `ProductServiceImpl` triển khai logic truy xuất dữ liệu từ Repository.
- [ ] 3.3 Viết Unit Test cho Happy Path (Có dữ liệu) và Unhappy Path (Sản phẩm trống).

## 4. Control Layer (REST API)

- [ ] 4.1 Tạo `ProductController` cấu hình REST Endpoint `GET /api/v1/products`.
- [ ] 4.2 Cấu hình REST Endpoint `GET /api/v1/products/{id}`.
- [ ] 4.3 Xử lý bắt lỗi dữ liệu `404 Not Found` nếu ID không hợp lệ.

## 5. View Layer (Frontend)

- [ ] 5.1 Dựng khung giao diện Web HTML/CSS (hoặc React/Vue) cho danh mục sản phẩm.
- [ ] 5.2 Lập trình gọi fetch() từ Client lên API server vừa tạo.
- [ ] 5.3 Tạo Component `ProductList` cùng Card Layout để vẽ lại danh sách items (Happy Path).
- [ ] 5.4 Tạo Component `EmptyState` render ra màn hình chữ "No data" khi DB rỗng (Unhappy Path).

## 6. Verify Acceptance

- [ ] 6.1 Chạy thử Server và mở Browser.
- [ ] 6.2 Kiểm tra luồng Happy Path đảm bảo dữ liệu văn phòng phẩm xuất hiện.
- [ ] 6.3 Xóa sạch dữ liệu DB để kiểm tra Unhappy Path, xem chữ "No data" có hiển thị đúng theo Spec không.
