package APILayer.Requests;

import java.util.UUID;

public class RemovePolicyTermRequest extends Request {
    
    private UUID storeId;
    private UUID itemId;
    private String categoryName;
    
    public RemovePolicyTermRequest(UUID clientCredentials, UUID storeId, UUID itemId, String categoryName) {
        super(clientCredentials);
        this.storeId = storeId;
        this.itemId = itemId;
        this.categoryName = categoryName;
    }
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
    
    public UUID getItemId() {
        return itemId;
    }
    
    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
