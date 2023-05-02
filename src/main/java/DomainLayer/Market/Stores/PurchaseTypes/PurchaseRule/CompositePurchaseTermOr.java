package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositePurchaseTermOr extends CompositePurchaseTerm {


    public CompositePurchaseTermOr(PurchaseRule PurchaseTerm) {
        super(PurchaseTerm);
    }



    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) {
        for(PurchaseTerm purchaseTerm:this.getPurchaseTerm() ){
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
        return getPurchaseTerm().equals(c.getPurchaseTerm());
    }
}


