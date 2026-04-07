## 1. Multi-Module Project Scaffold

- [x] 1.1 Khởi tạo cấu trúc `pom.xml` cha tại thư mục gốc `stationary` (đổi packaging type sang pom, khai báo `<modules>`).
- [x] 1.2 Di chuyển source code gốc vào module mới `stationary-core` và cấu hình `pom.xml` con tương ứng.
- [x] 1.3 Khởi tạo các thư mục plugin skeleton: `plugin-inventory`, `plugin-cart`, `plugin-order`, `plugin-notify`, `plugin-user` kèm theo file `pom.xml` cơ bản định tuyến về POM cha.

## 2. Core SPI & ServiceLoader Implementation

- [x] 2.1 Viết Cucumber BDD test xác nhận khả năng nạp 1 Plugin và 0 Plugin của `stationary-core`.
- [x] 2.2 Tạo interface `StationaryPlugin` ở `stationary-core` (chứa các method interface `getName`, `onInitData`, `registerRoutes`).
- [x] 2.3 Cấu trúc lại `MemoryDB` thành dạng Registry Class chứa Map các data store.
- [x] 2.4 Cài đặt cơ chế quét `java.util.ServiceLoader` trong Class `MainApp` để nạp Plugin trước khi Start Tomcat/Spark Server.

## 3. Tách ghép mã nguồn hiện tại sang plugin-inventory (Migrate Admin Inventory)

- [x] 3.1 Migrate các model Entity (Category, Product...) cũ sang thư mục mã nguồn của `plugin-inventory`.
- [x] 3.2 Migrate `AdminInventoryControl` và bọc vào Class `InventoryPlugin` (implements `StationaryPlugin`).
- [x] 3.3 Khởi tạo cấu hình nhận diện `META-INF/services/stationary.core.spi.StationaryPlugin` trỏ vào tên class `InventoryPlugin` ở module inventory.
- [x] 3.4 Di chuyển file feature `admin_inventory.feature` và Steps tương ứng sang codebase test của `plugin-inventory` và verify luồng Test chạy thành công theo kiến trúc mới.
