package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.CartItem;
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
        Map<UUID, CartItem> items = shoppingBasket.getItems();
        for (CartItem cartItem : items.values()) {
            if (cartItem!=null&&cartItem.getItem().containsCategory(category.getCategoryName())) {
                categoryQuantity += cartItem.getQuantity();
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
        return Objects.equals(this.category, other.category);
    }
}

