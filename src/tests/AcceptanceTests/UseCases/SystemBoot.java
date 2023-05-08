package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import org.junit.*;

import org.junit.Test;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SystemBoot extends ProjectTest {

    UUID clientCredentials;

    @BeforeAll
    public void beforeClass() {
    }

    @BeforeEach
    public void setUp() {
        clientCredentials = bridge.createClient().getValue();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(clientCredentials);
    }

    @AfterAll
    public void afterClass() {

    }

    @Test
    // test verifies that the system can successfully log in an admin user with valid credentials and checks for valid connections with external systems once they are implemented.
    public void systemBootSuccess() {
        UUID adminCredentials = bridge.login(clientCredentials, "admin", "Admin1").getValue().getId();
        Assert.assertNotNull(adminCredentials);
        // When external systems implementation works add a check for valid connections with them
    }
}
