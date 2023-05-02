package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

public interface PurchaseRule {
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, int quantity, Boolean atList);
    public Double getPrice(ShoppingBasket shoppingBasket, Store store);

}
