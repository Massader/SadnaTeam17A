package DomainLayer.Market.Stores.PurchaseRule;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Transient;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CompositePurchaseTerm extends PurchaseTerm {

    @Transient
    private Collection<PurchaseTerm> purchaseTerms;

    public CompositePurchaseTerm(PurchaseRule purchaseRule, ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms) {
        super(purchaseRule);
        this.purchaseTerms = purchaseTerms;
    }

    public CompositePurchaseTerm() {
        super();
    }

    public void setPurchaseRule(PurchaseRule purchaseRule) {
        super.setPurchaseRule(purchaseRule);
    }

    public void setPurchaseTerms(ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms) {
        this.purchaseTerms = purchaseTerms;
    }

    public Collection<PurchaseTerm> getPurchaseTerms() {
        return purchaseTerms;
    }
}

