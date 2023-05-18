package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class CompositePurchaseTerm extends PurchaseTerm {

    private ConcurrentLinkedQueue<PurchaseTerm> purchaseTerm;

    public CompositePurchaseTerm(PurchaseRule purchaseRule, ConcurrentLinkedQueue<PurchaseTerm> purchaseTerm) {
        super(purchaseRule);
        this.purchaseTerm = purchaseTerm;
    }
    //     public CompositePurchaseTerm(PurchaseRule purchaseRule) {
//        super(purchaseRule);
//    }

    public ConcurrentLinkedQueue<PurchaseTerm> getPurchaseTerm() {
        return purchaseTerm;
    }

}

