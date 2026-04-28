package stationary.usermanagement.control;

import stationary.usermanagement.entity.User;
import stationary.usermanagement.entity.UserDTO;
import java.util.HashMap;
import java.util.Map;

public class AdminControl {
    private Map<String, User> userDB = new HashMap<>();
    private Map<String, Integer> orderHistoryDB = new HashMap<>();

    public void addUser(String username, String password) {
        userDB.put(username, new User(username, password));
        orderHistoryDB.put(username, 0);
    }

    public void addOrderHistory(String username, int count) {
        int current = orderHistoryDB.getOrDefault(username, 0);
        orderHistoryDB.put(username, current + count);
    }

    // Lấy chi tiết tài khoản -> Đã mapper để bảo vệ Password không lọt ra ngoài View
    public UserDTO getUserDetailSecured(String username) {
        User u = userDB.get(username);
        if (u != null) {
            return u.toAdminDTO(); 
        }
        return null;
    }

    // Chỉ xóa được nếu người dùng đó CHƯA TỪNG đặt đơn hàng nào
    public void deleteUser(String username) {
        int totalOrders = orderHistoryDB.getOrDefault(username, 0);
        if (totalOrders > 0) {
            throw new RuntimeException("Không thể xóa: người dùng hệ thống đã có lịch sử đặt hàng");
        }
        userDB.remove(username);
    }

    public boolean userExists(String username) {
        return userDB.containsKey(username);
    }

    public java.util.List<UserDTO> getAllUsers() {
        java.util.List<UserDTO> list = new java.util.ArrayList<>();
        for (User u : userDB.values()) {
            UserDTO dto = u.toAdminDTO();
            dto.setOrderCount(orderHistoryDB.getOrDefault(u.getUsername(), 0));
            list.add(dto);
        }
        return list;
    }
}
