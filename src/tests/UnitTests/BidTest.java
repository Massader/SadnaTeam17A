package UnitTests;


import DomainLayer.Market.Stores.PurchaseTypes.Bid;
import DomainLayer.Market.Stores.PurchaseTypes.BidPurchase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
public class BidTest {
    private BidPurchase bidPurchase;

    @BeforeEach
    public void setUp() {
        bidPurchase = new BidPurchase("Bid");
    }

    @Test
    public void testAddBid() {
        UUID clientCredentials = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        double bidPrice = 10.0;
        int quantity = 5;

        bidPurchase.addBid(clientCredentials, storeId, itemId, bidPrice, quantity);

        assertEquals(1, bidPurchase.getBids().size());
        assertTrue(bidPurchase.getBids().containsKey(clientCredentials));
        assertEquals(bidPrice, bidPurchase.getBids().get(clientCredentials).getPrice());
        assertEquals(quantity, bidPurchase.getBids().get(clientCredentials).getQuantity());
    }

    @Test
    public void testRemoveBid() {
        UUID clientCredentials = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        double bidPrice = 10.0;
        int quantity = 5;

        bidPurchase.addBid(clientCredentials, storeId, itemId, bidPrice, quantity);

        bidPurchase.removeBid(clientCredentials);

        assertEquals(0, bidPurchase.getBids().size());
        assertFalse(bidPurchase.getBids().containsKey(clientCredentials));
    }

    @Test
    public void testAcceptBid() {
        UUID clientCredentials = UUID.randomUUID();
        UUID bidderId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        double bidPrice = 10.0;
        int quantity = 5;

        bidPurchase.addBid(bidderId, storeId, itemId, bidPrice, quantity);

        Bid acceptedBid = bidPurchase.acceptBid(clientCredentials, bidderId, bidPrice);

        //???assertTrue(acceptedBid.isAccepted());//TODO

    }

    @Test
    public void testIsBidAccepted() {//TODO
        UUID clientCredentials = UUID.randomUUID();
        UUID bidderId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        double bidPrice = 10.0;
        int quantity = 5;

//        bidPurchase.addBid(bidderId, storeId, itemId, bidPrice, quantity);
//
//        assertFalse(bidPurchase.isBidAccepted(clientCredentials));
//
//        bidPurchase.acceptBid(clientCredentials, bidderId, bidPrice);
//
//        assertTrue(bidPurchase.isBidAccepted(clientCredentials));
    }
}
