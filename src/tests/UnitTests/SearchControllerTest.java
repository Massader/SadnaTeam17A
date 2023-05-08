package UnitTests;

import DomainLayer.Market.SearchController;
import DomainLayer.Market.StoreController;
import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchControllerTest {

    private SearchController searchController;
    private StoreController storeController;

    private Store store1;
    private Store store2;
    private Store store3;

    private Item item1;
    private Item item2;
    private Item item3;
    private Item item4;

    private Category category1;
    private Category category2;
    private Category category3;

    @BeforeEach
    public void setUp() {
        searchController = SearchController.getInstance();
        storeController = StoreController.getInstance();
        storeController.init();

        store1 = new Store("s1", "Store 1");
        store2 = new Store("s2", "Store 2");
        store3 = new Store("s3", "Store 3");

        item1 = new Item(UUID.randomUUID(), "Item1", 1, store1.getStoreId(), 3.0, 5, "a");
        item2 = new Item(UUID.randomUUID(), "Item2", 10, store2.getStoreId(), 2,1, "b");
        item3 = new Item(UUID.randomUUID(), "Item3", 100, store3.getStoreId(), 5,100,  "c");
        item4 = new Item(UUID.randomUUID(), "Item4", 9000, store1.getStoreId(), 1,1000, "d");

        category1 = new Category("c1");
        category2 = new Category("c2");
        category3 = new Category("c3");

        item1.addCategory(category1);
        item1.addCategory(category2);
        item1.addCategory(category3);
        item2.addCategory(category1);
        item2.addCategory(category2);
        item3.addCategory(category2);
        item3.addCategory(category3);
        item4.addCategory(category2);
        try {
            store1.addItem(item1);
            store1.addItem(item2);
            store2.addItem(item3);
            store3.addItem(item4);
        } catch (Exception ignored) {}
        storeController.addStore(store1);
        storeController.addStore(store2);
        storeController.addStore(store3);

    }

    @Test
    public void testSearch1() {
        List<Item> expectedItems = new ArrayList<>();
        expectedItems.add(item1);
        expectedItems.add(item2);

        List<Item> actualItems = searchController.searchItem("Item", category1.getCategoryName(), 0, 50, 0, 0).getValue();

        assertEquals(expectedItems.size(), actualItems.size());
        Comparator<Item> comparator = new Comparator<>() {

            @Override
            public int compare(Item o1, Item o2) {
                return o1.hashCode() - o2.hashCode();
            }
        };
        expectedItems.sort(comparator);
        actualItems.sort(comparator);
        assertEquals(expectedItems, actualItems);
    }
}
