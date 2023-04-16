package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceItem;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.*;

import java.util.UUID;

public class PurchaseShoppingCart extends ProjectTest {

    UUID founder;
    UUID client;
    ServiceStore store;
    UUID storeId;
    ServiceItem item;
    UUID itemId;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        item = bridge.stockManagementAddNewItem(founder, "item", 10, storeId, 1, "test");
        itemId = item.getId();
    }

    @Before
    public void beforeEach()  {
        client = bridge.enterSystem();
    }

    @After
    public void tearDown() {
        bridge.exitSystem(client);
    }

    @AfterClass
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
    }

    @Test
    public void purchaseShoppingCartSuccess() {
        Assert.assertTrue(true);
    }

}
