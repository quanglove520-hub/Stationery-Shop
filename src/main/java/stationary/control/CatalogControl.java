package stationary.control;

import stationary.entity.Category;
import stationary.entity.Product;
import stationary.store.InMemoryStore;

import java.util.List;
import java.util.stream.Collectors;

public class CatalogControl {
    public List<Category> getCategories() {
        return InMemoryStore.getInstance().getCategories();
    }

    public List<Product> getProductsByCategory(String categoryId) {
        return InMemoryStore.getInstance().getProducts().stream()
                .filter(p -> p.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    public Product getProductById(String id) {
        return InMemoryStore.getInstance().getProducts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
