package stationary.usermanagement.steps;

import stationary.usermanagement.control.AdminControl;
import stationary.usermanagement.entity.UserDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import static org.junit.jupiter.api.Assertions.*;

public class AdminManageUsersSteps {

    private AdminControl adminControl = new AdminControl();
    private UserDTO lastFetchedUser = null;
    private Exception lastException = null;

    @Given("Hệ thống có người đăng nhập đóng vai trò {string}")
    public void heSystemCoNguoiDangNhapDongVaiTro(String quyen) {
        assertEquals("Admin", quyen);
    }

    @And("Hệ thống lưu trữ tài khoản {string} mật khẩu {string}")
    public void heSystemLuuTruTaiKhoanMatKhau(String ten, String matKhau) {
        adminControl.addUser(ten, matKhau);
    }

    @Given("Hệ thống lưu trữ tài khoản {string}")
    public void heSystemLuuTruTaiKhoan(String ten) {
        adminControl.addUser(ten, "default_password");
    }

    @When("Admin truy cập chi tiết thông tin của {string}")
    public void adminTruyCapChiTietThongTinCua(String ten) {
        lastFetchedUser = adminControl.getUserDetailSecured(ten);
    }

    @Then("Giao diện trả về Data Transfer Object của khách hàng")
    public void giaoDienTraVeDataTransferObjectCuaKhachHang() {
        assertNotNull(lastFetchedUser);
    }

    @And("Mật khẩu {string} tuyệt đối KHÔNG được trả về UI")
    public void matKhauTuyetDoiKHONGDuocTraVeUI(String matKhau) {
        assertNull(lastFetchedUser.getPassword()); // Đảm bảo security constraint trên ảnh
    }

    @And("{string} chưa từng đặt bất kỳ thiết bị văn phòng phẩm nào")
    public void chuaTungDatBatKyThietBiVanPhongPhamNao(String ten) {
        // Đã gán auto là 0 lúc addUser
    }

    @When("Admin click nút Xóa tài khoản {string}")
    public void adminClickNutXoaTaiKhoan(String ten) {
        try {
            adminControl.deleteUser(ten);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @Then("Tài khoản {string} bị xóa khỏi danh sách cơ sở dữ liệu")
    public void taiKhoanBiXoaKhoiDanhSachCoSoDuLieu(String ten) {
         assertFalse(adminControl.userExists(ten));
         assertNull(lastException);
    }

    @And("{string} đã từng đặt {int} đơn hàng {string} trong quá khứ")
    public void daTungDatDonHangTrongQuaKhu(String ten, int soLuong, String tenSp) {
         adminControl.addOrderHistory(ten, soLuong);
    }

    @Then("Lệnh xóa bị từ chối")
    public void lenhXoaBiTuChoi() {
         assertNotNull(lastException);
    }

    @And("Hệ thống ném ra lỗi {string}")
    public void heSystemNemRaLoi(String thongBaoLoi) {
         assertEquals(thongBaoLoi, lastException.getMessage());
    }
}
