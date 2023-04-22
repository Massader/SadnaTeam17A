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

    @Before
    public void setUp() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        client = bridge.createClient();
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        item = bridge.addItemToStore(founder, "item", 10, storeId, 1, "test");
        itemId = item.getId();
    }

    @After
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
    }

    @Test
    public void purchaseShoppingCartSuccess() {
        bridge.addItemToCart(client, itemId, 1, storeId);
        Assert.assertTrue( bridge.validateOrder(client));
        Assert.assertTrue(bridge.validatePayment(client));
        Assert.assertNotNull(bridge.confirmOrder(client));
    }

    @Test
    public void purchaseShoppingCartWhileItemRemoved() {

    }
}
