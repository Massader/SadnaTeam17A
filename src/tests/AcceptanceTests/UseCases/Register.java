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
        clientCredentials = bridge.createClient();
        clientCredentials2 = bridge.createClient();
    }

    @After
    public void tearDown() {
        bridge.closeClient(clientCredentials);
        bridge.closeClient(clientCredentials2);
    }

    @Test
    public void registerSuccess() {
        Boolean success = bridge.register("test2", "test");
        Assert.assertNotNull(success);
        Assert.assertTrue(success);
    }

    @Test
    public void registerExistingUserFail() {
        bridge.register("test", "test");
        Boolean success = bridge.register("test", "test");
        Assert.assertNotNull(success);
        Assert.assertFalse(success);
    }

    @Test
    public void registerNullUsernameFail() {
        Boolean success = bridge.register(null, "test");
        Assert.assertNotNull(success);
        Assert.assertFalse(success);
    }

    @Test
    public void registerNullPasswordFail() {
        Boolean success = bridge.register("test2", null);
        Assert.assertNotNull(success);
        Assert.assertFalse(success);
    }
}
