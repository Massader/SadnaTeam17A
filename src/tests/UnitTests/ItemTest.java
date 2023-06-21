package UnitTests;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private Item item;
    private Store store;

    @BeforeEach
    void setUp() {
        store = new Store("Test Store", "des");
        item = new Item("Test Item", 10.0, store, 4.0, 10, "Test description");
    }

    @Test
    void testGetId() {
        assertNull(item.getId());
    }

    @Test
    void testSetId() {
        UUID itemId = UUID.randomUUID();
        item.setId(itemId);
        assertEquals(itemId, item.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Test Item", item.getName());
    }

    @Test
    void testSetName() {
        try {
            item.setName("New Item Name");
        } catch (Exception e) {

        }
        assertEquals("New Item Name", item.getName());
    }

    @Test
    void testSetName_NullName() {
        assertThrows(RuntimeException.class, () -> item.setName(null));
    }

    @Test
    void testSetName_EmptyName() {
        assertThrows(RuntimeException.class, () -> item.setName(""));
    }

    @Test
    void testGetPrice() {
        assertEquals(10.0, item.getPrice());
    }

    @Test
    void testSetPrice() {
        item.setPrice(15.0);
        assertEquals(15.0, item.getPrice());
    }

    @Test
    void testSetPrice_NegativePrice() {
        assertThrows(RuntimeException.class, () -> item.setPrice(-5.0));
    }

    @Test
    void testGetStore() {
        assertEquals(store, item.getStore());
    }

    @Test
    void testSetStoreId() {
        Store newStore = new Store("New Store", "des");
        item.setStoreId(newStore);
        assertEquals(newStore, item.getStore());
    }

    @Test
    void testGetRating() {
        assertEquals(4.0, item.getRating());
    }
}


