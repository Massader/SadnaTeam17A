package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

public class AtMostPurchaseTerm extends PurchaseTerm {


    private int quantity;

    public AtMostPurchaseTerm(PurchaseRule purchaseRule, int quantity) {
        super(purchaseRule);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }



    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) {
        return purchaseRule.purchaseRuleOccurs(shoppingBasket,store,this.quantity,false);
    }

}
