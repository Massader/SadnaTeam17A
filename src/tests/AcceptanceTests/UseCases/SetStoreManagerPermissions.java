package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SetStoreManagerPermissions extends ProjectTest {

    UUID clientCredentials;
    UUID storeManager;
    UUID storeOwner;
    UUID storeId;
    List<Integer> permissions;
    UUID founder;
    UUID client;
    UUID client2;
    UUID client3;
    ServiceStore store;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("Manager1", "Pass1");
        client2 = bridge.createClient();
        storeManager = bridge.login(client2, "Manager1", "Pass1");
        Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId);
        bridge.register("toOwner", "Pass1");
        client3 = bridge.createClient();
        storeOwner = bridge.login(client3, "toOwner", "Pass1");
        Boolean AppointStoreOwner = bridge.appointStoreOwner(founder,storeOwner,storeId);
        permissions = new ArrayList<>();
    }

    @BeforeEach
    public void beforeEach()  {
        client = bridge.createClient();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(client);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
        bridge.logout(storeOwner);
        bridge.logout(storeManager);
        bridge.closeClient(client2);
        bridge.closeClient(client3);
    }

    @Test
    //This test verifies that a store owner can successfully set permissions for a store manager in their store.
    public void SetStoreManagerPermissionsSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("Manager1", "Pass1");
        client2 = bridge.createClient();
        storeManager = bridge.login(client2, "Manager1", "Pass1");
        Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId);
        bridge.register("toOwner", "Pass1");
        client3 = bridge.createClient();
        storeOwner = bridge.login(client3, "toOwner", "Pass1");
        Boolean AppointStoreOwner = bridge.appointStoreOwner(founder,storeOwner,storeId);
        permissions = new ArrayList<>();
        permissions.add(3);
        Boolean setPermission = bridge.setStoreManagerPermissions(storeOwner,storeManager,storeId,permissions);
        Assert.assertTrue(setPermission);
    }

    @Test
    //this test verifies that a store manager cannot set permissions for a store owner in their store.
    public void SetStoreManagerPermissionsFail() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("Manager1", "Pass1");
        client2 = bridge.createClient();
        storeManager = bridge.login(client2, "Manager1", "Pass1");
        Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId);
        bridge.register("toOwner", "Pass1");
        client3 = bridge.createClient();
        storeOwner = bridge.login(client3, "toOwner", "Pass1");
        Boolean AppointStoreOwner = bridge.appointStoreOwner(founder,storeOwner,storeId);
        permissions = new ArrayList<>();
        permissions.add(3);
        Boolean setPermission = bridge.setStoreManagerPermissions(storeManager,storeOwner,storeId,permissions);
        Assert.assertFalse(setPermission);
    }
}
