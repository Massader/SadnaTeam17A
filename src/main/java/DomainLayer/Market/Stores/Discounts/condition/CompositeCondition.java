package DomainLayer.Market.Stores.Discounts.condition;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class CompositeCondition implements Condition{
    private ConcurrentLinkedQueue<Condition> conditions;


}
