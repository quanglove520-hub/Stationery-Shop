package stationary.auth;
import stationary.auth.entity.Account;
import stationary.auth.entity.OtpSession;

import spark.Request;
import spark.Response;
import com.google.gson.Gson;


public class OtpApiControl {
    private final Gson gson = new Gson();

    public Object requestOtp(Request req, Response res) {
        res.type("application/json"); 
        
        // Mock logic sinh mã OTP
        return gson.toJson(new ApiResponse(true, "Mã OTP đã được gửi đến số điện thoại", null));
    }

    public Object verifyOtp(Request req, Response res) {
        res.type("application/json");
        
        // Mock logic xác thực OTP
        return gson.toJson(new ApiResponse(true, "Xác thực OTP thành công", "{ \"token\": \"jwt-token-123\" }"));
    }
}
