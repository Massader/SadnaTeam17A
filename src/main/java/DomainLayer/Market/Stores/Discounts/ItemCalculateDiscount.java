package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ItemCalculateDiscount implements CalculateDiscount {

    private UUID itemId;

    public ItemCalculateDiscount( UUID itemId) {
        this.itemId = itemId;
    }

    public UUID getItemId() {
        return itemId;
    }
    
    @Override
    public Double calculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage) {
        if (discountPercentage > 1 || discountPercentage <= 0)
            return 0.0;
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
        return discount;
    }

}