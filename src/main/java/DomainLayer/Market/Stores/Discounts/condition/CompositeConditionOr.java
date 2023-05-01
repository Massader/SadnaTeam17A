package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositeConditionOr extends CompositeCondition {
    private ConcurrentLinkedQueue<Condition> conditions;
    @Override
    public Boolean conditionOccurs(ShoppingBasket shoppingBasket, Store store) {
        for (Condition con:conditions) {
            if(con.conditionOccurs(shoppingBasket,store)){
                return true;}
        }
        return  false;
    }
}

