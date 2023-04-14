package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import org.junit.*;

import java.util.UUID;

public class EnterSystem extends ProjectTest {

    UUID clientCredentials;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
    }

    @Before
    public void beforeEach()  {
    }

    @After
    public void tearDown() {
        bridge.exitSystem(clientCredentials);
    }

    @Test
    public void enterSystemSuccess() {
        clientCredentials = bridge.enterSystem();
        Assert.assertNotNull(clientCredentials);
    }
}
