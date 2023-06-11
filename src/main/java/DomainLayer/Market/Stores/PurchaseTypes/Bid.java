package DomainLayer.Market.Stores.PurchaseTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bid {
    private UUID bidderId;
    private double price;
    private List<UUID> ownersAccepted;
    private int quantity;
    private boolean accepted;
    
    public Bid(UUID bidderId, double price, int quantity) {
        this.bidderId = bidderId;
        this.price = price;
        ownersAccepted = new ArrayList<>();
        accepted = false;
    }
    
    public UUID getBidderId() {
        return bidderId;
    }
    
    public void setBidderId(UUID bidderId) {
        this.bidderId = bidderId;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public List<UUID> getOwnersAccepted() {
        return ownersAccepted;
    }
    
    public Bid acceptBid(UUID owner) {
        if (!ownersAccepted.contains(owner))
            ownersAccepted.add(owner);
        return this;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public boolean isAccepted() {
        return accepted;
    }
    
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
