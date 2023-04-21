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
public class Supply extends ProjectTest {

    UUID clientCredentials;

    @BeforeAll
    public void setUp() {
    }

    @BeforeEach
    public void beforeEach() {
        clientCredentials = bridge.enterSystem();
    }

    @AfterEach
    public void tearDown() {
        bridge.exitSystem(clientCredentials);
    }

    @Test
    public void supplySuccess() {
        Assert.assertTrue(true);
    }
}
