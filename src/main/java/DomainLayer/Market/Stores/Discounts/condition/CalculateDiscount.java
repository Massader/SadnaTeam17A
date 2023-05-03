package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface CalculateDiscount {
    public Double CalculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage);

}
