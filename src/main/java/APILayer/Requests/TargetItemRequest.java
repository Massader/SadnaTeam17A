package APILayer.Requests;

import java.util.UUID;

public class TargetItemRequest extends Request {

    private UUID storeId;
    private UUID itemId;

    public TargetItemRequest(UUID clientCredentials, UUID storeId, UUID itemId) {
        super(clientCredentials);
        this.storeId = storeId;
        this.itemId = itemId;
    }
    public TargetItemRequest(){}

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
}
