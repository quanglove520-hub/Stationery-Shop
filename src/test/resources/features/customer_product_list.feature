Feature: Xem Danh sách Sản phẩm
  As a Khách hàng
  I want to xem danh sách các sản phẩm khi click vào một Danh mục ở Trang chủ
  So that tôi biết chi tiết từng mặt hàng và giá cả để mua.

  Scenario: Khách hàng xem danh sách sản phẩm của danh mục có chứa sản phẩm
    Given Hệ thống đã nạp dữ liệu mẫu
    And màn hình hiện tại là trang chủ "Danh Mục Sản Phẩm"
    When tôi nhấp vào thẻ bài "Bút - Viết"
    Then giao diện được chuyển sang Sản phẩm của "Bút - Viết"
    And tôi nhìn thấy thẻ sản phẩm "Bút Bi Thiên Long" nằm trong danh sách

  Scenario: Khách hàng xem danh mục rỗng
    Given Hệ thống đã nạp dữ liệu mẫu
    And màn hình hiện tại là trang chủ "Danh Mục Sản Phẩm"
    When tôi nhấp vào thẻ bài "Kéo - Dao rọc giấy"
    Then giao diện được chuyển sang Sản phẩm của "Kéo - Dao rọc giấy"
    And hệ thống hiển thị thông báo "Chưa có sản phẩm nào thuộc danh mục này"
