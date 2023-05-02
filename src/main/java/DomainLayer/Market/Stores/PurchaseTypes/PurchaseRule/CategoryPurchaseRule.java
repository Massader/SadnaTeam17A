package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryPurchaseRule implements PurchaseRule {
    Category category;

    public CategoryPurchaseRule(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket,Store store, int quantity, Boolean atList) {
        int categoryQuantity = 0;
        ConcurrentHashMap<UUID,Item> storeItems = store.getItems();
         ConcurrentHashMap<UUID,Integer> items = shoppingBasket.getItems();
        for (UUID itemId : items.keySet()) {
            Item item=  storeItems.get(itemId);
            if (item!=null&&item.getCategories().equals(getCategory())) {
                categoryQuantity+=items.get(itemId);
            }
        }
        boolean moreThenQuantity = categoryQuantity >= quantity;
        return (quantity == categoryQuantity || atList && moreThenQuantity || (!atList && !moreThenQuantity));
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

