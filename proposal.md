## Why

Hệ thống Website Văn Phòng Phẩm hiện tại chứa trống (chưa thể hiển thị mặt hàng gì cho khách xem). Đây là user story đầu tiên — dựng khung API và giao diện Web để hiển thị danh sách sản phẩm và thiết kế trạng thái "No data" đặc biệt phục vụ cho các trường hợp kho hàng đang bị rỗng. Story này sẽ thiết lập nền tảng UI (Giao diện lưới) và chuẩn Database (H2 MVC) cho hầu như tất cả các luồng E-commerce phía sau (ví dụ: giỏ hàng, thanh toán, login).

## What Changes

- Dựng **RESTful API** backend bằng Java Spring Boot:
  - GET endpoint lấy danh sách sản phẩm trang chủ
  - GET endpoint lấy thông số chi tiết
- Tích hợp **H2 Database** để dễ dàng chạy bằng bộ nhớ tạm (in-memory) phục vụ lập trình cục bộ.
- Dựng **Web UI** cơ bản xử lý State:
  - Component hiển thị khối danh sách các món đồ (Product Cards)
  - Layout hiển thị "No data" khi kho hàng rỗng (Unhappy path)
  - Layout xem chi tiết món đồ
- Thiết lập kiến trúc phần mềm tiêu chuẩn MVC với Controller, Service, Repository.

## Capabilities

### New Capabilities
- `view-products`: Hiển thị danh sách sản phẩm văn phòng phẩm. Hiển thị thông báo "No data" khi DB không có items nào. Kèm màn hình popup hoặc trang xem chi tiết sản phẩm.

### Modified Capabilities
_(Không có — đây là story đầu tiên)_

## Impact

- **Database Layer**: Tạo mới cấu trúc table `products` với các properties cho mảng văn phòng phẩm.
- **Backend API**: Mở cổng Server API cho Web và Mobile kết nối.
- **Web Frontend**: Định hình giao diện layout Component cho cả dự án sau này.
