package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Register extends ProjectTest {

    UUID clientCredentials;
    UUID clientCredentials2;

    @Before
    public void beforeEach()  {
        bridge.setReal();
        clientCredentials = bridge.createClient().getValue();
        clientCredentials2 = bridge.createClient().getValue();
    }

    @After
    public void tearDown() {
        bridge.closeClient(clientCredentials);
        bridge.closeClient(clientCredentials2);
    }

    @Test
    //Tests whether the registration of a new user with valid credentials is successful.
    public void registerSuccess() {
        Boolean success = bridge.register("test2", "test").getValue();
        Assert.assertNotNull(success);
        Assert.assertTrue(success);
    }

    @Test
    //Tests whether the registration of a user with already existing username fails.
    public void registerExistingUserFail() {
        bridge.register("test", "test");
        Boolean success = bridge.register("test", "test").getValue();
        Assert.assertNotNull(success);
        Assert.assertFalse(success);
    }

    @Test
    //Tests whether the registration of a user with a null username fails.
    public void registerNullUsernameFail() {
        Boolean success = bridge.register(null, "test").getValue();
        Assert.assertNotNull(success);
        Assert.assertFalse(success);
    }

    @Test
    //Tests whether the registration of a user with a null password fails.
    public void registerNullPasswordFail() {
        Boolean success = bridge.register("test2", null).getValue();
        Assert.assertNotNull(success);
        Assert.assertFalse(success);
    }
}
