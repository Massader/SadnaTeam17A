package DomainLayer.Market.Stores.PurchaseRule;

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
@Table(name = "PurchaseRule_ShoppingBasketPurchaseRule")
public class ShoppingBasketPurchaseRule extends PurchaseRule {

    public ShoppingBasketPurchaseRule() {
        super();
    }

    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store, int quantity, Boolean atLeast) {
            int price = 0;
            Collection<Item> storeItems = store.getItems();
            Collection<CartItem> cartItems = shoppingBasket.getItems();
            for (CartItem cartItem : cartItems) {
                if (storeItems.stream().anyMatch(item -> item.getId().equals(cartItem.getItemId()))) {
                    price += cartItem.getPrice() * cartItem.getQuantity();
                }
            }
            boolean moreThenQuantity = price >= quantity;
            return (quantity == price || atLeast && moreThenQuantity || (!atLeast && !moreThenQuantity));
        }

    public boolean equals(Object obj) {
        // Use default instanceof
        if(obj instanceof ShoppingBasketPurchaseRule)
            return true;
        return false;
    }
}
