package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MaxDiscounts extends NumericalAssemblyOfDiscount {

    public MaxDiscounts(ConcurrentLinkedQueue<Discount> discounts) {
        super(discounts);
    }
    public MaxDiscounts() {
        super();
    }

    @Override
    public Double CalculateDiscount(ShoppingBasket shoppingBasket, Store store) {
        ConcurrentLinkedQueue<Double> discountOption = new ConcurrentLinkedQueue<>();
        for ( Discount discount:getDiscounts()) {
            Double pricePerDiscount = discount.CalculateDiscount(shoppingBasket,store);
            discountOption.add(pricePerDiscount);
        }
        Double maxDiscount = discountOption.stream().max(Double::compare).orElse(0.0);
        return  maxDiscount;
    }
}
