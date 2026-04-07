package stationary.store;

import stationary.entity.Category;
import stationary.entity.Order;
import stationary.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStore {
    private static InMemoryStore instance;
    private List<Category> categories;
    private List<Product> products;
    private List<Order> orders;

    private InMemoryStore() {
        categories = new ArrayList<>();
        products = new ArrayList<>();
        orders = new ArrayList<>();
        seedDummyData();
    }

    private void seedDummyData() {
        categories.add(new Category("C1", "Bìa - Kệ hồ sơ", "📁"));
        categories.add(new Category("C2", "Giấy", "📄"));
        categories.add(new Category("C3", "Bút - Viết", "🖊️"));
        categories.add(new Category("C4", "Sổ - Tập", "📓"));
        categories.add(new Category("C5", "Kéo - Dao rọc giấy", "✂️"));
        categories.add(new Category("C6", "Dụng cụ khác", "📎"));

        products.add(new Product("P201", "C2", "Giấy A4 Double A", 65000, 100));
        products.add(new Product("P202", "C2", "Giấy Note Vàng", 12000, 50));
        
        products.add(new Product("P301", "C3", "Bút Bi Thiên Long", 5000, 200));
        products.add(new Product("P302", "C3", "Bút Máy Parker", 450000, 10));
        products.add(new Product("P303", "C3", "Bút Dạ Quang", 15000, 30));
    }

    public static synchronized InMemoryStore getInstance() {
        if (instance == null) {
            instance = new InMemoryStore();
        }
        return instance;
    }

    public void clear() {
        categories.clear();
        products.clear();
        orders.clear();
    }

    public void clearProducts() {
        products.clear();
    }

    public List<Category> getCategories() { return categories; }
    public List<Product> getProducts() { return products; }
    public List<Order> getOrders() { return orders; }

    public void addCategory(Category category) { categories.add(category); }
    public void addProduct(Product product) { products.add(product); }
    public void addOrder(Order order) { orders.add(order); }
    public void removeCategory(String id) { categories.removeIf(c -> c.getId().equals(id)); }
    public void removeProduct(String id) { products.removeIf(p -> p.getId().equals(id)); }
    
    public void updateCategory(Category category) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(category.getId())) {
                categories.set(i, category);
                return;
            }
        }
    }
    
    public void updateProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(product.getId())) {
                products.set(i, product);
                return;
            }
        }
    }
}
