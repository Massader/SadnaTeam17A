package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import org.junit.*;

import java.util.UUID;

public class RealTimeAlerts extends ProjectTest {

    UUID clientCredentials;

    @BeforeClass
    public void setUp() {

    }

    @Before
    public void beforeEach() {
        clientCredentials = bridge.enterSystem();
    }

    @After
    public void tearDown() {
        bridge.exitSystem(clientCredentials);
    }

    @Test
    public void realTimeAlertsSuccess() {
        Assert.assertTrue(true);
    }
}
