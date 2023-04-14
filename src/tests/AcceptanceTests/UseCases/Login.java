package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import org.junit.*;

import java.util.UUID;

public class Login extends ProjectTest {

    UUID clientCredentials;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
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
    public void loginSuccess() {
        UUID userId = bridge.login(clientCredentials, "test","test");
        Assert.assertNotNull(userId);
    }

    @Test
    public void loginFailWrongUsername() {
        UUID userId = bridge.login(clientCredentials, "nottest", "test");
        Assert.assertNull(userId);
    }

    @Test
    public void loginFailWrongPassword() {
        UUID userId = bridge.login(clientCredentials, "test", "nottest");
        Assert.assertNull(userId);
    }
}
