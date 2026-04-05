Feature: Quản lý Hình thức Danh mục và Sản phẩm
  As an Admin, I want to thao tác CRUD trên danh mục sản phẩm và sản phẩm, 
  so that I can cấu trúc các nhóm mặt hàng trong cửa hàng hợp lý.

  Scenario: Xóa danh mục không chứa sản phẩm (Happy Path)
    Given Một Danh mục (Category) bất kỳ đang tồn tại trong hệ thống và KHÔNG chứa bất kỳ Sản phẩm nào
    When Admin chọn và yêu cầu Xóa danh mục nói trên
    Then Hệ thống xóa Danh mục đó thành công khỏi cơ sở dữ liệu

  Scenario: Xóa danh mục đang chứa sản phẩm (Unhappy Path)
    Given Một Danh mục (Category) bất kỳ đang chứa một hoặc nhiều Sản phẩm bên trong
    When Admin chọn và yêu cầu Xóa danh mục nói trên
    Then Hệ thống từ chối thao tác xóa "Không thể xóa do danh mục đang chứa sản phẩm"

  Scenario: Xóa sản phẩm trống đơn hàng (Happy Path)
    Given Một Sản phẩm (Product) bất kỳ đang tồn tại và chưa từng xuất hiện trong bất cứ Order nào
    When Admin chọn và yêu cầu Xóa sản phẩm đó
    Then Hệ thống xóa Sản phẩm hoàn toàn khỏi cơ sở dữ liệu

  Scenario: Xóa sản phẩm đang vướng đơn hàng (Unhappy Path)
    Given Một Sản phẩm (Product) bất kỳ đang có mặt trong ít nhất một Order
    When Admin chọn và yêu cầu Xóa sản phẩm đó
    Then Hệ thống từ chối thao tác xóa "Không thể xóa do sản phẩm đang tồn tại trong đơn hàng"
