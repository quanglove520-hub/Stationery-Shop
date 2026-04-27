package stationary.ecommerce.control;

import stationary.ecommerce.entity.ProductReview;
import stationary.ecommerce.store.EcommerceStore;

import java.util.List;

public class EcommerceControl {
    private EcommerceStore store;

    public EcommerceControl() {
        this.store = new EcommerceStore();
    }

    public boolean validateDiscount(String code) {
        return "SALE10".equalsIgnoreCase(code);
    }

    public List<ProductReview> getProductReviews(String productId) {
        return store.getReviewsByProduct(productId);
    }

    public void addReview(String productId, String author, int rating, String comment) {
        String id = "R" + System.currentTimeMillis();
        ProductReview pr = new ProductReview(id, productId, author, rating, comment);
        store.addReview(pr);
    }
}

