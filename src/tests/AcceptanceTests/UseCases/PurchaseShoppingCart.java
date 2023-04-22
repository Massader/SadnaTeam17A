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
public class PurchaseShoppingCart extends ProjectTest {

    UUID founder;
    UUID client;
    ServiceStore store;
    UUID storeId;
    ServiceItem item;
    UUID itemId;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        item = bridge.addItemToStore(founder, "item", 10, storeId, 1, "test");
        itemId = item.getId();
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
    public void purchaseShoppingCartSuccess() {
        Assert.assertTrue(true);
    }
}
