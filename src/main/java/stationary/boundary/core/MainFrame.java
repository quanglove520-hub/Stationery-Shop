package stationary.boundary.core;

import stationary.boundary.admin.AdminPanel;
import stationary.boundary.customer.HomeUI;
import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Stationary Store");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void showHome() {
        setContentPane(new HomeUI());
        revalidate();
    }
    
    public void showAdminPanel() {
        setContentPane(new AdminPanel());
        revalidate();
    }
    
    public static void main(String[] args) {
        // Khởi tạo dữ liệu mẫu (Mock data)
        stationary.control.AdminInventoryControl adminCtrl = new stationary.control.AdminInventoryControl();
        adminCtrl.addCategory("c1", "Bút viết", "🖊");
        adminCtrl.addCategory("c2", "Vở học sinh", "📓");
        
        adminCtrl.addProduct("p1", "c1", "Bút bi Thiên Long", 5000, 100);
        adminCtrl.addProduct("p2", "c1", "Bút chì 2B", 3000, 200);
        adminCtrl.addProduct("p3", "c2", "Vở ô ly Hồng Hà", 12000, 50);
        adminCtrl.addProduct("p4", "c2", "Vở kẻ ngang Campus", 15000, 80);

        MainFrame app = new MainFrame();
        // Mở HomeUI thay vì AdminPanel để test giao diện khách hàng
        app.showHome();
        app.setVisible(true);
    }
}
