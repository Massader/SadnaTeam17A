package DomainLayer.Market.Stores.PurchaseTypes;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "BidId", nullable = false, unique = true)
    private UUID bidderId;
    @Column
    private double price;
    @ElementCollection
    @CollectionTable(name = "owners_list",
            joinColumns = @JoinColumn(name = "petition_id"))
    @Column(name = "owner_id")
    private List<UUID> ownersAccepted;
    @Column
    private int quantity;
    @Column
    private boolean accepted;
    
    public Bid(UUID bidderId, double price, int quantity) {
        this.bidderId = bidderId;
        this.price = price;
        ownersAccepted = new ArrayList<>();
        accepted = false;
    }

    public Bid() {}

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
