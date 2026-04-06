package stationary.boundary.admin;

import stationary.control.AdminInventoryControl;
import stationary.entity.Category;
import stationary.entity.Product;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminPanel extends JPanel {
    private AdminInventoryControl control = new AdminInventoryControl();
    
    public AdminPanel() {
        setLayout(new GridLayout(1, 2));
        
        JPanel catPanel = new JPanel(new BorderLayout());
        catPanel.setBorder(BorderFactory.createTitledBorder("Quản Lý Danh Mục"));
        JList<String> catList = new JList<>();
        JButton delCatBtn = new JButton("Xóa Danh Mục");
        catPanel.add(new JScrollPane(catList), BorderLayout.CENTER);
        catPanel.add(delCatBtn, BorderLayout.SOUTH);
        
        JPanel prodPanel = new JPanel(new BorderLayout());
        prodPanel.setBorder(BorderFactory.createTitledBorder("Quản Lý Sản Phẩm"));
        JList<String> prodList = new JList<>();
        JButton delProdBtn = new JButton("Xóa Sản Phẩm");
        prodPanel.add(new JScrollPane(prodList), BorderLayout.CENTER);
        prodPanel.add(delProdBtn, BorderLayout.SOUTH);
        
        add(catPanel);
        add(prodPanel);

        delCatBtn.addActionListener(e -> {
            try {
                List<Category> cats = control.getCategories();
                if(!cats.isEmpty()) {
                    control.deleteCategory(cats.get(0).getId());
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        delProdBtn.addActionListener(e -> {
            try {
                List<Product> prods = control.getProducts();
                if(!prods.isEmpty()) {
                    control.deleteProduct(prods.get(0).getId());
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
