package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositeCalculateDiscountXor extends CompositeCalculateDiscount {
    private ConcurrentLinkedQueue<CalculateDiscount> calculateDiscounts;
    @Override
    public Boolean conditionOccurs(ShoppingBasket shoppingBasket, Store store) {
        int counter=0;
        for (CalculateDiscount con: calculateDiscounts) {
            if(con.conditionOccurs(shoppingBasket,store)){
                counter++;
                if(counter>1){return false;}
            }
        }
        if(counter==1){return true;}
        return  false;
    }

}
