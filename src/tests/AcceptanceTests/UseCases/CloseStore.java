package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.*;

import java.util.UUID;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CloseStore extends ProjectTest {

    UUID storeFounderId;
    UUID storeOwnerId;
    UUID storeManagerId;
    UUID userId;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void beforeClass() {
        bridge.register("founder", "Aa1234");
        bridge.register("owner", "Aa1234");
        bridge.register("manager", "Aa1234");
        bridge.register("user", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "Aa1234").getValue().getId();
        storeManagerId = bridge.login(bridge.createClient().getValue(), "manager", "Aa1234").getValue().getId();
        userId = bridge.login(bridge.createClient().getValue(), "user", "Aa1234").getValue().getId();

        store = bridge.createStore(storeFounderId, "store", "test").getValue();
        storeId = store.getStoreId();

        bridge.appointStoreOwner(storeFounderId, storeOwnerId, storeId);
        bridge.appointStoreManager(storeFounderId, storeManagerId, storeId);

        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManagerId);
        bridge.logout(userId);
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "manager", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user", "Aa1234");
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
        bridge.resetService();
    }


    @Test
    //tests whether a store can be closed successfully by its founder.
    public void CloseStoreByFounderSuccess() {
        Response<Integer> stores0 = bridge.numOfOpenStores();
        Response<Boolean> close = bridge.closeStore(storeFounderId, storeId);
        Response<Integer> stores1 = bridge.numOfOpenStores();
        Response<ServiceStore> storeInfoByFounder = bridge.getStoreInformation(storeFounderId, storeId);
        Response<ServiceStore> storeInfoByUser = bridge.getStoreInformation(userId, storeId);

        assertFalse(stores0.isError(), String.format("bridge.numOfOpenStores() => %s", stores0.getMessage()));
        assertFalse(close.isError(), String.format("bridge.closeStore(storeFounderId, storeId) => %s", close.getMessage()));
        assertFalse(stores1.isError(), String.format("bridge.numOfOpenStores() => %s", stores1.getMessage()));
        assertFalse(storeInfoByFounder.isError(), String.format("bridge.getStoreInformation(storeFounderId, storeId) => %s", storeInfoByFounder.getMessage()));
        assertTrue(storeInfoByUser.isError(), "bridge.getStoreInformation(userId, storeId) should have failed");

        assertTrue(close.getValue(), "bridge.closeStore(storeFounderId, storeId) failed");
        assertEquals(1, stores0.getValue() - stores1.getValue(), "number of open stores did not decreased by 1");
        assertTrue(storeInfoByFounder.getValue().getIsClosed());
        assertEquals("Store is closed.", storeInfoByUser.getMessage(), "bridge.getStoreInformation(storeFounderId, storeId) should have failed");
    }

    @Test
    //tests whether a store can be closed by its owner.
    public void CloseStoreByOwnerFail() {
        Response<Integer> stores0 = bridge.numOfOpenStores();
        Response<Boolean> close = bridge.closeStore(storeOwnerId, storeId);
        Response<Integer> stores1 = bridge.numOfOpenStores();
        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeOwnerId, storeId);
    
        assertFalse(stores0.isError(), String.format("bridge.numOfOpenStores() => %s", stores0.getMessage()));
        assertTrue(close.isError(), "bridge.closeStore(storeOwnerId, storeId) should have failed");
        assertFalse(stores1.isError(), String.format("bridge.numOfOpenStores() => %s", stores1.getMessage()));
        assertFalse(storeInfo.isError(), String.format("bridge.getStoreInformation(storeOwnerId, storeId) => %s", storeInfo.getMessage()));
    
        assertEquals(stores0.getValue(), stores1.getValue(), "number of open stores has changed");
        assertFalse(storeInfo.getValue().getIsClosed(), "the store is closed");
    }

    @Test
    //tests whether a store can be closed successfully by a manager with permission.
    public void CloseStoreByManagerFail() {
        Response<Integer> stores0 = bridge.numOfOpenStores();
        Response<Boolean> close = bridge.closeStore(storeManagerId, storeId);
        Response<Integer> stores1 = bridge.numOfOpenStores();
        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeManagerId, storeId);

        assertFalse(stores0.isError(), String.format("bridge.numOfOpenStores() => %s", stores0.getMessage()));
        assertTrue(close.isError(), "bridge.closeStore(storeManagerId, storeId) should have failed");
        assertFalse(stores1.isError(), String.format("bridge.numOfOpenStores() => %s", stores1.getMessage()));
        assertFalse(storeInfo.isError(), String.format("bridge.getStoreInformation(storeManagerId, storeId) => %s", storeInfo.getMessage()));

        assertEquals(stores0.getValue(), stores1.getValue(), "number of open stores has changed");
        assertFalse(storeInfo.getValue().getIsClosed(), "the store is closed");
    }

    @Test
    //Tests whether a store can be closed unsuccessfully by a client who is not the founder of the store.
    public void CloseStoreByUserFail() {
        Response<Integer> stores0 = bridge.numOfOpenStores();
        Response<Boolean> close = bridge.closeStore(userId, storeId);
        Response<Integer> stores1 = bridge.numOfOpenStores();
        Response<ServiceStore> storeInfo = bridge.getStoreInformation(userId, storeId);

        assertFalse(stores0.isError(), String.format("bridge.numOfOpenStores() => %s", stores0.getMessage()));
        assertTrue(close.isError(), "bridge.closeStore(userId, storeId) should have failed");
        assertFalse(stores1.isError(), String.format("bridge.numOfOpenStores() => %s", stores1.getMessage()));
        assertFalse(storeInfo.isError(), String.format("bridge.getStoreInformation(userId, storeId) => %s", storeInfo.getMessage()));

        assertEquals(stores0.getValue(), stores1.getValue(), "number of open stores has changed");
        assertFalse(storeInfo.getValue().getIsClosed(), "the store is closed");
    }

    @Test
    //Tests whether a store can be closed unsuccessfully by a logged-out founder of the store.
    public void CloseStoreByLoggedOutFounderFail() {
        Response<Integer> stores0 = bridge.numOfOpenStores();
        Response<UUID> logout = bridge.logout(storeFounderId);
        Response<Boolean> close = bridge.closeStore(storeFounderId, storeId);
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        Response<Integer> stores1 = bridge.numOfOpenStores();
        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeFounderId, storeId);

        assertFalse(stores0.isError(), String.format("bridge.numOfOpenStores() => %s", stores0.getMessage()));
        assertFalse(logout.isError(), String.format("bridge.logout(storeFounderId) => %s", logout.getMessage()));
        assertTrue(close.isError(), "bridge.closeStore(storeFounderId, storeId) should have failed");
        assertFalse(login.isError(), String.format("bridge.login(bridge.createClient().getValue(), \"founder\", \"Aa1234\") => %s", login.getMessage()));
        assertFalse(stores1.isError(), String.format("bridge.numOfOpenStores() => %s", stores1.getMessage()));
        assertFalse(storeInfo.isError(), String.format("bridge.getStoreInformation(storeFounderId, storeId) => %s", storeInfo.getMessage()));

        assertEquals(stores0.getValue(), stores1.getValue(), "number of open stores has changed");
        assertFalse(storeInfo.getValue().getIsClosed(), "the store is closed");
    }
}