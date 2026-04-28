package stationary.auth.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class Account {
    private String id;
    private String email;
    private String username;
    private String passwordHash;
    private String fullName;
    private String address;
    private Role role;

    public enum Role {
        CUSTOMER, ADMIN
    }

    public Account(String email, String username, String rawPassword) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.username = username;
        this.passwordHash = hashPassword(rawPassword);
        this.role = Role.CUSTOMER;
    }

    private String hashPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPassword.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi băm mật khẩu", e);
        }
    }

    public boolean verifyPassword(String rawPassword) {
        return this.passwordHash.equals(hashPassword(rawPassword));
    }

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
