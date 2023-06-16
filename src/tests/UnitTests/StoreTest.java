package UnitTests;

import static org.junit.jupiter.api.Assertions.*;
//import DomainLayer.Market.Stores.Discounts.Discount;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StoreTest {

    private Store store;
    private Item item1;
    private Item item2;
<<<<<<< HEAD
    private Discount discount1;
    private Discount discount2;
    ConcurrentHashMap<UUID, CartItem> items;
=======
//    private Discount discount1;
//    private Discount discount2;
    ConcurrentHashMap<UUID,Integer> items;
>>>>>>> masterDAL

    @BeforeEach
    public void setUp() {
        store = new Store("Test Store", "A test store for unit testing");
        item1 = new Item("This is a test item1", 10.0, store, 2.0, 2, "aaaa");
        item2 = new Item("This is a test item2", 15.0, store,  5.0, 3, "bbb");
        try {
            store.addItem(item1);
            store.addItem(item2);
        } catch (Exception ignored) {}
        items = new ConcurrentHashMap<>();
        CartItem cartItem1 = new CartItem(item1, 2, item1.getPrice());
        CartItem cartItem2 = new CartItem(item2, 1, item2.getPrice());
        items.put(item1.getId(), cartItem1);
        items.put(item2.getId(), cartItem2);
    }

    @Test
    void testAddRatingSuccess() {
        // Setup
        store.addRating(4);

        // Verify
        assertEquals(4.0, store.getRating());
        assertEquals(1, store.getRatingCounter());
    }
    @Test
    void testAddRatingSuccess2() {
        // Setup
        store.addRating(4);
        store.addRating(2);

        // Verify
        assertEquals(3.0, store.getRating());
        assertEquals(2, store.getRatingCounter());
    }
    @Test
    public void testCalculatePriceOfBasketSuccess() {

        double expectedPrice = (item1.getPrice() * 2) + item2.getPrice();
        double actualPrice = store.calculatePriceOfBasket(items);
        assertEquals(expectedPrice, actualPrice, 0.0);
    }


}