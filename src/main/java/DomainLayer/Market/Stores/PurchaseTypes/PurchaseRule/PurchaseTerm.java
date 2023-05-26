package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;



import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.util.UUID;

public abstract class PurchaseTerm {

    PurchaseRule purchaseRule;

    public PurchaseTerm(PurchaseRule purchaseRule) {
        this.purchaseRule = purchaseRule;
    }

    public PurchaseRule getPurchaseRule() {
        return purchaseRule;
    }

    public abstract Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket, Store store) ;


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            // same object
            return true;
        }
        if (obj == null) {
            // object is null
            return false;
        }
        if (getClass() != obj.getClass()) {
            // object is not of the same class
            return false;
        }
        PurchaseTerm other = (PurchaseTerm) obj;
        if (purchaseRule == null) {
            // this object's purchaseRule is null
            if (other.purchaseRule != null) {
                return false;
            }
        } else if (!purchaseRule.equals(other.purchaseRule)) {
            // this object's purchaseRule is not equal to other's purchaseRule
            return false;
        }
        return true;
    }


    public PurchaseTerm creatingPurchaseTerm(int rule, Boolean atLeast, int quantity, UUID itemId, String category) throws Exception {
        PurchaseRule purchaseRule;
        switch (rule){
            case 1://Item
                if(itemId==null){ throw new Exception("can't Creating Purchase Term of Item Purchase Rule if item id is null");}
                purchaseRule = new ItemPurchaseRule(itemId);
                break;
            case  2://ShopingBasket
                purchaseRule = new ShoppingBasketPurchaseRule();
                break;
            case  3://category
                if(category.length()==0){ throw new Exception("can't Creating Purchase Term of Item Purchase Rule if category is empty");}
                purchaseRule = new CategoryPurchaseRule(new Category(category));
            default:
            { throw new Exception("can't Creating Purchase Term which is not a shopping basket item or category");}
        }
        if (atLeast){
            return new AtLeastPurchaseTerm(purchaseRule,quantity);
        }
        else return new AtMostPurchaseTerm(purchaseRule,quantity);

    }
    }






