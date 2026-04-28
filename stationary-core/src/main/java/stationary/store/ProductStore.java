package stationary.store;

import stationary.entity.Category;
import stationary.entity.Product;
import java.sql.SQLException;
import java.util.List;

public interface ProductStore {
    List<Category> getCategories() throws SQLException;
    boolean addCategory(Category category) throws SQLException;
    boolean updateCategory(String id, Category category) throws SQLException;
    boolean removeCategory(String id) throws SQLException;
    boolean categoryExists(String id) throws SQLException;

    List<Product> getProducts() throws SQLException;
    Product getProductById(String id) throws SQLException;
    boolean isProductInAnyOrder(String productId) throws SQLException;
    boolean addProduct(Product product) throws SQLException;
    boolean updateProduct(String oldId, Product product) throws SQLException;
    boolean removeProduct(String id) throws SQLException;
}
