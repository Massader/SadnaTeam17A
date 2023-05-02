package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ShopingBasketPurchaseRule implements PurchaseRule {
    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket,Store store, int quantity, Boolean atList) {
            int price = 0;
            ConcurrentHashMap<UUID,Item> storeItems = store.getItems();
            ConcurrentHashMap<UUID,Integer> items = shoppingBasket.getItems();
            for (UUID itemID : items.keySet()) {
                if(storeItems.containsKey(itemID)){
                 price+=storeItems.get(itemID).getPrice();}//TODO: check to option that we dont have item in store?
            }
            boolean moreThenQuantity = price >= quantity;
            return (quantity == price || atList && moreThenQuantity || (!atList && !moreThenQuantity));
        }

    public boolean equals(Object obj) {
        // Use default instanceof
            if(obj instanceof ShopingBasketPurchaseRule)
                return true;
            return false;
        }



}
