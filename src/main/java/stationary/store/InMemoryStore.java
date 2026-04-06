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

    public List<Category> getCategories() { return categories; }
    public List<Product> getProducts() { return products; }
    public List<Order> getOrders() { return orders; }

    public void addCategory(Category category) { categories.add(category); }
    public void addProduct(Product product) { products.add(product); }
    public void addOrder(Order order) { orders.add(order); }
    public void removeCategory(String id) { categories.removeIf(c -> c.getId().equals(id)); }
    public void removeProduct(String id) { products.removeIf(p -> p.getId().equals(id)); }
}
