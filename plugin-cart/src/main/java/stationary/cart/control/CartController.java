package stationary.cart.control;

import stationary.cart.entity.CartItem;
import stationary.cart.store.CartStore;
import stationary.control.CatalogControl;
import stationary.entity.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CartController {
    private final CartStore store = new CartStore();
    private final CatalogControl catalogControl = new CatalogControl();
    private final Gson gson = new Gson();

    public String addToCart(String requestBody) {
        try {
            CartItem item = gson.fromJson(requestBody, CartItem.class);
            // Default increment is 1 when adding from catalog
            boolean success = store.upsertCartItem(item.getProductId(), 1);
            if (success) {
                return "{\"status\": \"success\", \"message\": \"Added to cart\"}";
            } else {
                return "{\"status\": \"error\", \"message\": \"DB Error\"}";
            }
        } catch (Exception e) {
            return "{\"status\": \"error\", \"message\": \"Invalid JSON\"}";
        }
    }

    public String getCartContent() {
        List<CartItem> items = store.getAllItems();
        double total = 0;
        for (CartItem item : items) {
            Product p = catalogControl.getProductById(item.getProductId());
            if (p != null) {
                total += p.getPrice() * item.getQuantity();
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("items", items);
        response.put("total", total);
        return gson.toJson(response);
    }

    public String checkout(String requestBody) {
        List<CartItem> items = store.getAllItems();
        if (items.isEmpty()) {
            return "{\"status\": \"error\", \"message\": \"Cart is empty\"}";
        }

        String paymentMethod = "Tiền mặt";
        try {
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> body = gson.fromJson(requestBody, type);
            if (body != null && body.containsKey("paymentMethod")) {
                paymentMethod = body.get("paymentMethod");
            }
        } catch (Exception e) {
            // Fallback to default
        }

        store.clearCart();
        String message = "Checkout successful via " + paymentMethod;
        return "{\"status\": \"success\", \"message\": \"" + message + "\"}";
    }

    public String updateCart(String requestBody) {
        try {
            CartItem item = gson.fromJson(requestBody, CartItem.class);
            // item.getQuantity() is actually the delta (+1 or -1) from app.js
            int delta = item.getQuantity();
            boolean success = store.upsertCartItem(item.getProductId(), delta);
            
            // Clean up: remove items that reached 0 or less
            List<CartItem> all = store.getAllItems();
            for(CartItem i : all) {
                if(i.getQuantity() <= 0) {
                    store.deleteCartItem(i.getProductId());
                }
            }

            if (success) {
                return "{\"status\": \"success\", \"message\": \"Quantity updated\"}";
            } else {
                return "{\"status\": \"error\", \"message\": \"Not found in cart\"}";
            }
        } catch (Exception e) {
            return "{\"status\": \"error\", \"message\": \"Invalid update request\"}";
        }
    }

    public String removeItem(String productId) {
        boolean success = store.deleteCartItem(productId);
        if (success) {
            return "{\"status\": \"success\", \"message\": \"Sản phẩm đã được xóa khỏi giỏ\"}";
        } else {
            return "{\"status\": \"error\", \"message\": \"Không thể xóa sản phẩm\"}";
        }
    }
}
