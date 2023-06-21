package APILayer.Requests;

import java.util.UUID;

public class RemoveDiscountRequest extends Request {
    private UUID storeId;
    private UUID discountId;
    
    public RemoveDiscountRequest(UUID clientCredentials, UUID storeId, UUID discountId) {
        super(clientCredentials);
        this.storeId = storeId;
        this.discountId = discountId;
    }
    public RemoveDiscountRequest(){}
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
    
    public UUID getDiscountId() {
        return discountId;
    }
    
    public void setDiscountId(UUID discountId) {
        this.discountId = discountId;
    }
}
