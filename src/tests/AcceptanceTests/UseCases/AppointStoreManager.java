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
public class AppointStoreManager extends ProjectTest {

    UUID founder;
    UUID client;
    UUID storeManager;
    UUID client2;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("toManager", "pass");
        client2 = bridge.enterSystem();
        storeManager = bridge.login(client2, "toManager", "pass");
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
        bridge.logout(storeManager);
    }

    @Test
    public void AppointStoreManagerSuccess() {
    Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId);
    Assert.assertTrue(AppointStoreManager);
    }
    @Test
    public void AppointStoreManagerFail() {
        Boolean AppointStoreManager = bridge.appointStoreManager(storeManager,founder,storeId);
        Assert.assertFalse(AppointStoreManager);
    }
}

