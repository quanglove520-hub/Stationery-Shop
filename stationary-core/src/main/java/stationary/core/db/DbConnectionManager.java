package stationary.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionManager {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=StationaryDB;encrypt=true;trustServerCertificate=true;loginTimeout=2;";
    private static final String USER = "stationary";
    private static final String PWD = "123456789";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("CRITICAL: Failed to load SQL Server JDBC Driver!");
            e.printStackTrace();
        }
    }

    /**
     * Lấy Connection object từ Database pool chung.
     * Người gọi bắt buộc phải đóng connection (nên dùng try-with-resources).
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PWD);
    }
}
