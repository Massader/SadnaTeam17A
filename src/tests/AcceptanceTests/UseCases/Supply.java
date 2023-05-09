package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import org.junit.*;

import org.junit.Test;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Supply extends ProjectTest {

    @BeforeAll
    public void beforeClass() {
    }

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {

    }

    @AfterAll
    public void afterClass() {

    }

    @Test
    public void supplySuccess() {
        Assert.assertTrue(true);
    }
}
