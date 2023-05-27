package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositePurchaseTermAnd extends CompositePurchaseTerm {



    public CompositePurchaseTermAnd(PurchaseRule purchaseRule, ConcurrentLinkedQueue<PurchaseTerm> purchaseTerm) {
        super(purchaseRule, purchaseTerm);
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