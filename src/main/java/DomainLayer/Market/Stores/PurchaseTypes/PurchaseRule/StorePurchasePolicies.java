package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class StorePurchasePolicies {
    /**
     * managePurchasePolicies is a class that manages the purchase policies of a shop.
     * It stores PurchaseTerm and provides methods for adding and removing them.
     */
    private ConcurrentLinkedQueue<PurchaseTerm> purchasePolicies;

    public StorePurchasePolicies(ConcurrentLinkedQueue<PurchaseTerm> purchasePolicies) {
        this.purchasePolicies = purchasePolicies;
    }

    public ConcurrentLinkedQueue<PurchaseTerm> getPurchasePolicies() {
        return purchasePolicies;
    }

/**
 * he addPurchaseTerm() method adds a PurchaseTerm to a list of purchase policies.
 * If the new term is a CompositePurchaseTerm, any existing terms in the list that
 * are equal to it are removed. and term added to the list.
 *  */
    public synchronized void addPurchaseTerm(PurchaseTerm term) throws Exception {
        if (term == null) {
            throw new Exception("the purchase Term is null, please put valid purchaseTerm");
        }
        if (purchasePolicies.contains(term))
            throw new Exception("the purchase Term is already exist, please put valid purchaseTerm");

        if (term instanceof CompositePurchaseTerm) {
            // If the new term is a CompositePurchaseTerm, remove any existing terms in the purchasePolicies
            // that are equal to it (using the equals() method)
            CompositePurchaseTerm compositeTerm = (CompositePurchaseTerm) term;
            purchasePolicies.removeIf(p -> p.equals(term));
            }
            // If the new term is not a CompositePurchaseTerm, simply add it to purchasePolicies
            purchasePolicies.add(term);
        }



    public synchronized void removePurchaseTerm(PurchaseTerm term) throws Exception {
        if (term == null) {
            throw new Exception("the purchase Term is null, please put valid purchaseTerm to remove");
        }
        // Check if the term exists in purchasePolicies
        boolean termExists = purchasePolicies.remove(term);
        // If the term was not found in purchasePolicies, log a message and return
        if (!termExists) {
            throw new Exception("the purchase Term is not exist in purchase Policies,  please put valid purchaseTerm to remove");
        }
    }

    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store){
        for (PurchaseTerm purchaseTerm: getPurchasePolicies()) {
            if(!purchaseTerm.purchaseRuleOccurs(shoppingBasket,store)){
                return  false;}}
            return  true;
        }



}





