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
public class Login extends ProjectTest {

    UUID clientCredentials;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("test1", "Test1");
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
    public void loginSuccess() {
        bridge.register("test1", "Test1");
        UUID userId = bridge.login(clientCredentials, "test1","Test1");
        Assert.assertNotNull(userId);
    }

    @Test
    public void loginFailWrongUsername() {
        UUID userId = bridge.login(clientCredentials, "nottest", "Notest1");
        Assert.assertNull(userId);
    }

    @Test
    public void loginFailWrongPassword() {
        UUID userId = bridge.login(clientCredentials, "test", "nottest");
        Assert.assertNull(userId);
    }
}
