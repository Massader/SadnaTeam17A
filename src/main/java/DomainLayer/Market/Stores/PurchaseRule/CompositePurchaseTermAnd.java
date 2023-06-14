package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Table(name = "PurchaseRule_CompositePurchaseTermAnd")
public class CompositePurchaseTermAnd extends CompositePurchaseTerm {

    public CompositePurchaseTermAnd(PurchaseRule purchaseRule, ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms) {
        super(purchaseRule, purchaseTerms);
    }

    public CompositePurchaseTermAnd() {
        super();
    }


    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) {
        for (PurchaseTerm purchaseTerm : getPurchaseTerms()) {
            if (!purchaseTerm.purchaseRuleOccurs(shoppingBasket,store))
                return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CompositePurchaseTermAnd)) {
            return false;
        }
        CompositePurchaseTermAnd c = (CompositePurchaseTermAnd) o;
        return getPurchaseTerms().equals(c.getPurchaseTerms());
    }
}