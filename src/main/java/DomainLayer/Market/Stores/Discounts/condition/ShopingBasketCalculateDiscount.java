package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ShopingBasketCalculateDiscount implements CalculateDiscount {

    public ShopingBasketCalculateDiscount() {
    }

    @Override
    public ConcurrentLinkedQueue<Double> CalculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage) {
        ConcurrentLinkedQueue<Double> DiscountOption= new ConcurrentLinkedQueue<>();
        Double price =store.calculatePriceOfBasket(shoppingBasket.getItems());
        DiscountOption.add(price*discountPercentage);
        return  DiscountOption;
    }
}
