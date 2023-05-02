package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositePurchaseTermOr extends CompositePurchaseTerm {


    public CompositePurchaseTermOr(PurchaseRule PurchaseTerm) {
        super(PurchaseTerm);
    }



    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket) {
        for(PurchaseTerm purchaseTerm:this.getPurchaseTerm() ){
            if(purchaseTerm.purchaseRuleOccurs(shoppingBasket))
                return true;
        }
        return  false;
    }

}
