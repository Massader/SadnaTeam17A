package DomainLayer.Market.Stores.Discounts.condition;

import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.CompositePurchaseTerm;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.PurchaseTerm;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ManageDiscount {
    /**
     * ManageDiscount is a class that manages the discounts of a shop.
     * It stores discount and provides methods for adding and removing them.
     */
    private ConcurrentLinkedQueue<Discount> discounts;

    public ManageDiscount(ConcurrentLinkedQueue<Discount> discounts) {
        this.discounts = discounts;
    }

    public ConcurrentLinkedQueue<Discount> getDiscounts() {
        return discounts;
    }

    public synchronized void addDiscount(Discount discount) throws Exception {
        if (discount == null) {
            throw new Exception("the discount is null, please put valid discount");
        }
        if (discounts.contains(discount))
            throw new Exception("the discount is already exist, please put valid discount");

        if (discount.getPurchaseTerm() instanceof CompositePurchaseTerm) {
            // If the new term is a CompositePurchaseTerm, remove any existing terms in the purchasePolicies
            // that are equal to it (using the equals() method)
            CompositePurchaseTerm compositeTerm = (CompositePurchaseTerm) discount.getPurchaseTerm();
            discounts.removeIf(p -> p.getPurchaseTerm().equals(discount.getPurchaseTerm()));
        }
        // If the new term is not a CompositePurchaseTerm, simply add it to purchasePolicies
        discounts.add(discount);
    }

    public synchronized void removeDiscount(Discount discount) throws Exception {
        if (discount == null) {
            throw new Exception("the discount Term is null, please put valid discount to remove");
        }
        // Check if the discount exists in discounts
        boolean discountExists = discounts.remove(discount);
        // If the term was not found in purchasePolicies, log a message and return
        if (!discountExists) {
            throw new Exception("the discount Term is not exist in discounts  please put valid discount to remove");
        }
    }

   // public double CalculateDiscount()







}
