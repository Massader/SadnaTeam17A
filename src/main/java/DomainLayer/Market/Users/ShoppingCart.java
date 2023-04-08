package DomainLayer.Market.Users;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ShoppingCart {
    private UUID id;
    private ConcurrentLinkedQueue<ShoppingBasket> shoppingBaskets;

    public ShoppingCart(UUID id){
        this.id = id;
        this.shoppingBaskets = new ConcurrentLinkedQueue<>();
    }
    public ConcurrentLinkedQueue<ShoppingBasket> getShoppingBaskets() {
        return shoppingBaskets;
    }

    public void setShoppingBaskets(ConcurrentLinkedQueue<ShoppingBasket> shoppingBaskets) {
        this.shoppingBaskets = shoppingBaskets;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
