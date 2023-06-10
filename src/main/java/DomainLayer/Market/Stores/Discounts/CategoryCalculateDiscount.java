package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.CartItem;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryCalculateDiscount implements CalculateDiscount {
    Category category;

    public CategoryCalculateDiscount(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public Double calculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage) {
        if (discountPercentage > 1 || discountPercentage <= 0){
            return 0.0;
        }
        double categoryDiscount = 0.0;
        ConcurrentHashMap<UUID, CartItem> items = shoppingBasket.getItems();
        for (CartItem cartItem : items.values()) {
            if (cartItem != null && cartItem.getItem() != null && cartItem.getItem().containsCategory(category.getCategoryName())) {
                int quantity = cartItem.getQuantity();
                categoryDiscount += cartItem.getPrice() * quantity * discountPercentage;
            }
        }
        return categoryDiscount;
    }


}
