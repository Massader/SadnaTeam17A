package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class NumericalAssemblyOfDiscount {
    ConcurrentLinkedQueue<Discount> discounts;

    public NumericalAssemblyOfDiscount(ConcurrentLinkedQueue<Discount> discounts) {
        this.discounts = discounts;
    }

    public NumericalAssemblyOfDiscount(){
        this.discounts= new ConcurrentLinkedQueue<>();
    }

    public ConcurrentLinkedQueue<Discount> getDiscounts() {
        return discounts;
    }

    public abstract Double calculateDiscount(ShoppingBasket shoppingBasket, Store store);
    
    public abstract double calculateItemDiscount(ShoppingBasket shoppingBasket, Store store, UUID itemId);
}
