package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnterSystem extends ProjectTest {

    UUID clientCredentials;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
    }

    @BeforeEach
    public void beforeEach()  {
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(clientCredentials);
    }

    @Test
    //checks if a client can enter the system by creating a new client credentials and ensuring that the client credentials is not null.
    public void enterSystemSuccess() {
        clientCredentials = bridge.createClient().getValue();
        Assert.assertNotNull(clientCredentials);
    }
}
