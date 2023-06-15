package DomainLayer.Market.Stores.PurchaseRule;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CompositePurchaseTerm extends PurchaseTerm {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<PurchaseTerm> purchaseTerms;

    public CompositePurchaseTerm(PurchaseRule purchaseRule, ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms) {
        super(purchaseRule);
        this.purchaseTerms = purchaseTerms;
    }

    public CompositePurchaseTerm() {
        super();
    }

    public void setPurchaseTerms(ConcurrentLinkedQueue<PurchaseTerm> purchaseTerms) {
        this.purchaseTerms = purchaseTerms;
    }

    public Collection<PurchaseTerm> getPurchaseTerms() {
        return purchaseTerms;
    }
}

