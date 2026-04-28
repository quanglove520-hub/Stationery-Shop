package stationary.cart.store;

import stationary.core.db.DbConnectionManager; 
import stationary.cart.entity.CartItem;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CartStore {
    public boolean upsertCartItem(String productId, int delta) {
        try (Connection conn = DbConnectionManager.getConnection()) {
            // Check if exists
            String checkSql = "SELECT quantity FROM cart_items WHERE product_id = ?";
            int existingQty = -1;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, productId);
                try (java.sql.ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        existingQty = rs.getInt(1);
                    }
                }
            }

            if (existingQty != -1) {
                // UPDATE
                String updateSql = "UPDATE cart_items SET quantity = quantity + ? WHERE product_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, delta);
                    updateStmt.setString(2, productId);
                    return updateStmt.executeUpdate() > 0;
                }
            } else {
                // INSERT (only if delta > 0)
                if (delta <= 0) return true; 
                String insertSql = "INSERT INTO cart_items (product_id, quantity) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, productId);
                    insertStmt.setInt(2, delta);
                    return insertStmt.executeUpdate() > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCartItem(String productId, int quantity) {
        try (Connection conn = DbConnectionManager.getConnection()) {
            String sql = "UPDATE cart_items SET quantity = ? WHERE product_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, quantity);
                stmt.setString(2, productId);
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCartItem(String productId) {
        try (Connection conn = DbConnectionManager.getConnection()) {
            String sql = "DELETE FROM cart_items WHERE product_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, productId);
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<CartItem> getAllItems() {
        java.util.List<CartItem> items = new java.util.ArrayList<>();
        try (Connection conn = DbConnectionManager.getConnection()) {
            String sql = "SELECT product_id, quantity FROM cart_items";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new CartItem(rs.getString("product_id"), rs.getInt("quantity")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public void clearCart() {
        try (Connection conn = DbConnectionManager.getConnection()) {
            String sql = "DELETE FROM cart_items";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
