package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {
    private UUID userId;
    private ConcurrentHashMap<UUID,ShoppingBasket> shoppingBaskets;// store id,

    public ShoppingCart(UUID userId){
        this.userId = userId;
        this.shoppingBaskets = new ConcurrentHashMap<>();
    }
    public ConcurrentHashMap<UUID,ShoppingBasket> getShoppingBaskets() {
        return shoppingBaskets;
    }

    public UUID getId() {
        return userId;
    }

    public void setId(UUID id) {
        this.userId = id;
    }

    public Boolean addItemToCart(Item item, UUID storeId, int quantity) throws Exception {
        if (!shoppingBaskets.containsKey(storeId))
            shoppingBaskets.put(storeId, new ShoppingBasket(storeId));
        if (shoppingBaskets.get(storeId).addItem(item, quantity))
            return true;
        if (shoppingBaskets.get(storeId).getItems().isEmpty())
            shoppingBaskets.remove(storeId);
        return false;
    }

    public Boolean removeItemFromCart(Item item, UUID storeId, int quantity) {
        if(!shoppingBaskets.containsKey(storeId))
            return false;
        ShoppingBasket shoppingBasket = shoppingBaskets.get(storeId);
        boolean removalSuccess = shoppingBasket.removeItem(item,quantity);
        if (removalSuccess && shoppingBasket.getItems().isEmpty()) {
            shoppingBaskets.remove(storeId);
        }
        return removalSuccess;
    }

    public synchronized void clearCart() {
        shoppingBaskets.clear();
    }

    public int quantityOf(UUID storeId, UUID itemId){
        return shoppingBaskets.get(storeId).quantityOf(itemId);
    }

}



