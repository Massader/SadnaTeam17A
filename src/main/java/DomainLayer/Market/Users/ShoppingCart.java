package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Table(name = "ShoppingCarts")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne(mappedBy = "cart")
    private Client client;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<ShoppingBasket> shoppingBaskets;

    public ShoppingCart(){
       // this.shoppingBasketsMap = new ConcurrentHashMap<>();
        this.shoppingBaskets = new ConcurrentLinkedQueue<>();
    }

    public ShoppingCart(Client client){
        this.client = client;
       // this.shoppingBasketsMap = new ConcurrentHashMap<>();
        this.shoppingBaskets = new ConcurrentLinkedQueue<>();
    }
    /*
    public Map<UUID,ShoppingBasket> getShoppingBasketsMap() {
        return shoppingBasketsMap;
    }

     */
    public Collection<ShoppingBasket> getShoppingBaskets() {
        return shoppingBaskets;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    /*
    public Boolean addItemToCart(Item item, UUID storeId, int quantity) {
        if (!shoppingBasketsMap.containsKey(storeId))
            shoppingBasketsMap.put(storeId, new ShoppingBasket(storeId));
        return shoppingBasketsMap.get(storeId).addItem(item, quantity);
    }

     */

    public Boolean removeItemFromCart(UUID itemId, UUID storeId, int quantity) throws Exception {
        if (!shoppingBaskets.containsKey(storeId))
            return false;
        ShoppingBasket shoppingBasket = shoppingBaskets.get(storeId);
        boolean removalSuccess = shoppingBasket.removeItem(itemId, quantity);
        if (removalSuccess && shoppingBasket.getItems().isEmpty()) {
            shoppingBasketsMap.remove(storeId);
        }
        return removalSuccess;
    }

    public Boolean addItemToCart(CartItem cartItem, UUID storeId, int quantity) {
        ShoppingBasket basket = null;
        for (ShoppingBasket shoppingBasket : shoppingBaskets) {
            if (shoppingBasket.validateStore(storeId)) {
                basket = shoppingBasket;
                break;
            }
        }
        if (basket == null) {
            basket = new ShoppingBasket(storeId);
            shoppingBaskets.add(basket);
        }
        return basket.addItem(cartItem, quantity);
    }

    public synchronized void clearCart() {
        shoppingBaskets.clear();
    }

    /*
    public int quantityOf(UUID storeId, UUID itemId){
        return shoppingBasketsMap.get(storeId).quantityOf(itemId);
    }
     */

    public int quantityOf(UUID storeId, UUID itemId){
        for (ShoppingBasket basket : shoppingBaskets) {
            if (basket.validateStore(storeId)) {
                return basket.quantityOf(itemId);
            }
        }
        return 0;
    }

    public ShoppingBasket getBasketByStoreId(UUID storeId) {
        for (ShoppingBasket basket : shoppingBaskets) {
            if (basket.validateStore(storeId)) {
                return basket;
            }
        }
        return null;
    }
}



