package UnitTests;

import DataAccessLayer.controllers.PurchaseDalController;
import DomainLayer.Market.PurchaseController;
import DomainLayer.Market.StoreController;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.PurchaseTypes.Bid;
import DomainLayer.Market.Stores.PurchaseTypes.BidPurchase;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.ShoppingCart;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BidTest {
    private Item item;
    private BidPurchase bidPurchase;
    private UUID clientCredentials;
    private UUID storeId;
    private UUID itemId;
    private double bidPrice;
    private int quantity;



    @BeforeEach
    public void setup() {
        item = new Item("Item Name", 10.0, null, 0.0, 0, "Item Description");
        bidPurchase = new BidPurchase("Bid Purchase");
        clientCredentials = UUID.randomUUID();
        item.setPurchaseType(bidPurchase);
        storeId = UUID.randomUUID();
        bidPrice = 10.0;
        quantity = 5;
    }


    @Test
    public void testAddBid() {
        bidPurchase.addBid(clientCredentials, storeId, itemId, bidPrice, quantity);
        assertTrue(bidPurchase.hasBid(clientCredentials));
        assertEquals(quantity, bidPurchase.getBidByBidderId(clientCredentials).getQuantity());
        assertEquals(bidPrice, bidPurchase.getBidByBidderId(clientCredentials).getPrice());
    }

    @Test
    public void testRemoveBid() {
        bidPurchase.addBid(clientCredentials, storeId, itemId, bidPrice, quantity);
        assertTrue(bidPurchase.hasBid(clientCredentials));
        bidPurchase.removeBid(clientCredentials);
        assertFalse(bidPurchase.hasBid(clientCredentials));
    }



    @Test
    public void testHasBid() {
        assertFalse(bidPurchase.hasBid(clientCredentials));
        bidPurchase.addBid(clientCredentials, storeId, itemId, bidPrice, quantity);
        assertTrue(bidPurchase.hasBid(clientCredentials));
    }

    @Test
    public void testGetBidByBidderId() {
        assertNull(bidPurchase.getBidByBidderId(clientCredentials));
        bidPurchase.addBid(clientCredentials, storeId, itemId, bidPrice, quantity);
        assertNotNull(bidPurchase.getBidByBidderId(clientCredentials));
        assertEquals(clientCredentials, bidPurchase.getBidByBidderId(clientCredentials).getBidderId());
    }


    @Test
    void addBid_GetBidByBidderId_Success() {
        bidPurchase.addBid(clientCredentials, storeId, itemId, bidPrice, quantity);
        assertTrue(bidPurchase.hasBid(clientCredentials));
        assertNotNull(bidPurchase.getBidByBidderId(clientCredentials));
        assertEquals(clientCredentials, bidPurchase.getBidByBidderId(clientCredentials).getBidderId());
    }



}
