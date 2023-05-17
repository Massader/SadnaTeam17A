package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

public class AtLeastPurchaseRule extends PurchaseTerm {


    private int quantity;

    public AtLeastPurchaseRule(PurchaseRule purchaseRule, int quantity) {
        super(purchaseRule);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }



    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) {
        return purchaseRule.purchaseRuleOccurs(shoppingBasket,store,quantity,true);
    }



}
