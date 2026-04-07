**User Story:** As a System Administrator (hoặc Core Developer), I want cấu trúc hệ thống nạp tự động các plugin thông qua giao thức SPI lúc khởi động, so that tôi có thể dễ dàng quản lý (thêm/bớt) các module nghiệp vụ như Inventory, Cart mà không phải build lại toàn bộ core codebase.

## ADDED Requirements

### Requirement: Tự động nạp Plugin qua SPI
Hệ thống Core (`MainApp`) MUST sử dụng cơ chế `java.util.ServiceLoader` để nạp mọi classes/artifacts implement interface `StationaryPlugin`. Việc nạp này phải chia làm 2 giai đoạn: Khởi tạo Storage (DB) và Đăng ký API (Route).

#### Scenario: Tìm thấy và nạp thành công các plugin trên hệ thống
- **GIVEN** Project đã được đóng gói và có chứa cấu hình khe cắm tại `META-INF/services/stationary.core.spi.StationaryPlugin` với ít nhất 1 plugin (vd: InventoryPlugin)
- **WHEN** Hệ thống Core khởi động
- **THEN** PluginManager của Core nhận diện được InventoryPlugin
- **AND** Hệ thống gọi thành công hàm `onInitData(MemoryDB)`
- **AND** Hệ thống gọi tiếp hàm `registerRoutes()` để ánh xạ web routing

#### Scenario: Khởi động an toàn khi không có Plugin nào được cắm vào
- **GIVEN** Trong thư mục/classpath không chứa bất kỳ khe cắm Plugin nào
- **WHEN** Hệ thống Core khởi động
- **THEN** Core vẫn start thành công Spark Web Server ở port mặc định
- **AND** Không ghi nhận route/API rác nào (graceful start)

#### Scenario: Cách ly lỗi từ một Plugin hỏng
- **GIVEN** Có 2 plugin A và B được tìm thấy, trong đó Plugin B gặp lỗi RuntimeException ở hàm `registerRoutes()`
- **WHEN** Hệ thống Core khởi động và lặp qua các plugin
- **THEN** Hệ thống ghi nhận lỗi của Plugin B ra console/log
- **AND** Hệ thống MUST cấu hình plugin A thành công và tiếp tục khởi động Spark thay vì crash toàn bộ hệ thống
