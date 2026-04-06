## Context
Người dùng cần một không gian giỏ hàng ảo (Cart session) để lưu các măt hàng được thêm từ danh sách, cùng với một trang chi tiết Giỏ hàng (CartUI) để tổng kết.

## Goals
- Cung cấp Cart Session tại Runtime.
- Xem danh sách mặt hàng tại CartUI và tổng tiền.
- Clearing session khi Checkout.

## Non-Goals
- Không tạo Checkout Logic (Payment Gateway / Database logging).
- Không Auth/Login.
