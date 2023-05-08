package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;

import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.ServiceUser;
import org.junit.*;

import org.junit.Test;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Login extends ProjectTest {

    UUID userId;

    @BeforeAll
    public void beforeClass() {
        bridge.register("user", "1234");
        userId = bridge.login(bridge.createClient().getValue(), "test1","Test1").getValue().getId();
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
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "user","1234");
        Response<Boolean> loggedIn = bridge.isLoggedIn(login.getValue().getId());
        Response<Integer> loggedInUsers1 = bridge.numOfLoggedInUsers();

        Assert.assertFalse(loggedInUsers0.isError());
        Assert.assertFalse(login.isError());
        Assert.assertFalse(loggedIn.isError());
        Assert.assertFalse(loggedInUsers1.isError());

        Assert.assertNotNull(login.getValue());
        Assert.assertEquals(userId, login.getValue().getId());
        Assert.assertTrue(loggedIn.getValue());
        Assert.assertEquals(1, loggedInUsers1.getValue() - loggedInUsers0.getValue());
    }

    @Test
    //checks if a logged-in user can login again
    public void loginAlreadyLoggedInFail() {
        Response<Integer> loggedInUsers0 = bridge.numOfLoggedInUsers();
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "user","1234");
        Response<ServiceUser> loginAgain = bridge.login(bridge.createClient().getValue(), "user","1234");
        Response<Boolean> loggedIn = bridge.isLoggedIn(login.getValue().getId());
        Response<Integer> loggedInUsers1 = bridge.numOfLoggedInUsers();

        Assert.assertFalse(loggedInUsers0.isError());
        Assert.assertFalse(login.isError());
        Assert.assertTrue(loginAgain.isError());
        Assert.assertFalse(loggedIn.isError());
        Assert.assertFalse(loggedInUsers1.isError());

        Assert.assertEquals("User is already logged in, please log out first.", login.getMessage());
        Assert.assertTrue(loggedIn.getValue());
        Assert.assertEquals(loggedInUsers0, loggedInUsers1);
    }

    @Test
    //tests if the login function handles the scenario where the username is incorrect by asserting that the returned UUID is null.
    public void loginWrongUsernameFail() {
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "wrong","1234");

        Assert.assertTrue(login.isError());
        Assert.assertEquals("User is not registered in the system.", login.getMessage());
    }

    @Test
    // tests if the login function handles the scenario where the password is incorrect by asserting that the returned UUID is null.
    public void loginWrongPasswordFail() {
        Response<Integer> loggedInUsers0 = bridge.numOfLoggedInUsers();
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "user","wrong");
        Response<Integer> loggedInUsers1 = bridge.numOfLoggedInUsers();

        Assert.assertFalse(loggedInUsers0.isError());
        Assert.assertTrue(login.isError());
        Assert.assertFalse(loggedInUsers1.isError());

        Assert.assertEquals("Wrong password.", login.getMessage());
        Assert.assertEquals(loggedInUsers0, loggedInUsers1);
    }

    @Test
    public void loginConcurrently() {
        Response<Integer> loggedInUsers0 = bridge.numOfLoggedInUsers();

        for (int i = 0; i < 1000; i++) {
            bridge.register("user_" + i, "1234");
        }

        Response<ServiceUser>[] logins = new Response[1000];
        Thread[] threads = new Thread[1000];
        try {
            for (int i = 0; i < 1000; i++) {
                final int index = i;
                threads[i] = new Thread(() -> logins[index] = bridge.login(bridge.createClient().getValue(), "user_" + index, "1234"));
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
        for (Response<ServiceUser> l : logins) {
            Assert.assertFalse(l.isError());
            Assert.assertNotNull(l.getValue());
        }
        Assert.assertEquals(1000, loggedInUsers1.getValue() - loggedInUsers0.getValue());
    }
}
