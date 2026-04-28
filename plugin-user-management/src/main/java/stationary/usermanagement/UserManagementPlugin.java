package stationary.usermanagement;

import stationary.core.spi.StationaryPlugin;
import stationary.usermanagement.control.AdminControl;
import static spark.Spark.*;

public class UserManagementPlugin implements StationaryPlugin {

    private final AdminControl userControl;

    public UserManagementPlugin() {
        this.userControl = new AdminControl();
    }

    @Override
    public String getName() {
        return "User Management Plugin";
    }

    @Override
    public void onInitData(stationary.core.db.MemoryDB coreDb) {
        // Init data logic can go here
    }

    @Override
    public void registerRoutes() {
        path("/api/users", () -> {

            get("/:id", (req, res) -> {
                res.type("application/json;charset=utf-8");
                String userId = req.params(":id");
                return userControl.getUserDetailSecured(userId);
            });

            delete("/:id", (req, res) -> {
                res.type("application/json;charset=utf-8");
                String userId = req.params(":id");
                try {
                    userControl.deleteUser(userId);
                    return "{\"status\":\"success\"}";
                } catch (Exception e) {
                    res.status(400);
                    return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
                }
            });

            get("", (req, res) -> {
                res.type("application/json;charset=utf-8");
                return new com.google.gson.Gson().toJson(userControl.getAllUsers());
            });

        });
    }
}
