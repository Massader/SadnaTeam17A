package DomainLayer.Market.Stores.Discounts;

import DomainLayer.Market.Stores.PurchaseRule.CompositePurchaseTerm;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.*;

@Entity
@Table(name = "Market_Stores_Discounts_StoreDiscounts")
public class StoreDiscount {
    /**
     * ManageDiscount is a class that manages the discounts of a shop.
     * It stores discount and provides methods for adding and removing them.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Transient
    private NumericalAssemblyOfDiscount discountsAssembly;

    public StoreDiscount(NumericalAssemblyOfDiscount discountsAssembly) {
        this.discountsAssembly = discountsAssembly;
    }
    public StoreDiscount(Boolean max){
        if(max){this.discountsAssembly= new MaxDiscounts();}
            else{this.discountsAssembly= new CombiningDiscounts();}}

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

    public synchronized void removeDiscount(Discount discount) throws Exception {
        if (discount == null) {
            throw new Exception("the discount Term is null, please put valid discount to remove");
        }
        // Check if the discount exists in discounts
        boolean discountExists = discountsAssembly.getDiscounts().remove(discount);
        // If the term was not found in purchasePolicies, log a message and return
        if (!discountExists) {
            throw new Exception("the discount Term is not exist in discounts  please put valid discount to remove");
        }
    }

    public synchronized double calculateDiscount(ShoppingBasket shoppingBasket, Store store){
        return this.discountsAssembly.calculateDiscount(shoppingBasket,store);
    }

    public synchronized double calculateShoppingBasket(ShoppingBasket shoppingBasket, Store store){
        double originalPrice= store.calculatePriceOfBasket(shoppingBasket.getItems());
        double discount = this.calculateDiscount(shoppingBasket,store);
        return  Math.max(0.0, originalPrice - discount);
    }
    public void changeNumericalAssemblyOfDiscount(){
        if( getDiscountsAssembly() instanceof CombiningDiscounts){
            this.discountsAssembly = new MaxDiscounts(getDiscountsAssembly().getDiscounts());
        }
        else this.discountsAssembly = new CombiningDiscounts(getDiscountsAssembly().getDiscounts());
    }










}
