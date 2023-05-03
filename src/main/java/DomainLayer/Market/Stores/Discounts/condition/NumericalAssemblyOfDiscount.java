package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class NumericalAssemblyOfDiscount {
    ConcurrentLinkedQueue<Discount> discounts;

    public NumericalAssemblyOfDiscount(ConcurrentLinkedQueue<Discount> discounts) {
        this.discounts = discounts;
    }

    public ConcurrentLinkedQueue<Discount> getDiscounts() {
        return discounts;
    }

    public abstract Double CalculateDiscount(ShoppingBasket shoppingBasket, Store store);

}
