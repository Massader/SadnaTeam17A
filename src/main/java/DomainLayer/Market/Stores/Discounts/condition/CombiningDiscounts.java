package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CombiningDiscounts extends NumericalAssemblyOfDiscount {

    public CombiningDiscounts(ConcurrentLinkedQueue<Discount> discounts) {
        super(discounts);
    }

    @Override
    public Double CalculateDiscount(ShoppingBasket shoppingBasket, Store store) {
        ConcurrentLinkedQueue<Double> discountOption = new ConcurrentLinkedQueue<>();
        for ( Discount discount:getDiscounts()) {
            Double pricePerDiscount = discount.CalculateDiscount(shoppingBasket,store);
            discountOption.add(pricePerDiscount);
        }
        Double sumDiscount=0.0;
        for (Double discount : discountOption) {
            sumDiscount += discount;
        }
        return  sumDiscount;
            }
}
