package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ItemCalculateDiscount implements CalculateDiscount {

    private UUID itemId;

    public ItemCalculateDiscount( UUID itemId) {
        this.itemId = itemId;
    }

    public UUID getItemId() {
        return itemId;
    }




    @Override
    public Double CalculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage) {
      //  ConcurrentLinkedQueue<Double> discountOption = new ConcurrentLinkedQueue<>();
        double price = store.calculatePriceOfBasket(shoppingBasket.getItems());
        double discount = 0;
        ConcurrentHashMap<UUID, Item> storeItems = store.getItems();
        ConcurrentHashMap<UUID, Integer> items = shoppingBasket.getItems();
        if (items.containsKey(getItemId())) {
            int quantity = items.get(itemId);
            discount = storeItems.get(getItemId()).getPrice() * quantity * discountPercentage;
        }
       // discountOption.add(discount);
        return  discount;
    }

}
