package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PurchaseTermConditioning extends PurchaseTerm {

    private ConcurrentLinkedQueue<PurchaseTerm> purchaseTermThen;
    private ConcurrentLinkedQueue<PurchaseTerm> purchaseTermIf;

    public PurchaseTermConditioning(PurchaseRule purchaseRule) {
        super(purchaseRule);
    }

    public PurchaseTermConditioning(PurchaseRule purchaseRule, ConcurrentLinkedQueue<PurchaseTerm> purchaseTermThen, ConcurrentLinkedQueue<PurchaseTerm> purchaseTermIf) {
        super(purchaseRule);
        this.purchaseTermThen = purchaseTermThen;
        this.purchaseTermIf = purchaseTermIf;
    }

    @Override
    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) {
        boolean toCheckResult = false;
        if (purchaseTermIf != null && !purchaseTermThen.isEmpty()) {return true;}
            for (PurchaseTerm purchaseTerm : purchaseTermThen) {
                if (purchaseTerm.purchaseRuleOccurs(shoppingBasket, store)) {
                    toCheckResult = true;
                    break;
                }
            }

        if (!toCheckResult) {
            return true;
        }
        if (purchaseTermIf != null && !purchaseTermIf.isEmpty()) {
            for (PurchaseTerm purchaseTerm : purchaseTermIf) {
                if (!purchaseTerm.purchaseRuleOccurs(shoppingBasket, store)) {
                    return false;
                }
            }
        }
        return true;
    }

}

