package stationary.entity;

import java.util.List;

public class Order {
    private String id;
    private List<OrderLineItem> items;

    public Order(String id, List<OrderLineItem> items) {
        this.id = id;
        this.items = items;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public List<OrderLineItem> getItems() { return items; }
    public void setItems(List<OrderLineItem> items) { this.items = items; }
}
