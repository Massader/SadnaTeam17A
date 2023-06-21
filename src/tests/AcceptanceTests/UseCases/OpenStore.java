package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.*;

import java.util.UUID;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OpenStore extends ProjectTest {

    UUID storeFounderId;
    @BeforeAll
    public void beforeClass() {

    }

    @BeforeEach
    public void setUp()  {
        bridge.resetService();
        bridge.register("founder", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(),"founder", "Aa1234").getValue().getId();
        bridge.logout(storeFounderId);
        bridge.login(bridge.createClient().getValue(),"founder", "Aa1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        deleteDB();
        bridge.resetService();
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    //tests if the createStore function works correctly by registering a founder, logging in, creating a store with valid credentials, and asserting that the returned store object is not null
    public void openStoreSuccess() {
        Response<Long> stores0 = bridge.numOfStores();
        Response<ServiceStore> open = bridge.createStore(storeFounderId, "store", "desc");
        Response<Long> stores1 = bridge.numOfStores();

        assertFalse(stores0.isError(), String.format("bridge.numOfStores() => %s", stores0.getMessage()));
        assertFalse(open.isError(), String.format("bridge.createStore(storeFounderId, \"store\", \"desc\") => %s", open.getMessage()));
        assertNotNull(open.getValue(), "bridge.createStore(storeFounderId, \"store\", \"desc\") failed");
        assertFalse(stores1.isError(), String.format("bridge.numOfStores() => %s", stores1.getMessage()));

        assertEquals(1, stores1.getValue() - stores0.getValue());
    }

    @Test
    //tests if the createStore function handles the scenario where the founder is not logged in by attempting to create a store with invalid credentials and asserting that the returned store object is null.
    public void openStoreLoggedOutUserFail() {
        Response<Long> stores0 = bridge.numOfStores();
        Response<UUID> logout = bridge.logout(storeFounderId);
        Response<ServiceStore> open = bridge.createStore(storeFounderId, "store", "desc");
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        Response<Long> stores1 = bridge.numOfStores();

        assertFalse(stores0.isError(), String.format("bridge.numOfStores() => %s", stores0.getMessage()));
        assertFalse(logout.isError(), String.format("bridge.logout(storeFounderId) => %s", logout.getMessage()));
        assertTrue(open.isError(), "bridge.createStore(storeFounderId, \"store\", \"desc\") should have failed");
        assertFalse(login.isError(), String.format("bridge.login(bridge.createClient().getValue(), \"founder\", \"Aa1234\") => %s", login.getMessage()));
        assertFalse(stores1.isError(), String.format("bridge.numOfStores() => %s", stores1.getMessage()));

        assertEquals(stores0.getValue(), stores1.getValue(), "number of stores has changed");
    }

    @Test
    //tests if the createStore function handles the scenario where the founder is not logged in by attempting to create a store with invalid credentials and asserting that the returned store object is null.
    public void openStoreNotRegisteredUserFail() {
        Response<Long> stores0 = bridge.numOfStores();
        Response<ServiceStore> open = bridge.createStore(bridge.createClient().getValue(), "store", "desc");
        Response<Long> stores1 = bridge.numOfStores();

        assertFalse(stores0.isError(), String.format("bridge.numOfStores() => %s", stores0.getMessage()));
        assertTrue(open.isError(), "bridge.createStore(bridge.createClient().getValue(), \"store\", \"desc\") should have failed");
        assertFalse(stores1.isError(), String.format("bridge.numOfStores() => %s", stores1.getMessage()));

        assertEquals(stores0.getValue(), stores1.getValue(), "number of stores has changed");
    }
}