package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.*;

import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Table(name = "PurchaseRule_ConditionalPurchaseTerm")
public class ConditionalPurchaseTerm extends PurchaseTerm {

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PurchaseTerm purchaseTermThen;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private PurchaseTerm purchaseTermIf;

    public ConditionalPurchaseTerm(PurchaseRule purchaseRule) {
        super(purchaseRule);
    }

    public ConditionalPurchaseTerm(PurchaseRule purchaseRule, PurchaseTerm purchaseTermIf,
                                   PurchaseTerm purchaseTermThen) {
        super(purchaseRule);
        this.purchaseTermIf = purchaseTermIf;
        this.purchaseTermThen = purchaseTermThen;
    }

    public ConditionalPurchaseTerm() {
        super();
    }

    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) {
        if (purchaseTermIf != null && purchaseTermIf.purchaseRuleOccurs(shoppingBasket, store) && purchaseTermThen != null)
            return purchaseTermThen.purchaseRuleOccurs(shoppingBasket, store);
        else if (purchaseTermIf != null && !purchaseTermIf.purchaseRuleOccurs(shoppingBasket, store))
            return true;
        return false;
    }
    
    public PurchaseTerm getPurchaseTermThen() {
        return purchaseTermThen;
    }
    
    public PurchaseTerm getPurchaseTermIf() {
        return purchaseTermIf;
    }
    
    public void setPurchaseTermThen(PurchaseTerm purchaseTermThen) {
        this.purchaseTermThen = purchaseTermThen;
    }
    
    public void setPurchaseTermIf(PurchaseTerm purchaseTermIf) {
        this.purchaseTermIf = purchaseTermIf;
    }
}

