package DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule;



import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingBasket;

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
    }






