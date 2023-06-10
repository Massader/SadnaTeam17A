package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Users.CartItem;

public class ServiceCartItem {
    
    private ServiceItem item;
    private int quantity;
    private double price;
    
    public ServiceCartItem(ServiceItem item, int quantity, double price) {
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }
    
    public ServiceCartItem(CartItem cartItem) {
        this.item = new ServiceItem(cartItem.getItem());
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getPrice();
    }
    
    public ServiceItem getItem() {
        return item;
    }
    
    public void setItem(ServiceItem item) {
        this.item = item;
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
}
