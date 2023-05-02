package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class NumericalAssemblyOfDiscount {
    CalculateDiscount calculateDiscount;

    public NumericalAssemblyOfDiscount(CalculateDiscount calculateDiscount) {
        this.calculateDiscount = calculateDiscount;
    }

    public CalculateDiscount getCalculateDiscount() {
        return calculateDiscount;
    }

    public abstract Double CalculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage);

}
