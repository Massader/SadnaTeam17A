package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;
import jakarta.persistence.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    private Map<UUID,ShoppingBasket> shoppingBaskets;// store id,

    public ShoppingCart(){
        this.shoppingBaskets = new ConcurrentHashMap<>();
    }
    public ShoppingCart(UUID userId){
        this.userId = userId;
        this.shoppingBaskets = new ConcurrentHashMap<>();
    }
    public Map<UUID,ShoppingBasket> getShoppingBaskets() {
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
        boolean removalSuccess = shoppingBasket.removeItem(item,quantity);
        if (removalSuccess && shoppingBasket.getItems().isEmpty()) {
            shoppingBaskets.remove(storeId);
        }
        return removalSuccess;
    }

    public synchronized void clearCart() {
        shoppingBaskets.clear();
    }

    public int quantityOf(UUID storeId, UUID itemId){
        return shoppingBaskets.get(storeId).quantityOf(itemId);
    }

}



