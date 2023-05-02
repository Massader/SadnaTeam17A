package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Users.ShoppingBasket;

public class CategoryPurchaseRule implements PurchaseRule {
    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, int quantity, Boolean atList) {
        return null;
    }
}
