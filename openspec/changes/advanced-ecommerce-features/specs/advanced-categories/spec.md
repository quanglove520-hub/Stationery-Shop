## ADDED Requirements

### Requirement: Phân loại danh mục theo thẻ (Tag/Category filtering)
Là một khách hàng, tôi muốn lọc/tìm kiếm sản phẩm văn phòng phẩm dựa trên danh mục cụ thể hoặc thẻ (ví dụ thẻ "Giấy in") để thu hẹp phạm vi chọn một cách tối ưu.

#### Scenario: Chọn danh mục hợp lệ có sản phẩm (Happy Path)
- **GIVEN** Có 3 bút bi (thuộc danh mục Bút) và 2 tệp giấy (thuộc danh mục Giấy) trong Store
- **WHEN** Khách hàng ấn chọn lọc theo danh mục "Giấy"
- **THEN** Màn hình chỉ tải lưới chứa 2 tệp giấy 
- **AND** Cập nhật UI trạng thái "Đang lọc: Giấy"

#### Scenario: Chọn lọc danh mục trống (Unhappy Path)
- **GIVEN** Nút lọc danh mục "Balo" xuất hiện nhưng trong kho hiện đã hết sản phẩm "Balo"
- **WHEN** Khách hàng ấn chọn mục "Balo"
- **THEN** Giao diện lưới hiện Data Loading...
- **AND** Ngay sau đó hiển thị State "Không có dữ liệu - No data" thay vì grid trắng bóng.
