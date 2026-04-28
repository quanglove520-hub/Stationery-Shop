Feature: Quản lý và Xóa Tài Khoản bởi Admin

  Scenario: Admin xem thông tin tài khoản mà không thấy mật khẩu
    Given Hệ thống có người đăng nhập đóng vai trò "Admin"
    And Hệ thống lưu trữ tài khoản "Nguyen Van A" mật khẩu "secret123"
    When Admin truy cập chi tiết thông tin của "Nguyen Van A"
    Then Giao diện trả về Data Transfer Object của khách hàng
    And Mật khẩu "secret123" tuyệt đối KHÔNG được trả về UI

  Scenario: Xóa tài khoản người dùng chưa có đơn hàng
    Given Hệ thống lưu trữ tài khoản "User_Moi"
    And "User_Moi" chưa từng đặt bất kỳ thiết bị văn phòng phẩm nào
    When Admin click nút Xóa tài khoản "User_Moi"
    Then Tài khoản "User_Moi" bị xóa khỏi danh sách cơ sở dữ liệu

  Scenario: Thất bại khi xóa người dùng có lịch sử mua hàng
    Given Hệ thống lưu trữ tài khoản "User_Cu"
    And "User_Cu" đã từng đặt 1 đơn hàng "Bút bi" trong quá khứ
    When Admin click nút Xóa tài khoản "User_Cu"
    Then Lệnh xóa bị từ chối
    And Hệ thống ném ra lỗi "Không thể xóa: người dùng hệ thống đã có lịch sử đặt hàng"
