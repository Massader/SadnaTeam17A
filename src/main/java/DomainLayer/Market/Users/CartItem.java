package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;

import java.util.UUID;

public class CartItem {
    
    private Item item;
    private int quantity;
    private double price;
    
    public CartItem(Item item, int quantity, double price) {
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }
    
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
}
