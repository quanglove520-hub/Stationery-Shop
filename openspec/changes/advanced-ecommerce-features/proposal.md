## Why

Hệ thống Văn Phòng Phẩm hiện tại tuy đã hiển thị được sản phẩm nhưng các chức năng phân loại và tương tác của khách hàng với mặt hàng vẫn còn khá sơ sài. Để giúp hệ thống hoàn thiện hơn, hấp dẫn người dùng và tối ưu vận hành lớn, chúng ta cần bổ sung thêm các tính năng thương mại điện tử chuyên sâu: Phân loại sản phẩm nâng cao, Mã giảm giá, Wishlist, Kỹ thuật Infinite Scroll Lazy Load, Server & Client Cache, và UX Animation mượt mà.

## What Changes

- Phân loại mặt hàng chi tiết theo thẻ danh mục.
- Bổ sung tính năng Mã giảm giá (Discount vouchers/Coupons) giả lập và hiển thị form.
- Nâng cấp Modal Chi tiết sản phẩm: Tích hợp Image Gallery, Zoom in, Reviews ảo, Sản phẩm Gợi ý (Related Products).
- Thay thế pagination click sang Infinite Scroll Pagination bằng JS `IntersectionObserver`.
- Thêm Cache trên JVM server & Client `sessionStorage`.
- Bổ sung UI/UX cao cấp: Skeleton Screens, Toast Notification, Heart Icon Wishlist.

## Capabilities

### New Capabilities

- `discount-codes`: Áp dụng mã giảm giá làm giảm hóa đơn.
- `advanced-categories`: Lọc mặt hàng chi tiết theo các danh mục phụ.
- `product-reviews`: Hiển thị đánh giá và sản phẩm liên quan.
- `user-wishlist`: Tính năng thêm sản phẩm yêu thích (lưu Local).
- `ux-elevate`: Cài đặt Skeleton Load, Load lazy auto append, Toast notification và Image Gallery.

### Modified Capabilities

- `view-products`: Sửa logic load page, thêm Cache cấp dịch vụ và phân loại.

## Impact

- **Database Layer**: Gắn thêm thuộc tính `additionalImages` vào array products In-Memory.
- **Backend API**: Thêm bộ Controller và logic Service xử lý Pagination cache, Coupon, và Related Products.
- **Web Frontend**: Thay đổi 90% logic Fetch ở `app.js`, cải tạo `.product-grid`, xử lý Notification, IntersectionObserver và LocalStorage cho Wishlist.
