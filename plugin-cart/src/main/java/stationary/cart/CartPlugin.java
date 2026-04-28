package stationary.cart;

import stationary.core.db.MemoryDB;
import stationary.core.spi.StationaryPlugin;
import stationary.control.CustomerOrderControl;
import stationary.control.CatalogControl;
import stationary.store.SqlServerOrderStore;
import com.google.gson.Gson;
import static spark.Spark.*;

public class CartPlugin implements StationaryPlugin {
    
    private CustomerOrderControl orderControl;
    private final Gson gson = new Gson();

    @Override
    public String getName() {
        return "Shopping Cart Plugin";
    }

    @Override
    public void onInitData(MemoryDB coreDb) {
        orderControl = new CustomerOrderControl();
        // Cần truyền CatalogControl để tra cứu giá sản phẩm khi tính tiền
        orderControl.setCatalogControl(new CatalogControl());
        // Cần truyền OrderStore để lưu trữ đơn hàng bền vững
        orderControl.setOrderStore(SqlServerOrderStore.getInstance());
    }

    @Override
    public void registerRoutes() {
        path("/api/cart", () -> {
            get("", (req, res) -> {
                res.type("application/json; charset=utf-8");
                return gson.toJson(orderControl.getCartItems());
            });

            post("/add", (req, res) -> {
                res.type("application/json; charset=utf-8");
                String productId = req.queryParams("productId");
                int qty = Integer.parseInt(req.queryParams("quantity"));
                orderControl.addToCart(productId, qty);
                return "{\"status\":\"success\"}";
            });

            post("/checkout", (req, res) -> {
                res.type("application/json; charset=utf-8");
                try {
                    orderControl.checkout();
                    return "{\"status\":\"success\", \"message\":\"Đơn hàng đã được lưu trữ vĩnh viễn vào hệ thống.\"}";
                } catch (Exception e) {
                    res.status(400);
                    return "{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}";
                }
            });
        });
    }
}
