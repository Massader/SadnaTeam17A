package APILayer.Requests;

import java.util.UUID;

public class ChangeItemPurchaseTypeRequest extends Request {
    private UUID storeId;
    private UUID itemId;
    private String purchaseType;
    
    public ChangeItemPurchaseTypeRequest(UUID clientCredentials, UUID storeId, UUID itemId, String purchaseType) {
        super(clientCredentials);
        this.storeId = storeId;
        this.itemId = itemId;
        this.purchaseType = purchaseType;
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
    
    public String getPurchaseType() {
        return purchaseType;
    }
    
    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }
}
