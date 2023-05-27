package DomainLayer.Market.Stores.PurchaseRule;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class CompositePurchaseTerm extends PurchaseTerm {

    private ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms;

    public CompositePurchaseTerm(PurchaseRule purchaseRule, ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms) {
        super(purchaseRule);
        this.purchaseTerms = purchaseTerms;
    }


    public ConcurrentLinkedQueue<PurchaseTerm> getPurchaseTerms() {
        return purchaseTerms;
    }
    
    public void setPurchaseTerms(ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms) {
        this.purchaseTerms = purchaseTerms;
    }
}

