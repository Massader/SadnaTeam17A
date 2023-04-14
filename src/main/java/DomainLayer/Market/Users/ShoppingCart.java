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

    public Boolean addItemToCart(Item item, UUID storeID, int  quantity ) {
        ShoppingBasket shoppingBasket = getShoppingBasket(storeID);
        return shoppingBasket.addItem(item, quantity);
    }

    public Boolean removeItemFromCart(Item item, UUID storeId, int quantity) {
        if(shoppingBaskets.containsKey(storeId))
            return false;
        ShoppingBasket shoppingBasket = getShoppingBasket(storeId);
        return shoppingBasket.removeItem(item,quantity);
    }

 public ShoppingBasket getShoppingBasket(UUID storeID){
     ShoppingBasket shoppingBasket;
     if(shoppingBaskets.containsKey(storeID)){
         shoppingBasket = shoppingBaskets.get(storeID);
     }
     else{
         shoppingBasket = new ShoppingBasket(storeID);
         shoppingBaskets.put(storeID,shoppingBasket);
     }
     return shoppingBasket;

 }

// public Double calculateTotalPrice() {
//        double total =0;
//     for (ShoppingBasket basket:shoppingBaskets.values()) {
//         total+=basket.calculateTotalPrice();
//     }
//     return total;

 }


