package DomainLayer.Market.Stores.PurchaseTypes;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "bid_purchases")
public class BidPurchase extends PurchaseType {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKeyColumn(name = "client_credentials")
    private ConcurrentHashMap<UUID, Bid> bids;

    public BidPurchase(String purchaseType) {
        super(purchaseType);
        bids = new ConcurrentHashMap<>();
    }
    public BidPurchase(){super();}
    
    public ConcurrentHashMap<UUID, Bid> getBids() {
        return bids;
    }
    
    public void addBid(UUID clientCredentials, double bidAmount, int quantity) {
        bids.put(clientCredentials, new Bid(clientCredentials, bidAmount, quantity));
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
