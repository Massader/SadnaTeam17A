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
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("toOwner", "Pass2");
        client2 = bridge.enterSystem();
        storeOwner = bridge.login(client2, "toOwner", "Pass2");
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
    }

    @Test
    public void AppointStoreManagerSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("toOwner", "Pass2");
        client2 = bridge.enterSystem();
        storeOwner = bridge.login(client2, "toOwner", "Pass2");
        Boolean AppointStoreOwner = bridge.appointStoreOwner(founder,storeOwner,storeId);
        Assert.assertTrue(AppointStoreOwner);
    }
    @Test
    public void AppointStoreManagerFail() {
        bridge.register("founder", "Pass1");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("toOwner", "Pass2");
        client2 = bridge.enterSystem();
        storeOwner = bridge.login(client2, "toOwner", "Pass2");
        Boolean AppointStoreOwner = bridge.appointStoreOwner(storeOwner,founder,storeId);
        Assert.assertFalse(AppointStoreOwner);
    }
}


