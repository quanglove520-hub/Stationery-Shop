## ADDED Requirements

### Requirement: Áp dụng mã giảm giá
Hệ thống cho phép nhập trực tiếp mã voucher / thẻ quà tặng tại quá trình checkout hoặc xem giỏ hàng để giảm số tiền phải thanh toán dựa theo cấu hình của Voucher.

#### Scenario: Nhập mã hợp lệ (Happy Path)
- **GIVEN** Giỏ hàng có chứa sản phẩm tổng giá trị > 0
- **AND** Khách hàng nhập đúng mã giảm giá "SALE10" (Giảm 10%)
- **WHEN** Khách hàng nhấn nút "Áp Dụng"
- **THEN** Hệ thống thông báo áp dụng thành công
- **AND** Tổng tiền giỏ hàng hiển thị giảm 10%
- **AND** UI render thêm một dòng thể hiện "Tiết kiệm được X tiền"

#### Scenario: Nhập mã sai/hết hạn (Unhappy Path)
- **GIVEN** Khách hàng nhập mã "FAKECODE" hoặc mã đã quá thời gian sử dụng
- **WHEN** Hệ thống rà soát Validate tại Database coupon
- **THEN** Hệ thống hiển thị cảnh báo "Mã giảm giá không hợp lệ hoặc đã hết hạn"
- **AND** Tổng tiền giỏ hàng giữ nguyên không giảm.
