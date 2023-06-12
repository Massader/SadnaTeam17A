package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Table(name = "Market_Stores_Discounts_CombiningDiscounts")
public class CombiningDiscounts extends NumericalAssemblyOfDiscount {

    public CombiningDiscounts(Collection<Discount> discounts) {
        super(discounts);
    }
    public CombiningDiscounts() {
        super();
    }

    @Override
    public Double calculateDiscount(ShoppingBasket shoppingBasket, Store store) {
        ConcurrentLinkedQueue<Double> discountOption = new ConcurrentLinkedQueue<>();
        for (Discount discount : getDiscounts()) {
            Double pricePerDiscount = discount.calculateDiscount(shoppingBasket, store);
            discountOption.add(pricePerDiscount);
        }
        Double sumDiscount = 0.0;
        for (Double discount : discountOption) {
            sumDiscount += discount;
        }
        return sumDiscount;
    }
}
