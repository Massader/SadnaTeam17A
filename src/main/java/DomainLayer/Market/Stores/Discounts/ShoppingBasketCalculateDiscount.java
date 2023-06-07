package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

public class ShoppingBasketCalculateDiscount implements CalculateDiscount {

    public ShoppingBasketCalculateDiscount() {
    }

    @Override
    public Double calculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage) {
        if (discountPercentage > 1 || discountPercentage <= 0)
            return 0.0;
       // ConcurrentLinkedQueue<Double> DiscountOption= new ConcurrentLinkedQueue<>();
        Double price = store.calculatePriceOfBasket(shoppingBasket.getItems());
       // DiscountOption.add(price*discountPercentage);
        return price * discountPercentage;
    }
}
