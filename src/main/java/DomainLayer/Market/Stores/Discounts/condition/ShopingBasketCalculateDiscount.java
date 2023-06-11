package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ShopingBasketCalculateDiscount implements CalculateDiscount {

    public ShopingBasketCalculateDiscount() {
    }

    @Override
    public Double CalculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage) {
        if (discountPercentage>1||discountPercentage<=0){return 0.0;}
       // ConcurrentLinkedQueue<Double> DiscountOption= new ConcurrentLinkedQueue<>();
        Double price =store.calculatePriceOfBasket(shoppingBasket.getItems());
       // DiscountOption.add(price*discountPercentage);
        return  price*discountPercentage;
    }
}
