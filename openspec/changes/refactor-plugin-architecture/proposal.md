## Why

Hệ thống hiện tại đang phát triển theo hướng nguyên khối (Monolith), dẫn đến khả năng tái sử dụng kém và rủi ro conflict code cao khi các thành viên hay team khác nhau cùng phát triển tính năng mới (như Giỏ hàng, Đơn hàng...). Chuyển đổi sang kiến trúc Microkernel / Plugin-based Architecture ("Bo mạch chính" và "Khe cắm") ở mức Multi-Module Maven sẽ giúp phân rã rõ ràng các Domain, cho phép cắm/rút tính năng dạng `.jar` độc lập tại Runtime mà không làm ảnh hưởng (zero-impact) tới mã nguồn cốt lõi (Core system).

## What Changes

- **BREAKING**: Cơ cấu lại cấu trúc Project từ ứng dụng đơn lẻ sang Multi-Module Maven Project.
- Định nghĩa mô-đun `stationary-core` làm "Bo Mạch Chính" (chứa Spark Web Server, Share In-Memory Database context, và khai báo `StationaryPlugin` interface/SPI).
- Thiết lập cơ chế tự động quét, tìm và nạp các plugin lúc runtime bằng `java.util.ServiceLoader`.
- Tạo trước cấu trúc bộ khung (Skeleton module) cho 5 Khe Cắm chính đã thỏa thuận:
  1. Plugin: Catalog (Sản phẩm & Danh mục)
  2. Plugin: Cart (Giỏ hàng)
  3. Plugin: Order (Đơn hàng)
  4. Plugin: Notify (Thông báo/Email)
  5. Plugin: User/Auth (Mock UI API)
- Tuân thủ cấu trúc Strict BCE trong lõi Core và các Plugin.
- Tuân thủ luật "Zero backend logic API" đối với User Mock plugin.

### Scope & Non-goals
- **Scope**: Xây dựng kiến trúc Multi-module, thiết kế Plugin SPI, và cấu hình build ra các file `.jar` có thể map với nhau.
- **Non-Goals**: Không viết Business Layout thực tế cho 4 Plugin mới (Cart, Order, Notify, User) trong Change này. Không đổi sang lưu trữ DB ngoài (vẫn là In-Memory DB đặt chung ở Core). Bỏ qua Spring Boot, vẫn dùng SparkJava gốc.

## Capabilities

### New Capabilities
- `core-plugin-loader`: Chịu trách nhiệm lifecycle (Init DB, Register Route) của các Plugins gắn kèm.

### Modified Capabilities
- (Để trống - Các chức năng Inventory, Cart, Product List hiện tại sẽ được merge logic thành các Module nhưng không làm thay đổi behavior mô tả ở cấp độ Feature/Spec)

## Impact

- **Mã Nguồn**: Toàn bộ dự án phải đập đi thư mục gốc, chuyển thành dạng Multi-Module (`pom.xml` cha để đóng gói, các `pom.xml` con tương ứng mỗi Slot).
- **Run & Build**: Quá trình Run server sẽ chạy từ `stationary-core`, với các plugin dependencies được ném vào classpath/plugins dir.
- **Testing**: Tách Component Test (JUnit, Cucumber) vào riêng từng module, test module nào chạy module đó.
