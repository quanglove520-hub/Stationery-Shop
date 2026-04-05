## Context

Đây là story đầu tiên của Web Văn Phòng Phẩm — làm tiền đề cho hệ thống E-commerce bán lẻ. Cần dựng:
1. Khung ứng dụng Web (Frontend Client) hiển thị danh sách các mặt hàng văn phòng phẩm.
2. Kiến trúc Backend MVC tiêu biển (Controller-Service-Repository) trên Spring Boot.
3. Cơ sở dữ liệu in-memory siêu nhẹ H2 Database.
4. Logic giao diện để xử lý trạng thái kho hàng rỗng ("No data").

## Goals / Non-Goals

**Goals:**
- Dựng giao diện Web danh sách mặt hàng dưới dạng khối lưới (Grid layout).
- Dựng giao diện xem thông tin sản phẩm.
- Liên kết Web Frontend tới Backend bằng API JSON.
- Hiển thị thông báo "No data" lên màn hình khi Data API trả về mảng trống.
- Thiết lập nền móng kỹ thuật cho toàn dự án.

**Non-Goals:**
- Thêm hàng vào Giỏ (Cart) (story sau).
- Bấm nút Mua/Trang Thanh toán (Checkout).
- Hệ thống Đăng nhập / User roles.

## Decisions

### Decision 1: Lựa chọn Spring Boot và in-memory H2 DB
**Chọn:** Khởi tạo lớp Backend tự chạy bằng Spring Boot và DB H2.
**Lý do:** Đây là tech-stack yêu cầu để tương tác dễ nhất ở local cũng như hợp với việc test tự động trên nền tảng Antigravity mà không cần setup máy chủ MySQL hay XAMPP rườm rà.

### Decision 2: Phương án giải quyết mảng DB rỗng
**Chọn:** Khởi tạo Component Frontend riêng `EmptyState` sẽ được kích hoạt thay vì vòng lặp danh sách mặc định.
**Lý do:** Tăng thẩm mỹ cho Use case rỗng thay vì ném ra màn hình lỗi HTTP hoặc trang web trắng bóc. Nâng cao trải nghiệm User Story.

## Use Case

```
Use Case:     Xem danh sách các sản phẩm văn phòng phẩm
Actor:        Khách hàng tiềm năng (Customer)
Input:        Người dùng truy cập vào trang Shop / Trang chủ
Output:       Màn hình render giao diện danh sách đồ dùng
              HOẶC render khối thông báo "No data" đặc thù.

Main Flow:
  1. Khách hàng bật trình duyệt và truy cập trang sản phẩm.
  2. Web Client gửi truy vấn GET request đến Backend API server.
  3. ProductController yêu cầu ProductService lấy danh sách từ H2 DB.
  4. Web Client nhận được Data và chuyển từ Loading sang HAS_DATA.
  5. Màn hình tự động chèn các sản phẩm (tên, giá thẻ) vào giao diện khung lưới.

Alternative Flows:
  (Không có)

Exception Flows:
  E1. Database đang bị xoá sạch dữ liệu (tại bước 3):
    E1.1. Web Client nhận HTTP 200 nhưng nội dung mảng là [].
    E1.2. Mạch giao diện rẽ sang trạng thái EMPTY.
    E1.3. Màn hình in ra dòng chữ "No data" và ảnh minh hoạ buồn bã.
```

## Architecture Diagram (BCE inspired)

