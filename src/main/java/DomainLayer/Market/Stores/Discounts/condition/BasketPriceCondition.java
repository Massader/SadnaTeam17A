package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

public class BasketPriceCondition  implements  Condition{
    private Double priceCondition;
    @Override
    public Boolean conditionOccurs(ShoppingBasket shoppingBasket, Store store) {
        if(store.calculatePriceOfBasket(shoppingBasket.getItems())>priceCondition)
            return  true;
        return  false;
    }


}
