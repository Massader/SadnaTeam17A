package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ShoppingBasket {
    private UUID id;
    private UUID storeId;
    private ConcurrentHashMap<UUID,Integer> itemsID;//UUID itemId, int quantity

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

    public boolean addItem(UUID itemId, int quantity){
        if(this.itemsID.get(itemId)!=null){
            int oldQuantity= this.itemsID.get(itemId);
            itemsID.put(itemId,oldQuantity+quantity);
        }
        else {this.itemsID.put(itemId,quantity);}
        return  true;
    }
    public boolean removeItem(UUID itemId, int quantity){
        if(itemsID.get(itemId)==null){return  false;}// dont have any item like this in ShoppingBasket
        int oldQuantity= this.itemsID.get(itemId);
        if(oldQuantity<quantity){ return false;}// there is less item's then the quantity to remove
        itemsID.put(itemId,oldQuantity-quantity);
        return true;

    }
    public Double calculateTotalPrice() {
        return 0.0;}
}
