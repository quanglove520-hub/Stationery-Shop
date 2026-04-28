package stationary.control;

import stationary.entity.Category;
import stationary.entity.Order;
import stationary.entity.OrderLineItem;
import stationary.entity.Product;
import stationary.store.InMemoryStore;

import java.sql.SQLException;
import java.util.List;
import java.util.Collections;

public class AdminInventoryControl {
    public void addCategory(String id, String name, String iconPath) {
        try {
            InMemoryStore.getInstance().addCategory(new Category(id, name, iconPath));
        } catch (SQLException e) {
            throw new RuntimeException("DB Error", e);
        }
    }
    
    public void addProduct(String id, String categoryId, String name, double price, int stock) {
        try {
            InMemoryStore.getInstance().addProduct(new Product(id, categoryId, name, price, stock));
        } catch (SQLException e) {
            throw new RuntimeException("DB Error", e);
        }
    }

    public void updateCategory(String id, String name, String iconPath) {
        try {
            InMemoryStore.getInstance().updateCategory(id, new Category(id, name, iconPath));
        } catch (SQLException e) {
            throw new RuntimeException("DB Error", e);
        }
    }

    public void updateProduct(String id, String categoryId, String name, double price, int stock) {
        try {
            InMemoryStore.getInstance().updateProduct(id, new Product(id, categoryId, name, price, stock));
        } catch (SQLException e) {
            throw new RuntimeException("DB Error", e);
        }
    }

    public boolean deleteCategory(String categoryId) {
        try {
            List<Product> products = InMemoryStore.getInstance().getProducts();
            for (Product p : products) {
                if (p.getCategoryId().equals(categoryId)) {
                    throw new RuntimeException("Không thể xóa do danh mục đang chứa sản phẩm");
                }
            }
            InMemoryStore.getInstance().removeCategory(categoryId);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("DB Error", e);
        }
    }

    public boolean deleteProduct(String productId) {
        try {
            List<Order> orders = InMemoryStore.getInstance().getAllOrders();
            for (Order o : orders) {
                for (OrderLineItem item : o.getItems()) {
                    if (item.getProductId().equals(productId)) {
                        throw new RuntimeException("Không thể xóa do sản phẩm đang tồn tại trong đơn hàng");
                    }
                }
            }
            InMemoryStore.getInstance().removeProduct(productId);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("DB Error", e);
        }
    }

    public List<Category> getCategories() {
        try {
            return InMemoryStore.getInstance().getCategories();
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    public List<Product> getProducts() {
        try {
            return InMemoryStore.getInstance().getProducts();
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }
}
