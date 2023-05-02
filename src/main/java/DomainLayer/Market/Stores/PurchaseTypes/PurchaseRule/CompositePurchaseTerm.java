package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class CompositePurchaseTerm extends PurchaseTerm {

    private ConcurrentLinkedQueue<PurchaseTerm> purchaseTerm;

    public CompositePurchaseTerm(PurchaseRule PurchaseTerm) {
        super(PurchaseTerm);
    }

    public ConcurrentLinkedQueue<PurchaseTerm> getPurchaseTerm() {
        return purchaseTerm;
    }

}

