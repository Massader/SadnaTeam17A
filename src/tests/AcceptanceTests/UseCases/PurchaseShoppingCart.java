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
    public void beforeClass() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        client = bridge.createClient().getValue();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        item = bridge.addItemToStore(founder, "item", 10, storeId, 1, "test").getValue();
        itemId = item.getId();
    }

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {

    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
    }

    @Test
    //The test checks if a user can successfully purchase an item added to their shopping cart by validating the order, validating the payment, and confirming the order
    public void purchaseShoppingCartSuccess() {
        bridge.addItemToCart(client, itemId, 1, storeId);
        Assert.assertTrue( bridge.validateOrder(client).getValue());
        Assert.assertTrue(bridge.validatePayment(client).getValue());
        Assert.assertNotNull(bridge.confirmOrder(client).getValue());
    }

    @Test
    public void purchaseShoppingCartWhileItemRemoved() {

    }

    @Test
    public void purchaseShoppingCartWrongDetailsFail() {

    }

    @Test
    public void purchaseShoppingCartNotMatchToPolicyFail() {

    }
}
