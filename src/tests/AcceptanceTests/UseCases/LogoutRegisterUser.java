package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import org.junit.*;

import java.util.UUID;

public class LogoutRegisterUser extends ProjectTest {

    UUID client1;
    UUID client2;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
        bridge.register("test", "test");
    }

    @Before
    public void beforeEach()  {
        client1 = bridge.enterSystem();
        client2 = bridge.login(client1, "test","test");
    }

    @After
    public void tearDown() {
        bridge.exitSystem(client1);
        bridge.exitSystem(client2);
    }

    @Test
    public void logoutSuccess() {
        client2 = bridge.logout(client2);
        Assert.assertNotNull(client2);
    }

    @Test
    public void logoutNotRegisterFail() {
        client2 = bridge.logout(client2);
        UUID client3 = bridge.logout(client2);
        Assert.assertNull(client3);
    }
}