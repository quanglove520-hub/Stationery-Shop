## ADDED Requirements

### Requirement: Quản lý Yêu Thích Bằng Client
Cho phép thu gọn ID văn phòng phẩm vào 1 bộ nhớ đệm (Browser Array) để đánh dấu Tim màu đỏ, hỗ trợ khách mua xem lại dễ dàng.

#### Scenario: Gắn yêu thích lần đầu (Happy Path)
- **GIVEN** Sổ Da Cao Cấp không có trong Wishlist của local browser
- **WHEN** User click Heart Icon ở thẻ Card góc phải
- **THEN** JS đẩy ID vào Array và set Item vào LocalStorage
- **AND** Heart Image biến thành Màu đỏ nhúng CSS active
- **AND** Toast Message đẩy lên báo "Đã lưu vào bộ sưu tập".

#### Scenario: Hủy bỏ yêu thích ngược (Happy Path)
- **GIVEN** Sản phẩm ID 1 đang tim màu Đỏ
- **WHEN** User click lại
- **THEN** Slice ID ra khỏi Array
- **AND** Heart đổi thành dạng outline rỗng
- **AND** Reload F5 trình duyệt vẫn giữ đúng trang thái outline rỗng.
