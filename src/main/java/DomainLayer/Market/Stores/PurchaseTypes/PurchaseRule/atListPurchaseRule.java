package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Users.ShoppingBasket;

public class atListPurchaseRule extends PurchaseTerm {


    private int quantity;

    public atListPurchaseRule(PurchaseRule purchaseRule, int quantity) {
        super(purchaseRule);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }



    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket) {
        return purchaseRule.purchaseRuleOccurs(shoppingBasket,quantity,true);
    }


}
