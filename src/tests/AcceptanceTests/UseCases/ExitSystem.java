package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExitSystem extends ProjectTest {

    UUID clientCredentials;

    @Before
    public void beforeEach()  {
        bridge.setReal();
        clientCredentials = bridge.createClient();
    }

    @After
    public void tearDown() {
        bridge.closeClient(clientCredentials);
    }

    @Test
    //Tests that a client can successfully exit the system. It creates a client and then attempts to close it, asserting that the operation is successful.
    public void exitSystemSuccess() {
        Boolean success = bridge.closeClient(clientCredentials);
        Assert.assertNotNull(success);
        Assert.assertTrue(success);
    }
}