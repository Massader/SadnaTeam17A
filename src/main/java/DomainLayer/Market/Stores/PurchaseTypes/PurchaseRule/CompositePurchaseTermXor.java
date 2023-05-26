package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositePurchaseTermXor extends CompositePurchaseTerm {
    
    public CompositePurchaseTermXor(PurchaseRule purchaseRule, ConcurrentLinkedQueue<PurchaseTerm> purchaseTerm) {
        super(purchaseRule, purchaseTerm);
    }


    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) {
        int counter=0;
        for (PurchaseTerm purchaseTerm : this.getPurchaseTerms()) {
            if (purchaseTerm.purchaseRuleOccurs(shoppingBasket,store))
                counter++;
            if (counter>1)return  false;
        }
        if (counter==1)return  true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CompositePurchaseTermXor)) {
            return false;
        }
        CompositePurchaseTermXor c = (CompositePurchaseTermXor) o;
        return getPurchaseTerms().equals(c.getPurchaseTerms());
    }
}