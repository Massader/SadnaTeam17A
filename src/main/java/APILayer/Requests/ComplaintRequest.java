package APILayer.Requests;

import java.util.UUID;

public class ComplaintRequest extends Request {
    private UUID storeId;
    private UUID itemId;
    private UUID purchaseId;
    private String body;
    
    public ComplaintRequest(UUID clientCredentials, UUID storeId, UUID itemId, UUID purchaseId, String body) {
        super(clientCredentials);
        this.storeId = storeId;
        this.itemId = itemId;
        this.purchaseId = purchaseId;
        this.body = body;
    }
    public ComplaintRequest(){}
    
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
    
    public UUID getPurchaseId() {
        return purchaseId;
    }
    
    public void setPurchaseId(UUID purchaseId) {
        this.purchaseId = purchaseId;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
}
