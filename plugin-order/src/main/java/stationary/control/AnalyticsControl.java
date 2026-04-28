package stationary.control;

import stationary.entity.Order;
import stationary.entity.OrderLineItem;
import stationary.store.OrderStore;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsControl {

    private OrderStore store;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void setStore(OrderStore store) {
        this.store = store;
    }

    public Map<String, Double> getRevenueByDate() throws SQLException {
        List<Order> orders = store.getOrdersByStatus("COMPLETED");
        Map<String, Double> revenueMap = new HashMap<>();

        for (Order order : orders) {
            if (order.getOrderDate() != null) {
                String dateStr = dateFormat.format(order.getOrderDate());
                double current = revenueMap.getOrDefault(dateStr, 0.0);
                revenueMap.put(dateStr, current + order.getTotalAmount());
            }
        }
        return revenueMap;
    }

    public List<Map<String, Object>> getTopSellingProducts(int limit) throws SQLException {
        List<Order> orders = store.getOrdersByStatus("COMPLETED");
        Map<String, Integer> productSales = new HashMap<>();

        for (Order order : orders) {
            for (OrderLineItem item : order.getItems()) {
                String name = item.getProductName();
                if (name == null || name.isEmpty()) continue;
                
                int currentQty = productSales.getOrDefault(name, 0);
                productSales.put(name, currentQty + item.getQuantity());
            }
        }

        // Sort descending by sold quantity
        List<Map.Entry<String, Integer>> sortedList = productSales.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(limit)
                .collect(Collectors.toList());

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedList) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", entry.getKey());
            map.put("sold", entry.getValue());
            result.add(map);
        }

        return result;
    }
}
