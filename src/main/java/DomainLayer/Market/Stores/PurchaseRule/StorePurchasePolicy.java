package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StorePurchasePolicy {
    /**
     * managePurchasePolicies is a class that manages the purchase policies of a shop.
     * It stores PurchaseTerm and provides methods for adding and removing them.
     */
    private ConcurrentLinkedQueue<PurchaseTerm> purchasePolicies;

    public StorePurchasePolicy(ConcurrentLinkedQueue<PurchaseTerm> purchasePolicies) {
        this.purchasePolicies = purchasePolicies;
    }
    public StorePurchasePolicy() {
        this.purchasePolicies = new ConcurrentLinkedQueue<>();
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
        if (term.getPurchaseRule() instanceof ShoppingBasketPurchaseRule) {
            for (PurchaseTerm purchaseTerm : purchasePolicies) {
                if (purchaseTerm.getPurchaseRule() instanceof ShoppingBasketPurchaseRule)
                    throw new Exception("Only one shopping basket purchase term can exist at one time.");
            }
        }
    
        if (term.getPurchaseRule() instanceof CategoryPurchaseRule) {
            for (PurchaseTerm purchaseTerm : purchasePolicies) {
                if (purchaseTerm.getPurchaseRule() instanceof CategoryPurchaseRule &&
                ((CategoryPurchaseRule) purchaseTerm.getPurchaseRule()).getCategory().getCategoryName()
                        .equals(((CategoryPurchaseRule) term.getPurchaseRule()).getCategory().getCategoryName()))
                    throw new Exception("Only one purchase term per category can exist at one time.");
            }
        }
    
        if (term.getPurchaseRule() instanceof ItemPurchaseRule) {
            for (PurchaseTerm purchaseTerm : purchasePolicies) {
                if (purchaseTerm.getPurchaseRule() instanceof ItemPurchaseRule &&
                ((ItemPurchaseRule) purchaseTerm.getPurchaseRule()).getItemId()
                        .equals(((ItemPurchaseRule) term.getPurchaseRule()).getItemId()))
                    throw new Exception("Only one purchase term per item can exist at one time.");
            }
        }
        purchasePolicies.add(term);
    }



    public synchronized void removePurchaseTerm(UUID termId) throws Exception {
        if (termId == null) {
            throw new Exception("Failed to remove purchase term - term id was null");
        }
        boolean deleted = false;
        for (PurchaseTerm term : purchasePolicies) {
            if (term.getTermId().equals(termId)) {
                purchasePolicies.remove(term);
                deleted = true;
                break;
            }
        }
        if (!deleted)
            throw new Exception("Failed to remove purchase term - term not found");
    }

    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store){
        for (PurchaseTerm purchaseTerm: getPurchasePolicies()) {
            if(!purchaseTerm.purchaseRuleOccurs(shoppingBasket,store)){
                return  false;}}
            return  true;
        }



}





