Feature: Luồng thao tác Giỏ Hàng Simulator
  As a Khách hàng
  I want to đưa các mặt hàng vào giỏ và xem tổng tiền
  So that tôi có thể kiểm kê trước khi bấm thanh toán định danh.

  Scenario: Thêm thành công sản phẩm từ Danh mục vào Cart
    Given Hệ thống đã nạp dữ liệu mẫu
    And màn hình hiện tại là trang chủ "Danh Mục Sản Phẩm"
    When tôi nhấp vào thẻ bài "Bút - Viết"
    And tôi click Thêm vào giỏ hàng cho sản phẩm "Bút Bi Thiên Long"
    Then giỏ hàng của tôi sẽ có 1 mặt hàng "P301"

  Scenario: Vào CartUI và thấy tổng tiền tính chính xác
    Given Hệ thống đã nạp dữ liệu mẫu
    And màn hình hiện tại là trang chủ "Danh Mục Sản Phẩm"
    When tôi nhấp vào thẻ bài "Giấy"
    And tôi click Thêm vào giỏ hàng cho sản phẩm "Giấy A4 Double A"
    And tôi truy cập nút Giỏ Hàng trên cùng
    Then màn hình chuyển sang Giỏ Hàng
    And hệ thống hiển thị tổng tiền là "65000"
