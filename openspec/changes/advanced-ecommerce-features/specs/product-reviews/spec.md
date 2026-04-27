## ADDED Requirements

### Requirement: Đánh giá sản phẩm (Product Reviews)
Là một khách hàng có ý định xem chi tiết sản phẩm, tôi muốn thấy điểm đánh giá Rating (số Sao) và bình luận từ những người từng mua mặt hàng này để vững tay checkout.

#### Scenario: Tải Reivew thành công (Happy Path)
- **GIVEN** Khách hàng bấm xem mặt hàng "Bút bi Thiên Long" (ID=1) 
- **AND** Mặt hàng này có 3 đánh giá kèm điểm số (Rating 5)
- **WHEN** Form/Modal Chi Tiết hiển thị
- **THEN** Hệ thống tích hợp gọi riêng API để bóc Comments và hiển thị phần Đánh Giá khách quan
- **AND** Hiện biểu tượng trung bình N/5 sao một cách trực quan.

#### Scenario: Chưa có Preview (Happy Path/Alternate)
- **GIVEN** Sản phẩm "Kéo cắt" vừa thêm mới 100%, chưa ai review
- **WHEN** Khách hàng click xem chi tiết
- **THEN** Vùng đánh giá hiển thị mượt mà "Chưa có đánh giá nào cho sản phẩm. Trở thành người đầu tiên review!" thay vì ném lỗi HTTP hay báo Unknown.
