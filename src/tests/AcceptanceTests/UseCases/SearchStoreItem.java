package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class SearchStoreItem extends ProjectTest {

    UUID founder;
    UUID client;
    ServiceStore store;
    List<ServiceItem> items;
    ServiceItem item;

    @BeforeAll
    public void beforeClass() {

    }

    @BeforeEach
    public void setUp()  {
        bridge.setReal();
        bridge.register("founder", "Password1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Password1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        if (item == null) {
            item = bridge.addItemToStore(founder, "exampleItem", 10, store.getStoreId(), 10, "test").getValue();
            bridge.addItemCategory(founder, store.getStoreId(), item.getId(), "category");
        }
        items = new ArrayList<>();
        client = bridge.createClient().getValue();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(client);
        bridge.closeStore(founder, store.getStoreId());
        bridge.logout(founder);
    }

    @AfterAll
    public void afterClass() {

    }

    @Test
    //Tests if searching for an item by name and category returns at least one item.
    public void searchItemSuccess() {
        items = bridge.searchItem("example", "category", 0, 100, 0, -1).getValue();
        Assert.assertNotNull(items);
        Assert.assertTrue(items.size() > 0);
    }

    @Test
    //Tests if searching for an item by name and category returns no items when there are no items matching the search criteria.
    public void searchItemNoItemsSuccess() {
        items = bridge.searchItem("example", "General", 11, 100, 0, -1).getValue();
        Assert.assertNotNull(items);
        Assert.assertEquals(0, items.size());
    }
}
