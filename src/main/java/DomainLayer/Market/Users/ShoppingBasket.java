package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingBasket {
    private UUID id;
    private UUID storeId;
    private final ConcurrentHashMap<UUID, CartItem> items;//UUID itemId, int quantity

    public ConcurrentHashMap<UUID, CartItem> getItems() {
        return items;
    }


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

<<<<<<< Updated upstream
    public boolean addItem(Item item, int quantity){
        if(item.getQuantity()<quantity)
            return false;
        if (this.items.get(item.getId()) == null)
            items.put(item.getId(), 0);
        int oldQuantity = this.items.get(item.getId());
        items.put(item.getId(), oldQuantity + quantity);
        //item.setQuantity(item.getQuantity() - quantity);
=======
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
>>>>>>> Stashed changes
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

}
