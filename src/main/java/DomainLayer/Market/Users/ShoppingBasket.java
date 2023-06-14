package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;
import jakarta.persistence.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "ShoppingBaskets")
public class ShoppingBasket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id; //id of the shopping basket

    @Column(name = "storeId")
    private UUID storeId;

    @ElementCollection
    @CollectionTable(name = "BasketItems", joinColumns = @JoinColumn(name = "basket_id"))
    @MapKeyColumn(name = "item_id")
    @Column(name = "quantity")
    private Map<UUID,Integer> items;//UUID itemId, int quantity


    public Map<UUID, Integer> getItems() {
        return items;
    }

    public ShoppingBasket() {}

    public ShoppingBasket(UUID storeId) {
        this.id = UUID.randomUUID();
        this.storeId = storeId;
        this.items = new ConcurrentHashMap<>();
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
        if (this.items.get(cartItem.getItemId()) == null) {
            items.put(cartItem.getItemId(), cartItem);
        }
        else {
            int oldQuantity = items.get(cartItem.getItemId()).getQuantity();
            items.get(cartItem.getItemId()).setQuantity(oldQuantity + quantity);
        }
        return true;
    }

    public boolean removeItem(UUID itemId, int quantity) throws Exception {
        synchronized (this) {
            if(items.get(itemId) == null)
                throw new Exception("Item is not in the cart");
            int oldQuantity = items.get(itemId).getQuantity();
            if (oldQuantity <= quantity) {
                items.remove(itemId);
                return true;
            }
            else
                items.get(itemId).setQuantity(oldQuantity - quantity);
            return true;
        }
    }

    public void clearBasket() {
        items.clear();
    }

    public int quantityOf(UUID itemId){return items.get(itemId).getQuantity();}

    public boolean validateStore(UUID storeId) {
        return storeId.equals(this.storeId);
    }
}
