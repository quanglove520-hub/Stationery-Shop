## MODIFIED Requirements

### Requirement: Xem danh sách sản phẩm cơ bản
Hệ thống cho phép lấy về danh sách Products hiện hữu, nay bổ sung thêm hỗ trợ Truy vấn chuỗi lọc CategoryId để render Grid một cách linh động.

#### Scenario: Tải danh mục với Filter (Happy Path)
- **GIVEN** Khách hàng đang ở grid danh sách 
- **WHEN** Query `GET /api/v1/products?categoryId=2` được Request
- **THEN** Backend lọc H2 bằng Condition `AND category_id = 2`
- **AND** Trả về List Mảng JSON đã có Tag hợp lệ
