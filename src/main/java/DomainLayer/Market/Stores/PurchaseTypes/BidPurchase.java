package DomainLayer.Market.Stores.PurchaseTypes;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Table(name = "bid_purchases")
public class BidPurchase extends PurchaseType {

    /*
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKeyColumn(name = "client_credentials")
    private ConcurrentHashMap<UUID, Bid> bidsMap;
     */

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKeyColumn(name = "client_credentials")
    private Collection<Bid> bids;


    public BidPurchase(String purchaseType) {
        super(purchaseType);
   //     bidsMap = new ConcurrentHashMap<>();
        bids = new ConcurrentLinkedQueue<>();
    }
    public BidPurchase(){super();}

    /*
    public ConcurrentHashMap<UUID, Bid> getBidsMap() {
        return bidsMap;
    }
     */

    public Collection<Bid> getBids() {
        return bids;
    }
    
    public void addBid(UUID clientCredentials, double bidAmount, int quantity) {
        if (hasBid(clientCredentials)) {
            removeBid(clientCredentials);
        }
        bids.add(new Bid(clientCredentials, bidAmount, quantity));
    }
    
    public void removeBid(UUID clientCredentials) {
        for (Bid bid : bids) {
            if (bid.getBidderId().equals(clientCredentials)) {
                bids.remove(bid);
            }
        }
    }
/*
    public Bid acceptBid(UUID clientCredentials, UUID bidderId, double bidPrice) {
        if (bidsMap.get(bidderId).getPrice() != bidPrice)
            throw new RuntimeException("Bid amount changed during processing.");
        return bidsMap.get(bidderId).acceptBid(clientCredentials);
    }
 */


    public Bid acceptBid(UUID clientCredentials, UUID bidderId, double bidPrice) {
        if (!hasBid(clientCredentials)) {
            throw new RuntimeException("Client has no bid");
        }
        if (getBidByBidderId(clientCredentials).getPrice() != bidPrice) {
            throw new RuntimeException("Bid amount changed during processing.");
        }
        return getBidByBidderId(clientCredentials).acceptBid(clientCredentials);
    }

    /*
    public boolean isBidAccepted(UUID clientCredentials) {
        if (!bidsMap.containsKey(clientCredentials))
            throw new RuntimeException("This item does not have a bid by this user.");
        return bidsMap.get(clientCredentials).isAccepted();
    }

     */

    public boolean isBidAccepted(UUID clientCredentials) {
        if (!hasBid(clientCredentials)) {
            throw new RuntimeException("This item does not have a bid by this user.");
        }
        return getBidByBidderId(clientCredentials).isAccepted();
    }

    public boolean hasBid(UUID clientCredentials) {
        for (Bid bid : bids) {
            if (bid.getBidderId().equals(clientCredentials)) {
                return true;
            }
        }
        return false;
    }

    public Bid getBidByBidderId(UUID clientCredentials) {
        for (Bid bid : bids) {
            if (bid.getBidderId().equals(clientCredentials)) {
                return bid;
            }
        }
        return null;
    }
}
