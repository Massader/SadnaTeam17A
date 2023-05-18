package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositePurchaseTermXor extends CompositePurchaseTerm {

    public CompositePurchaseTermXor(PurchaseRule purchaseRule) {
        super(purchaseRule, new ConcurrentLinkedQueue<>());
    }


    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) {
        int counter=0;
        for (PurchaseTerm purchaseTerm : this.getPurchaseTerm()) {
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
        return getPurchaseTerm().equals(c.getPurchaseTerm());
    }
}