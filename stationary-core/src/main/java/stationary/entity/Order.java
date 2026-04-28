package stationary.entity;

import java.util.List;

public class Order {
    private String id;
    private List<OrderLineItem> items;
    private java.util.Date orderDate;
    private String status = "COMPLETED";
    private double totalAmount;
    private String customerEmail;

    public Order(String id, List<OrderLineItem> items) {
        this.id = id;
        this.items = items;
        this.orderDate = new java.util.Date();
    }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String email) { this.customerEmail = email; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public List<OrderLineItem> getItems() { return items; }
    public void setItems(List<OrderLineItem> items) { this.items = items; }
    
    public java.util.Date getOrderDate() { return orderDate; }
    public void setOrderDate(java.util.Date orderDate) { this.orderDate = orderDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}
