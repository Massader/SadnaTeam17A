package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Table(name = "PurchaseRule_StorePurchasePolicy")
public class StorePurchasePolicy {
    /**
     * managePurchasePolicies is a class that manages the purchase policies of a shop.
     * It stores PurchaseTerm and provides methods for adding and removing them.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Transient
    private Collection<PurchaseTerm> purchasePolicies;

    public StorePurchasePolicy(ConcurrentLinkedQueue<PurchaseTerm> purchasePolicies) {
        this.purchasePolicies = purchasePolicies;
    }
    public StorePurchasePolicy() {
        this.purchasePolicies = new ConcurrentLinkedQueue<>();
    }

    public Collection<PurchaseTerm> getPurchasePolicies() {
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

        if (term instanceof CompositePurchaseTerm compositeTerm) {
            // If the new term is a CompositePurchaseTerm, remove any existing terms in the purchasePolicies
            // that are equal to it (using the equals() method)
            purchasePolicies.removeIf(p -> p.equals(compositeTerm));
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
            if(!purchaseTerm.purchaseRuleOccurs(shoppingBasket, store)) {
                return false;
            }
        }
        return true;
    }
}





