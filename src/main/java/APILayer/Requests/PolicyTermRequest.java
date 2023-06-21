package APILayer.Requests;

import java.util.UUID;

public class PolicyTermRequest extends Request {
    private UUID storeId;
    private boolean atLeast;
    private int quantity;
    
    public PolicyTermRequest(UUID clientCredentials, UUID storeId, boolean atLeast, int quantity) {
        super(clientCredentials);
        this.storeId = storeId;
        this.atLeast = atLeast;
        this.quantity = quantity;
    }
    public PolicyTermRequest(){}
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public boolean getAtLeast() {
        return atLeast;
    }
    
    public int getQuantity() {
        return quantity;
    }
}
