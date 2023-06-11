package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositePurchaseTermOr extends CompositePurchaseTerm {


    public CompositePurchaseTermOr(PurchaseRule purchaseRule, ConcurrentLinkedQueue<PurchaseTerm> purchaseTerm) {
        super(purchaseRule, purchaseTerm);
    }

    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) {
        for(PurchaseTerm purchaseTerm:this.getPurchaseTerms() ){
            if(purchaseTerm.purchaseRuleOccurs(shoppingBasket,store))
                return true;
        }
        return  false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CompositePurchaseTermOr)) {
            return false;
        }
        CompositePurchaseTermOr c = (CompositePurchaseTermOr) o;
        return getPurchaseTerms().equals(c.getPurchaseTerms());
    }
}


