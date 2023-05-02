package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositePurchaseTermOr extends CompositePurchaseTerm {
    private ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms;

    public CompositePurchaseTermOr(PurchaseRule PurchaseTerm, ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms) {
        super(PurchaseTerm);
        purchaseTerms = purchaseTerms;
    }

    public ConcurrentLinkedQueue<PurchaseTerm> getPurchaseTerms() {
        return purchaseTerms;
    }

    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket) {
        for(PurchaseTerm purchaseTerm:getPurchaseTerms() ){
            if(purchaseTerm.purchaseRuleOccurs(shoppingBasket))
                return true;
        }
        return  false;
    }

}
