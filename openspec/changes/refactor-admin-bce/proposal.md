## Why

Hệ thống hiện tại đang vi phạm nghiêm trọng Quy tắc kiến trúc BCE tại Module Admin (`AdminPanel.java`). Lớp Boundary đang gọi trực tiếp Entity và Database (`InMemoryStore.getInstance().getCategories()`), đi vòng qua lớp Control (`AdminInventoryControl`). Việc này tạo ra tech debt, đi ngược lại thiết kế đã chốt và cần được xử lý triệt để ngay lập tức trước khi dự án phát triển thêm các tính năng mới.

## What Changes

- Bổ sung hàm tiện ích lấy dữ liệu danh sách Category và Product vào lớp `AdminInventoryControl`.
- Cập nhật lớp `AdminPanel` (Boundary) chuyển sang sử dụng `AdminInventoryControl` để truy vấn danh sách, phục vụ cho việc validation nội bộ trong GUI (ví dụ: lấy item đầu tiên của mảng để gọi hàm gỡ bỏ).
- Loại bỏ hoàn toàn sự phụ thuộc (import) vào `InMemoryStore` trong `AdminPanel` hoặc bất kỳ lớp Boundary nào khác.

### Phạm Vi (Scope)
- Sửa đổi cấu trúc gọi hàm của Boundary `AdminPanel`.
- Mở rộng api nội bộ cho `AdminInventoryControl`.

### Không Thuộc Phạm Vi (Non-goals)
- Không làm thay đổi giao diện UI hoặc User Experience của Admin Panel.
- Không chỉnh sửa luồng lấy dữ liệu của Khách hàng (`HomeUI`), do phần này đã tuân thủ tốt.
- Không thêm tính năng quản lý, thao tác mới.

## Capabilities

### New Capabilities
*(Không có)*

### Modified Capabilities
*(Không có)*
(Việc cập nhật chỉ tác động đến implementation code, hoàn toàn không làm thay đổi requirement hay behavior của specs.)

## Impact

- Tác động trực tiếp vào class `stationary.boundary.admin.AdminPanel` và `stationary.control.AdminInventoryControl`.
- Clean code: loại bỏ dependency sai luồng, giúp ứng dụng sẵn sàng mở rộng mà không vướng víu tech debt ở tầng UI.
