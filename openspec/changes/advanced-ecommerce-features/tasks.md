## 1. Cơ sở dữ liệu và Cấu trúc Data (In-memory)

- [ ] 1.1 Khởi tạo các bảng/Data List `categories`, `coupons`, `product_reviews`. Bổ sung `additionalImages` vào array.
- [ ] 1.2 Tạo Entity `Category`, Cập nhật Entity `Product` (Thêm categoryId, tags dạng text).
- [ ] 1.3 Cấu hình Data Seed (`data.sql` / Seed script) với dữ liệu hình ảnh, review.

## 2. Tính năng Lọc và Tối ưu Backend (Cache & Pagination API)

- [ ] 2.1 Viết API `/api/v1/products` hỗ trợ tham số page, size, category.
- [ ] 2.2 Triển khai Basic Caching bằng `ConcurrentHashMap` trong `ProductService` cho các request page 0.
- [ ] 2.3 Viết API gán Coupon + Related Products (`GET /api/v1/products/{id}/related`).

## 3. Web UI UX, Wishlist và Skeleton (Frontend JS)

- [ ] 3.1 Xóa Pagination cũ, thay bằng IntersectionObserver bắt action kéo cuộn đáy trang (Infinite Scroll Lazy Load).
- [ ] 3.2 HTML/CSS: Thiết lập vùng Skeleton Loading, Template Toast Notification.
- [ ] 3.3 Thiết kế lại Modal Layout: Gắn lưới Image Gallery scale zoom-in, thêm list hàng gợi ý.
- [ ] 3.4 JS: Viết luồng `WishlistManager` save vào browser bộ đệm, bắt click Heart icon.
- [ ] 3.5 Tích hợp Toast notification và Response Caching.
