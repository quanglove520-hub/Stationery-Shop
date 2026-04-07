## Yêu cầu BDD

### Feature: Xem Danh sách Sản phẩm
**As a** Khách hàng
**I want to** xem danh sách các sản phẩm khi click vào một Danh mục ở Trang chủ
**So that** tôi biết chi tiết từng mặt hàng và giá cả để mua.

### Scenario: Khách hàng click vào danh mục có sản phẩm (Happy Path)
- **GIVEN** Hệ thống đang hiển thị HomeUI
- **AND** Danh mục "Bút - Viết" có chứa "Bút Bi" và "Bút Máy"
- **WHEN** Khách hàng click vào thẻ "Bút - Viết"
- **THEN** Giao diện chuyển sang ProductListUI
- **AND** Hiển thị danh sách 2 sản phẩm "Bút Bi" và "Bút Máy"
- **AND** Hiển thị nút "Quay lại"

### Scenario: Khách hàng click vào danh mục rỗng (Unhappy Path)
- **GIVEN** Danh mục "Kéo" không có sản phẩm nào
- **WHEN** Khách hàng click vào thẻ "Kéo"
- **THEN** Giao diện chuyển sang ProductListUI
- **AND** Hiển thị câu báo "Chưa có sản phẩm nào thuộc danh mục này"
