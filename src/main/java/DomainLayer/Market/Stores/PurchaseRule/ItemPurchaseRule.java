package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.CartItem;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "PurchaseRule_ItemPurchaseRule")
public class ItemPurchaseRule implements PurchaseRule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

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
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store, int quantity, Boolean atLeast) {
        Collection<CartItem> items = shoppingBasket.getItems();
        int basketQuantity = 0;
        if (items.stream().noneMatch(cartItem -> cartItem.getItemId().equals(itemId)))
            return true;
        basketQuantity = shoppingBasket.getCartItem(itemId).getQuantity();
        boolean moreThenQuantity = basketQuantity >= quantity;
        return (quantity == basketQuantity || (atLeast && moreThenQuantity) || (!atLeast && !moreThenQuantity));

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPurchaseRule that = (ItemPurchaseRule) o;
        return Objects.equals(itemId, that.itemId);
    }
}
