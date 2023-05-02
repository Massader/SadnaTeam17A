package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositeCalculateDiscountOr extends CompositeCalculateDiscount {
    private ConcurrentLinkedQueue<CalculateDiscount> calculateDiscounts;
    @Override
    public Boolean conditionOccurs(ShoppingBasket shoppingBasket, Store store) {
        for (CalculateDiscount con: calculateDiscounts) {
            if(con.conditionOccurs(shoppingBasket,store)){
                return true;}
        }
        return  false;
    }
}

