package APILayer.Requests;

import java.util.UUID;

public class ItemPolicyTermRequest extends PolicyTermRequest {
    private UUID itemId;
    
    public ItemPolicyTermRequest(UUID clientCredentials, UUID storeId, boolean atLeast, int quantity, UUID itemId) {
        super(clientCredentials, storeId, atLeast, quantity);
        this.itemId = itemId;
    }
    
    public UUID getItemId() {
        return itemId;
    }
}
