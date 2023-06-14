package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "Market_Stores_Discounts_ItemCalculateDiscount")
public class ItemCalculateDiscount implements CalculateDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "itemId", nullable = false, unique = true)
    private UUID itemId;

    public ItemCalculateDiscount(UUID itemId) {
        this.itemId = itemId;
    }

    public ItemCalculateDiscount() {

    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }
    
    @Override
    public Double calculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage) {
        if (discountPercentage > 1 || discountPercentage <= 0)
            return 0.0;
      //  ConcurrentLinkedQueue<Double> discountOption = new ConcurrentLinkedQueue<>();
        double price = store.calculatePriceOfBasket(shoppingBasket.getItems());
        double discount = 0;
        Collection<Item> storeItems = store.getItems();
        Map<UUID, Integer> items = shoppingBasket.getItems();
        if (items.containsKey(getItemId())) {
            int quantity = items.get(itemId);
            discount = store.getItem(itemId).getPrice() * quantity * discountPercentage;
        }
       // discountOption.add(discount);
        return discount;
    }

}
