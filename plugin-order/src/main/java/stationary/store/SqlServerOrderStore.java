package stationary.store;

import stationary.core.db.DbConnectionManager;
import stationary.entity.Order;
import stationary.entity.OrderLineItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlServerOrderStore implements OrderStore {
    private static SqlServerOrderStore instance;

    public static synchronized SqlServerOrderStore getInstance() {
        if (instance == null) {
            instance = new SqlServerOrderStore();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DbConnectionManager.getConnection();
    }

    @Override
    public boolean saveOrder(Order order) throws SQLException {
        String insertOrder = "INSERT INTO Orders (id, orderDate, status, totalAmount) VALUES (?, GETDATE(), ?, ?)";
        String insertItem = "INSERT INTO OrderLineItem (orderId, productId, productName, quantity, price) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrder)) {
                orderStmt.setString(1, order.getId());
                orderStmt.setString(2, order.getStatus() == null ? "COMPLETED" : order.getStatus());
                orderStmt.setDouble(3, order.getTotalAmount());
                orderStmt.executeUpdate();
            }

            try (PreparedStatement itemStmt = conn.prepareStatement(insertItem)) {
                for (OrderLineItem item : order.getItems()) {
                    itemStmt.setString(1, order.getId());
                    itemStmt.setString(2, item.getProductId());
                    itemStmt.setString(3, item.getProductName());
                    itemStmt.setInt(4, item.getQuantity());
                    itemStmt.setDouble(5, item.getPrice());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    @Override
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders ORDER BY orderDate DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = new Order(rs.getString("id"), new ArrayList<>());
                order.setOrderDate(rs.getTimestamp("orderDate"));
                order.setStatus(rs.getString("status"));
                order.setTotalAmount(rs.getDouble("totalAmount"));
                
                // Fetch items for each order
                fetchItemsForOrder(conn, order);
                orders.add(order);
            }
        }
        return orders;
    }

    private void fetchItemsForOrder(Connection conn, Order order) throws SQLException {
        String sql = "SELECT * FROM OrderLineItem WHERE orderId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, order.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderLineItem item = new OrderLineItem(
                        rs.getString("productId"),
                        rs.getInt("quantity")
                    );
                    item.setProductName(rs.getString("productName"));
                    item.setPrice(rs.getDouble("price"));
                    order.getItems().add(item);
                }
            }
        }
    }

    @Override
    public List<Order> getOrdersByStatus(String status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE status = ? ORDER BY orderDate DESC";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(rs.getString("id"), new ArrayList<>());
                    order.setOrderDate(rs.getTimestamp("orderDate"));
                    order.setStatus(rs.getString("status"));
                    order.setTotalAmount(rs.getDouble("totalAmount"));
                    fetchItemsForOrder(conn, order);
                    orders.add(order);
                }
            }
        }
        return orders;
    }
}
