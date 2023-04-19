package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
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
        bridge.exitSystem(clientCredentials);
    }

    @Test
    public void enterSystemSuccess() {
        clientCredentials = bridge.enterSystem();
        Assert.assertNotNull(clientCredentials);
    }
}
