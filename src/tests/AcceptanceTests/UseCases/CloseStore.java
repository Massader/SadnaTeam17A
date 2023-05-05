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
public class CloseStore extends ProjectTest {

    UUID founder;
    UUID client;
    UUID client2;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = null;
        storeId = null;
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
    }

    @BeforeEach
    public void beforeEach()  {
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
    //tests whether a store can be closed successfully by its founder.
    public void CloseStoreSuccess() {
        int stores0 = bridge.numOfStores().getValue();
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = null;
        storeId = null;
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        Boolean close = bridge.closeStore(founder,storeId).getValue();
        int stores1 = bridge.numOfStores().getValue();

        Assert.assertTrue(close);
        Assert.assertEquals(stores0 - 1, stores1);
    }

    @Test
    public void CloseStoreFail() {
        //Tests whether a store can be closed unsuccessfully by a client who is not the founder of the store.
        int stores0 = bridge.numOfStores().getValue();
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = null;
        storeId = null;
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        client2 = bridge.createClient().getValue();
        Boolean close = bridge.closeStore(client2,storeId).getValue();
        int stores1 = bridge.numOfStores().getValue();

        Assert.assertFalse(close);
        Assert.assertEquals(stores0, stores1);
        //bridge.logout(client2);
    }
}
