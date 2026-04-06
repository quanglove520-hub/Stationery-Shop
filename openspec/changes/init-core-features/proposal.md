## Why
Dự án cần một nền tảng cơ bản vững chắc (Core Features) với kiến trúc BCE (Boundary - Control - Entity) trên môi trường Java Swing. Việc triển khai sớm chức năng xem danh mục cho Khách hàng và chức năng quản lý kho cơ bản cho Admin giúp thiết lập luồng đi xuyên suốt (vertical slice) để các tính năng phức tạp hơn về sau có thể dựa vào. Ở giai đoạn này, hệ thống hoàn toàn bỏ qua khái niệm phân quyền hay đăng nhập để tập trung 100% vào nghiệp vụ lõi (Catalog & Inventory).

## What Changes
- Xây dựng giao diện Trang chủ hiển thị 6 danh mục sản phẩm mặc định dưới dạng lưới (Grid).
- Xây dựng Admin Panel chứa hai module: Quản lý Danh mục (Category) và Quản lý Sản phẩm (Product).
- Tích hợp nghiệp vụ Ràng buộc dữ liệu (Data Validation): Không cho phép xoá Category nếu bên trong đang chứa Product. Không cho phép xoá Product nếu thuộc về một đơn hàng (Order) nào đó.

### Phạm vi (Scope)
- Customer: Chỉ xem danh mục tại trang chủ.
- Admin: Thêm/Sửa/Xóa/Xem Category & Product (in-memory DB). Mở trực tiếp không qua đăng nhập.

### Những gì không nằm trong phạm vi (Non-goals)
- Bỏ qua TOÀN BỘ chức năng quản lý tài khoản, phân quyền, hay Đăng nhập/Đăng ký.
- Bỏ qua chức năng mua hàng (Giỏ hàng, Checkout, Lịch sử đơn hàng).

## Capabilities
### New Capabilities
- `customer-home`: Chức năng hiển thị danh mục sản phẩm bề mặt UI lưới.
- `admin-inventory`: Chức năng quản lý danh mục và sản phẩm (CRUD) kèm logic validate.

### Modified Capabilities
- (Trống)

## Impact
- Định hình cấu trúc package và quy tắc kiến trúc (Boundary, Control, Entity).
- Quyết định cách cài đặt In-Memory Data Store.
