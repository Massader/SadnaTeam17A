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
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("Manager1", "pass");
        client2 = bridge.enterSystem();
        storeManager = bridge.login(client2, "Manager1", "pass");
        Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId);
        bridge.register("toOwner", "pass");
        client3 = bridge.enterSystem();
        storeOwner = bridge.login(client3, "toOwner", "pass");
        Boolean AppointStoreOwner = bridge.appointStoreOwner(founder,storeOwner,storeId);
        permissions = new ArrayList<>();
    }

    @BeforeEach
    public void beforeEach()  {
        client = bridge.enterSystem();
    }

    @AfterEach
    public void tearDown() {
        bridge.exitSystem(client);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
        bridge.logout(storeOwner);
        bridge.logout(storeManager);
        bridge.exitSystem(client2);
        bridge.exitSystem(client3);
    }

    @Test
    public void SetStoreManagerPermissionsSuccess() {
        permissions.add(3);
        Boolean setPermission = bridge.setStoreManagerPermissions(storeOwner,storeManager,storeId,permissions);
        Assert.assertTrue(setPermission);
    }

    @Test
    public void SetStoreManagerPermissionsFail() {
        permissions.add(3);
        Boolean setPermission = bridge.setStoreManagerPermissions(storeManager,storeOwner,storeId,permissions);
        Assert.assertFalse(setPermission);
    }
}
