package DomainLayer.Market.Users;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client {
    private UUID clientCredentials;
    private ShoppingCart cart;

    public Client(UUID id){
        this.clientCredentials = id;
        cart = new ShoppingCart();
    }

    public UUID getId() {
        return clientCredentials;
    }

//    public boolean AddItemToCart(int itemId, int qantity){
//        return false;
//    }

}
