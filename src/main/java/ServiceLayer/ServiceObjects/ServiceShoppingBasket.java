package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Users.ShoppingBasket;

import java.util.HashMap;
import java.util.UUID;

public class ServiceShoppingBasket {
    private  UUID id;
    private  UUID storeId;
    private HashMap<UUID, ServiceCartItem> items; // Map of Item ID -> Quantity

    public ServiceShoppingBasket(ShoppingBasket basket) {
        this.id = basket.getId();
        this.storeId = basket.getStoreId();
        items = new HashMap<>();
        basket.getItems().forEach(cartItem -> items.put(cartItem.getItemId(), new ServiceCartItem(cartItem)));
    }
    public ServiceShoppingBasket(){

    }

    public UUID getId() {
        return id;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public HashMap<UUID, ServiceCartItem> getItems() {
        return items;
    }
}
