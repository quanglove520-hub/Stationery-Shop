## 1. Acceptance Test Chuẩn Bị (BDD)

- [x] 1.1 Cập nhật hoặc review lại file `admin_inventory.feature`. Vì đây là refactor kiến trúc, cần đảm bảo có sẵn scenario kiểm tra quyền truy cập/load dữ liệu UI để phát hiện hồi quy (regression) nếu thao tác Boundary -> Control -> Store đi sai. (Chỉ tạo Acceptance Test / step definition nếu thiếu).

## 2. Refactor Control Layer

- [x] 2.1 Cập nhật `AdminInventoryControl.java`: Bổ sung method `public List<Category> getCategories()` gọi tới `InMemoryStore.getInstance().getCategories()`.
- [x] 2.2 Cập nhật `AdminInventoryControl.java`: Bổ sung method `public List<Product> getProducts()` gọi tới `InMemoryStore.getInstance().getProducts()`.

## 3. Refactor Boundary Layer (UI)

- [x] 3.1 Mở `AdminPanel.java`, xóa lệnh `import stationary.store.InMemoryStore;`.
- [x] 3.2 Refactor logic Nút Xóa Category: Chuyển `InMemoryStore.getInstance().getCategories()` thành cú pháp sử dụng biến local `control.getCategories()`.
- [x] 3.3 Refactor logic Nút Xóa Product: Chuyển `InMemoryStore.getInstance().getProducts()` thành cú pháp sử dụng biến local `control.getProducts()`.

## 4. Verification & Unit Test

- [x] 4.1 Biên dịch lại toàn bộ dự án (`mvn clean compile`).
- [x] 4.2 Chạy lại toàn bộ test suite (BDD + Unit Tests) để đảm bảo việc đổi layer không gây ra NullPointerException hoặc bất định trên giao diện.
