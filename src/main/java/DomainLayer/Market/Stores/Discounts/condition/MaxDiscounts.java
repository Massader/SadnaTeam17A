package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MaxDiscounts extends NumericalAssemblyOfDiscount {

    public MaxDiscounts(CalculateDiscount calculateDiscount) {
        super(calculateDiscount);
    }

    @Override
    public Double CalculateDiscount(ShoppingBasket shoppingBasket, Store store,Double discountPercentage) {
        ConcurrentLinkedQueue<Double> discountOption =getCalculateDiscount().CalculateDiscount(shoppingBasket,store, discountPercentage);
        double sumDiscount = 0.0;
        for (Double discount : discountOption) {
            sumDiscount += discount;
        }
        return  sumDiscount;
    }
}
