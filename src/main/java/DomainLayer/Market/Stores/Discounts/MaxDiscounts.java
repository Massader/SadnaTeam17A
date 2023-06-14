package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.CartItem;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Table(name = "Market_Stores_Discounts_MaxDiscounts")
public class MaxDiscounts extends NumericalAssemblyOfDiscount {

    public MaxDiscounts(Collection<Discount> discounts) {
        super(discounts);
    }
    public MaxDiscounts() {
        super();
    }

    @Override
    public Double calculateDiscount(ShoppingBasket shoppingBasket, Store store) {
        ConcurrentLinkedQueue<Double> discountOption = new ConcurrentLinkedQueue<>();
        for (Discount discount : getDiscounts()) {
            Double pricePerDiscount = discount.calculateDiscount(shoppingBasket,store);
            discountOption.add(pricePerDiscount);
        }
        return discountOption.stream().max(Double::compare).orElse(0.0);
    }
    
    @Override
    public double calculateItemDiscount(ShoppingBasket shoppingBasket, Store store, UUID itemId) {
        double itemDiscount = 0;
        CartItem cartItem = shoppingBasket.getCartItem(itemId);
        Item item = cartItem.getItem();
        for (Discount discount: discounts) {
            if (discount.getPurchaseTerm() == null || discount.getPurchaseTerm().purchaseRuleOccurs(shoppingBasket, store)) {
                if (discount.getOptionCalculateDiscount() instanceof ItemCalculateDiscount &&
                ((ItemCalculateDiscount) discount.getOptionCalculateDiscount()).getItemId().equals(itemId)) {
                    itemDiscount = Math.max(itemDiscount, discount.getDiscountPercentage());
                }
                else if (discount.getOptionCalculateDiscount() instanceof CategoryCalculateDiscount &&
                        item.getCategories().contains(((CategoryCalculateDiscount) discount.getOptionCalculateDiscount()).getCategory())) {
                    itemDiscount = Math.max(itemDiscount, discount.getDiscountPercentage());
                }
                else if (discount.getOptionCalculateDiscount() instanceof ShoppingBasketCalculateDiscount)
                    itemDiscount = Math.max(itemDiscount, discount.getDiscountPercentage());
            }
        }
        return Math.min(itemDiscount, 1);
    }
}
