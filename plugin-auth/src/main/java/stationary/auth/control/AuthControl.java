package stationary.auth.control;
import stationary.auth.entity.Account;


import java.util.HashMap;
import java.util.Map;

public class AuthControl {
    private Map<String, Account> accountDb = new HashMap<>(); // email -> Account

    public AuthControl() {
        // Tạo sẵn tài khoản admin
        Account admin = new Account("admin@example.com", "admin", "admin1234");
        admin.setRole(Account.Role.ADMIN);
        accountDb.put(admin.getEmail(), admin);

        // Tạo sẵn tài khoản khách hàng
        Account customer = new Account("customer@example.com", "customer", "cust1234");
        accountDb.put(customer.getEmail(), customer);
    }

    public Account register(String email, String password, String username) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("định dạng không hợp lệ"); // Thống nhất với message lỗi định dạng
                                                                          // (Scenario 1.1)
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("định dạng không hợp lệ");
        }

        if (accountDb.containsKey(email)) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }

        Account newAccount = new Account(email, username, password);
        accountDb.put(email, newAccount);
        return newAccount;
    }

    public Account login(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin");
        }

        Account account = accountDb.get(email);
        if (account == null || !account.verifyPassword(password)) {
            throw new IllegalArgumentException("Email hoặc mật khẩu không chính xác");
        }

        return account;
    }
}
