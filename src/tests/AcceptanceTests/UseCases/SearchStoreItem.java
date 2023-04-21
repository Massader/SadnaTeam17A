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
public class SearchStoreItem extends ProjectTest {

    UUID founder;
    UUID client;
    ServiceStore store;
    UUID storeId;
    List<ServiceItem> items;

    @BeforeAll
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
