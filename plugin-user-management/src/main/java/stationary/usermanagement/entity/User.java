package stationary.usermanagement.entity;

public class User {
    private String username;
    private String passwordHash;
    private String email;
    private String phone = "Chưa cập nhật";
    private String status = "Active";
    private String avatarUrl;

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = username + "@example.com";
        this.avatarUrl = "https://ui-avatars.com/api/?name=" + username;
    }

    public String getUsername() {
        return username;
    }

    // Bảo mật: Admin không được thấy password
    public UserDTO toAdminDTO() {
        UserDTO dto = new UserDTO(this.username, null);
        dto.setEmail(this.email);
        dto.setPhone(this.phone);
        dto.setStatus(this.status);
        dto.setAvatarUrl(this.avatarUrl);
        return dto;
    }

    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getStatus() { return status; }
    public String getAvatarUrl() { return avatarUrl; }
}
