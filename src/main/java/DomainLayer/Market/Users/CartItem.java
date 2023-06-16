package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Users_CartItem")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;
    @Column
    private int quantity;
    @Column
    private double price;
    
    public CartItem(Item item, int quantity, double price) {
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }

    public CartItem(){}
    
    public Item getItem() {
        return item;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public UUID getItemId() {
        return item.getId();
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
