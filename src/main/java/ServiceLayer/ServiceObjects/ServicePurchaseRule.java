package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.PurchaseRule.CategoryPurchaseRule;
import DomainLayer.Market.Stores.PurchaseRule.ItemPurchaseRule;
import DomainLayer.Market.Stores.PurchaseRule.PurchaseRule;

public class ServicePurchaseRule {
    private String type;
    private String itemIdOrCategoryOrNull;
    
    public ServicePurchaseRule(String type, String itemIdOrCategoryOrNull) {
        this.type = type;
        this.itemIdOrCategoryOrNull = itemIdOrCategoryOrNull;
    }
    
    public ServicePurchaseRule(PurchaseRule rule) {
        if (rule instanceof ItemPurchaseRule) {
            type = "ITEM";
            itemIdOrCategoryOrNull = ((ItemPurchaseRule) rule).getItemId().toString();
        }
        else if (rule instanceof CategoryPurchaseRule) {
            type = "CATEGORY";
            itemIdOrCategoryOrNull = ((CategoryPurchaseRule) rule).getCategory().getCategoryName();
        }
        else
            type = "BASKET";
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getItemIdOrCategoryOrNull() {
        return itemIdOrCategoryOrNull;
    }
    
    public void setItemIdOrCategoryOrNull(String itemIdOrCategoryOrNull) {
        this.itemIdOrCategoryOrNull = itemIdOrCategoryOrNull;
    }
}
