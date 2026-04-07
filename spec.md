## ADDED Requirements

### Requirement: Xem danh sách và chi tiết sản phẩm

Là một Khách hàng, tôi muốn xem danh sách các đồ dùng văn phòng phẩm và chi tiết của từng sản phẩm để có thể tham khảo thông tin trước khi mua.

Hệ thống PHẢI hiển thị danh sách sản phẩm lấy từ cơ sở dữ liệu. Màn hình PHẢI hiển thị thông báo "No data" nếu không có sản phẩm nào.

#### Scenario: Hiển thị danh sách sản phẩm khi có dữ liệu (Happy Path)

- **GIVEN** Cơ sở dữ liệu có chứa sản phẩm văn phòng phẩm
- **AND** Khách hàng truy cập vào trang danh sách sản phẩm
- **WHEN** Hệ thống tải dữ liệu thành công
- **THEN** Màn hình hiển thị danh sách các sản phẩm (tên, giá, hình ảnh)
- **AND** Người dùng có thể nhấp vào một sản phẩm để xem chi tiết

#### Scenario: Hiển thị "No data" khi không có sản phẩm (Unhappy Path)

- **GIVEN** Cơ sở dữ liệu không có sản phẩm nào (rỗng)
- **AND** Khách hàng truy cập vào trang danh sách sản phẩm
- **WHEN** Hệ thống trả về mảng dữ liệu trống
- **THEN** Màn hình hiển thị thông báo "No data"
- **AND** Không có thành phần sản phẩm nào được hiển thị
