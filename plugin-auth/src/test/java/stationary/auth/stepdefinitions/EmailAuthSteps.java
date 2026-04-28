package stationary.auth.stepdefinitions;



import stationary.auth.control.AuthControl;
import stationary.auth.entity.Account;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;

public class EmailAuthSteps {

    private AuthControl authControl = new AuthControl();
    private String currentError;
    private Account loggedInAccount;

    @Given("tôi đang ở màn hình Đăng ký")
    public void toiDangOMangHinhDangKy() {
        currentError = null;
    }

    @When("tôi nhập email {string}, username {string} và mật khẩu {string} và xác nhận {string}")
    public void toiNhapEmailUsernameVaMatKhau(String email, String username, String password, String confirm) {
        if (!password.equals(confirm)) {
            currentError = "Mật khẩu xác nhận không khớp";
        } else {
            try {
                authControl.register(email, password, username);
            } catch (Exception e) {
                currentError = e.getMessage();
            }
        }
    }

    @When("tôi nhấn nút Đăng ký")
    public void toiNhanNutDangKy() {
        // Hành động được trigger bên trên
    }

    @Then("hệ thống thông báo {string}")
    public void heThongThongBao(String msg) {
        if (msg.equals("Đăng ký thành công") || msg.equals("Đăng nhập thành công")) {
            assertNull(currentError);
        } else {
            assertEquals(msg, currentError);
        }
    }

    @Then("tôi được chuyển sang màn hình Đăng nhập")
    public void toiDuocChuyenSangManHinhDangNhap() {
        assertNull(currentError);
    }

    @Given("tài khoản email {string} với mật khẩu {string} và username {string} đã tồn tại")
    public void taiKhoanEmailDaTonTaiTrongHeThong(String email, String password, String username) {
        try {
            authControl.register(email, password, username);
        } catch (Exception e) {
            // Already exists or invalid format
        }
    }

    @Then("hệ thống báo lỗi {string}")
    public void heThongBaoLoi(String msg) {
        assertNotNull(currentError);
        assertTrue(currentError.contains(msg), "Kỳ vọng: " + msg + ", Thực tế: " + currentError);
    }

    @Given("tôi đang ở màn hình Đăng nhập")
    public void toiDangOMangHinhDangNhap() {
        currentError = null;
        loggedInAccount = null;
    }

    @When("tôi nhập email {string} và mật khẩu {string}")
    public void toiNhapEmailVaMatKhau(String email, String password) {
        try {
            loggedInAccount = authControl.login(email, password);
        } catch (Exception e) {
            currentError = e.getMessage();
        }
    }

    @When("tôi nhấn nút Đăng nhập")
    public void toiNhanNutDangNhap() {
        // Triggered above
    }

    @Then("tôi được chuyển vào Trang chủ")
    public void toiDuocChuyenVaoTrangChu() {
        assertNotNull(loggedInAccount);
        assertNull(currentError);
    }
}
