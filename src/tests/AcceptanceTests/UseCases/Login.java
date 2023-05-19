package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;

import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.ServiceUser;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Login extends ProjectTest {

    UUID userId;

    @BeforeAll
    public void beforeClass() {
        bridge.register("user", "Aa1234");
        userId = bridge.login(bridge.createClient().getValue(), "user","Aa1234").getValue().getId();
        bridge.logout(userId);
    }

    @BeforeEach
    public void setUp() {

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
    //tests if the login function works correctly by registering and logging in with valid credentials and asserting that the client's UUID is not null.
    public void loginSuccess() {
        Response<Integer> loggedInUsers0 = bridge.numOfLoggedInUsers();
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "user","Aa1234");
        Response<Boolean> loggedIn = bridge.isLoggedIn(login.getValue().getId());
        Response<Integer> loggedInUsers1 = bridge.numOfLoggedInUsers();

        assertFalse(loggedInUsers0.isError(), String.format("bridge.numOfLoggedInUsers() => %s", loggedInUsers0.getMessage()));
        assertFalse(login.isError(), String.format("bridge.login(bridge.createClient().getValue(), \"user\",\"Aa1234\") => %s", login.getMessage()));
        assertFalse(loggedIn.isError(), String.format("bridge.isLoggedIn(login.getValue().getId()) => %s", loggedIn.getMessage()));
        assertFalse(loggedInUsers1.isError(), String.format("bridge.numOfLoggedInUsers() => %s", loggedInUsers1.getMessage()));

        assertNotNull(login.getValue(), "bridge.login(bridge.createClient().getValue(), \"user\",\"Aa1234\") failed");
        assertEquals(userId, login.getValue().getId(), "login returns wrong UUID");
        assertTrue(loggedIn.getValue(), "user is not logged-in");
        assertEquals(1, loggedInUsers1.getValue() - loggedInUsers0.getValue(), "number of logged-in users did not increased by 1");
    }

    @Test
    //checks if a logged-in user can login again
    public void loginAlreadyLoggedInFail() {
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "user","Aa1234");
        Response<Integer> loggedInUsers0 = bridge.numOfLoggedInUsers();
        Response<ServiceUser> loginAgain = bridge.login(bridge.createClient().getValue(), "user","Aa1234");
        Response<Boolean> loggedIn = bridge.isLoggedIn(login.getValue().getId());
        Response<Integer> loggedInUsers1 = bridge.numOfLoggedInUsers();

        assertFalse(login.isError(), String.format("bridge.login(bridge.createClient().getValue(), \"user\",\"Aa1234\") => %s", login.getMessage()));
        assertFalse(loggedInUsers0.isError(), String.format("bridge.numOfLoggedInUsers() => %s", loggedInUsers0.getMessage()));
        assertTrue(loginAgain.isError(), "bridge.login(bridge.createClient().getValue(), \"user\",\"Aa1234\") should have failed");
        assertFalse(loggedIn.isError(), String.format("bridge.isLoggedIn(login.getValue().getId()) => %s", loggedIn.getMessage()));
        assertFalse(loggedInUsers1.isError(), String.format("bridge.numOfLoggedInUsers() => %s", loggedInUsers1.getMessage()));

        //assertEquals("User is already logged in, please log out first.", login.getMessage(), "bridge.login(bridge.createClient().getValue(), \"user\",\"Aa1234\") should have failed");
        assertTrue(loggedIn.getValue(), "user is not logged-in");
        assertEquals(loggedInUsers0.getValue(), loggedInUsers1.getValue(), "number of logged-in users has changed");
    }

    @Test
    //tests if the login function handles the scenario where the username is incorrect by asserting that the returned UUID is null.
    public void loginWrongUsernameFail() {
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "wrong","Aa1234");

        assertTrue(login.isError(), "bridge.login(bridge.createClient().getValue(), \"wrong\",\"Aa1234\") should have failed");
        assertEquals("User is not registered in the system.", login.getMessage(), "client should not be registered");
    }

    @Test
    // tests if the login function handles the scenario where the password is incorrect by asserting that the returned UUID is null.
    public void loginWrongPasswordFail() {
        Response<Integer> loggedInUsers0 = bridge.numOfLoggedInUsers();
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "user","wrong");
        Response<Integer> loggedInUsers1 = bridge.numOfLoggedInUsers();

        assertFalse(loggedInUsers0.isError(), String.format("bridge.numOfLoggedInUsers() => %s", loggedInUsers0.getMessage()));
        assertTrue(login.isError(), "bridge.login(bridge.createClient().getValue(), \"user\",\"wrong\") should have failed");
        assertFalse(loggedInUsers1.isError(), String.format("bridge.numOfLoggedInUsers() => %s", loggedInUsers1.getMessage()));

        assertEquals("Incorrect password", login.getMessage(), login.getMessage());
        assertEquals(loggedInUsers0.getValue(), loggedInUsers1.getValue(), "number of logged-in users has changed");
    }

    @Test
    public void loginConcurrently() {
        Response<Integer> loggedInUsers0 = bridge.numOfLoggedInUsers();

        for (int i = 0; i < 1000; i++) {
            bridge.register("user_" + i, "Aa1234");
        }

        Response<ServiceUser>[] logins = new Response[1000];
        Thread[] threads = new Thread[1000];
        try {
            for (int i = 0; i < 1000; i++) {
                final int index = i;
                threads[i] = new Thread(() -> logins[index] = bridge.login(bridge.createClient().getValue(), "user_" + index, "Aa1234"));
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
        for (Response<ServiceUser> l : logins) {
            assertFalse(l.isError(), String.format("bridge.login(bridge.createClient().getValue(), \"user_\" + index, \"Aa1234\") => %s", l.getMessage()));
            assertNotNull(l.getValue(), "bridge.login(bridge.createClient().getValue(), \"user_\" + index, \"Aa1234\") failed");
        }
        assertEquals(1000, loggedInUsers1.getValue() - loggedInUsers0.getValue(), "number of logged-in users did not increased by 1000");
    }
}