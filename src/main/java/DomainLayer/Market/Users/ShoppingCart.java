package DomainLayer.Market.Users;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ShoppingCart {
    private UUID id;
    private ConcurrentHashMap<UUID,ShoppingBasket> shoppingBaskets;// store id,

    public ShoppingCart(UUID id){
        this.id = id;
        this.shoppingBaskets = new ConcurrentHashMap<>();
    }
    public ConcurrentHashMap<UUID,ShoppingBasket> getShoppingBaskets() {
        return shoppingBaskets;
    }

    //TODO: why we need this?
    public void setShoppingBaskets(ConcurrentHashMap<UUID,ShoppingBasket> shoppingBaskets) {
        this.shoppingBaskets = shoppingBaskets;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean addItemToCart(UUID itemId, UUID storeID, int  quantity ) {
        ShoppingBasket shoppingBasket = getShoppingBasket(storeID);
        return shoppingBasket.addItem(itemId, quantity);

    }

    public Boolean removeItemToCart(UUID itemId, UUID storeId, int quantity) {
        if(shoppingBaskets.containsKey(storeId)){return  false;}
        ShoppingBasket shoppingBasket = getShoppingBasket(storeId);
        return shoppingBasket.removeItem(itemId,quantity);
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

 public Double calculateTotalPrice() {
        double total =0;
     for (ShoppingBasket basket:shoppingBaskets.values()) {
         total+=basket.calculateTotalPrice();
     }
     return total;

 }


}
