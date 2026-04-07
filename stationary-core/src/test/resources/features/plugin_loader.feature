Feature: Nạp các module Plugin qua giao thức SPI
  As a Core Developer
  I want hệ thống tự nạp plugin thông qua ServiceLoader
  So that tôi có thể linh hoạt rút cắm chức năng mà không động vào system core

  Scenario: Tự động nạp thành công plugin khi được đăng ký vào Meta-inf
    Given Hệ thống có một TestPlugin được thiết lập hợp lệ
    When Core Application tiến hành nạp Plugin SPI
    Then Hệ thống ghi nhận có 1 plugin hoạt động
    And Tên plugin là "Test Plugin"
    And Hệ thống đã gọi hàm khởi tạo DB của plugin đó

  Scenario: Hệ thống khởi chạy an toàn khi rỗng Plugin
    Given Hệ thống không có bất kỳ cấu hình META-INF plugin nào trong Context
    When Core Application tiến hành nạp Plugin SPI
    Then Hệ thống ghi nhận có 0 plugin
    And Spark Server khởi động an toàn
