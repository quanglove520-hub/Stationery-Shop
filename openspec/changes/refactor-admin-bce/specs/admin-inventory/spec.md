## MODIFIED Requirements

### Requirement: Admin xem danh sách Danh mục và Sản phẩm (Kiến trúc chuẩn)
Mặc dù UI và tính năng không có bất kì sự thay đổi nào đối với user, việc lấy danh sách Danh Mục (Categories) và Sản Phẩm (Products) bên trong giao diện Admin Panel SHALL tuân thủ tuyệt đối quy tắc dòng chảy BCE (đi xuyên qua qua lớp Control, tuyệt đối không truy vấn DB trực tiếp từ giao diện).

#### Scenario: Hệ thống lấy danh sách dữ liệu an toàn dựa trên chuẩn kiến trúc BCE
- **GIVEN** hệ thống có sẵn dữ liệu Danh mục và Sản phẩm trong Database in-memory
- **WHEN** giao diện Admin Panel được load và khởi tạo danh sách ban đầu
- **THEN** các thông tin danh mục, sản phẩm phải được trả về đầy đủ mà không bắn ra lỗi vòng lặp phụ thuộc (vi phạm Boundary)
