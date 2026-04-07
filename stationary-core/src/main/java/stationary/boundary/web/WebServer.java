package stationary.boundary.web;

import com.google.gson.Gson;
import stationary.control.CatalogControl;
import stationary.control.CustomerOrderControl;
import stationary.control.AdminInventoryControl;
import stationary.entity.Product;
import stationary.entity.OrderLineItem;
import stationary.entity.Category;
import stationary.entity.OrderLineItem;

import java.util.List;
import static spark.Spark.*;

public class WebServer {
    private static CatalogControl catalogControl = new CatalogControl();
    private static CustomerOrderControl orderControl = new CustomerOrderControl();
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/public");

        // API Catalog
        get("/api/categories", (req, res) -> {
            res.type("application/json");
            return catalogControl.getCategories();
        }, gson::toJson);

        get("/api/products", (req, res) -> {
            res.type("application/json");
            String catId = req.queryParams("categoryId");
            return catalogControl.getProductsByCategory(catId);
        }, gson::toJson);

        get("/api/products/:id", (req, res) -> {
            res.type("application/json");
            Product p = catalogControl.getProductById(req.params(":id"));
            if(p == null) {
                res.status(404);
                return "{\"error\":\"Not found\"}";
            }
            return p;
        }, gson::toJson);

        // API Cart
        get("/api/cart", (req, res) -> {
            res.type("application/json");
            List<OrderLineItem> items = orderControl.getCartItems();
            double total = orderControl.calculateTotal();
            return new CartResponse(items, total);
        }, gson::toJson);

        post("/api/cart/add", (req, res) -> {
            res.type("application/json");
            AddToCartRequest reqBody = gson.fromJson(req.body(), AddToCartRequest.class);
            orderControl.addToCart(reqBody.productId, reqBody.quantity);
            return "{\"status\":\"ok\"}";
        });

        post("/api/cart/checkout", (req, res) -> {
            res.type("application/json");
            orderControl.checkout();
            return "{\"status\":\"ok\"}";
        });

        // ================= API ADMIN ================= //
        AdminInventoryControl adminControl = new AdminInventoryControl();

        post("/api/admin/categories", (req, res) -> {
            res.type("application/json");
            Category reqBody = gson.fromJson(req.body(), Category.class);
            adminControl.addCategory(reqBody.getId(), reqBody.getName(), reqBody.getIconPath());
            return "{\"status\":\"ok\"}";
        });

        put("/api/admin/categories/:id", (req, res) -> {
            res.type("application/json");
            Category reqBody = gson.fromJson(req.body(), Category.class);
            adminControl.updateCategory(req.params(":id"), reqBody.getName(), reqBody.getIconPath());
            return "{\"status\":\"ok\"}";
        });

        delete("/api/admin/categories/:id", (req, res) -> {
            res.type("application/json");
            try {
                adminControl.deleteCategory(req.params(":id"));
                return "{\"status\":\"ok\"}";
            } catch (Exception e) {
                res.status(400);
                return "{\"error\":\"" + e.getMessage() + "\"}";
            }
        });

        post("/api/admin/products", (req, res) -> {
            res.type("application/json");
            Product reqBody = gson.fromJson(req.body(), Product.class);
            adminControl.addProduct(reqBody.getId(), reqBody.getCategoryId(), reqBody.getName(), reqBody.getPrice(), reqBody.getStock());
            return "{\"status\":\"ok\"}";
        });

        put("/api/admin/products/:id", (req, res) -> {
            res.type("application/json");
            Product reqBody = gson.fromJson(req.body(), Product.class);
            adminControl.updateProduct(req.params(":id"), reqBody.getCategoryId(), reqBody.getName(), reqBody.getPrice(), reqBody.getStock());
            return "{\"status\":\"ok\"}";
        });

        delete("/api/admin/products/:id", (req, res) -> {
            res.type("application/json");
            try {
                adminControl.deleteProduct(req.params(":id"));
                return "{\"status\":\"ok\"}";
            } catch (Exception e) {
                res.status(400);
                return "{\"error\":\"" + e.getMessage() + "\"}";
            }
        });
        
        System.out.println("System Initialized! Go to http://localhost:4567");
    }

    private static class CartResponse {
        List<OrderLineItem> items;
        double total;
        CartResponse(List<OrderLineItem> items, double total) {
            this.items = items;
            this.total = total;
        }
    }

    private static class AddToCartRequest {
        String productId;
        int quantity;
    }
}
