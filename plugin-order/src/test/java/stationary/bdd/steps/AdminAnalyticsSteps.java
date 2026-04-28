package stationary.bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import stationary.control.AnalyticsControl;
import stationary.entity.Order;
import stationary.entity.OrderLineItem;
import stationary.store.InMemoryStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class AdminAnalyticsSteps {
    private AnalyticsControl analyticsControl;
    private Map<String, Double> revenueResult;
    private List<Map<String, Object>> topProductsResult;

    @Given("hệ thống có cấu hình {string} nạp dữ liệu mẫu với các giao dịch thành công")
    public void he_thong_co_du_lieu_mau_giao_dich(String configType) throws SQLException {
        InMemoryStore store = InMemoryStore.getInstance();
        store.clear();
        
        List<OrderLineItem> items1 = new ArrayList<>();
        OrderLineItem item1 = new OrderLineItem("P1", "Bút Bi", 2, 5000.0);
        items1.add(item1);
        Order o1 = new Order("O1", items1);
        o1.setTotalAmount(10000);
        o1.setStatus("COMPLETED");
        store.saveOrder(o1);

        analyticsControl = new AnalyticsControl();
        analyticsControl.setStore(store);
    }

    @When("Admin truy cập vào tab Báo cáo")
    public void admin_truy_cap_tab_bao_cao() throws SQLException {
        revenueResult = analyticsControl.getRevenueByDate();
    }

    @Then("hệ thống trả lời báo cáo tổng doanh thu của từng ngày đúng theo dữ liệu mẫu")
    public void he_thong_tra_ve_doanh_thu() {
        assertNotNull(revenueResult);
        String today = LocalDate.now().toString();
        assertTrue(revenueResult.containsKey(today));
        assertEquals(10000.0, revenueResult.get(today), 0.1);
    }

    @When("Admin truy cập vào Dashboard và yêu cầu top sản phẩm")
    public void admin_yeu_cau_top_san_pham() throws SQLException {
        topProductsResult = analyticsControl.getTopSellingProducts(5);
    }

    @Then("hệ thống trả về top các sản phẩm bán chạy nhất được sắp xếp từ cao xuống thấp")
    public void he_thong_tra_ve_top_sp() {
        assertNotNull(topProductsResult);
        assertEquals(1, topProductsResult.size());
        assertEquals("Bút Bi", topProductsResult.get(0).get("name"));
        assertEquals(2, topProductsResult.get(0).get("sold"));
    }

    @Given("hệ thống chưa từng ghi nhận đơn hàng nào")
    public void he_thong_chua_ghi_nhan_don_hang() {
        InMemoryStore.getInstance().clear();
        analyticsControl = new AnalyticsControl();
        analyticsControl.setStore(InMemoryStore.getInstance());
    }

    @Then("hệ thống trả về dữ liệu quản trị rỗng mà không bị crash")
    public void he_thong_tra_ve_rong() {
        assertNotNull(revenueResult);
        assertTrue(revenueResult.isEmpty());
    }
}
