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
public class AppointStoreOwner extends ProjectTest {

    UUID founder;
    UUID client;
    UUID storeOwner;
    UUID client2;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1").getId();
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("toOwner", "Pass2");
        client2 = bridge.createClient();
        storeOwner = bridge.login(client2, "toOwner", "Pass2").getId();
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
    }

    @Test
    //Tests if a store owner can be successfully appointed by the store's founder.
    public void AppointStoreManagerSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1").getId();
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("toOwner", "Pass2");
        client2 = bridge.createClient();
        storeOwner = bridge.login(client2, "toOwner", "Pass2").getId();
        Boolean AppointStoreOwner = bridge.appointStoreOwner(founder,storeOwner,storeId);
        Assert.assertTrue(AppointStoreOwner);
    }
    @Test
    //Tests if a store owner cannot be appointed by a user who is not at list store's manager.
    public void AppointStoreManagerFail() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1").getId();
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("toOwner", "Pass2");
        client2 = bridge.createClient();
        storeOwner = bridge.login(client2, "toOwner", "Pass2").getId();
        Boolean AppointStoreOwner = bridge.appointStoreOwner(storeOwner,founder,storeId);
        Assert.assertFalse(AppointStoreOwner);
    }
}


