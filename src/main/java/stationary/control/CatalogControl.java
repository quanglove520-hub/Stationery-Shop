package stationary.control;

import stationary.entity.Category;
import stationary.store.InMemoryStore;

import java.util.List;

public class CatalogControl {
    public List<Category> getCategories() {
        return InMemoryStore.getInstance().getCategories();
    }
}
