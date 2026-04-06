package stationary.control;

import stationary.entity.Category;
import stationary.entity.Product;
import stationary.store.InMemoryStore;

import java.util.List;

public class CatalogControl {
    public List<Category> getCategories() {
        return InMemoryStore.getInstance().getCategories();
    }

    public List<Product> getAllProducts() {
        return InMemoryStore.getInstance().getProducts();
    }
}
