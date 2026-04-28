package stationary.cart.entity;

public class CartItem {
    private String productId;
    private int quantity;

    public CartItem() {} // For JSON deserialization

    public CartItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}
