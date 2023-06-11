package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConditionalPurchaseTerm extends PurchaseTerm {

    private ConcurrentLinkedQueue<PurchaseTerm> purchaseTermsThen;
    private ConcurrentLinkedQueue<PurchaseTerm> purchaseTermsIf;

    public ConditionalPurchaseTerm(PurchaseRule purchaseRule) {
        super(purchaseRule);
    }
    
    public ConditionalPurchaseTerm(PurchaseRule purchaseRule, List<PurchaseTerm> purchaseTermsIf,
                                   List<PurchaseTerm> purchaseTermsThen) {
        super(purchaseRule);
        this.purchaseTermsIf = new ConcurrentLinkedQueue<>(purchaseTermsIf);
        this.purchaseTermsThen = new ConcurrentLinkedQueue<>(purchaseTermsThen);
    }

    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) {
        boolean toCheckResult = false;
        if (purchaseTermsIf != null && !purchaseTermsThen.isEmpty()) {return true;}
            for (PurchaseTerm purchaseTerm : purchaseTermsThen) {
                if (purchaseTerm.purchaseRuleOccurs(shoppingBasket, store)) {
                    toCheckResult = true;
                    break;
                }
            }

        if (!toCheckResult) {
            return true;
        }
        if (purchaseTermsIf != null && !purchaseTermsIf.isEmpty()) {
            for (PurchaseTerm purchaseTerm : purchaseTermsIf) {
                if (!purchaseTerm.purchaseRuleOccurs(shoppingBasket, store)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public ConcurrentLinkedQueue<PurchaseTerm> getPurchaseTermsThen() {
        return purchaseTermsThen;
    }
    
    public ConcurrentLinkedQueue<PurchaseTerm> getPurchaseTermsIf() {
        return purchaseTermsIf;
    }
    
    public void setPurchaseTermsThen(ConcurrentLinkedQueue<PurchaseTerm> purchaseTermsThen) {
        this.purchaseTermsThen = purchaseTermsThen;
    }
    
    public void setPurchaseTermsIf(ConcurrentLinkedQueue<PurchaseTerm> purchaseTermsIf) {
        this.purchaseTermsIf = purchaseTermsIf;
    }
}

