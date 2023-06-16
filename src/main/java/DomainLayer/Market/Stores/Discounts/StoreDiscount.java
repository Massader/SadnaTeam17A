package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.PurchaseRule.CompositePurchaseTerm;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.CartItem;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Discounts_StoreDiscount")
public class StoreDiscount {
    /**
     * ManageDiscount is a class that manages the discounts of a shop.
     * It stores discount and provides methods for adding and removing them.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private NumericalAssemblyOfDiscount discountsAssembly;

    public StoreDiscount(NumericalAssemblyOfDiscount discountsAssembly) {
        this.discountsAssembly = discountsAssembly;
    }
    public StoreDiscount(Boolean max) {
        if(max) {
            this.discountsAssembly = new MaxDiscounts();
        } else {
            this.discountsAssembly = new CombiningDiscounts();
        }
    }

    public StoreDiscount() {

    }

    public NumericalAssemblyOfDiscount getDiscountsAssembly() {
        return discountsAssembly;
    }

    public synchronized void addDiscount(Discount discount) throws Exception {
        if (discount == null) {
            throw new Exception("the discount is null");
        }
        if (discountsAssembly.getDiscounts().contains(discount))
            throw new Exception("the discount already exists");
        for (Discount dis : discountsAssembly.getDiscounts()) {
            if(dis.getOptionCalculateDiscount().equals(discount.getOptionCalculateDiscount()))
                throw new Exception("the discount already exists");
        }
        if (discount.getPurchaseTerm() instanceof CompositePurchaseTerm) {
            // If the new term is a CompositePurchaseTerm, remove any existing terms in the purchasePolicies
            // that are equal to it (using the equals() method)
            discountsAssembly.getDiscounts().removeIf(p -> p.getPurchaseTerm().equals(discount.getPurchaseTerm()));
        }
        // If the new term is not a CompositePurchaseTerm, simply add it to purchasePolicies
        this.discountsAssembly.getDiscounts().add(discount);
    }

    public synchronized void removeDiscount(UUID discountId) throws Exception {
        if (discountId == null) {
            throw new Exception("Passed discount id is null.");
        }
        List<Discount> discounts = discountsAssembly.getDiscounts().stream().filter(discount -> discount.getId().equals(discountId)).toList();
        // If the term was not found in purchasePolicies, log a message and return
        if (discounts.isEmpty()) {
            throw new Exception("the discount does not exist.");
        }
        discountsAssembly.getDiscounts().remove(discounts.get(0));
    }

    public synchronized double calculateDiscount(ShoppingBasket shoppingBasket, Store store){
        return this.discountsAssembly.calculateDiscount(shoppingBasket,store);
    }

    public synchronized double calculateShoppingBasket(ShoppingBasket shoppingBasket, Store store){
        double originalPrice = store.calculatePriceOfBasket(shoppingBasket.getItems());
        double discount = this.calculateDiscount(shoppingBasket,store);
        return  Math.max(0.0, originalPrice - discount);
    }
    public void changeNumericalAssemblyOfDiscount(){
        if( getDiscountsAssembly() instanceof CombiningDiscounts){
            this.discountsAssembly = new MaxDiscounts(getDiscountsAssembly().getDiscounts());
        }
        else this.discountsAssembly = new CombiningDiscounts(getDiscountsAssembly().getDiscounts());
    }
    
    public double calculateItemDiscount(ShoppingBasket shoppingBasket, Store store, UUID itemId) {
        return this.discountsAssembly.calculateItemDiscount(shoppingBasket, store, itemId);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
