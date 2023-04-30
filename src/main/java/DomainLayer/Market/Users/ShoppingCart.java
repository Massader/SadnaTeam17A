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

    public Boolean addItemToCart(Item item, UUID storeId, int quantity) {
        if (!shoppingBaskets.containsKey(storeId))
            shoppingBaskets.put(storeId, new ShoppingBasket(storeId));
        return shoppingBaskets.get(storeId).addItem(item, quantity);
    }

    public Boolean removeItemFromCart(Item item, UUID storeId, int quantity) {
        if(!shoppingBaskets.containsKey(storeId))
            return false;
        ShoppingBasket shoppingBasket = shoppingBaskets.get(storeId);
        return shoppingBasket.removeItem(item,quantity);
    }

    public synchronized void clearCart() {
        for (ShoppingBasket shoppingBasket : shoppingBaskets.values()) {
            shoppingBasket.clearBasket();
        }
    }

    public int quantityOf(UUID stroeId, UUID itemId){
        return shoppingBaskets.get(stroeId).quantityOf(itemId);
    }
    public void RemovingPurchases(){
        shoppingBaskets.clear();

    }
}



