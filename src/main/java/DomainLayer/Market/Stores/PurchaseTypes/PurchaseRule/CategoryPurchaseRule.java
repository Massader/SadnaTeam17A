package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

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
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, int quantity, Boolean atList) {
        int categoryQuantity = 0;
        // ConcurrentHashMap<Item,Integer> items = shoppingBasket.getItems(); change
        ConcurrentHashMap<Item, Integer> items = new ConcurrentHashMap<>();
        for (Item item : items.keySet()) {
            if (item.getCategories().equals(getCategory())) {
                categoryQuantity++;
            }
        }
        boolean moreThenQuantity = categoryQuantity >= quantity;
        return (quantity == categoryQuantity || atList && moreThenQuantity || (!atList && !moreThenQuantity));
    }

    @Override
    public Double getPrice(ShoppingBasket shoppingBasket, Store store) {
        return null;
    }
}

