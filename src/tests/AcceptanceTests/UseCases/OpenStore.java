package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.*;

import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OpenStore extends ProjectTest {

    UUID storeFounderId;
    UUID storeId;
    @BeforeAll
    public void beforeClass() {
        bridge.resetService();
        bridge.register("founder", "1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(),"founder", "1234").getValue().getId();
        bridge.logout(storeFounderId);

        storeId = null;
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(),"founder", "1234").getValue().getId();
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    //tests if the createStore function works correctly by registering a founder, logging in, creating a store with valid credentials, and asserting that the returned store object is not null
    public void openStoreSuccess() {
        Response<Integer> stores0 = bridge.numOfStores();
        Response<ServiceStore> open = bridge.createStore(storeFounderId, "store", "desc");
        Response<Integer> stores1 = bridge.numOfStores();

        Assert.assertFalse(stores0.isError());
        Assert.assertFalse(open.isError());
        Assert.assertNotNull(open.getValue());
        Assert.assertFalse(stores1.isError());

        storeId = open.getValue().getStoreId();

        Assert.assertEquals(1, stores1.getValue() - stores0.getValue());
    }

    @Test
    //tests if the createStore function handles the scenario where the founder is not logged in by attempting to create a store with invalid credentials and asserting that the returned store object is null.
    public void openStoreLoggedOutUserFail() {
        Response<Integer> stores0 = bridge.numOfStores();
        Response<UUID> logout = bridge.logout(storeFounderId);
        Response<Boolean> open = bridge.closeStore(storeFounderId, storeId);
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "founder", "1234");
        Response<Integer> stores1 = bridge.numOfStores();

        Assert.assertFalse(stores0.isError());
        Assert.assertFalse(logout.isError());
        Assert.assertTrue(open.isError());
        Assert.assertFalse(login.isError());
        Assert.assertFalse(stores1.isError());

        Assert.assertEquals(stores0.getValue(), stores1.getValue());
    }

    @Test
    //tests if the createStore function handles the scenario where the founder is not logged in by attempting to create a store with invalid credentials and asserting that the returned store object is null.
    public void openStoreNotRegisteredUserFail() {
        Response<Integer> stores0 = bridge.numOfStores();
        Response<Boolean> open = bridge.closeStore(bridge.createClient().getValue(), storeId);
        Response<Integer> stores1 = bridge.numOfStores();

        Assert.assertFalse(stores0.isError());
        Assert.assertTrue(open.isError());
        Assert.assertFalse(stores1.isError());

        Assert.assertEquals(stores0.getValue(), stores1.getValue());
    }
}