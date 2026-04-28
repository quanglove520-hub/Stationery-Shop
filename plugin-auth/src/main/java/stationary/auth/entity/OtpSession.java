package stationary.auth.entity;

import java.time.LocalDateTime;

public class OtpSession {
    private String phoneNumber;
    private String otpCode;
    private LocalDateTime createdAt;
    private int invalidAttempts;

    private static final int MAX_ATTEMPTS = 5;
    private static final int EXPIRY_MINUTES = 3;

    public OtpSession(String phoneNumber, String otpCode) {
        this.phoneNumber = phoneNumber;
        this.otpCode = otpCode;
        this.createdAt = LocalDateTime.now();
        this.invalidAttempts = 0;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getInvalidAttempts() {
        return invalidAttempts;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(createdAt.plusMinutes(EXPIRY_MINUTES));
    }

    public boolean isLocked() {
        return invalidAttempts >= MAX_ATTEMPTS;
    }

    public boolean verifyOtp(String inputOtp) {
        if (isLocked() || isExpired()) {
            return false;
        }

        if (this.otpCode.equals(inputOtp)) {
            return true;
        } else {
            invalidAttempts++;
            return false;
        }
    }
}
