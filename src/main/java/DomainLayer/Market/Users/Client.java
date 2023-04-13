package DomainLayer.Market.Users;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client {
    private UUID clientCredentials;
    private ShoppingCart cart;

    public Client(UUID id){
        this.clientCredentials = id;
        cart = new ShoppingCart(UUID.randomUUID());
    }

    public void setCart(ShoppingCart cart){
        this.cart = cart;
    }

    public void setId(UUID id) {
        this.clientCredentials = id;
    }

    public UUID getId() {
        return clientCredentials;
    }

    public ShoppingCart getCart() {
        return cart;
    }





}
