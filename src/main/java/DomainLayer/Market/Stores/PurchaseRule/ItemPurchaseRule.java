package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "Market_Stores_PurchaseRule_ItemPurchaseRule")
public class ItemPurchaseRule implements PurchaseRule {

    @Transient
    UUID itemId;

    public ItemPurchaseRule(UUID itemId) {
        this.itemId = itemId;
    }

    public ItemPurchaseRule() {

    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public UUID getItemId() {
        return itemId;
    }

    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket,Store store, int quantity,Boolean atLeast) {
        ConcurrentHashMap<UUID,Integer> items = shoppingBasket.getItems();
        int basketQuantity=0;
        if(items.containsKey(getItemId())){
            basketQuantity= items.get(getItemId());}
        boolean moreThenQuantity = basketQuantity>=quantity;
        return  (quantity==basketQuantity||atLeast&&moreThenQuantity||(!atLeast&&!moreThenQuantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPurchaseRule that = (ItemPurchaseRule) o;
        return Objects.equals(itemId, that.itemId);
    }

}
