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
        MainFrame app = new MainFrame();
        app.showAdminPanel();
        app.setVisible(true);
    }
}
