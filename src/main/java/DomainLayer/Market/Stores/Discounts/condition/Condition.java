package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

public interface Condition {
    public Boolean conditionOccurs(ShoppingBasket shoppingBasket, Store store);
}
