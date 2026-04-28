package stationary.bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import stationary.control.AdminInventoryControl;
import stationary.entity.Category;
import stationary.entity.Order;
import stationary.entity.OrderLineItem;
import stationary.entity.Product;
import stationary.store.InMemoryStore;

import java.util.ArrayList;
import java.util.List;
import stationary.store.SqlServerStore;
import io.cucumber.java.Before;

public class AdminInventorySteps {
    private AdminInventoryControl control = new AdminInventoryControl();
    private Exception caughtException = null;

    @Before
    public void setup() {
        control.setStore(InMemoryStore.getInstance());
    }

    @Given("Một Danh mục \\(Category) bất kỳ đang tồn tại trong hệ thống và KHÔNG chứa bất kỳ Sản phẩm nào")
    public void setupEmptyCategory() throws Exception {
        InMemoryStore.getInstance().clear();
        InMemoryStore.getInstance().addCategory(new Category("CAT1", "Category 1", "icon.png"));
    }

    @Given("Một Danh mục \\(Category) bất kỳ đang chứa một hoặc nhiều Sản phẩm bên trong")
    public void setupCategoryWithProduct() throws Exception {
        InMemoryStore.getInstance().clear();
        InMemoryStore.getInstance().addCategory(new Category("CAT2", "Category 2", "icon.png"));
        InMemoryStore.getInstance().addProduct(new Product("PROD1", "CAT2", "Product 1", 10.0, 1));
    }

    @Given("Một Sản phẩm \\(Product) bất kỳ đang tồn tại và chưa từng xuất hiện trong bất cứ Order nào")
    public void setupProductNoOrder() throws Exception {
        InMemoryStore.getInstance().clear();
        InMemoryStore.getInstance().addProduct(new Product("PROD2", "CAT3", "Product 2", 10.0, 1));
    }

    @Given("Một Sản phẩm \\(Product) bất kỳ đang có mặt trong ít nhất một Order")
    public void setupProductInOrder() throws Exception {
        InMemoryStore.getInstance().clear();
        InMemoryStore.getInstance().addProduct(new Product("PROD3", "CAT3", "Product 3", 10.0, 1));
        List<OrderLineItem> items = new ArrayList<>();
        items.add(new OrderLineItem("PROD3", 2));
        InMemoryStore.getInstance().addOrder(new Order("ORD1", items));
    }

    @When("Admin chọn và yêu cầu Xóa danh mục nói trên")
    public void deleteCategory() {
        try {
            List<Category> cats = InMemoryStore.getInstance().getCategories();
            if(!cats.isEmpty()){
                control.deleteCategory(cats.get(0).getId());
            }
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @When("Admin chọn và yêu cầu Xóa sản phẩm đó")
    public void deleteProduct() {
        try {
            List<Product> prods = InMemoryStore.getInstance().getProducts();
            if(!prods.isEmpty()){
                control.deleteProduct(prods.get(0).getId());
            }
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("Hệ thống xóa Danh mục đó thành công khỏi cơ sở dữ liệu")
    public void verifyCategoryDeleted() throws Exception {
        Assertions.assertTrue(InMemoryStore.getInstance().getCategories().isEmpty());
    }

    @Then("Hệ thống xóa Sản phẩm hoàn toàn khỏi cơ sở dữ liệu")
    public void verifyProductDeleted() throws Exception {
        Assertions.assertTrue(InMemoryStore.getInstance().getProducts().isEmpty());
    }

    @Then("Hệ thống từ chối thao tác xóa {string}")
    public void verifyExceptionMessage(String msg) {
        Assertions.assertNotNull(caughtException);
        Assertions.assertEquals(msg, caughtException.getMessage());
    }
}
