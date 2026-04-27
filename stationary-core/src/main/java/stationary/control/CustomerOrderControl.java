package stationary.control;

import stationary.entity.Order;
import stationary.entity.OrderLineItem;
import stationary.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerOrderControl {
    private Order currentCart;
    // private CatalogControl catalogControl;

    public CustomerOrderControl() {
        this.currentCart = new Order("CART-SESSION", new ArrayList<>());
        // this.catalogControl = new CatalogControl();
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
            Product p = null; // catalogControl.getProductById(item.getProductId());
            if (p != null) {
                total += p.getPrice() * item.getQuantity();
            }
        }
        return total;
    }

    public void checkout() {
        // Giả lập Thanh Toán bằng cách dọn dẹp giỏ hàng
        currentCart.getItems().clear();
    }
}
