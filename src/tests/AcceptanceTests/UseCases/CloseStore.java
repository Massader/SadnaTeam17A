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
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = null;
        storeId = null;
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        Boolean close = bridge.closeStore(founder,storeId).getValue();
        Assert.assertTrue(close);
    }

    @Test
    public void CloseStoreFail() {
        //Tests whether a store can be closed unsuccessfully by a client who is not the founder of the store.
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = null;
        storeId = null;
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        client2 = bridge.createClient().getValue();
        Boolean close = bridge.closeStore(client2,storeId).getValue();
        Assert.assertFalse(close);
        //bridge.logout(client2);
    }
}
