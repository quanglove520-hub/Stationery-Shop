package stationary.ecommerce.entity;

public class ProductReview {
    private String id;
    private String productId;
    private String author;
    private int rating; // 1 to 5
    private String comment;

    public ProductReview(String id, String productId, String author, int rating, String comment) {
        this.id = id;
        this.productId = productId;
        this.author = author;
        this.rating = rating;
        this.comment = comment;
    }

    public String getId() { return id; }
    public String getProductId() { return productId; }
    public String getAuthor() { return author; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
}
