Feature: Lưu trữ lịch sử giao dịch
  Là hệ thống
  Tôi muốn lưu trữ giao dịch khi khách hàng thanh toán
  Để có dữ liệu lịch sử cho báo cáo doanh số

  Scenario: Lưu đơn hàng thành công khi checkout
    Given hệ thống có cấu hình "Order Persistence" nạp dữ liệu mẫu
    And hệ thống có giỏ hàng hiện tại chứa sản phẩm "Bút Bi Thiên Long" số lượng 2 với giá 5000
    When hàm checkout được thực thi
    Then hệ thống lưu đối tượng Order vào bộ nhớ với tổng giá là 10000
    And hệ thống xóa sạch giỏ hàng sau khi thanh toán
