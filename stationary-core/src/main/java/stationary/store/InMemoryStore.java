package stationary.store;

import stationary.entity.Category;
import stationary.entity.Product;
import stationary.entity.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import java.sql.SQLException;

public class InMemoryStore implements ProductStore, OrderStore {
    private static InMemoryStore instance;
    private List<Category> categories = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

    private InMemoryStore() {}

    public static synchronized InMemoryStore getInstance() {
        if (instance == null) {
            instance = new InMemoryStore();
        }
        return instance;
    }

    public synchronized void clear() {
        categories.clear();
        products.clear();
        orders.clear();
    }

    @Override
    public List<Category> getCategories() throws SQLException { return categories; }
    
    @Override
    public List<Product> getProducts() throws SQLException { return products; }

    @Override
    public List<Order> getAllOrders() throws SQLException { return orders; }

    @Override
    public List<Order> getOrdersByStatus(String status) throws SQLException {
        return orders.stream()
                .filter(o -> o.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public boolean saveOrder(Order order) throws SQLException {
        orders.add(order);
        return true;
    }

    public List<Order> getOrders() { return orders; }

    @Override
    public boolean addCategory(Category category) throws SQLException { 
        categories.add(category); 
        return true;
    }

    @Override
    public boolean updateCategory(String id, Category category) throws SQLException {
        removeCategory(id);
        addCategory(category);
        return true;
    }

    @Override
    public boolean removeCategory(String id) throws SQLException {
        return categories.removeIf(c -> c.getId().equals(id));
    }

    @Override
    public boolean categoryExists(String id) throws SQLException {
        return categories.stream().anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public boolean addProduct(Product product) throws SQLException { 
        products.add(product); 
        return true;
    }

    @Override
    public Product getProductById(String id) throws SQLException {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public boolean isProductInAnyOrder(String productId) throws SQLException {
        return orders.stream().anyMatch(o -> o.getItems().stream().anyMatch(i -> i.getProductId().equals(productId)));
    }

    @Override
    public boolean updateProduct(String oldId, Product product) throws SQLException {
        removeProduct(oldId);
        addProduct(product);
        return true;
    }

    @Override
    public boolean removeProduct(String id) throws SQLException { 
        return products.removeIf(p -> p.getId().equals(id)); 
    }

    public void addOrder(Order order) { orders.add(order); }
}
