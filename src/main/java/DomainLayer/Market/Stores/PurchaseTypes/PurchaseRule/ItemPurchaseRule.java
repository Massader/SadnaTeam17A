package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

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
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, int quantity,Boolean atList) {
        ConcurrentHashMap<UUID,Integer> items = shoppingBasket.getItems();
        int basketQuantity= items.get(getItemId());
        boolean moreThenQuantity = basketQuantity>=quantity;
        return  (quantity==basketQuantity||atList&&moreThenQuantity||(!atList&&!moreThenQuantity));
    }

    @Override
    public Double getPrice(ShoppingBasket shoppingBasket, Store store) {
        return null;
    }
}
