package stationary.bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import stationary.control.CustomerOrderControl;
import stationary.control.CatalogControl;
import stationary.entity.Order;
import stationary.entity.OrderLineItem;
import stationary.entity.Product;
import stationary.store.InMemoryStore;

import java.util.List;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class OrderPersistenceSteps {
    private CustomerOrderControl orderControl = new CustomerOrderControl();

    @Given("hệ thống có cấu hình {string} nạp dữ liệu mẫu")
    public void he_thong_co_cau_hinh_nap_du_lieu_mau(String configType) throws SQLException {
        InMemoryStore store = InMemoryStore.getInstance();
        store.clear();
        store.addProduct(new Product("P1", "C3", "Bút Bi Thiên Long", 5000, 200));
        
        CatalogControl catControl = new CatalogControl();
        catControl.setStore(store);
        
        orderControl.setCatalogControl(catControl);
        orderControl.setOrderStore(store);
    }

    @Given("hệ thống có giỏ hàng hiện tại chứa sản phẩm {string} số lượng {int} với giá {int}")
    public void he_thong_co_gio_hang_chua_san_pham(String productName, int qty, int price) throws SQLException {
        Product p = InMemoryStore.getInstance().getProducts().stream()
                .filter(prod -> prod.getName().equals(productName))
                .findFirst().orElseThrow();
        orderControl.addToCart(p.getId(), qty);
    }

    @When("hàm checkout được thực thi")
    public void ham_checkout_duoc_thuc_thi() {
        orderControl.checkout();
    }

    @Then("hệ thống lưu đối tượng Order vào bộ nhớ với tổng giá là {int}")
    public void he_thong_luu_order_voi_tong_gia(int expectedTotal) throws SQLException {
        List<Order> orders = InMemoryStore.getInstance().getAllOrders();
        assertEquals(1, orders.size(), "Order was not saved!");
        assertEquals((double)expectedTotal, orders.get(0).getTotalAmount(), 0.1);
    }

    @Then("hệ thống xóa sạch giỏ hàng sau khi thanh toán")
    public void he_thong_xoa_sach_gio_hang() {
        assertEquals(0, orderControl.getCartItems().size());
    }
}
