package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class quantityItemCondition implements Condition{
    private int quantity;
    private UUID itemId;

    public quantityItemCondition(int quantity, UUID itemId) {
        this.quantity = quantity;
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public UUID getItemId() {
        return itemId;
    }


    @Override
    public Boolean conditionOccurs(ShoppingBasket shoppingBasket, Store store) {
        ConcurrentHashMap<UUID,Integer> items = shoppingBasket.getItems();
        if(items.containsKey(itemId)&&items.get(itemId)>=quantity)
            return true;
        return  false;
    }
}
