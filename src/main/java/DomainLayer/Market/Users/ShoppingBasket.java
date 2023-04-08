package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ShoppingBasket {
    private UUID id;
    private long storeId;
    private ConcurrentLinkedQueue<Item> items;

    public ShoppingBasket(UUID id, long storeId) {
        this.id = id;
        this.storeId = storeId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public boolean addItem(UUID itemId, int quantity){
        return false;
    }
}
