package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Table(name = "Market_Stores_PurchaseRule_ConditionalPurchaseTerm")
public class ConditionalPurchaseTerm extends PurchaseTerm {

    @Transient
    private PurchaseTerm purchaseTermThen;
    @Transient
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

