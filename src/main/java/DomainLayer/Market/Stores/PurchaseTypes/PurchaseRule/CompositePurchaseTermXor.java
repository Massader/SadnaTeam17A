package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CompositePurchaseTermXor extends CompositePurchaseTerm {
    private ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms;

    public CompositePurchaseTermXor(PurchaseRule PurchaseTerm, ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms) {
        super(PurchaseTerm);
        purchaseTerms = purchaseTerms;
    }

    public ConcurrentLinkedQueue<PurchaseTerm> getPurchaseTerms() {
        return purchaseTerms;
    }

    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket) {
        int counter=0;
        for (PurchaseTerm purchaseTerm : getPurchaseTerms()) {
            if (purchaseTerm.purchaseRuleOccurs(shoppingBasket))
                counter++;
            if (counter>1)return  false;
        }
        if (counter==1)return  true;
        return false;
    }
}