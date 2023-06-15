package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class NumericalAssemblyOfDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "numerical_assembly_of_discount_id")
    protected Collection<Discount> discounts;

    public NumericalAssemblyOfDiscount(Collection<Discount> discounts) {
        this.discounts = discounts;
    }

    public NumericalAssemblyOfDiscount(){
        this.discounts= new ConcurrentLinkedQueue<>();
    }

    public Collection<Discount> getDiscounts() {
        return discounts;
    }

    public abstract Double calculateDiscount(ShoppingBasket shoppingBasket, Store store);
    
    public abstract double calculateItemDiscount(ShoppingBasket shoppingBasket, Store store, UUID itemId);

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setDiscounts(Collection<Discount> discounts) {
        this.discounts = discounts;
    }
}
