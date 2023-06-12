package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface CalculateDiscount {

    public Double calculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage);
}
