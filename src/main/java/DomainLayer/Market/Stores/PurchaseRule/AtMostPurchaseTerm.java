package DomainLayer.Market.Stores.PurchaseRule;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import ServiceLayer.ServiceObjects.ServicePurchaseTerm;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.UUID;

@Entity
@Table(name = "PurchaseRule_AtMostPurchaseTerm")
public class AtMostPurchaseTerm extends PurchaseTerm {


    @Transient
    private int quantity;

    public AtMostPurchaseTerm(PurchaseRule purchaseRule, int quantity) {
        super(purchaseRule);
        this.quantity = quantity;
    }

    public AtMostPurchaseTerm() {
        super();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPurchaseRule(PurchaseRule purchaseRule) {
        super.setPurchaseRule(purchaseRule);
    }
    
    public AtMostPurchaseTerm(ServicePurchaseTerm serviceTerm) {
        super(serviceTerm);
        this.quantity = serviceTerm.getQuantity();
    }

    public int getQuantity() {
        return quantity;
    }



    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) {
        return purchaseRule.purchaseRuleOccurs(shoppingBasket,store,this.quantity,false);
    }
}
