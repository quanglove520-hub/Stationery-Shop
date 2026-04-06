## ADDED Requirements

### Requirement: Quản lý Danh mục (Category CRUD)
As an Admin, I want to thao tác CRUD trên danh mục sản phẩm.

#### Scenario: Xóa danh mục không chứa sản phẩm (Happy Path)
- **GIVEN** Một Danh mục (Category) bất kỳ đang tồn tại trong hệ thống và KHÔNG chứa bất kỳ Sản phẩm nào
- **WHEN** Admin chọn và yêu cầu Xóa danh mục nói trên
- **THEN** Hệ thống xóa Danh mục đó thành công khỏi cơ sở dữ liệu

#### Scenario: Xóa danh mục đang chứa sản phẩm (Unhappy Path)
- **GIVEN** Một Danh mục (Category) bất kỳ đang chứa một hoặc nhiều Sản phẩm bên trong
- **WHEN** Admin chọn và yêu cầu Xóa danh mục nói trên
- **THEN** Hệ thống từ chối thao tác xóa "Không thể xóa do danh mục đang chứa sản phẩm"

### Requirement: Quản lý Sản phẩm (Product CRUD)
As an Admin, I want to thao tác CRUD trên thông tin sản phẩm.

#### Scenario: Xóa sản phẩm trống đơn hàng (Happy Path)
- **GIVEN** Một Sản phẩm (Product) bất kỳ đang tồn tại và chưa từng xuất hiện trong bất cứ Order nào
- **WHEN** Admin chọn và yêu cầu Xóa sản phẩm đó
- **THEN** Hệ thống xóa Sản phẩm hoàn toàn khỏi cơ sở dữ liệu

#### Scenario: Xóa sản phẩm đang vướng đơn hàng (Unhappy Path)
- **GIVEN** Một Sản phẩm (Product) bất kỳ đang có mặt trong ít nhất một Order
- **WHEN** Admin chọn và yêu cầu Xóa sản phẩm đó
- **THEN** Hệ thống từ chối thao tác xóa "Không thể xóa do sản phẩm đang tồn tại trong đơn hàng"
