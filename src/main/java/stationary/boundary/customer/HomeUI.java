package stationary.boundary.customer;

import stationary.control.CatalogControl;
import stationary.entity.Category;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HomeUI extends JPanel {
    private CatalogControl catalogControl;
    private JPanel gridPanel;

    public HomeUI() {
        catalogControl = new CatalogControl();
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Danh Mục Sản Phẩm", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns
        add(new JScrollPane(gridPanel), BorderLayout.CENTER);

        loadCategories();
    }

    private void loadCategories() {
        List<Category> categories = catalogControl.getCategories();
        if (categories.isEmpty()) {
            gridPanel.setLayout(new BorderLayout());
            gridPanel.add(new JLabel("Chưa có danh mục nào, vui lòng quay lại sau", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        for (Category category : categories) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            
            JLabel iconLabel = new JLabel(category.getIconPath(), SwingConstants.CENTER);
            JLabel nameLabel = new JLabel(category.getName(), SwingConstants.CENTER);
            
            card.add(iconLabel, BorderLayout.CENTER);
            card.add(nameLabel, BorderLayout.SOUTH);
            
            gridPanel.add(card);
        }
    }
}
