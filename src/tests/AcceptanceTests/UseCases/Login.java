package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Login extends ProjectTest {

    UUID clientCredentials;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("test1", "Test1");
    }

    @BeforeEach
    public void beforeEach()  {
        clientCredentials = bridge.createClient();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(clientCredentials);
    }

    @Test
    //tests if the login function works correctly by registering and logging in with valid credentials and asserting that the client's UUID is not null.
    public void loginSuccess() {
        bridge.register("test1", "Test1");
        UUID userId = bridge.login(clientCredentials, "test1","Test1").getId();
        Assert.assertNotNull(userId);
    }

    @Test
    //tests if the login function handles the scenario where the username is incorrect by asserting that the returned UUID is null.
    public void loginFailWrongUsername() {
        UUID userId = bridge.login(clientCredentials, "nottest", "Notest1").getId();
        Assert.assertNull(userId);
    }

    @Test
    // tests if the login function handles the scenario where the password is incorrect by asserting that the returned UUID is null.
    public void loginFailWrongPassword() {
        UUID userId = bridge.login(clientCredentials, "test", "nottest").getId();
        Assert.assertNull(userId);
    }

    @Test
// tests if the login function handles the scenario where the user is already logged in by asserting that the returned UUID is null.
    public void loginFailLoginTwice() {
        // register and log in user
        bridge.register("test1", "Test1");
        UUID userId = bridge.login(clientCredentials, "test1", "Test1").getId();
        Assert.assertNotNull(userId);
        // try to log in again
        UUID secondUserId = bridge.login(clientCredentials, "test1", "Test1").getId();
        Assert.assertNull(secondUserId);
    }
}
