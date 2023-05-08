package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import org.junit.*;

import org.junit.Test;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Register extends ProjectTest {

    @BeforeAll
    public void beforeClass() {

    }

    @BeforeEach
    public void setUp() {
        bridge.resetService();
    }

    @AfterEach
    public void tearDown() {

    }

    @AfterAll
    public void afterClass() {

    }

    @Test
    //Tests whether the registration of a new user with valid credentials is successful.
    public void registerSuccess() {
        int users0 = bridge.numOfUsers().getValue();
        Boolean success = bridge.register("test2", "test").getValue();
        int users1 = bridge.numOfUsers().getValue();

        Assert.assertNotNull(success);
        Assert.assertTrue(success);
        //assert that there is one more registered user.
        Assert.assertEquals(users0 + 1, users1);

    }

    @Test
    //Tests whether the registration of a user with already existing username fails.
    public void registerExistingUserFail() {
        int users0 = bridge.numOfUsers().getValue();
        bridge.register("test", "test");
        int users1 = bridge.numOfUsers().getValue();
        Boolean success = bridge.register("test", "test").getValue();
        int users2 = bridge.numOfUsers().getValue();

        Assert.assertNotNull(success);
        Assert.assertFalse(success);
        Assert.assertEquals(users0 + 1, users1);
        //assert that no users has been added when fail
        Assert.assertEquals(users1, users2);
    }

    @Test
    //Tests whether the registration of a user with a null username fails.
    public void registerNullUsernameFail() {
        int users0 = bridge.numOfUsers().getValue();
        Boolean success = bridge.register(null, "test").getValue();
        int users1 = bridge.numOfUsers().getValue();

        Assert.assertNotNull(success);
        Assert.assertFalse(success);
        //assert that no users has been added when fail
        Assert.assertEquals(users0, users1);
    }

    @Test
    //Tests whether the registration of a user with a null password fails.
    public void registerNullPasswordFail() {
        int users0 = bridge.numOfUsers().getValue();
        Boolean success = bridge.register("test2", null).getValue();
        int users1 = bridge.numOfUsers().getValue();

        Assert.assertNotNull(success);
        Assert.assertFalse(success);
        //assert that no users has been added when fail
        Assert.assertEquals(users0, users1);
    }
}
