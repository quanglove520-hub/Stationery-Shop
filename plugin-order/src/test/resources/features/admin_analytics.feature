Feature: Thống kê và hiển thị báo cáo cho Admin
  Là một Admin
  Tôi muốn xem dashboard với tổng doanh thu và top sản phẩm
  Để phân tích hiệu suất kinh doanh

  Scenario: Xem biểu đồ doanh thu thành công
    Given hệ thống có cấu hình "Analytics" nạp dữ liệu mẫu với các giao dịch thành công
    When Admin truy cập vào tab Báo cáo
    Then hệ thống trả lời báo cáo tổng doanh thu của từng ngày đúng theo dữ liệu mẫu

  Scenario: Xem biểu đồ sản phẩm bán chạy nhất
    Given hệ thống có cấu hình "Analytics" nạp dữ liệu mẫu với các giao dịch thành công
    When Admin truy cập vào Dashboard và yêu cầu top sản phẩm
    Then hệ thống trả về top các sản phẩm bán chạy nhất được sắp xếp từ cao xuống thấp
