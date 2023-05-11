package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CategoryCalculateDiscount implements CalculateDiscount {
    Category category;

    public CategoryCalculateDiscount(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public Double CalculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage) {
        if (discountPercentage>1||discountPercentage<=0){return 0.0;}
    //    ConcurrentLinkedQueue<Double> DiscountOption= new ConcurrentLinkedQueue<>();
        Double categoryDiscount = 0.0;
        ConcurrentHashMap<UUID, Item> storeItems = store.getItems();
        ConcurrentHashMap<UUID,Integer> items = shoppingBasket.getItems();
        for (UUID itemId : items.keySet()) {
            Item item=  storeItems.get(itemId);
            if (item!=null&&item.getCategories().equals(getCategory())) {
                int quantity = items.get(itemId);
                categoryDiscount+=item.getPrice()*quantity*discountPercentage;
            }
        }
      //  DiscountOption.add(categoryDiscount);

        return categoryDiscount;
    }


}
