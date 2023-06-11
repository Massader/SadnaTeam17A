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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "uuid", nullable = false)
    private Long uuid;

//    @OneToOne(mappedBy = "cart")
    @Column(name = "userId")
    private UUID userId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Map<UUID,ShoppingBasket> shoppingBasketsMap;// store id,

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<ShoppingBasket> shoppingBaskets;

    public ShoppingCart(){
       // this.shoppingBasketsMap = new ConcurrentHashMap<>();
        this.shoppingBaskets = new ConcurrentLinkedQueue<>();
    }
    public ShoppingCart(UUID userId){
        this.userId = userId;
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
        return userId;
    }

    public void setId(UUID id) {
        this.userId = id;
    }

    /*
    public Boolean addItemToCart(Item item, UUID storeId, int quantity) {
        if (!shoppingBasketsMap.containsKey(storeId))
            shoppingBasketsMap.put(storeId, new ShoppingBasket(storeId));
        return shoppingBasketsMap.get(storeId).addItem(item, quantity);
    }

     */

    public Boolean addItemToCart(Item item, UUID storeId, int quantity) {
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
        return basket.addItem(item, quantity);
    }

    /*
    public Boolean removeItemFromCart(Item item, UUID storeId, int quantity) {
        if(!shoppingBasketsMap.containsKey(storeId))
            return false;
        ShoppingBasket shoppingBasket = shoppingBasketsMap.get(storeId);
        boolean removalSuccess = shoppingBasket.removeItem(item,quantity);
        if (removalSuccess && shoppingBasket.getItems().isEmpty()) {
            shoppingBasketsMap.remove(storeId);
        }
        return removalSuccess;
    }
     */

    public Boolean removeItemFromCart(Item item, UUID storeId, int quantity) {
        for (ShoppingBasket basket : shoppingBaskets) {
            if (basket.validateStore(storeId)) {
                boolean removalSuccess = basket.removeItem(item,quantity);
                if (removalSuccess && basket.getItems().isEmpty()) {
                    shoppingBaskets.remove(basket);
                }
                return removalSuccess;
            }
        }
        return false;
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



