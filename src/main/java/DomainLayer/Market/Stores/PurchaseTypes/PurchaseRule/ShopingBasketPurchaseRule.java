package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ShopingBasketPurchaseRule implements PurchaseRule {
    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, int quantity, Boolean atList) {
            int price = 0;
            // ConcurrentHashMap<Item,Integer> items = shoppingBasket.getItems(); change
            ConcurrentHashMap<Item, Integer> items = new ConcurrentHashMap<>();
            for (Item item : items.keySet()) {
                price+=item.getPrice();
            }
            boolean moreThenQuantity = price >= quantity;
            return (quantity == price || atList && moreThenQuantity || (!atList && !moreThenQuantity));
        }


    @Override
    public Double getPrice(ShoppingBasket shoppingBasket, Store store) {
        return null;
    }
}
