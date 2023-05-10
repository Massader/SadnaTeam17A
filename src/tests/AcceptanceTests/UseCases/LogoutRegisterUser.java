package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;

import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.ServiceUser;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.*;

import org.junit.Test;
import org.junit.jupiter.api.*;

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

        Assert.assertFalse(loggedInUsers0.isError());
        Assert.assertFalse(logout.isError());
        Assert.assertFalse(loggedInUsers1.isError());

        Assert.assertNotNull(logout.getValue());
        Assert.assertNotEquals(userId, logout.getValue());
        Assert.assertEquals(1, loggedInUsers0.getValue() - loggedInUsers1.getValue());
    }

    @Test
    public void logoutNotLoggedInFail() {
        bridge.logout(userId);

        Response<Integer> loggedInUsers0 = bridge.numOfLoggedInUsers();
        Response<UUID> logoutAgain = bridge.logout(userId);
        Response<Integer> loggedInUsers1 = bridge.numOfLoggedInUsers();

        Assert.assertFalse(loggedInUsers0.isError());
        Assert.assertTrue(logoutAgain.isError());
        Assert.assertFalse(loggedInUsers1.isError());

        Assert.assertEquals("this user is already logged out", logoutAgain.getMessage());
        Assert.assertEquals(loggedInUsers0.getValue(), loggedInUsers1.getValue());
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

        Assert.assertFalse(loggedInUsers0.isError());
        Assert.assertFalse(loggedInUsers1.isError());
        for (Response<UUID> l : logouts) {
            Assert.assertFalse(l.isError());
            Assert.assertNotNull(l.getValue());
        }
        Assert.assertEquals(1000, loggedInUsers0.getValue() - loggedInUsers1.getValue());
    }
}