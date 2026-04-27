package stationary.ecommerce.entity;

public class DiscountCode {
    private String code;
    private double discountRate;

    public DiscountCode(String code, double discountRate) {
        this.code = code;
        this.discountRate = discountRate;
    }

    public String getCode() { return code; }
    public double getDiscountRate() { return discountRate; }
}
