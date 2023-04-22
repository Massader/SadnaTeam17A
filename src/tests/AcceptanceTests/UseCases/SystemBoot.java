package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SystemBoot extends ProjectTest {

    UUID clientCredentials;

    @BeforeAll
    public void setUp() {
    }

    @BeforeEach
    public void beforeEach() {
        clientCredentials = bridge.createClient();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(clientCredentials);
    }

    @Test
    public void systemBootSuccess() {
        UUID adminCredentials = bridge.login(clientCredentials, "admin", "Admin1");
        Assert.assertNotNull(adminCredentials);
        // When external systems implementation works add a check for valid connections with them
    }
}
