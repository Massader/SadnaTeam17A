package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import DomainLayer.Market.Users.Roles.StorePermissions;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.*;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SetStoreManagerPermissions extends ProjectTest {

    UUID storeFounderId;
    UUID storeOwnerId;
    UUID storeManager1Id;
    UUID storeManager2Id;
    UUID userId;
    ServiceStore store;
    UUID storeId;
    UUID item1Id;
    UUID item2Id;
    UUID item3Id;
    UUID item4Id;

    /*
    ----------Store Permissions----------
    0   STORE_COMMUNICATION     (default)
    1   STORE_SALE_HISTORY      (default)
    2   STORE_STOCK_MANAGEMENT  (default)
    3   STORE_ITEM_MANAGEMENT
    4   STORE_POLICY_MANAGEMENT
    5   STORE_DISCOUNT_MANAGEMENT
    6   STORE_MANAGEMENT_INFORMATION
    7   STORE_OWNER
    8   STORE_FOUNDER
    -------------------------------------
    */

    @BeforeAll
    public void beforeClass() {
        bridge.register("founder", "Aa1234");
        bridge.register("owner", "Aa1234");
        bridge.register("manager1", "Aa1234");
        bridge.register("manager2", "Aa1234");
        bridge.register("user", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "Aa1234").getValue().getId();
        storeManager1Id = bridge.login(bridge.createClient().getValue(), "manager1", "Aa1234").getValue().getId();
        storeManager2Id = bridge.login(bridge.createClient().getValue(), "manager2", "Aa1234").getValue().getId();
        userId = bridge.login(bridge.createClient().getValue(), "user", "Aa1234").getValue().getId();

        store = bridge.createStore(storeFounderId, "store", "test").getValue();
        storeId = store.getStoreId();

        bridge.appointStoreOwner(storeFounderId, storeOwnerId, storeId);
        bridge.appointStoreManager(storeFounderId, storeManager1Id, storeId);
        bridge.appointStoreManager(storeFounderId, storeManager2Id, storeId);

        item1Id = bridge.addItemToStore(storeFounderId, "item1", 10, storeId, 100, "test").getValue().getId();
        item2Id = bridge.addItemToStore(storeFounderId, "item2", 20, storeId, 100, "test").getValue().getId();
        item3Id = bridge.addItemToStore(storeFounderId, "item3", 30, storeId, 100, "test").getValue().getId();
        item4Id = bridge.addItemToStore(storeFounderId, "item4", 40, storeId, 100, "test").getValue().getId();

        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManager1Id);
        bridge.logout(storeManager2Id);
        bridge.logout(userId);
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "manager1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "manager2", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user", "Aa1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManager1Id);
        bridge.logout(storeManager2Id);
        bridge.logout(userId);
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    public void setPermissionsSuccess() {
        List<Integer> permissionsByFounder = new ArrayList<>();
        permissionsByFounder.add(3);
        List<Integer> permissionsByOwner = new ArrayList<>();
        permissionsByOwner.add(4);

        Response<ServiceUser> manager1_0 = bridge.getUserInfo(storeManager1Id);
        Response<Boolean> byFounder = bridge.setStoreManagerPermissions(storeFounderId, storeManager1Id, storeId, permissionsByFounder);
        Response<ServiceUser> manager1_1 = bridge.getUserInfo(storeManager1Id);
        Response<Boolean> byOwner = bridge.setStoreManagerPermissions(storeFounderId, storeManager1Id, storeId, permissionsByOwner);
        Response<ServiceUser> manager1_2 = bridge.getUserInfo(storeManager1Id);

        assertFalse(manager1_0.isError(), String.format("bridge.getUserInfo(storeManager1Id) => %s", manager1_0.getMessage()));
        assertFalse(byFounder.isError(), String.format("bridge.setStoreManagerPermissions(storeFounderId, storeManager1Id, storeId, permissionsByFounder) => %s", byFounder.getMessage()));
        assertFalse(manager1_1.isError(), String.format("bridge.getUserInfo(storeManager1Id) => %s", manager1_1.getMessage()));
        assertFalse(byOwner.isError(), String.format("bridge.setStoreManagerPermissions(storeFounderId, storeManager1Id, storeId, permissionsByOwner) => %s", byOwner.getMessage()));
        assertFalse(manager1_2.isError(), String.format("bridge.getUserInfo(storeManager1Id) => %s", manager1_2.getMessage()));

        List<StorePermissions> permissions0 = manager1_0.getValue().getRoles().get(storeId);
        List<StorePermissions> permissions1 = manager1_1.getValue().getRoles().get(storeId);
        List<StorePermissions> permissions2 = manager1_2.getValue().getRoles().get(storeId);

        assertEquals(3, permissions0.size(), "list size is not 3");
        assertEquals(4, permissions0.size(), "list size is not 4");
        assertEquals(5, permissions0.size(), "list size is not 5");

        assertFalse(permissions0.stream().anyMatch(permission -> permission == StorePermissions.STORE_ITEM_MANAGEMENT), "list should not contain STORE_ITEM_MANAGEMENT");
        assertFalse(permissions0.stream().anyMatch(permission -> permission == StorePermissions.STORE_POLICY_MANAGEMENT), "list should not contain STORE_POLICY_MANAGEMENT");

        assertTrue(permissions1.containsAll(permissions0), "list does not contain all elements from the other list");
        assertTrue(permissions1.stream().anyMatch(permission -> permission == StorePermissions.STORE_ITEM_MANAGEMENT), "list does not contain STORE_ITEM_MANAGEMENT");

        assertTrue(permissions2.containsAll(permissions1), "list does not contain all elements from the other list");
        assertTrue(permissions2.stream().anyMatch(permission -> permission == StorePermissions.STORE_POLICY_MANAGEMENT), "list does not contain STORE_POLICY_MANAGEMENT");
    }

    @Test
    public void setPermissionsByManagerFail() {
        List<Integer> permissions = new ArrayList<>();
        permissions.add(3);

        Response<ServiceUser> manager2_0 = bridge.getUserInfo(storeManager2Id);
        Response<Boolean> byManager = bridge.setStoreManagerPermissions(storeManager1Id, storeManager2Id, storeId, permissions);
        Response<ServiceUser> manager2_1 = bridge.getUserInfo(storeManager2Id);

        assertFalse(manager2_0.isError(), String.format("bridge.getUserInfo(storeManager2Id) => %s", manager2_0.getMessage()));
        assertTrue(byManager.isError(), "bridge.setStoreManagerPermissions(storeManager1Id, storeManager2Id, storeId, permissions) should have failed");
        assertFalse(manager2_1.isError(), String.format("bridge.getUserInfo(storeManager2Id) => %s", manager2_1.getMessage()));

        List<StorePermissions> permissions0 = manager2_0.getValue().getRoles().get(storeId);
        List<StorePermissions> permissions1 = manager2_1.getValue().getRoles().get(storeId);

        assertEquals(3, permissions0.size(), "list size is not 3");
        assertEquals(3, permissions1.size(), "list size is not 3");

        assertFalse(permissions0.stream().anyMatch(permission -> permission == StorePermissions.STORE_ITEM_MANAGEMENT), "list should not contain STORE_ITEM_MANAGEMENT");

        assertTrue(permissions1.containsAll(permissions0), "list does not contain all elements in the other list");
        assertTrue(permissions0.containsAll(permissions1), "list does not contain all elements in the other list");
    }

    @Test
    public void setPermissionsByUserFail() {
        List<Integer> permissions = new ArrayList<>();
        permissions.add(3);

        Response<ServiceUser> manager2_0 = bridge.getUserInfo(storeManager2Id);
        Response<Boolean> byManager = bridge.setStoreManagerPermissions(userId, storeManager2Id, storeId, permissions);
        Response<ServiceUser> manager2_1 = bridge.getUserInfo(storeManager2Id);

        assertFalse(manager2_0.isError(), String.format("bridge.getUserInfo(storeManager2Id) => %s", manager2_0.getMessage()));
        assertTrue(byManager.isError(), "bridge.setStoreManagerPermissions(userId, storeManager2Id, storeId, permissions) should have failed");
        assertFalse(manager2_1.isError(), String.format("bridge.getUserInfo(storeManager2Id) => %s", manager2_1.getMessage()));

        List<StorePermissions> permissions0 = manager2_0.getValue().getRoles().get(storeId);
        List<StorePermissions> permissions1 = manager2_1.getValue().getRoles().get(storeId);

        assertEquals(3, permissions0.size(), "list size is not 3");
        assertEquals(3, permissions1.size(), "list size is not 3");

        assertFalse(permissions0.stream().anyMatch(permission -> permission == StorePermissions.STORE_ITEM_MANAGEMENT), "list should not contain STORE_ITEM_MANAGEMENT");

        assertTrue(permissions1.containsAll(permissions0), "list does not contain all elements in the other list");
        assertTrue(permissions0.containsAll(permissions1), "list does not contain all elements in the other list");
    }
}