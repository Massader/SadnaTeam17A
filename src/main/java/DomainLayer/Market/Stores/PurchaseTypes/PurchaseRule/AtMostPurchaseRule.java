package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;

import DomainLayer.Market.Users.ShoppingBasket;

public class AtMostPurchaseRule extends PurchaseTerm {


    private int quantity;

    public AtMostPurchaseRule(PurchaseRule purchaseRule, int quantity) {
        super(purchaseRule);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }



    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket) {
        return purchaseRule.purchaseRuleOccurs(shoppingBasket,this.quantity,false);
    }

}
