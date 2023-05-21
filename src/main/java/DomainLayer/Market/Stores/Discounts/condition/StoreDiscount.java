package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.CompositePurchaseTerm;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

public class StoreDiscount {
    /**
     * ManageDiscount is a class that manages the discounts of a shop.
     * It stores discount and provides methods for adding and removing them.
     */
    private NumericalAssemblyOfDiscount discountsAssembly;

    public StoreDiscount(NumericalAssemblyOfDiscount discountsAssembly) {
        this.discountsAssembly = discountsAssembly;
    }
    public StoreDiscount(Boolean max){
        if(max){this.discountsAssembly= new MaxDiscounts();}
            else{this.discountsAssembly= new CombiningDiscounts();}}


    public NumericalAssemblyOfDiscount getDiscountsAssembly() {
        return discountsAssembly;
    }

    public synchronized void addDiscount(Discount discount) throws Exception {
        if (discount == null) {
            throw new Exception("the discount is null, please put valid discount");
        }
        if (discountsAssembly.getDiscounts().contains(discount))
            throw new Exception("the discount is already exist, please put valid discount");

        for (Discount dis:discountsAssembly.getDiscounts()) {
            if(dis.getOptioncalculateDiscount().equals(discount.getOptioncalculateDiscount()))
                throw new Exception("the discount is already exist, please put valid discount");
        }

        if (discount.getPurchaseTerm() instanceof CompositePurchaseTerm) {
            // If the new term is a CompositePurchaseTerm, remove any existing terms in the purchasePolicies
            // that are equal to it (using the equals() method)
            CompositePurchaseTerm compositeTerm = (CompositePurchaseTerm) discount.getPurchaseTerm();
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

    public synchronized double CalculateDiscount(ShoppingBasket shoppingBasket, Store store){
        return  this.discountsAssembly.CalculateDiscount(shoppingBasket,store);
    }

    public synchronized double CalculateShoppingBasket(ShoppingBasket shoppingBasket, Store store){
        double originalPrice= store.calculatePriceOfBasket(shoppingBasket.getItems());
        double discount = this.CalculateDiscount(shoppingBasket,store);
        return  Math.max(0.0, originalPrice-discount);
    }
    public void changeNumericalAssemblyOfDiscount(){
        if( getDiscountsAssembly() instanceof CombiningDiscounts){
            this.discountsAssembly =new MaxDiscounts(getDiscountsAssembly().getDiscounts());
        }
        else this.discountsAssembly= new CombiningDiscounts(getDiscountsAssembly().getDiscounts());
    }










}
