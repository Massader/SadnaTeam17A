package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ShoppingBasket {
    private UUID id;
    private UUID storeId;
    private ConcurrentHashMap<UUID,Integer> itemsID;//UUID itemId, int quantity

    public ConcurrentHashMap<UUID, Integer> getItemsID() {
        return itemsID;
    }

    public void setItemsID(ConcurrentHashMap<UUID, Integer> itemsID) {
        this.itemsID = itemsID;
    }

    public ShoppingBasket(UUID storeId) {
        this.id = UUID.randomUUID();
        this.storeId = storeId;
        this.itemsID= new ConcurrentHashMap<>();
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

    public boolean addItem(Item item, int quantity){
        synchronized (item) {
            if(item.getQuantity()<quantity)
                return false;
            if (this.itemsID.get(item.getId()) == null)
                itemsID.put(item.getId(), 0);
            int oldQuantity = this.itemsID.get(item.getId());
            itemsID.put(item.getId(), oldQuantity + quantity);
            item.setQuantity(item.getQuantity() - quantity);
            return true;
        }
    }

    public boolean removeItem(Item item, int quantity){
        synchronized (item) {
            // dont have any item like this in ShoppingBasket
            int oldQuantity = item.getQuantity();
            if (oldQuantity < quantity)
                return false;
            // there is less item's then the quantity to remove
            itemsID.put(item.getId(), oldQuantity - quantity);
            item.setQuantity(item.getQuantity() + quantity);
            return true;
        }
    }
//    public Double calculateTotalPrice() {
//        return 0.0;}
}
