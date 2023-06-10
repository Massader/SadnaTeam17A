package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.CartItem;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ItemPurchaseRule implements PurchaseRule {
    UUID itemId;

    public ItemPurchaseRule(UUID itemId) {
        this.itemId = itemId;
    }

    public UUID getItemId() {
        return itemId;
    }

    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket,Store store, int quantity,Boolean atLeast) {
        ConcurrentHashMap<UUID, CartItem> items = shoppingBasket.getItems();
        int basketQuantity=0;
        if(items.containsKey(getItemId())){
            basketQuantity = items.get(getItemId()).getQuantity();
        }
        boolean moreThenQuantity = basketQuantity >= quantity;
        return (quantity == basketQuantity || atLeast&&moreThenQuantity || (!atLeast && !moreThenQuantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPurchaseRule that = (ItemPurchaseRule) o;
        return Objects.equals(itemId, that.itemId);
    }

}
