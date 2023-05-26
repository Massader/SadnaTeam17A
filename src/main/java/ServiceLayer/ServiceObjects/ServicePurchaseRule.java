package ServiceLayer.ServiceObjects;

public class ServicePurchaseRule {
    private String type;
    private String itemIdOrCategoryOrNull;
    
    public ServicePurchaseRule(String type, String itemIdOrCategoryOrNull) {
        this.type = type;
        this.itemIdOrCategoryOrNull = itemIdOrCategoryOrNull;
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
