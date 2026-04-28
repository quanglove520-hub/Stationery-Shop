package stationary.order;

import stationary.core.db.MemoryDB;
import stationary.core.spi.StationaryPlugin;
import stationary.control.AnalyticsControl;
import stationary.store.OrderStore;
import stationary.store.SqlServerOrderStore;
import stationary.entity.Order;
import stationary.entity.OrderLineItem;
import com.google.gson.Gson;
import static spark.Spark.*;

public class OrderPlugin implements StationaryPlugin {
    
    private final OrderStore orderStore;
    private final AnalyticsControl analyticsControl;
    private final Gson gson = new Gson();

    public OrderPlugin() {
        // Tạm thời sử dụng InMemoryStore cho demo
        stationary.store.InMemoryStore mockStore = stationary.store.InMemoryStore.getInstance();
        
        // Dữ liệu mẫu cực kỳ sống động cho 7 ngày gần nhất
        try {
            mockStore.getOrders().clear(); // Clear old demo data if any
            java.util.Random rnd = new java.util.Random();
            for (int i = 6; i >= 0; i--) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.DATE, -i);
                
                stationary.entity.Order o = new stationary.entity.Order("ORD_DEMO_" + i, new java.util.ArrayList<>());
                o.setStatus("COMPLETED");
                o.setTotalAmount(200000 + rnd.nextInt(800000)); // 200k - 1M
                o.setOrderDate(new java.sql.Timestamp(cal.getTimeInMillis()));
                
                // Thêm vài sản phẩm mẫu cho biểu đồ Top Products
                stationary.entity.OrderLineItem item = new stationary.entity.OrderLineItem("S1", 1 + rnd.nextInt(5));
                item.setProductName("Bút bi Thiên Long");
                item.setPrice(5000.0);
                o.getItems().add(item);
                
                mockStore.saveOrder(o);
            }
        } catch (Exception e) {}

        this.orderStore = mockStore;
        this.analyticsControl = new AnalyticsControl();
        this.analyticsControl.setStore(this.orderStore);
    }

    @Override
    public String getName() {
        return "Order Management & Analytics Plugin";
    }

    @Override
    public void onInitData(MemoryDB coreDb) {
    }

    @Override
    public void registerRoutes() {
        // API Báo cáo cho Admin
        path("/api/admin/analytics", () -> {
            get("/revenue", (req, res) -> {
                res.type("application/json; charset=utf-8");
                return gson.toJson(analyticsControl.getRevenueByDate());
            });
            
            get("/top-products", (req, res) -> {
                res.type("application/json; charset=utf-8");
                return gson.toJson(analyticsControl.getTopSellingProducts(5));
            });
        });

        // API Thanh toán cho Khách hàng
        post("/api/checkout", (req, res) -> {
            res.type("application/json; charset=utf-8");
            try {
                Order order = gson.fromJson(req.body(), Order.class);
                if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
                    res.status(400);
                    return "{\"status\":\"error\",\"message\":\"Giỏ hàng trống\"}";
                }
                
                // Mặc định tạo mã đơn hàng nếu chưa có
                if (order.getId() == null || order.getId().isEmpty()) {
                    order.setId("ORD-" + System.currentTimeMillis());
                }
                
                // 1. Lưu đơn hàng
                orderStore.saveOrder(order);
                
                // 2. Gửi Email thông báo
                stationary.order.service.EmailService emailService = new stationary.order.service.EmailService();
                emailService.sendOrderConfirmation(order);
                
                return "{\"status\":\"success\",\"message\":\"Thanh toán thành công! Đã gửi mail xác nhận.\", \"orderId\":\"" + order.getId() + "\"}";
            } catch (Exception e) {
                res.status(500);
                return "{\"status\":\"error\",\"message\":\"Lỗi hệ thống: " + e.getMessage() + "\"}";
            }
        });
    }

    public OrderStore getOrderStore() {
        return orderStore;
    }
}
