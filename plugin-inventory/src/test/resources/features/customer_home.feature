Feature: Xem danh mục trên Trang Chủ
  As a Khách hàng, I want to xem trang chủ hiển thị lưới các danh mục sản phẩm,
  so that I can biết cửa hàng có bán những loại mặt hàng nào.

  Scenario: Hiển thị mặc định 6 danh mục chính
    Given Dữ liệu mẫu đã được nạp với 6 danh mục chuẩn
    When Khách hàng mở trang chủ
    Then Hệ thống hiển thị danh sách 6 danh mục
    And Mỗi danh mục hiển thị đầy đủ Icon và Tên

  Scenario: Không có danh mục nào (Unhappy Path)
    Given Dữ liệu danh mục trong DB trống rỗng
    When Khách hàng mở trang chủ
    Then Hệ thống hiển thị thông báo "Chưa có danh mục nào"
