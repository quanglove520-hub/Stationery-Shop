package stationary.ecommerce.store;

import stationary.ecommerce.entity.ProductReview;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EcommerceStore {
    // Lưu tạm wishlist/cart dưới store nếu cần sau này (hiện tại Frontend dùng localStorage làm chính)
    public Map<String, String> sessionData = new HashMap<>();

    private List<ProductReview> reviews = new ArrayList<>();

    public EcommerceStore() {
        seedReviews();
    }

    private void seedReviews() {
        // P1 is "Bút bi Thiên Long"
        reviews.add(new ProductReview("R1", "P1", "Nguyễn Văn A", 5, "Bút viết rất êm, mực ra đều. Ủng hộ shop!"));
        reviews.add(new ProductReview("R2", "P1", "Trần Thị B", 5, "Hàng chính hãng Thiên Long, giá tốt."));
        reviews.add(new ProductReview("R3", "P1", "Lê Văn C", 5, "Rất hài lòng với sản phẩm."));
    }

    public List<ProductReview> getReviewsByProduct(String productId) {
        return reviews.stream()
                .filter(r -> r.getProductId().equals(productId))
                .collect(Collectors.toList());
    }

    public void addReview(ProductReview review) {
        reviews.add(review);
    }
}
