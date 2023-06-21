package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import DataAccessLayer.UserRepository;
import DomainLayer.Market.UserController;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.ServiceUser;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

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
//        UserRepository userRepository = UserController.repositoryFactory.userRepository;
        deleteDB();
        bridge.resetService();
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    //Tests whether the registration of a new user with valid credentials is successful.
    public void registerSuccess() {
        Response<Integer> users0 = bridge.numOfUsers();
        Response<ConcurrentHashMap<String, UUID>> userNames0Response = bridge.getUserNames();
        ConcurrentHashMap<String, UUID> userNames0 = new ConcurrentHashMap<>(userNames0Response.getValue());
        Response<Boolean> register = bridge.register("user1", "Aa1234");
        Response<Integer> users1 = bridge.numOfUsers();
        Response<ConcurrentHashMap<String, UUID>> userNames1 = bridge.getUserNames();

        assertFalse(users0.isError(), String.format("bridge.numOfUsers() => %s", users0.getMessage()));
        assertFalse(userNames0Response.isError(), String.format("bridge.getUserNames() => %s", userNames0Response.getMessage()));
        assertFalse(register.isError(), String.format("bridge.register(\"user1\", \"Aa1234\") => %s", register.getMessage()));
        assertFalse(users1.isError(), String.format("bridge.numOfUsers() => %s", users1.getMessage()));
        assertFalse(userNames1.isError(), String.format("bridge.getUserNames() => %s", userNames1.getMessage()));

        assertTrue(register.getValue(), "bridge.register(\"user1\", \"Aa1234\") failed");     //register success
        assertEquals(1, users1.getValue() - users0.getValue(), "number of registered users did not increased by 1");    //one more user is registered
        assertNotNull(userNames0, "bridge.getUserNames() failed");
        assertFalse(userNames0.containsKey("user1"), "user names list contain \"user1\" before registration");     //no username "user1" before registration
        assertNotNull(userNames1.getValue(), "bridge.getUserNames() failed");
        assertTrue(userNames1.getValue().containsKey("user1"), "user names list does not contain \"user1\" after registration");      //there is "user1" username after registration
    }

    @Test
    //Tests whether the registration of a user with already existing username fails.
    public void registerExistingUserFail() {
        bridge.register("user2", "Aa1234");

        Response<Integer> users0 = bridge.numOfUsers();
        Response<ConcurrentHashMap<String, UUID>> userNames0 = bridge.getUserNames();
        Response<Boolean> register = bridge.register("user2", "4321");
        Response<Integer> users1 = bridge.numOfUsers();
        Response<ConcurrentHashMap<String, UUID>> userNames1 = bridge.getUserNames();

        assertFalse(users0.isError(), String.format("bridge.numOfUsers() => %s", users0.getMessage()));
        assertFalse(userNames0.isError(), String.format("bridge.getUserNames() => %s", userNames0.getMessage()));
        assertTrue(register.isError(), "bridge.register(\"user2\", \"4321\") should have failed");
        assertFalse(users1.isError(), String.format("bridge.numOfUsers() => %s", users1.getMessage()));
        assertFalse(userNames1.isError(), String.format("bridge.getUserNames() => %s", userNames1.getMessage()));

        assertEquals("This username is already in use.", register.getMessage(), register.getMessage());     //register failed
        assertEquals(users1.getValue(), users0.getValue(), "number of users has changed");    //users amount remain the same
        assertNotNull(userNames0.getValue(), "bridge.getUserNames() failed");
        assertTrue(userNames0.getValue().containsKey("user2"), "user names list contain \"user2\" before registration");     //there is "user2" username before registration
        assertNotNull(userNames1.getValue(), "bridge.getUserNames() failed");
        assertTrue(userNames1.getValue().containsKey("user2"), "user names list does not contain \"user1\" after registration");      //there is "user2" username after registration
    }

    @Test
    //Tests whether the registration of a user with a null username fails.
    public void registerNullUsernameFail() {
        Response<Integer> users0 = bridge.numOfUsers();
        Response<Boolean> register = bridge.register(null, "4321");
        Response<Integer> users1 = bridge.numOfUsers();

        assertFalse(users0.isError(), String.format("bridge.numOfUsers() => %s", users0.getMessage()));
        assertTrue(register.isError(), "bridge.register(null, \"4321\") should have failed");
        assertFalse(users1.isError(), String.format("bridge.numOfUsers() => %s", users1.getMessage()));

        assertEquals("No username input.", register.getMessage(), register.getMessage());     //register failed
        assertEquals(users1.getValue(), users0.getValue(), "number of users has changed");    //users amount remain the same
    }

    @Test
    //Tests whether the registration of a user with a null password fails.
    public void registerNullPasswordFail() {
        Response<Integer> users0 = bridge.numOfUsers();
        Response<Boolean> register = bridge.register("user3", null);
        Response<Integer> users1 = bridge.numOfUsers();

        assertFalse(users0.isError(), String.format("bridge.numOfUsers() => %s", users0.getMessage()));
        assertTrue(register.isError(), "bridge.register(\"user3\", null) should have failed");
        assertFalse(users1.isError(), String.format("bridge.numOfUsers() => %s", users1.getMessage()));

        assertEquals("No password input.", register.getMessage(), register.getMessage());    //register failed
        assertEquals(users1.getValue(), users0.getValue(), "number of users has changed");    //users amount remain the same
    }

    @Test
    public void registerConcurrently() {
        Response<Integer> users0 = bridge.numOfUsers();
        Response<ConcurrentHashMap<String, UUID>> userNames0Response = bridge.getUserNames();
        ConcurrentHashMap<String, UUID> userNames0 = new ConcurrentHashMap<>(userNames0Response.getValue());
        Response<Boolean>[] registrations = new Response[200];
        Thread[] threads = new Thread[200];
        try {
            for (int i = 0; i < 200; i++) {
                final int index = i;
                threads[i] = new Thread(() -> registrations[index] = bridge.register("user_" + index, "Aa1234"));
                threads[i].start();
            }
            for (Thread t : threads) {
                t.join();
            }
        }
        catch (Exception ignore) {}

        Response<Integer> users1 = bridge.numOfUsers();
        Response<ConcurrentHashMap<String, UUID>> userNames1 = bridge.getUserNames();

        assertFalse(users0.isError(), String.format("bridge.numOfUsers() => %s", users0.getMessage()));
        assertFalse(userNames0Response.isError(), String.format("bridge.getUserNames() => %s", userNames0Response.getMessage()));
        assertFalse(users1.isError(), String.format("bridge.numOfUsers() => %s", users1.getMessage()));
        assertFalse(userNames1.isError(), String.format("bridge.getUserNames() => %s", userNames1.getMessage()));
        for (Response<Boolean> r : registrations) {
            assertFalse(r.isError(), String.format("bridge.register(\"user_\" + index, \"Aa1234\") => %s", r.getMessage()));
            assertTrue(r.getValue(), "bridge.register(\"user_\" + index, \"Aa1234\") failed");
        }
        for (int i = 0; i < 200; i++) {
            assertFalse(userNames0.containsKey("user_" + i), "user names list contain \"user_" + i + "\" before registration");
            assertTrue(userNames1.getValue().containsKey("user_" + i), "user names list does not contain \"user_" + i + "\" after registration");
        }
        assertEquals(200, users1.getValue() - users0.getValue(), "number of users did not increased by 1000");
    }
}