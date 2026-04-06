## Feature: Luồng thao tác Giỏ Hàng Simulator
**As a** Khách hàng
**I want to** đưa các mặt hàng vào giỏ và xem tổng tiền
**So that** tôi có thể kiểm kê trước khi bấm thanh toán định danh.

### Scenario: Thêm thành công sản phẩm từ ProductList vào Cart
- **GIVEN** Khách hàng ở màn hình ProductList "Bút - Viết"
- **WHEN** Khách hàng ấn nút "Thêm vào giỏ" dưới card "Bút Bi Thiên Long"
- **THEN** Giỏ hàng (ẩn) lưu trữ 1 mặt hàng "Bút Bi Thiên Long"

### Scenario: Vào CartUI và thấy tổng tiền tính chính xác
- **GIVEN** Giỏ hàng đang có "Bút Bi Thiên Long" giá 5000đ (SL: 2)
- **WHEN** Khách hàng ấn vào biểu tượng Giỏ Hàng trên Top Bar
- **THEN** Giao diện kích hoạt CartUI
- **AND** Hiển thị tổng cộng số tiền là 10000đ
