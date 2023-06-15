package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.*;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CalculateDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    public void setId(UUID id) {
        this.id = id;
    }

    public abstract Double calculateDiscount(ShoppingBasket shoppingBasket, Store store, Double discountPercentage);
}
