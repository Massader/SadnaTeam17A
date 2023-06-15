package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Table(name = "Users_ShoppingBasket")
public class ShoppingBasket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id; //id of the shopping basket
    @Column(name = "storeId")
    private UUID storeId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<CartItem> items;//UUID itemId, int quantity

    public Collection<CartItem> getItems() {
        return items;
    }

    public ShoppingBasket() {
        this.items = new ConcurrentLinkedQueue<>();}

    public ShoppingBasket(UUID storeId) {
        this.id = UUID.randomUUID();
        this.storeId = storeId;
        this.items = new ConcurrentLinkedQueue<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public boolean addItem(CartItem cartItem, int quantity) throws Exception {
        if(cartItem.getItem().getQuantity() < quantity)
            throw new Exception("Quantity of cart item is higher than the quantity in stock.");
        CartItem cartItem1 = getCartItem(cartItem.getItemId());
        if (cartItem1 == null) {
            items.add( cartItem);
        }
        else {
            int oldQuantity = cartItem1.getQuantity();
            cartItem1.setQuantity(oldQuantity + quantity);
        }
        return true;
    }

    public boolean removeItem(UUID itemId, int quantity) throws Exception {
        synchronized (this) {
            CartItem item = getCartItem(itemId);
            if(item == null)
                throw new Exception("Item is not in the cart");
            int oldQuantity = item.getQuantity();
            if (oldQuantity <= quantity) {
                items.remove(getCartItem(itemId));
                return true;
            }
            else
                item.setQuantity(oldQuantity - quantity);
            return true;
        }
    }

    public void clearBasket() {
        items.clear();
    }

    public int quantityOf(UUID itemId){return getItem(itemId).getQuantity();}

    public boolean validateStore(UUID storeId) {
        return storeId.equals(this.storeId);
    }

    public CartItem getCartItem(UUID itemId){
        for(CartItem cartItem : items){
            if (cartItem.getItem().getId().equals(itemId))
                return cartItem;
        }
        return null;
    }

    public Item getItem(UUID itemId){
        for(CartItem cartItem : items){
            if (cartItem.getItem().getId().equals(itemId))
                return cartItem.getItem();
        }
        return null;
    }
    public boolean hasItem(UUID itemId){
        for(CartItem cartItem : items){
            if (cartItem.getItem().getId().equals(itemId))
                return true;
        }
        return false;
    }

    public void setItems(Collection<CartItem> items) {
        this.items = items;
    }
}
