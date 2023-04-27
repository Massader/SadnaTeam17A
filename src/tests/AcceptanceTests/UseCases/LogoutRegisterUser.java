package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogoutRegisterUser extends ProjectTest {

    UUID client1;
    UUID client2;


    @Before
    public void setUp()  {
        bridge.setReal();
        bridge.register("test", "TestPass1");
        client1 = bridge.createClient();
        client2 = bridge.login(client1, "test","TestPass1");
    }

    @After
    public void tearDown() {
        bridge.closeClient(client1);
        bridge.closeClient(client2);
    }

    @Test
    //tests if the logout function works correctly by asserting that the client's UUID is not null after logging out.
    public void logoutSuccess() {
        client2 = bridge.logout(client2);
        Assert.assertNotNull(client2);
    }

    @Test
    //tests if the logout function handles the scenario where the client is not registered or already logged out by asserting that the returned UUID is null after attempting to logout twice.
    public void logoutNotRegisterFail() {
        client2 = bridge.logout(client2);
        UUID client3 = bridge.logout(client2);
        Assert.assertNull(client3);
    }
}