package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import org.junit.*;

import org.junit.Test;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RealTimeAlerts extends ProjectTest {

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
    public void realTimeAlertsSuccess() {
        Assert.assertTrue(true);
    }
}
