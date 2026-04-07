## ADDED Requirements

### Requirement: Xem danh mục trên Trang Chủ
As a Khách hàng, I want to xem trang chủ hiển thị lưới các danh mục sản phẩm, so that I can biết cửa hàng có bán những loại mặt hàng nào.

#### Scenario: Hiển thị mặc định 6 danh mục chính
- **GIVEN** Dữ liệu mẫu đã được nạp với 6 danh mục chuẩn
- **WHEN** Khách hàng khởi động ứng dụng và vào trang chủ
- **THEN** Hệ thống hiển thị 1 màn hình có dạng lưới (Grid) chứa 6 card danh mục
- **AND** Mỗi danh mục hiển thị đầy đủ Icon và Tên

#### Scenario: Không có danh mục nào (Unhappy Path)
- **GIVEN** Dữ liệu danh mục trong DB trống rỗng
- **WHEN** Khách hàng mở ứng dụng và vào trang chủ
- **THEN** Hệ thống hiển thị thông báo "Chưa có danh mục nào, vui lòng quay lại sau"
