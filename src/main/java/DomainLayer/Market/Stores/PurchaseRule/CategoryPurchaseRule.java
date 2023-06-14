package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "Market_Stores_PurchaseRule_CategoryPurchaseRule")
public class CategoryPurchaseRule implements PurchaseRule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Transient
    Category category;

    public CategoryPurchaseRule(Category category) {
        this.category = category;
    }

    public CategoryPurchaseRule() {

    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket,Store store, int quantity, Boolean atLeast) {
        int categoryQuantity = 0;
        Collection<Item> storeItems = store.getItems();
         ConcurrentHashMap<UUID,Integer> items = shoppingBasket.getItems();
        for (UUID itemId : items.keySet()) {
            Item item = store.getItem(itemId);
            if (item!=null&&item.containsCategory(category.getCategoryName())) {
                categoryQuantity+=items.get(itemId);
            }
        }
        boolean moreThenQuantity = categoryQuantity >= quantity;
        return (quantity == categoryQuantity || atLeast && moreThenQuantity || (!atLeast && !moreThenQuantity));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CategoryPurchaseRule other = (CategoryPurchaseRule) obj;
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        return true;
    }



}

