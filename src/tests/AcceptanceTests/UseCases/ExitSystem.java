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

    @BeforeAll
    public void setUp() {
        bridge.setReal();
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
    public void exitSystemSuccess() {
        Boolean success = bridge.closeClient(clientCredentials);
        Assert.assertNotNull(success);
        Assert.assertTrue(success);
    }
}