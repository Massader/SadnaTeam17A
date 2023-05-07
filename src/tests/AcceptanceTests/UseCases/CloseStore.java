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
public class CloseStore extends ProjectTest {

    UUID storeFounderId;
    UUID storeOwnerId;
    UUID storeManagerId;
    UUID userId;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void setUp() {
        bridge.register("founder", "1234");
        bridge.register("owner", "1234");
        bridge.register("manager", "1234");
        bridge.register("user", "1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "1234").getValue().getId();
        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "1234").getValue().getId();
        storeManagerId = bridge.login(bridge.createClient().getValue(), "manager", "1234").getValue().getId();
        userId = bridge.login(bridge.createClient().getValue(), "user", "1234").getValue().getId();

        store = bridge.createStore(storeFounderId, "test", "test").getValue();
        storeId = store.getStoreId();

        bridge.appointStoreOwner(storeFounderId, storeOwnerId, storeId);
        bridge.appointStoreManager(storeFounderId, storeManagerId, storeId);

        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManagerId);
        bridge.logout(userId);
    }

    @BeforeEach
    public void beforeEach()  {
        bridge.login(bridge.createClient().getValue(), "founder", "1234");
        bridge.login(bridge.createClient().getValue(), "owner", "1234");
        bridge.login(bridge.createClient().getValue(), "manager", "1234");
        bridge.login(bridge.createClient().getValue(), "user", "1234");
        bridge.reopenStore(storeFounderId, storeId);
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManagerId);
        bridge.logout(userId);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(storeFounderId, storeId);
    }

    @Test
    //tests whether a store can be closed successfully by its founder.
    public void CloseStoreByFounderSuccess() {
        Response<Integer> stores0 = bridge.numOfOpenStores();
        Response<Boolean> close = bridge.closeStore(storeFounderId, storeId);
        Response<Integer> stores1 = bridge.numOfOpenStores();
        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeId);

        Assert.assertFalse(stores0.isError());
        Assert.assertFalse(close.isError());
        Assert.assertFalse(stores1.isError());
        Assert.assertTrue(storeInfo.isError());

        Assert.assertTrue(close.getValue());
        Assert.assertEquals(1, stores0.getValue() - stores1.getValue());
        Assert.assertEquals("Store is closed.", storeInfo.getMessage());
    }

    @Test
    //tests whether a store can be closed successfully by its owner.
    public void CloseStoreByOwnerSuccess() {
        Response<Integer> stores0 = bridge.numOfOpenStores();
        Response<Boolean> close = bridge.closeStore(storeOwnerId, storeId);
        Response<Integer> stores1 = bridge.numOfOpenStores();
        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeId);

        Assert.assertFalse(stores0.isError());
        Assert.assertFalse(close.isError());
        Assert.assertFalse(stores1.isError());
        Assert.assertTrue(storeInfo.isError());

        Assert.assertTrue(close.getValue());
        Assert.assertEquals(1, stores0.getValue() - stores1.getValue());
        Assert.assertEquals("Store is closed.", storeInfo.getMessage());
    }

    @Test
    //tests whether a store can be closed successfully by a manager with permission.
    public void CloseStoreByManagerFail() {
        Response<Integer> stores0 = bridge.numOfOpenStores();
        Response<Boolean> close = bridge.closeStore(storeManagerId, storeId);
        Response<Integer> stores1 = bridge.numOfOpenStores();
        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeId);

        Assert.assertFalse(stores0.isError());
        Assert.assertTrue(close.isError());
        Assert.assertFalse(stores1.isError());
        Assert.assertFalse(storeInfo.isError());

        Assert.assertEquals(stores0.getValue(), stores1.getValue());
        Assert.assertFalse(storeInfo.getValue().isClosed());
    }

    @Test
    //Tests whether a store can be closed unsuccessfully by a client who is not the founder of the store.
    public void CloseStoreByUserFail() {
        Response<Integer> stores0 = bridge.numOfOpenStores();
        Response<Boolean> close = bridge.closeStore(userId, storeId);
        Response<Integer> stores1 = bridge.numOfOpenStores();
        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeId);

        Assert.assertFalse(stores0.isError());
        Assert.assertTrue(close.isError());
        Assert.assertFalse(stores1.isError());
        Assert.assertFalse(storeInfo.isError());

        Assert.assertEquals(stores0.getValue(), stores1.getValue());
        Assert.assertFalse(storeInfo.getValue().isClosed());
    }

    @Test
    //Tests whether a store can be closed unsuccessfully by a logged-out founder of the store.
    public void CloseStoreByLoggedOutFounderFail() {
        Response<Integer> stores0 = bridge.numOfOpenStores();
        Response<UUID> logout = bridge.logout(storeFounderId);
        Response<Boolean> close = bridge.closeStore(storeFounderId, storeId);
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "founder", "1234");
        Response<Integer> stores1 = bridge.numOfOpenStores();
        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeId);

        Assert.assertFalse(stores0.isError());
        Assert.assertFalse(logout.isError());
        Assert.assertTrue(close.isError());
        Assert.assertFalse(login.isError());
        Assert.assertFalse(stores1.isError());
        Assert.assertFalse(storeInfo.isError());

        Assert.assertEquals(stores0.getValue(), stores1.getValue());
        Assert.assertFalse(storeInfo.getValue().isClosed());
    }
}
