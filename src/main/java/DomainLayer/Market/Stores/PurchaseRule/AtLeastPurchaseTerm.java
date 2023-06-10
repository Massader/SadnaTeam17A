package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import ServiceLayer.ServiceObjects.ServicePurchaseTerm;

import java.util.UUID;

public class AtLeastPurchaseTerm extends PurchaseTerm {


    private int quantity;

    public AtLeastPurchaseTerm(PurchaseRule purchaseRule, int quantity) {
        super(purchaseRule);
        this.quantity = quantity;
    }

    public AtLeastPurchaseTerm(ServicePurchaseTerm serviceTerm) {
        super(serviceTerm);
        this.quantity = serviceTerm.getQuantity();
    }
    
    public int getQuantity() {
        return quantity;
    }

    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) {
        return purchaseRule.purchaseRuleOccurs(shoppingBasket, store, quantity, true);
    }



}
