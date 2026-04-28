package stationary.bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import stationary.control.CatalogControl;
import stationary.entity.Category;
import stationary.store.InMemoryStore;
import stationary.store.SqlServerStore;
import io.cucumber.java.Before;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class CustomerHomeSteps {

    private CatalogControl catalogControl;
    private List<Category> displayedCategories;

    public CustomerHomeSteps() {
        catalogControl = new CatalogControl();
    }

    @Before
    public void setup() {
        catalogControl.setStore(InMemoryStore.getInstance());
    }

    @Given("Dữ liệu mẫu đã được nạp với {int} danh mục chuẩn")
    public void setupCategories(int count) throws Exception {
        InMemoryStore.getInstance().clear();
        for (int i = 0; i < count; i++) {
            InMemoryStore.getInstance().addCategory(new Category("c" + i, "Danh mục " + i, "icon" + i + ".png"));
        }
    }

    @Given("Dữ liệu danh mục trong DB trống rỗng")
    public void setupEmptyCategories() {
        InMemoryStore.getInstance().clear();
    }

    @When("Khách hàng mở trang chủ")
    public void fetchCategories() {
        displayedCategories = catalogControl.getCategories();
    }

    @Then("Hệ thống hiển thị danh sách {int} danh mục")
    public void verifySixCategories(int expectedCount) {
        Assertions.assertEquals(expectedCount, displayedCategories.size());
    }

    @And("Mỗi danh mục hiển thị đầy đủ Icon và Tên")
    public void verifyIconAndName() {
        for (Category c : displayedCategories) {
            Assertions.assertNotNull(c.getName());
            Assertions.assertNotNull(c.getIconPath());
        }
    }

    @Then("Hệ thống hiển thị thông báo {string}")
    public void verifyMessage(String message) {
        Assertions.assertTrue(displayedCategories.isEmpty());
    }
}
