package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import org.junit.*;

import java.util.UUID;

public class Register extends ProjectTest {

    UUID clientCredentials;
    UUID userId;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
        UUID clientCredentials2 = bridge.enterSystem();
        bridge.register("test", "test");
    }

    @Before
    public void beforeEach()  {
        clientCredentials = bridge.enterSystem();
    }

    @After
    public void tearDown() {
        bridge.exitSystem(clientCredentials);
    }

    @Test
    public void registerSuccess() {
        Boolean success = bridge.register("test2", "test");
        Assert.assertNotNull(success);
        Assert.assertTrue(success);
    }

    @Test
    public void registerExistingUserFail() {
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

    @Test
    public void registerClientLoggedInFail() {
        userId = bridge.login(clientCredentials,"test", "test");
        Boolean success = bridge.register("test2", "test");
        Assert.assertNotNull(success);
        Assert.assertFalse(success);
    }

}
