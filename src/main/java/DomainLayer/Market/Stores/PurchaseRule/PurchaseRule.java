package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.*;

import java.util.UUID;
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PurchaseRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    public abstract Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket,Store store, int quantity, Boolean atLeast);

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
