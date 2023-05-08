package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StoreManagerActions extends ProjectTest {

    UUID storeManager;
    UUID storeId;
    List<Integer> permissions;
    UUID founder;
    UUID client;
    UUID client2;
    ServiceStore store;

    @BeforeAll
    public void beforeClass() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        bridge.register("Manager1", "Pass1");
        client2 = bridge.createClient().getValue();
        storeManager = bridge.login(client2, "Manager1", "Pass1").getValue().getId();
        Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId).getValue();
    }

    @BeforeEach
    public void setUp()  {
        client = bridge.createClient().getValue();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(client);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
        bridge.logout(storeManager);
        bridge.closeClient(client2);
    }

    @Test
    // This test case checks whether a store manager can successfully view the store's sale history. It creates a store, appoints a manager, and check.
    public void StoreManagerActionsSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        bridge.register("Manager1", "Pass1");
        client2 = bridge.createClient().getValue();
        storeManager = bridge.login(client2, "Manager1", "Pass1").getValue().getId();
        Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId).getValue();
        List<ServiceSale> saleHistory = bridge.getStoreSaleHistory(storeManager,storeId).getValue();
        Assert.assertTrue(saleHistory.isEmpty());}

    @Test
    //this test case checks whether a store manager can add an item to the store. It creates a store, appoints a manager, and then tries to add an item to the store. The test asserts that the returned service item is null, indicating that the addition of the item failed.
    public void StoreManagerActionsFail() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        bridge.register("Manager1", "Pass1");
        client2 = bridge.createClient().getValue();
        storeManager = bridge.login(client2, "Manager1", "Pass1").getValue().getId();
        Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId).getValue();
        ServiceItem serviceItem = bridge.addItemToStore(storeManager, "bannana", 5.5, storeId, 20, "yellow fruit").getValue();
        Assert.assertNull(serviceItem);
    }
}
