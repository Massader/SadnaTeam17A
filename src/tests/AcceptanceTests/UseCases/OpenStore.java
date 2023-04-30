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
        founder = bridge.login(client, "founder", "Pass1").getId();
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
    //tests if the createStore function works correctly by registering a founder, logging in, creating a store with valid credentials, and asserting that the returned store object is not null
    public void openStoreSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1").getId();
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        Assert.assertNotNull(store);
    }

    @Test
    //tests if the createStore function handles the scenario where the founder is not logged in by attempting to create a store with invalid credentials and asserting that the returned store object is null.
    public void openStoreNotLoggedInFail() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        ServiceStore storeFail = bridge.createStore(client, "fail", "fail");
        Assert.assertNull(storeFail);
    }
}
