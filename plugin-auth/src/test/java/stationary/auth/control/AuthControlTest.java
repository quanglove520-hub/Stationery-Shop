package stationary.auth.control;


import stationary.auth.control.AuthControl;
import stationary.auth.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthControlTest {

    private AuthControl authControl;

    @BeforeEach
    public void setUp() {
        authControl = new AuthControl();
    }

    @Test
    public void testRegisterSuccess() {
        Account account = authControl.register("testuser@example.com", "password123", "testuser");
        assertNotNull(account);
        assertEquals("testuser@example.com", account.getEmail());
    }

    @Test
    public void testRegisterDuplicate() {
        authControl.register("testuser@example.com", "password123", "testuser");
        assertThrows(IllegalArgumentException.class, () -> {
            authControl.register("testuser@example.com", "newpassword123", "testuser2");
        });
    }

    @Test
    public void testRegisterInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            authControl.register("testuser", "password123", "testuser");
        });
    }

    @Test
    public void testRegisterInvalidPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            authControl.register("testuser@example.com", "pass", "testuser");
        });
    }

    @Test
    public void testLoginSuccess() {
        authControl.register("testuser@example.com", "password123", "testuser");
        Account account = authControl.login("testuser@example.com", "password123");
        assertNotNull(account);
    }

    @Test
    public void testLoginWrongPassword() {
        authControl.register("testuser@example.com", "password123", "testuser");
        assertThrows(IllegalArgumentException.class, () -> {
            authControl.login("testuser@example.com", "wrongpass123");
        });
    }
}