```
┌─────────────────────────────────────────────────────────────────┐
│                     Web Client (Frontend)                       │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                 «Boundary & View»                         │  │
│  │                                                           │  │
│  │  ┌──────────────┐  ┌──────────────────────────────────┐  │  │
│  │  │ HTTP Client  │  │ User Interface (DOM View)        │  │  │
│  │  │ (Fetch API)  │──│                                  │  │  │
│  │  │              │  │ + renderProductList(data)        │  │  │
│  │  │ - request()  │  │ + renderEmptyState("No data")    │  │  │
│  │  │              │  │ + renderProductDetail(id)        │  │  │
│  │  └──────────────┘  └──────────────────────────────────┘  │  │
│  └───────────────────────────────────┬───────────────────────┘  │
│                                      │ GET /api/v1/products     │
├──────────────────────────────────────┼──────────────────────────┤
│                                      │                          │
│  ┌───────────────────────────────────▼───────────────────────┐  │
│  │               Spring Boot Backend (API)                   │  │
│  │                                                           │  │
│  │  ┌────────────────────────┐  ┌────────────────────────┐   │  │
│  │  │ «Control»              │  │ «Entity»               │   │  │
│  │  │ ProductController      │  │ Product                │   │  │
│  │  │ + getProducts()        │  │ - id, name, price      │   │  │
│  │  │ + getProduct(id)       │  │ - stock, category      │   │  │
│  │  └───┬────────────────────┘  └──────▲─────────────────┘   │  │
│  │      │                              │                     │  │
│  │      │ uses                         │                     │  │
│  │      ▼                              │ mapped by           │  │
│  │  ┌────────────────────────┐         │                     │  │
│  │  │ «Service»              │         │                     │  │
│  │  │ ProductService         │         │                     │  │
│  │  │ + fetchAllProducts()   │         │                     │  │
│  │  └───┬────────────────────┘         │                     │  │
│  │      │                              │                     │  │
│  │      │ uses                         │                     │  │
│  │      ▼                              │                     │  │
│  │  ┌───────────────────────────┐      │                     │  │
│  │  │ «Repository/Boundary»     │      │                     │  │
│  │  │ ProductRepository         ├──────┘                     │  │
│  │  │ (JpaRepository)           │                            │  │
│  │  └───────────────────────────┘                            │  │
│  └───────────────────────────────────┬───────────────────────┘  │
│                                      │ JPA / SQL Layer          │
├──────────────────────────────────────┼──────────────────────────┤
│                                      │                          │
│  ┌───────────────────────────────────▼───────────────────────┐  │
│  │                In-memory H2 Database                      │  │
│  │                  (Table: PRODUCTS)                        │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## State Diagram (Frontend Flow)

```
                 ┌─────────┐
                 │ INITIAL │
                 └────┬────┘
                      │ Truy cập trang chủ shop
                      ▼
              ┌────────────────┐
              │    LOADING     │
              │ (Fetch API)    │
              └───┬────────┬───┘
                  │        │
     Kết quả trả về        Kết quả trả về
    Array có dữ liệu       Array trống []
                  │        │
                  ▼        ▼
           ┌────────┐   ┌─────────────┐
           │        │   │             │
           │HAS_DATA│   │ NO_DATA     │
           │        │   │             │
           └────────┘   └─────────────┘
          (Vẽ Card SP)  (In ra màn hình "No data")
```

## Sequence Diagram — Happy Path

```
  Customer      Browser (UI)     ProductController    ProductService       H2 DB
   │               │                   │                  │                  │
   │ Vào trang Web │                   │                  │                  │
   │──────────────▶│                   │                  │                  │
   │               │ GET /products     │                  │                  │
   │               │──────────────────▶│                  │                  │
   │               │                   │ fetchAllProducts │                  │
   │               │                   │─────────────────▶│                  │
   │               │                   │                  │ SELECT * ...     │
   │               │                   │                  │─────────────────▶│
   │               │                   │                  │   [Records]      │
   │               │                   │                  │◀─────────────────│
   │               │                   │  List<Product>   │                  │
   │               │                   │◀─────────────────│                  │
   │               │ 200 OK (JSON)     │                  │                  │
   │               │◀──────────────────│                  │                  │
   │               │                   │                  │                  │
   │ render View   │                   │                  │                  │
   │◀──────────────│                   │                  │                  │
   │ Hiển thị Bút, │                   │                  │                  │
   │ Giấy, Tẩy...  │                   │                  │                  │
```

## Sequence Diagram — Unhappy Path (Sản phẩm rỗng)

```
  Customer      Browser (UI)     ProductController    ProductService       H2 DB
   │               │                   │                  │                  │
   │ Vào trang Web │                   │                  │                  │
   │──────────────▶│                   │                  │                  │
   │               │ GET /products     │                  │                  │
   │               │──────────────────▶│                  │                  │
   │               │                   │ fetchAllProducts │                  │
   │               │                   │─────────────────▶│                  │
   │               │                   │                  │ SELECT * ...     │
   │               │                   │                  │─────────────────▶│
   │               │                   │                  │    Empty []      │
   │               │                   │                  │◀─────────────────│
   │               │                   │   Empty List     │                  │
   │               │                   │◀─────────────────│                  │
   │               │ 200 OK (JSON [])  │                  │                  │
   │               │◀──────────────────│                  │                  │
   │               │                   │                  │                  │
   │ render "No    │                   │                  │                  │
   │ data" State   │                   │                  │                  │
   │◀──────────────│                   │                  │                  │
