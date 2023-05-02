package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositePurchaseTermAnd extends CompositePurchaseTerm {


    public CompositePurchaseTermAnd(PurchaseRule PurchaseTerm) {
        super(PurchaseTerm);
    }


    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket) {
        for (PurchaseTerm purchaseTerm : getPurchaseTerm()) {
            if (!purchaseTerm.purchaseRuleOccurs(shoppingBasket))
                return false;
        }
        return false;
    }
}