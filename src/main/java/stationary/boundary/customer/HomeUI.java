package stationary.boundary.customer;

import stationary.control.CatalogControl;
import stationary.entity.Category;
import stationary.entity.Product;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HomeUI extends JPanel {
    private CatalogControl catalogControl;
    private JPanel mainPanel;

    public HomeUI() {
        catalogControl = new CatalogControl();
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Cửa Hàng Văn Phòng Phẩm", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        loadCategories();
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Khoảng cách
        loadProducts();

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadCategories() {
        JPanel catWrapper = new JPanel(new BorderLayout());
        JLabel catTitle = new JLabel(" Danh Mục Sản Phẩm");
        catTitle.setFont(new Font("Arial", Font.BOLD, 18));
        catWrapper.add(catTitle, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 10, 10)); 
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Category> categories = catalogControl.getCategories();
        if (categories.isEmpty()) {
            gridPanel.setLayout(new BorderLayout());
            gridPanel.add(new JLabel("Chưa có danh mục nào", SwingConstants.CENTER), BorderLayout.CENTER);
        } else {
            for (Category category : categories) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                card.setPreferredSize(new Dimension(100, 80));
                
                JLabel iconLabel = new JLabel(category.getIconPath(), SwingConstants.CENTER);
                iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
                JLabel nameLabel = new JLabel(category.getName(), SwingConstants.CENTER);
                
                card.add(iconLabel, BorderLayout.CENTER);
                card.add(nameLabel, BorderLayout.SOUTH);
                
                gridPanel.add(card);
            }
        }
        
        catWrapper.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(catWrapper);
    }

    private void loadProducts() {
        JPanel prodWrapper = new JPanel(new BorderLayout());
        JLabel prodTitle = new JLabel(" Tất Cả Sản Phẩm");
        prodTitle.setFont(new Font("Arial", Font.BOLD, 18));
        prodWrapper.add(prodTitle, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Product> products = catalogControl.getAllProducts();
        if (products.isEmpty()) {
            gridPanel.setLayout(new BorderLayout());
            gridPanel.add(new JLabel("Đang cập nhật sản phẩm", SwingConstants.CENTER), BorderLayout.CENTER);
        } else {
            for (Product product : products) {
                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                
                JLabel nameLabel = new JLabel(product.getName());
                nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                JLabel priceLabel = new JLabel(String.format("%,.0f VNĐ", product.getPrice()));
                priceLabel.setForeground(Color.RED);
                priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                JLabel stockLabel = new JLabel("Kho: " + product.getStock());
                stockLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JButton viewBtn = new JButton("Chi tiết");
                viewBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

                card.add(nameLabel);
                card.add(Box.createRigidArea(new Dimension(0, 5)));
                card.add(priceLabel);
                card.add(Box.createRigidArea(new Dimension(0, 5)));
                card.add(stockLabel);
                card.add(Box.createRigidArea(new Dimension(0, 10)));
                card.add(viewBtn);
                
                gridPanel.add(card);
            }
        }

        prodWrapper.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(prodWrapper);
    }
}