```

## Project Structure

```
stationery-shop/
├── backend/
│   ├── src/main/java/com/stationery/shop/
│   │   ├── controller/
│   │   │   └── ProductController.java       # Control Layer (REST API)
│   │   ├── service/
│   │   │   ├── ProductService.java          # Service interface
│   │   │   └── ProductServiceImpl.java      # Service logic xử lý
│   │   ├── repository/
│   │   │   └── ProductRepository.java       # JpaRepository giao tiếp DB (Boundary)
│   │   └── entity/
│   │       └── Product.java                 # Ánh xạ CSDL
│   └── src/main/resources/
│       └── application.properties           # Khai báo H2 config
│
├── frontend/
│   ├── index.html                           # Giao diện chính cho Khách hàng
│   ├── app.js                               # Logic fetch() & chuyển đổi state
│   └── style.css                            # CSS Style cho Card và Empty State
```

## Color Scheme

| Component | Background | Text | Ghi chú |
|-----------|-----------|------|---------|
| Page Body | `#F5F7FA` Light Gray | `#333333` Dark | Khác biệt thân thiện |
| Product Card | `#FFFFFF` White | `#212121` Black | Thẻ bo tròn nền trắng |
| Brand Color/Buttons| `#8A4FFF` Purple | `#FFFFFF` White | Nút Hành động nổi bật |
| Empty State | `#ECEFF1` Blue Grey | `#78909C` Muted | Hiển thị chữ No data |
| Prices | `Transparent` | `#E53935` Red | Thu hút ánh nhìn giá cả |

## Screen Mockup — Danh mục sản phẩm (Happy Path)

```
╔══════════════════════════════════════════════════════════════════╗
║  [Tùy chọn] SHOP VĂN PHÒNG PHẨM                     🔍 Search... ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║   DANH MỤC MẶT HÀNG MỚI                                          ║
║                                                                  ║
║   ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
║   │  [Hình Bút Bi]   │  │  [Hình Tẩy]      │  │  [Hình Sổ Tay]   │
║   │                  │  │                  │  │                  │
║   │ Bút bi Thiên Long│  │ Tẩy Gôm Trắng    │  │ Sổ tay Campus B5 │
║   │ $0.50            │  │ $1.00            │  │ $3.50            │
║   │                  │  │                  │  │                  │
║   │   [CHI TIẾT]     │  │   [CHI TIẾT]     │  │   [CHI TIẾT]     │
║   └──────────────────┘  └──────────────────┘  └──────────────────┘
║                                                                  ║
║   ┌──────────────────┐  ┌──────────────────┐                     ║
║   │  [Hình Balo]     │  │  [Hình Thước]    │                     ║
║   │                  │  │                  │                     ║
║   │ Balo sinh viên   │  │ Thước kẻ 30cm    │                     ║
║   │ $15.00           │  │ $0.80            │                     ║
║   │                  │  │                  │                     ║
║   │   [CHI TIẾT]     │  │   [CHI TIẾT]     │                     ║
║   └──────────────────┘  └──────────────────┘                     ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

## Screen Mockup — Trống Rỗng (Unhappy Path - No Data)

```
╔══════════════════════════════════════════════════════════════════╗
║  [Tùy chọn] SHOP VĂN PHÒNG PHẨM                     🔍 Search... ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║                                                                  ║
║                                                                  ║
║                      ┌────────────────────┐                      ║
║                      │                    │                      ║
║                      │    (✖╭╮✖)          │                      ║
║                      │                    │                      ║
║                      │     NO DATA        │                      ║
║                      │                    │                      ║
║                      │  Không có sản phẩm │                      ║
║                      │  để hiển thị       │                      ║
║                      └────────────────────┘                      ║
║                                                                  ║
║                                                                  ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

## Risks / Trade-offs

| Risk | Mitigation |
|------|------------|
| Database in-memory H2 bị mất dữ liệu khi restart code | Dùng file `data.sql` để seed dữ liệu dummy bút thước mỗi lần boot |
| Frontend phải xử lý 2 trạng thái màn hình | Dựng code bằng các hàm render logic riêng biệt (`renderItems`, `renderEmpty`), tránh trộn lộn |
| Mảng trống làm vỡ giao diện website | Thiết lập CSS min-height cứng cho container giữ form giao diện |
