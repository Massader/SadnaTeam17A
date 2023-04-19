package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Register extends ProjectTest {

    UUID clientCredentials;
    UUID userId;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        UUID clientCredentials2 = bridge.enterSystem();
        bridge.register("test", "test");
    }

    @BeforeEach
    public void beforeEach()  {
        clientCredentials = bridge.enterSystem();
    }

    @AfterEach
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
