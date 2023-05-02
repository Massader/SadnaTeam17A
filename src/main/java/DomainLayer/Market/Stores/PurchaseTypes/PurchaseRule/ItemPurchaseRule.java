package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;

public class ItemPurchaseRule implements PurchaseRule {
    UUID itemId;

    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, int quantity,Boolean atLis) {
        return null;
    }
}
