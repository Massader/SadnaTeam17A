package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConditionalPurchaseTerm extends PurchaseTerm {

    private PurchaseTerm purchaseTermThen;
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

