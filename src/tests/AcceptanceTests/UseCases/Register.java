package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.ServiceUser;
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
        bridge.resetService();
    }

    @Test
    //Tests whether the registration of a new user with valid credentials is successful.
    public void registerSuccess() {
        Response<Integer> users0 = bridge.numOfUsers();
        Response<ConcurrentHashMap<String, UUID>> userNames0 = bridge.getUserNames();
        Response<Boolean> register = bridge.register("user1", "1234");
        Response<Integer> users1 = bridge.numOfUsers();
        Response<ConcurrentHashMap<String, UUID>> userNames1 = bridge.getUserNames();

        Assert.assertFalse(users0.isError());
        Assert.assertFalse(userNames0.isError());
        Assert.assertFalse(register.isError());
        Assert.assertFalse(users1.isError());
        Assert.assertFalse(userNames1.isError());

        Assert.assertTrue(register.getValue());     //register success
        Assert.assertEquals(1, users1.getValue() - users0.getValue());    //one more user is registered
        Assert.assertNotNull(userNames0.getValue());
        Assert.assertFalse(userNames1.getValue().containsKey("user1"));     //no username "user1" before registration
        Assert.assertNotNull(userNames1.getValue());
        Assert.assertTrue(userNames1.getValue().containsKey("user1"));      //there is "user1" username after registration
    }

    @Test
    //Tests whether the registration of a user with already existing username fails.
    public void registerExistingUserFail() {
        bridge.register("user2", "1234");

        Response<Integer> users0 = bridge.numOfUsers();
        Response<ConcurrentHashMap<String, UUID>> userNames0 = bridge.getUserNames();
        Response<Boolean> register = bridge.register("user2", "4321");
        Response<Integer> users1 = bridge.numOfUsers();
        Response<ConcurrentHashMap<String, UUID>> userNames1 = bridge.getUserNames();

        Assert.assertFalse(users0.isError());
        Assert.assertFalse(userNames0.isError());
        Assert.assertTrue(register.isError());
        Assert.assertFalse(users1.isError());
        Assert.assertFalse(userNames1.isError());

        Assert.assertEquals("This username is already in use.", register.getMessage());     //register failed
        Assert.assertEquals(users1.getValue(), users0.getValue());    //users amount remain the same
        Assert.assertNotNull(userNames0.getValue());
        Assert.assertTrue(userNames1.getValue().containsKey("user2"));     //there is "user2" username before registration
        Assert.assertNotNull(userNames1.getValue());
        Assert.assertTrue(userNames1.getValue().containsKey("user2"));      //there is "user2" username after registration
    }

    @Test
    //Tests whether the registration of a user with a null username fails.
    public void registerNullUsernameFail() {
        Response<Integer> users0 = bridge.numOfUsers();
        Response<Boolean> register = bridge.register(null, "4321");
        Response<Integer> users1 = bridge.numOfUsers();

        Assert.assertFalse(users0.isError());
        Assert.assertTrue(register.isError());
        Assert.assertFalse(users1.isError());

        Assert.assertEquals("No username input.", register.getMessage());     //register failed
        Assert.assertEquals(users1.getValue(), users0.getValue());    //users amount remain the same
    }

    @Test
    //Tests whether the registration of a user with a null password fails.
    public void registerNullPasswordFail() {
        Response<Integer> users0 = bridge.numOfUsers();
        Response<Boolean> register = bridge.register("user3", null);
        Response<Integer> users1 = bridge.numOfUsers();

        Assert.assertFalse(users0.isError());
        Assert.assertTrue(register.isError());
        Assert.assertFalse(users1.isError());

        Assert.assertEquals("No password input.", register.getMessage());     //register failed
        Assert.assertEquals(users1.getValue(), users0.getValue());    //users amount remain the same
    }

    @Test
    public void registerConcurrently() {
        Response<Integer> users0 = bridge.numOfUsers();
        Response<ConcurrentHashMap<String, UUID>> userNames0 = bridge.getUserNames();

        Response<Boolean>[] registrations = new Response[1000];
        Thread[] threads = new Thread[1000];
        try {
            for (int i = 0; i < 1000; i++) {
                final int index = i;
                threads[i] = new Thread(() -> registrations[index] = bridge.register("user_" + index, "1234"));
                threads[i].start();
            }
            for (Thread t : threads) {
                t.join();
            }
        }
        catch (Exception ignore) {}

        Response<Integer> users1 = bridge.numOfUsers();
        Response<ConcurrentHashMap<String, UUID>> userNames1 = bridge.getUserNames();

        Assert.assertFalse(users0.isError());
        Assert.assertFalse(userNames0.isError());
        Assert.assertFalse(users1.isError());
        Assert.assertFalse(userNames0.isError());
        for (Response<Boolean> r : registrations) {
            Assert.assertFalse(r.isError());
            Assert.assertTrue(r.getValue());
        }
        for (int i = 0; i < 1000; i++) {
            Assert.assertFalse(userNames0.getValue().containsKey("user_" + i));
            Assert.assertTrue(userNames1.getValue().containsKey("user_" + i));
        }
        Assert.assertEquals(1000, users1.getValue() - users0.getValue());
    }
}
