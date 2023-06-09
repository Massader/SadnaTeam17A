package DomainLayer.Market.Stores.PurchaseTypes;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BidPurchase extends PurchaseType{
    
    private final ConcurrentHashMap<UUID, Bid> bids;
    
    public BidPurchase(String purchaseType) {
        super(purchaseType);
        bids = new ConcurrentHashMap<>();
    }
    
    public ConcurrentHashMap<UUID, Bid> getBids() {
        return bids;
    }
    
    public void addBid(UUID clientCredentials, UUID itemId, double bidPrice, int quantity) {
        bids.put(clientCredentials, new Bid(clientCredentials, itemId, bidPrice, quantity));
    }
    
    public void removeBid(UUID clientCredentials) {
        bids.remove(clientCredentials);
    }
    
    public Bid acceptBid(UUID clientCredentials, UUID bidderId, double bidPrice) {
        if (bids.get(bidderId).getPrice() != bidPrice)
            throw new RuntimeException("Bid amount changed during processing.");
        return bids.get(bidderId).acceptBid(clientCredentials);
    }
    
    public boolean isBidAccepted(UUID clientCredentials) {
        if (!bids.containsKey(clientCredentials))
            throw new RuntimeException("This item does not have a bid by this user.");
        return bids.get(clientCredentials).isAccepted();
    }
}
