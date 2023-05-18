package APILayer.Requests;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.*;

import java.util.UUID;

public class SimplePolicy extends Request{

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
    private UUID StoreId;



//item
    public SimplePolicy(UUID clientCredentials, SimplePolicy.policyType policyType, UUID itemId, int quantity, Boolean atLeast,UUID storeId) {
        super(clientCredentials);
        this.policyType = policyType;
        this.itemId = itemId;
        this.quantity = quantity;
        this.atLeast = atLeast;
        this.StoreId=storeId;
    }

//category
    public SimplePolicy(UUID clientCredentials, SimplePolicy.policyType policyType, Category category, int quantity, Boolean atLeast,UUID storeId) {
        super(clientCredentials);
        this.policyType = policyType;
        this.category = category;
        this.quantity = quantity;
        this.atLeast = atLeast;
        this.StoreId=storeId;
    }

    //basket
    public SimplePolicy(UUID clientCredentials, SimplePolicy.policyType policyType, int quantity, Boolean atLeast,UUID storeId) {
        super(clientCredentials);
        this.policyType = policyType;
        this.quantity = quantity;
        this.atLeast = atLeast;
        this.StoreId=storeId;
    }

    public Category getCategory() {
        return category;
    }
    public SimplePolicy.policyType getPolicyType() {
        return policyType;
    }

    public UUID getItemId() {
        return itemId;
    }

    public UUID getStoreId() {
        return StoreId;
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
                purchaseRule = new ShopingBasketPurchaseRule();
            case CategoryPurchaseRule:
                purchaseRule = new CategoryPurchaseRule(getCategory());
            default:
                purchaseRule = new ShopingBasketPurchaseRule();

        }
            if(atLeast){return new AtLeastPurchaseRule(purchaseRule,getQuantity());}
            else { return new AtMostPurchaseRule(purchaseRule,getQuantity());}
                }
}

