package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.CartItem;
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
        double discount = 0;

        Map<UUID, CartItem> items = shoppingBasket.getItems();
        if (items.containsKey(getItemId())) {
            int quantity = items.get(itemId).getQuantity();
            discount = items.get(getItemId()).getPrice() * quantity * discountPercentage;
        }
        return discount;
    }

}
