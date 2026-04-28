package stationary.auth;

import stationary.core.spi.StationaryPlugin;

import static spark.Spark.*;

public class AuthPlugin implements StationaryPlugin {

    @Override
    public String getName() {
        return "Passwordless Auth Module";
    }

    @Override
    public void onInitData(stationary.core.db.MemoryDB coreDb) {
        // Init data logic can go here
    }

    @Override
    public void registerRoutes() {
        OtpApiControl apiControl = new OtpApiControl();
        
        stationary.auth.control.AuthControl authControl = new stationary.auth.control.AuthControl();
        
        post("/api/login", (req, res) -> {
            res.type("application/json;charset=utf-8");
            try {
                String username = req.queryParams("username");
                String password = req.queryParams("password");
                String email = username;
                if (!email.contains("@")) {
                    email = email + "@example.com";
                }
                stationary.auth.entity.Account account = authControl.login(email, password);
                return "{\"status\":\"success\",\"role\":\"ROLE_" + account.getRole().name() + "\",\"username\":\"" + account.getUsername() + "\"}";
            } catch (Exception e) {
                res.status(401);
                return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
            }
        });

        post("/api/register", (req, res) -> {
            res.type("application/json;charset=utf-8");
            try {
                String email = req.queryParams("email");
                String password = req.queryParams("password");
                String username = req.queryParams("username");
                
                stationary.auth.entity.Account account = authControl.register(email, password, username);
                return "{\"status\":\"success\",\"message\":\"Đăng ký thành công!\"}";
            } catch (Exception e) {
                res.status(400);
                return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
            }
        });

        path("/api/auth", () -> {
            post("/otp/request", apiControl::requestOtp);
            post("/otp/verify", apiControl::verifyOtp);
        });
        
        System.out.println("[Plugin] Passwordless Auth Module is plugged in!");
    }
}
