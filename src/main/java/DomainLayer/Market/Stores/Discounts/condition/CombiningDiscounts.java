package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CombiningDiscounts extends NumericalAssemblyOfDiscount {

    public CombiningDiscounts(CalculateDiscount calculateDiscount) {
        super(calculateDiscount);
    }

    @Override
    public Double CalculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage) {
        ConcurrentLinkedQueue<Double> discountOption =getCalculateDiscount().CalculateDiscount(shoppingBasket,store, discountPercentage);
        Double maxDiscount = discountOption.stream().max(Double::compare).orElse(0.0);
        return  maxDiscount;
    }
}
