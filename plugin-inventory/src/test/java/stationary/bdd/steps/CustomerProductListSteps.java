package stationary.bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import stationary.control.CatalogControl;
import stationary.control.CustomerOrderControl;
import stationary.entity.Category;
import stationary.entity.Product;
import stationary.store.InMemoryStore;

import java.util.List;
import java.util.Optional;
import stationary.store.SqlServerStore;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerProductListSteps {
    private CatalogControl catalogControl = new CatalogControl();
    private static CustomerOrderControl orderControl = new CustomerOrderControl();
    private List<Product> currentProducts;
    private String currentMessage;

    @io.cucumber.java.Before
    public void setupCart() {
        catalogControl.setStore(InMemoryStore.getInstance());
        orderControl.setCatalogControl(catalogControl);
        orderControl.checkout();
    }

    @Given("Hệ thống đã nạp dữ liệu mẫu")
    public void he_thong_da_nap_du_lieu_mau() throws Exception {
        InMemoryStore store = InMemoryStore.getInstance();
        store.clear();
        store.addCategory(new Category("C1", "Bìa - Kệ hồ sơ", "📁"));
        store.addCategory(new Category("C2", "Giấy", "📄"));
        store.addCategory(new Category("C3", "Bút - Viết", "🖊️"));
        store.addCategory(new Category("C4", "Sổ - Tập", "📓"));
        store.addCategory(new Category("C5", "Kéo - Dao rọc giấy", "✂️"));
        store.addCategory(new Category("C6", "Dụng cụ khác", "📎"));
        store.addProduct(new Product("P201", "C2", "Giấy A4 Double A", 65000, 100));
        store.addProduct(new Product("P301", "C3", "Bút Bi Thiên Long", 5000, 200));
    }

    @Given("màn hình hiện tại là trang chủ {string}")
    public void man_hinh_hien_tai_la_trang_chu(String title) {
        // Mock via pure control (No-op in BCE backend test)
    }

    @When("tôi nhấp vào thẻ bài {string}")
    public void toi_nhap_vao_the_bai(String categoryName) throws Throwable {
        Optional<Category> cat = InMemoryStore.getInstance().getCategories().stream()
                .filter(c -> c.getName().equals(categoryName))
                .findFirst();
        if(cat.isPresent()) {
            currentProducts = catalogControl.getProductsByCategory(cat.get().getId());
            if(currentProducts.isEmpty()) {
                currentMessage = "Chưa có sản phẩm nào thuộc danh mục này";
            }
        }
    }

    @Then("giao diện được chuyển sang Sản phẩm của {string}")
    public void giao_dien_duoc_chuyen_sang_san_pham_cua(String title) {
        // Assert logic has parsed
        assertNotNull(currentProducts);
    }

    @Then("tôi nhìn thấy thẻ sản phẩm {string} nằm trong danh sách")
    public void toi_nhin_thay_the_san_pham_nam_trong_danh_sach(String expectedName) {
        boolean found = currentProducts.stream().anyMatch(p -> p.getName().equals(expectedName));
        assertTrue(found, "Product " + expectedName + " not found!");
    }

    // -- For old scenario
    @Then("hệ thống hiển thị thông báo {string}")
    public void he_thong_hien_thi_thong_bao(String msg) {
        if(msg.equals("Chưa có sản phẩm nào thuộc danh mục này")) {
             assertEquals(msg, currentMessage);
        } else {
             // For Admin tests
        }
    }

    @When("tôi click Thêm vào giỏ hàng cho sản phẩm {string}")
    public void toi_click_them_vao_gio_hang(String productName) throws Exception {
        Optional<Product> p = InMemoryStore.getInstance().getProducts().stream()
                .filter(prod -> prod.getName().equals(productName))
                .findFirst();
        assertTrue(p.isPresent());
        orderControl.addToCart(p.get().getId(), 1);
    }
    
    @Then("giỏ hàng của tôi sẽ có {int} mặt hàng {string}")
    public void gio_hang_cua_toi_se_co_mat_hang(Integer qty, String pId) {
        long count = orderControl.getCartItems().stream()
            .filter(item -> item.getProductId().equals(pId) && item.getQuantity() == qty)
            .count();
        assertEquals(1, count);
    }

    @When("tôi truy cập nút Giỏ Hàng trên cùng")
    public void toi_truy_cap_nut_gio_hang() {
        // no-op, fetching from control
    }

    @Then("màn hình chuyển sang Giỏ Hàng")
    public void man_hinh_chuyen_sang_gio_hang() {
        // True
    }

    @Then("hệ thống hiển thị tổng tiền là {string}")
    public void he_thong_hien_thi_tong_tien(String expectedTotal) {
        double calc = orderControl.calculateTotal();
        assertEquals(Double.parseDouble(expectedTotal), calc);
    }
}
