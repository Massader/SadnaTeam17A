package APILayer.Requests;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.PurchaseRule.*;

import java.util.UUID;

public class SimplePolicyRequest extends Request{

    enum policyType{
        ItemPurchaseRule,
        ShoppingBasketPurchaseRule,
        CategoryPurchaseRule
    }
    private policyType policyType;
    private UUID itemId;
    private Category category;
    private int quantity;
    private  Boolean atLeast;
    private UUID storeId;



//item
    public SimplePolicyRequest(UUID clientCredentials, SimplePolicyRequest.policyType policyType, UUID itemId, int quantity, Boolean atLeast, UUID storeId) {
        super(clientCredentials);
        this.policyType = policyType;
        this.itemId = itemId;
        this.quantity = quantity;
        this.atLeast = atLeast;
        this.storeId =storeId;
    }

//category
    public SimplePolicyRequest(UUID clientCredentials, SimplePolicyRequest.policyType policyType, Category category, int quantity, Boolean atLeast, UUID storeId) {
        super(clientCredentials);
        this.policyType = policyType;
        this.category = category;
        this.quantity = quantity;
        this.atLeast = atLeast;
        this.storeId = storeId;
    }

    //basket
    public SimplePolicyRequest(UUID clientCredentials, SimplePolicyRequest.policyType policyType, int quantity, Boolean atLeast, UUID storeId) {
        super(clientCredentials);
        this.policyType = policyType;
        this.quantity = quantity;
        this.atLeast = atLeast;
        this.storeId = storeId;
    }

    public Category getCategory() {
        return category;
    }
    
    public SimplePolicyRequest.policyType getPolicyType() {
        return policyType;
    }

    public UUID getItemId() {
        return itemId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Boolean getAtLeast() {
        return atLeast;
    }

    public PurchaseTerm getPurchaseTerm(){
        PurchaseRule purchaseRule;
        switch (policyType) {
            case ItemPurchaseRule:
                purchaseRule = new ItemPurchaseRule(getItemId());
            case ShoppingBasketPurchaseRule:
                purchaseRule = new ShoppingBasketPurchaseRule();
            case CategoryPurchaseRule:
                purchaseRule = new CategoryPurchaseRule(getCategory());
            default:
                purchaseRule = new ShoppingBasketPurchaseRule();

        }
            if(atLeast){return new AtLeastPurchaseTerm(purchaseRule,getQuantity());}
            else { return new AtMostPurchaseTerm(purchaseRule,getQuantity());}
                }
}

