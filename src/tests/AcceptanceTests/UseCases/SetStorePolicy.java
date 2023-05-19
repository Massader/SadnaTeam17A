package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SetStorePolicy extends ProjectTest {
/*
    UUID storeFounderId;
    UUID storeOwnerId;
    UUID storeManagerWithPermissionId;
    UUID storeManagerNoPermissionId;
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
        bridge.register("managerWithPermission", "Aa1234");
        bridge.register("managerNoPermission", "Aa1234");
        bridge.register("user1", "Aa1234");
        bridge.register("user2", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "Aa1234").getValue().getId();
        storeManagerWithPermissionId = bridge.login(bridge.createClient().getValue(), "managerWithPermission", "Aa1234").getValue().getId();
        storeManagerNoPermissionId = bridge.login(bridge.createClient().getValue(), "managerNoPermission", "Aa1234").getValue().getId();
        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "Aa1234").getValue().getId();
        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "Aa1234").getValue().getId();

        store = bridge.createStore(storeFounderId, "store", "test").getValue();
        storeId = store.getStoreId();

        bridge.appointStoreOwner(storeFounderId, storeOwnerId, storeId);
        bridge.appointStoreManager(storeFounderId, storeManagerWithPermissionId, storeId);
        bridge.appointStoreManager(storeFounderId, storeManagerNoPermissionId, storeId);

        List<Integer> permissions = new ArrayList<Integer>();
        permissions.add(6); // STORE_MANAGEMENT_INFORMATION = 1
        bridge.setStoreManagerPermissions(storeFounderId, storeManagerWithPermissionId, storeId, permissions);

        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManagerWithPermissionId);
        bridge.logout(storeManagerNoPermissionId);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "managerWithPermission", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "managerNoPermission", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user2", "Aa1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManagerWithPermissionId);
        bridge.logout(storeManagerNoPermissionId);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    public void setStorePolicySuccess() {

    }
    */
}
