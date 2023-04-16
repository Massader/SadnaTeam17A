package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.ServiceItem;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.*;

import java.util.List;
import java.util.UUID;

public class SearchStoreItem extends ProjectTest {

    UUID founder;
    UUID client;
    ServiceStore store;
    UUID storeId;
    List<ServiceItem> items;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.stockManagementAddNewItem(founder, "exampleItem", 10, storeId, 10, "test");
        items = null;
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
    public void searchItemSuccess() {
        items = bridge.searchItem("example", "General", 0, 100, 0, -1);
        Assert.assertNotNull(items);
        Assert.assertTrue(items.size() > 0);
    }

    @Test
    public void searchItemNoItemsSuccess() {
        items = bridge.searchItem("example", "General", 11, 100, 0, -1);
        Assert.assertNotNull(items);
        Assert.assertEquals(0, items.size());
    }
}
