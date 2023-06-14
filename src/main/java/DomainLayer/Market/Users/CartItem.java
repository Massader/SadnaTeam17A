package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Users_CartItem")
public class CartItem {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;
    private int quantity;
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
