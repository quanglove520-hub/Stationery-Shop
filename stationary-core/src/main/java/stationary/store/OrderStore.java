package stationary.store;

import stationary.entity.Order;
import java.sql.SQLException;
import java.util.List;

public interface OrderStore {
    boolean saveOrder(Order order) throws SQLException;
    List<Order> getAllOrders() throws SQLException;
    List<Order> getOrdersByStatus(String status) throws SQLException;
}
