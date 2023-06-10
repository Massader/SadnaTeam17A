package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.CartItem;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CombiningDiscounts extends NumericalAssemblyOfDiscount {

    public CombiningDiscounts(ConcurrentLinkedQueue<Discount> discounts) {
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
    
    @Override
    public double calculateItemDiscount(ShoppingBasket shoppingBasket, Store store, UUID itemId) {
        double itemDiscount = 0;
        CartItem cartItem = shoppingBasket.getItems().get(itemId);
        Item item = cartItem.getItem();
        for (Discount discount: discounts) {
            if (discount.getPurchaseTerm() == null || discount.getPurchaseTerm().purchaseRuleOccurs(shoppingBasket, store)) {
                if (discount.getOptionCalculateDiscount() instanceof ItemCalculateDiscount &&
                        ((ItemCalculateDiscount) discount.getOptionCalculateDiscount()).getItemId().equals(itemId)) {
                    itemDiscount += discount.getDiscountPercentage();
                }
                else if (discount.getOptionCalculateDiscount() instanceof CategoryCalculateDiscount &&
                        item.getCategories().contains(((CategoryCalculateDiscount) discount.getOptionCalculateDiscount()).getCategory())) {
                    itemDiscount += discount.getDiscountPercentage();
                }
                else if (discount.getOptionCalculateDiscount() instanceof ShoppingBasketCalculateDiscount)
                    itemDiscount += discount.getDiscountPercentage();
            }
        }
        return Math.min(100, itemDiscount);
    }
}
