package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Users.ShoppingBasket;

import java.util.HashMap;
import java.util.UUID;

public class ServiceShoppingBasket {
    private UUID userId;
    private UUID storeId;
    private HashMap<UUID, Integer> items; // Map of Item ID -> Quantity

    public ServiceShoppingBasket(ShoppingBasket basket) {
        this.userId = basket.getId();
        this.storeId = basket.getStoreId();
        items = new HashMap<>(basket.getItems());
    }
}
