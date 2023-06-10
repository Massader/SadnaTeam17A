package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.CartItem;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingBasketPurchaseRule implements PurchaseRule {
    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store, int quantity, Boolean atLeast) {
            int price = 0;
            ConcurrentHashMap<UUID, Item> storeItems = store.getItems();
            ConcurrentHashMap<UUID, CartItem> items = shoppingBasket.getItems();
            for (CartItem cartItem : items.values()) {
                if(storeItems.containsKey(cartItem.getItemId())){
                    price+= cartItem.getPrice() * cartItem.getQuantity();
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
