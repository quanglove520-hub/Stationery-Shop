## ADDED Requirements

### Requirement: Tối Ưu Trải nghiệm Người dùng (UX High-end)
Cải tiến đột phá giao diện hệ thống cung cấp các trạng thái loading, phản hồi và lazy render cao cấp.

#### Scenario: Skeleton Animations chống chói (Happy Path)
- **GIVEN** Mạng khách hàng quá chậm, Client fetch REST API tốn nhiều thời gian
- **WHEN** Quá trình Loading State được kích hoạt
- **THEN** Client chèn các dòng thẻ Skeleton có hiệu ứng flashwave sóng trắng
- **AND** Tuyệt đối không treo giao diện hay để trống hoác web.

#### Scenario: Toast Message xác nhận (Happy Path)
- **GIVEN** Action thành công diễn ra (VD add cart, add heart)
- **WHEN** Người dùng click button
- **THEN** Hàm Helper nảy lên 1 thanh Toast góc màn hình báo Message text với màu nền xanh Validate
- **AND** Tự Fade Out mất tích sau xấp xỉ 3000ms.

#### Scenario: Kéo tới đâu hiển thị tới đó (Lazy Load)
- **GIVEN** Danh mục sổ tay đang có 30 sản phẩm
- **AND** User kéo tuột thanh cuộn quá 85% chiều dọc grid products
- **WHEN** JS IntersectionObserver bắt ngưỡng
- **THEN** Fetch API page tiếp theo
- **AND** Quẳng thêm các thẻ Card tiếp vào đít DOM thay đổi mượt khung hình mà không Re-render (F5) trang.
