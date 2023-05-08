package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReceiveStoreInfo extends ProjectTest {

    UUID founder;
    UUID client;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void beforeClass() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
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
    }

    @Test
    // retrieves the information of a store and verifies that it is equal to the information of the store created
    public void receiveStoreInfoSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        ServiceStore store2 = bridge.getStoreInformation(storeId).getValue();
        Assert.assertNotNull(store2);
        Assert.assertEquals(store.getStoreId(), store2.getStoreId());
        Assert.assertEquals(store.getName(), store2.getName());
        Assert.assertEquals(store.getDescription(), store2.getDescription());
    }

    @Test
    public void receiveStoreInfoNotExistingStoreFail() {
        //tries to retrieve the information of a non-existing store and verifies that the result is null
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        ServiceStore store2 = bridge.getStoreInformation(UUID.randomUUID()).getValue();
        Assert.assertNull(store2);
    }
}
