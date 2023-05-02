package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositePurchaseTermXor extends CompositePurchaseTerm {

    public CompositePurchaseTermXor(PurchaseRule PurchaseTerm) {
        super(PurchaseTerm);
    }


    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket) {
        int counter=0;
        for (PurchaseTerm purchaseTerm : this.getPurchaseTerm()) {
            if (purchaseTerm.purchaseRuleOccurs(shoppingBasket))
                counter++;
            if (counter>1)return  false;
        }
        if (counter==1)return  true;
        return false;
    }
}