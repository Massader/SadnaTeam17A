package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import org.junit.*;

import java.util.UUID;

public class ExitSystem extends ProjectTest {

    UUID clientCredentials;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
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
    public void exitSystemSuccess() {
        Boolean success = bridge.exitSystem(clientCredentials);
        Assert.assertNotNull(success);
        Assert.assertTrue(success);
    }
}