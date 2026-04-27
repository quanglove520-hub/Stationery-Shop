package stationary.ecommerce;

import stationary.core.spi.StationaryPlugin;
import stationary.core.db.MemoryDB;
import stationary.ecommerce.control.EcommerceControl;
import static spark.Spark.*;

public class EcommercePlugin implements StationaryPlugin {
    private EcommerceControl ecommerceControl;

    @Override
    public String getName() {
        return "Advanced Ecommerce Plugin";
    }

    @Override
    public void onInitData(MemoryDB db) {
        ecommerceControl = new EcommerceControl();
    }

    @Override
    public void registerRoutes() {
        // Ecommerce Routes API
        
        post("/api/ecommerce/cart", (req, res) -> {
            res.type("application/json");
            return "{\"status\":\"ok\", \"message\":\"Cart checkout stub\"}";
        });

        post("/api/ecommerce/wishlist", (req, res) -> {
            res.type("application/json");
            // Thao tác logic thêm/xóa wishlist Server-side có thể triển khai ở đây
            return "{\"status\":\"ok\"}";
        });

        post("/api/ecommerce/discount/apply", (req, res) -> {
            res.type("application/json");
            String code = req.queryParams("code");
            if ("SALE10".equalsIgnoreCase(code)) {
                return "{\"status\":\"success\", \"discountRate\": 0.1, \"message\":\"Áp dụng SALE10 thành công!\"}";
            }
            res.status(400);
            return "{\"status\":\"error\", \"message\":\"Mã không hợp lệ\"}";
        });

        get("/api/ecommerce/products/:id/reviews", (req, res) -> {
            res.type("application/json");
            String productId = req.params(":id");
            java.util.List<stationary.ecommerce.entity.ProductReview> reviews = ecommerceControl.getProductReviews(productId);
            // using simple manual conversion or standard string builder since GSON might not be imported 
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < reviews.size(); i++) {
                stationary.ecommerce.entity.ProductReview r = reviews.get(i);
                sb.append("{")
                  .append("\"id\":\"").append(r.getId()).append("\",")
                  .append("\"productId\":\"").append(r.getProductId()).append("\",")
                  .append("\"author\":\"").append(r.getAuthor()).append("\",")
                  .append("\"rating\":").append(r.getRating()).append(",")
                  .append("\"comment\":\"").append(r.getComment()).append("\"")
                  .append("}");
                if (i < reviews.size() - 1) sb.append(",");
            }
            sb.append("]");
            return sb.toString();
        });

        post("/api/ecommerce/products/:id/reviews", (req, res) -> {
            res.type("application/json");
            String productId = req.params(":id");
            String author = req.queryParams("author");
            String ratingStr = req.queryParams("rating");
            String comment = req.queryParams("comment");

            if (author == null || comment == null || author.isEmpty() || comment.isEmpty()) {
                res.status(400);
                return "{\"error\":\"Missing fields\"}";
            }

            int rating = 5;
            try { rating = Integer.parseInt(ratingStr); } catch(Exception e) {}
            
            ecommerceControl.addReview(productId, author, rating, comment);
            return "{\"status\":\"success\"}";
        });
    }
}
