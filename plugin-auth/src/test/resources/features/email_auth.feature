Feature: Xác thực bằng Email (Email Authentication)

  Scenario: Định dạng Email hoặc Password sai khi đăng ký
    Given tôi đang ở màn hình Đăng ký
    When tôi nhập email "invalid-email", username "testuser" và mật khẩu "123" và xác nhận "123"
    And tôi nhấn nút Đăng ký
    Then hệ thống thông báo "định dạng không hợp lệ"

  Scenario: Đăng ký với Email đã tồn tại
    Given tài khoản email "testuser@example.com" với mật khẩu "password123" và username "testuser" đã tồn tại
    And tôi đang ở màn hình Đăng ký
    When tôi nhập email "testuser@example.com", username "newuser" và mật khẩu "password123" và xác nhận "password123"
    And tôi nhấn nút Đăng ký
    Then hệ thống báo lỗi "Email đã được sử dụng"

  Scenario: Đăng ký thành công
    Given tôi đang ở màn hình Đăng ký
    When tôi nhập email "newuser@example.com", username "newuser" và mật khẩu "password123" và xác nhận "password123"
    And tôi nhấn nút Đăng ký
    Then hệ thống thông báo "Đăng ký thành công"
    And tôi được chuyển sang màn hình Đăng nhập

  Scenario: Đăng nhập với tài khoản không tồn tại
    Given tôi đang ở màn hình Đăng nhập
    When tôi nhập email "notfound@example.com" và mật khẩu "password123"
    And tôi nhấn nút Đăng nhập
    Then hệ thống báo lỗi "Email hoặc mật khẩu không chính xác"

  Scenario: Đăng nhập với mật khẩu sai
    Given tài khoản email "testuser@example.com" với mật khẩu "password123" và username "testuser" đã tồn tại
    And tôi đang ở màn hình Đăng nhập
    When tôi nhập email "testuser@example.com" và mật khẩu "wrongpass"
    And tôi nhấn nút Đăng nhập
    Then hệ thống báo lỗi "Email hoặc mật khẩu không chính xác"

  Scenario: Đăng nhập thành công
    Given tài khoản email "testuser@example.com" với mật khẩu "password123" và username "testuser" đã tồn tại
    And tôi đang ở màn hình Đăng nhập
    When tôi nhập email "testuser@example.com" và mật khẩu "password123"
    And tôi nhấn nút Đăng nhập
    Then hệ thống thông báo "Đăng nhập thành công"
    And tôi được chuyển vào Trang chủ
