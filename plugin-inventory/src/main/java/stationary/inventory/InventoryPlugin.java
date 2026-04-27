package stationary.inventory;

import stationary.core.spi.StationaryPlugin;
import stationary.core.db.MemoryDB;
import stationary.control.AdminInventoryControl;
import stationary.control.CatalogControl;
import stationary.entity.Category;
import stationary.entity.Product;
import com.google.gson.Gson;
import static spark.Spark.*;

public class InventoryPlugin implements StationaryPlugin {
    
    private AdminInventoryControl adminControl;
    private CatalogControl catalogControl;
    private Gson gson;

    @Override
    public String getName() {
        return "Inventory Catalog Plugin";
    }

    @Override
    public void onInitData(MemoryDB db) {
        adminControl = new AdminInventoryControl();
        catalogControl = new CatalogControl();
        catalogControl.seedProducts();
        gson = new Gson();
        // Currently data is still using legacy InMemoryStore via AdminInventoryControl internally.
        // We can just leave it as is for now to ensure BCE and tests stay intact.
    }

    @Override
    public void registerRoutes() {
        // Migration of routes from WebServer to Plugin
        
        get("/api/categories", (req, res) -> {
            res.type("application/json");
            return catalogControl.getCategories();
        }, gson::toJson);

        get("/api/products", (req, res) -> {
            res.type("application/json");
            String catId = req.queryParams("categoryId");
            String search = req.queryParams("search");
            String sort = req.queryParams("sort");
            int page = 0;
            int size = 12;
            try {
                if (req.queryParams("page") != null) page = Integer.parseInt(req.queryParams("page"));
                if (req.queryParams("size") != null) size = Integer.parseInt(req.queryParams("size"));
            } catch (Exception e) {}
            
            return catalogControl.getAllProducts(catId, search, sort, page, size);
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
    }
}
