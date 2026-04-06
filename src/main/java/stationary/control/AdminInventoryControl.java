package stationary.control;

import stationary.entity.Category;
import stationary.entity.Order;
import stationary.entity.OrderLineItem;
import stationary.entity.Product;
import stationary.store.InMemoryStore;

import java.util.List;

public class AdminInventoryControl {
    public void addCategory(String id, String name, String iconPath) {
        InMemoryStore.getInstance().addCategory(new Category(id, name, iconPath));
    }
    
    public void addProduct(String id, String categoryId, String name, double price, int stock) {
        InMemoryStore.getInstance().addProduct(new Product(id, categoryId, name, price, stock));
    }

    public boolean deleteCategory(String categoryId) {
        List<Product> products = InMemoryStore.getInstance().getProducts();
        for (Product p : products) {
            if (p.getCategoryId().equals(categoryId)) {
                throw new RuntimeException("Không thể xóa do danh mục đang chứa sản phẩm");
            }
        }
        InMemoryStore.getInstance().removeCategory(categoryId);
        return true;
    }

    public boolean deleteProduct(String productId) {
        List<Order> orders = InMemoryStore.getInstance().getOrders();
        for (Order o : orders) {
            for (OrderLineItem item : o.getItems()) {
                if (item.getProductId().equals(productId)) {
                    throw new RuntimeException("Không thể xóa do sản phẩm đang tồn tại trong đơn hàng");
                }
            }
        }
        InMemoryStore.getInstance().removeProduct(productId);
        return true;
    }

    public List<Category> getCategories() {
        return InMemoryStore.getInstance().getCategories();
    }

    public List<Product> getProducts() {
        return InMemoryStore.getInstance().getProducts();
    }
}
