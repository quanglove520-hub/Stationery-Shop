package stationary.auth;
import stationary.auth.entity.OtpSession;

import stationary.core.db.DbConnectionManager;


import java.sql.Connection;
import java.sql.PreparedStatement;

public class OtpStore {
    
    public boolean saveOtp(OtpSession session) {
        String sql = "INSERT INTO opt_sessions (phone, otp_code, expires_at) VALUES (?, ?, ?)";
        
        try (Connection conn = DbConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, session.getPhoneNumber());
            stmt.setString(2, session.getOtpCode());
            stmt.setObject(3, session.getCreatedAt());
            
            return stmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
