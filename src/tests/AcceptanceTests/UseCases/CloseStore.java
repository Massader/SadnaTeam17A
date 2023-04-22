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
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = null;
        storeId = null;
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
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
    }

    @Test
    public void CloseStoreSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = null;
        storeId = null;
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        Boolean close = bridge.closeStore(founder,storeId);
        Assert.assertTrue(close);
    }

    @Test
    public void CloseStoreFail() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = null;
        storeId = null;
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        client2 = bridge.createClient();
        Boolean close = bridge.closeStore(client2,storeId);
        Assert.assertFalse(close);
        //bridge.logout(client2);
    }
}
