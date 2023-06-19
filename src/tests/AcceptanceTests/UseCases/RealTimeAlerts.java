package AcceptanceTests.UseCases;
import AcceptanceTests.*;

import java.util.List;
import java.util.UUID;

import DomainLayer.Market.Notification;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RealTimeAlerts extends ProjectTest {

    UUID storeFounderId;
    UUID storeOwnerId;
    UUID storeManagerId;
    UUID user1Id;
    UUID user2Id;
    ServiceStore store;
    UUID storeId;
    UUID item1Id;
    UUID item2Id;
    UUID item3Id;
    UUID item4Id;

    @BeforeAll
    public void beforeClass() {
        bridge.register("founder", "Aa1234");
        bridge.register("owner", "Aa1234");
        bridge.register("manager", "Aa1234");
        bridge.register("user1", "Aa1234");
        bridge.register("user2", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "Aa1234").getValue().getId();
        storeManagerId = bridge.login(bridge.createClient().getValue(), "manager", "Aa1234").getValue().getId();
        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "Aa1234").getValue().getId();
        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "Aa1234").getValue().getId();

        store = bridge.createStore(storeFounderId, "store", "test").getValue();
        storeId = store.getStoreId();

        bridge.appointStoreOwner(storeFounderId, storeOwnerId, storeId);
        bridge.appointStoreManager(storeFounderId, storeManagerId, storeId);

        item1Id = bridge.addItemToStore(storeFounderId, "item1", 10, storeId, 100, "test").getValue().getId();
        item2Id = bridge.addItemToStore(storeFounderId, "item2", 20, storeId, 100, "test").getValue().getId();
        item3Id = bridge.addItemToStore(storeFounderId, "item3", 30, storeId, 100, "test").getValue().getId();
        item4Id = bridge.addItemToStore(storeFounderId, "item4", 40, storeId, 100, "test").getValue().getId();

        bridge.addItemToCart(user1Id, item1Id, 3, storeId);
        bridge.addItemToCart(user1Id, item2Id, 6, storeId);
        bridge.purchaseCart(user1Id, bridge.getCartTotal(user1Id).getValue(), "address", "Aa12340000Aa12340000",
                "12", 1,"address", "Aa12340000Aa12340000", "12","address", "Aa12340000Aa12340000", "12");

        bridge.addItemToCart(user2Id, item3Id, 9, storeId);
        bridge.addItemToCart(user2Id, item4Id, 12, storeId);
        bridge.purchaseCart(user2Id, bridge.getCartTotal(user2Id).getValue(), "address", "Aa12340000Aa12340000",
                "12", 1,"address", "Aa12340000Aa12340000", "12","address", "Aa12340000Aa12340000", "12");

        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManagerId);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "manager", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user2", "Aa1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManagerId);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
        deleteDB();

    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    public void realTimeAlertsPurchaseInStoreOwnerSuccess() {
        bridge.addItemToCart(user1Id, item1Id, 1, storeId);

        Response<Boolean> purchase = bridge.purchaseCart(user1Id, 160, "address", "Aa12340000Aa12340000",
                "12", 1,"address", "Aa12340000Aa12340000", "12","address", "Aa12340000Aa12340000", "12");
        Response<List<Notification>> notifications = bridge.getNotifications(storeOwnerId, storeOwnerId);
        Response<List<Notification>> notifications2 = bridge.getNotifications(storeOwnerId, storeOwnerId);

        assertFalse(purchase.isError(), purchase.getMessage());
        assertFalse(notifications.isError(), notifications.getMessage());
        assertFalse(notifications2.isError(), notifications.getMessage());

        assertTrue(notifications.getValue().stream().anyMatch(notification -> notification.getMessage().contains("A purchase from " + store.getName() + " has been made.")), "no notification for purchase");
        assertTrue(notifications2.getValue().isEmpty(), "notification list is not empty");
    }

    @Test
    public void realTimeAlertsCloseStoreOwnerSuccess() {
        Response<Boolean> close = bridge.closeStore(storeFounderId, storeId);
        Response<List<Notification>> notifications = bridge.getNotifications(storeOwnerId, storeOwnerId);
        Response<List<Notification>> notifications2 = bridge.getNotifications(storeOwnerId, storeOwnerId);

        assertFalse(close.isError(), close.getMessage());
        assertFalse(notifications.isError(), notifications.getMessage());
        assertFalse(notifications2.isError(), notifications.getMessage());

        assertTrue(notifications.getValue().stream().anyMatch(notification -> notification.getMessage().equals("Owned store " + store.getName() + " has been closed by founder.")), "no notification for closing the store");
        assertTrue(notifications2.getValue().isEmpty(), "notification list is not empty");
    }

    @Test
    public void realTimeAlertsRemoveRoleManagerSuccess() {
        Response<Boolean> remove = bridge.removeStoreRole(storeFounderId, storeManagerId, storeId);
        Response<List<Notification>> notifications = bridge.getNotifications(storeManagerId, storeManagerId);
        Response<List<Notification>> notifications2 = bridge.getNotifications(storeManagerId, storeManagerId);

        assertFalse(remove.isError(), remove.getMessage());
        assertFalse(notifications.isError(), notifications.getMessage());
        assertFalse(notifications2.isError(), notifications.getMessage());

        assertTrue(notifications.getValue().stream().anyMatch(notification -> notification.getMessage().equals("Your role has been removed from " + store.getName())), "no notification for removing role");
        assertTrue(notifications2.getValue().isEmpty(), "notification list is not empty");
    }
}
