package DomainLayer.Market.Users;

import DomainLayer.Market.Stores.Item;
import jakarta.persistence.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "ShoppingBaskets")
public class ShoppingBasket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id; //id of the shopping basket

    @Column(name = "storeId")
    private UUID storeId;

//    @OneToMany
//    @Transient

@ElementCollection
@CollectionTable(name = "BasketItems", joinColumns = @JoinColumn(name = "basket_id"))
@MapKeyColumn(name = "item_id")
@Column(name = "quantity")
    private Map<UUID,Integer> items;//UUID itemId, int quantity


    public Map<UUID, Integer> getItems() {
        return items;
    }

    public ShoppingBasket() {}

    public ShoppingBasket(UUID storeId) {
        this.id = UUID.randomUUID();
        this.storeId = storeId;
        this.items = new ConcurrentHashMap<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public boolean addItem(Item item, int quantity){
        if(item.getQuantity()<quantity)
            return false;
        if (this.items.get(item.getId()) == null)
            items.put(item.getId(), 0);
        int oldQuantity = this.items.get(item.getId());
        items.put(item.getId(), oldQuantity + quantity);
        //item.setQuantity(item.getQuantity() - quantity);
        return true;
    }

    public boolean removeItem(Item item, int quantity){
        synchronized (this) {
            int oldQuantity = items.get(item.getId());
            if (oldQuantity < quantity)
                return false;
            if (oldQuantity == quantity) {
                items.remove(item.getId());
                return true;
            }
            items.put(item.getId(), oldQuantity - quantity);
           // item.setQuantity(item.getQuantity() + quantity);
            return true;
        }
    }

    public void clearBasket() {
        items.clear();
    }

    public int quantityOf(UUID itemId){return items.get(itemId);}

    public boolean validateStore(UUID storeId) {
        return storeId.equals(this.storeId);
    }


}
