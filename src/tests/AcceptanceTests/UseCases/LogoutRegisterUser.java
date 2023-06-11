package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;

import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.ServiceUser;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogoutRegisterUser extends ProjectTest {

    UUID userId;

    @BeforeAll
    public void beforeClass()  {
        bridge.register("user", "Aa1234");
        userId = bridge.login(bridge.createClient().getValue(), "user", "Aa1234").getValue().getId();
        bridge.logout(userId);
    }

    @BeforeEach
    public void setUp() {
        bridge.login(bridge.createClient().getValue(), "user", "Aa1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(userId);
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }


    @Test
    //tests if the logout function works correctly by asserting that the client's UUID is not null after logging out.
    public void logoutSuccess() {
        Response<Integer> loggedInUsers0 = bridge.numOfLoggedInUsers();
        Response<UUID> logout = bridge.logout(userId);
        Response<Integer> loggedInUsers1 = bridge.numOfLoggedInUsers();

        assertFalse(loggedInUsers0.isError(), String.format("bridge.numOfLoggedInUsers() => %s", loggedInUsers0.getMessage()));
        assertFalse(logout.isError(), String.format("bridge.logout(userId) => %s", logout.getMessage()));
        assertFalse(loggedInUsers1.isError(), String.format("bridge.numOfLoggedInUsers() => %s", loggedInUsers1.getMessage()));

        assertNotNull(logout.getValue(), "bridge.logout(userId) failed");
        assertNotEquals(userId, logout.getValue(), "logout returned wrong UUID");
        assertEquals(1, loggedInUsers0.getValue() - loggedInUsers1.getValue(), "number of logged-in users did not decreased by 1");
    }

    @Test
    public void logoutNotLoggedInFail() {
        bridge.logout(userId);

        Response<Integer> loggedInUsers0 = bridge.numOfLoggedInUsers();
        Response<UUID> logoutAgain = bridge.logout(userId);
        Response<Integer> loggedInUsers1 = bridge.numOfLoggedInUsers();

        assertFalse(loggedInUsers0.isError(), String.format("bridge.numOfLoggedInUsers() => %s", loggedInUsers0.getMessage()));
        assertTrue(logoutAgain.isError(), "bridge.logout(userId) should have failed");
        assertFalse(loggedInUsers1.isError(), String.format("bridge.numOfLoggedInUsers() => %s", loggedInUsers1.getMessage()));

        assertEquals("this user is already logged out", logoutAgain.getMessage(), logoutAgain.getMessage());
        assertEquals(loggedInUsers0.getValue(), loggedInUsers1.getValue(), "number of logged-in users has changed");
    }

    @Test
    public void logoutConcurrently() {
        UUID[] ids = new UUID[1000];
        for (int i = 0; i < 1000; i++) {
            bridge.register("user_" + i, "Aa1234");
            ids[i] = bridge.login(bridge.createClient().getValue(), "user_" + i, "Aa1234").getValue().getId();
        }

        Response<Integer> loggedInUsers0 = bridge.numOfLoggedInUsers();
        Response<UUID>[] logouts = new Response[1000];
        Thread[] threads = new Thread[1000];
        try {
            for (int i = 0; i < 1000; i++) {
                final int index = i;
                threads[i] = new Thread(() -> logouts[index] = bridge.logout(ids[index]));
                threads[i].start();
            }
            for (Thread t : threads) {
                t.join();
            }
        }
        catch (Exception ignore) {}

        Response<Integer> loggedInUsers1 = bridge.numOfLoggedInUsers();

        assertFalse(loggedInUsers0.isError(), String.format("bridge.numOfLoggedInUsers() => %s", loggedInUsers0.getMessage()));
        assertFalse(loggedInUsers1.isError(), String.format("bridge.numOfLoggedInUsers() => %s", loggedInUsers1.getMessage()));
        for (Response<UUID> l : logouts) {
            assertFalse(l.isError(), String.format("bridge.login(bridge.createClient().getValue(), \"user_\" + index, \"Aa1234\") => %s", l.getMessage()));
            assertNotNull(l.getValue(), "bridge.login(bridge.createClient().getValue(), \"user_\" + index, \"Aa1234\") failed");
        }
        assertEquals(1000, loggedInUsers0.getValue() - loggedInUsers1.getValue(), "number of logged-in users did not decreased by 1000");
    }
}