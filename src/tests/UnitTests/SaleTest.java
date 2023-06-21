package UnitTests;

import DomainLayer.Market.Stores.Sale;
import DomainLayer.Market.Stores.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SaleTest {
    private Sale sale;
    private UUID userId;
    private UUID storeId;
    private UUID itemId;
    private Store store;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        storeId = UUID.randomUUID();
        itemId = UUID.randomUUID();
        store = new Store( "Test Store", "as");
        sale = new Sale(userId, storeId, itemId, 3, store);
    }

    @Test
    void testGetId() {
        assertNull(sale.getId());
    }

    @Test
    void testSetId() {
        UUID saleId = UUID.randomUUID();
        sale.setId(saleId);
        assertEquals(saleId, sale.getId());
    }

    @Test
    void testGetUserId() {
        assertEquals(userId, sale.getUserId());
    }

    @Test
    void testSetUserId() {
        UUID newUserId = UUID.randomUUID();
        sale.setUserId(newUserId);
        assertEquals(newUserId, sale.getUserId());
    }

    @Test
    void testGetItemId() {
        assertEquals(itemId, sale.getItemId());
    }

    @Test
    void testSetItemId() {
        UUID newItemId = UUID.randomUUID();
        sale.setItemId(newItemId);
        assertEquals(newItemId, sale.getItemId());
    }

    @Test
    void testGetDate() {
        assertNotNull(sale.getDate());
    }

    @Test
    void testSetDate() {
        Date newDate = Date.from(Instant.now());
        sale.setDate(newDate);
        assertEquals(newDate, sale.getDate());
    }

    @Test
    void testGetQuantity() {
        assertEquals(3, sale.getQuantity());
    }

    @Test
    void testSetQuantity() {
        sale.setQuantity(5);
        assertEquals(5, sale.getQuantity());
    }

    @Test
    void testGetStore() {
        assertEquals(store, sale.getStore());
    }

    @Test
    void testSetStore() {
        Store newStore = new Store("New Store", "s");
        sale.setStore(newStore);
        assertEquals(newStore, sale.getStore());
    }
}
