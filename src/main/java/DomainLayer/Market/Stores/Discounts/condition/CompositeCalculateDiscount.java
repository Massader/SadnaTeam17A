package DomainLayer.Market.Stores.Discounts.condition;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class CompositeCalculateDiscount  extends  NumericalAssemblyOfDiscount {
    private ConcurrentLinkedQueue<NumericalAssemblyOfDiscount> AssemblyOfDiscount;


    public  CompositeCalculateDiscount(CalculateDiscount calculateDiscount) {
        super(calculateDiscount);
    }
}
