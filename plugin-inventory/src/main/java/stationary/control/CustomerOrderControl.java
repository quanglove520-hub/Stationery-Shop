package stationary.control;

import stationary.entity.Order;
import stationary.entity.OrderLineItem;
import stationary.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.UUID;
import stationary.store.OrderStore;

public class CustomerOrderControl {
    private Order currentCart;
    private CatalogControl catalogControl;
    private OrderStore orderStore;

    public CustomerOrderControl() {
        this.currentCart = new Order("CART-SESSION", new ArrayList<>());
        this.catalogControl = new CatalogControl();
    }

    public void setCatalogControl(CatalogControl catalogControl) {
        this.catalogControl = catalogControl;
    }

    public void setOrderStore(OrderStore store) {
        this.orderStore = store;
    }

    public void addToCart(String productId, int quantity) {
        List<OrderLineItem> items = currentCart.getItems();
        Optional<OrderLineItem> existing = items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
        } else {
            items.add(new OrderLineItem(productId, quantity));
        }
    }

    public List<OrderLineItem> getCartItems() {
        return currentCart.getItems();
    }

    public double calculateTotal() {
        double total = 0;
        for (OrderLineItem item : currentCart.getItems()) {
            try {
                Product p = catalogControl.getProductById(item.getProductId());
                if (p != null) {
                    total += p.getPrice() * item.getQuantity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    public void checkout() {
        if (currentCart.getItems().isEmpty()) return;

        try {
            double total = calculateTotal();
            String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            // Tạo bản sao snapshot của các item (tên, giá hiện tại)
            List<OrderLineItem> snapshotItems = new ArrayList<>();
            for (OrderLineItem item : currentCart.getItems()) {
                Product p = catalogControl.getProductById(item.getProductId());
                if (p != null) {
                    OrderLineItem snapshot = new OrderLineItem(p.getId(), p.getName(), item.getQuantity(), p.getPrice());
                    snapshotItems.add(snapshot);
                }
            }

            Order finalOrder = new Order(orderId, snapshotItems);
            finalOrder.setTotalAmount(total);
            finalOrder.setStatus("COMPLETED");

            if (orderStore != null) {
                orderStore.saveOrder(finalOrder);
            }

            // Dọn dẹp giỏ hàng
            currentCart.getItems().clear();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thanh toán: " + e.getMessage());
        }
    }
}
