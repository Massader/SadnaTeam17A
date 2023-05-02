package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;



import DomainLayer.Market.Users.ShoppingBasket;

public abstract class PurchaseTerm {

    PurchaseRule purchaseRule;

    public PurchaseTerm(PurchaseRule purchaseRule) {
        this.purchaseRule = purchaseRule;
    }

    public PurchaseRule getPurchaseRule() {
        return purchaseRule;
    }

    public abstract Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket) ;

}




