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
public class OpenStore extends ProjectTest {

    UUID founder;
    UUID client;
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
    public void openStoreSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        Assert.assertNotNull(store);
    }

    @Test
    public void openStoreNotLoggedInFail() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        ServiceStore storeFail = bridge.createStore(client, "fail", "fail");
        Assert.assertNull(storeFail);
    }
}
