package APILayer.Requests;

import java.util.UUID;

public class CartItemRequest extends Request {

    private UUID itemId;
    private UUID storeId;
    private int quantity;

    public CartItemRequest(UUID clientCredentials, UUID itemId, UUID storeId, int quantity) {
        super(clientCredentials);
        this.itemId = itemId;
        this.storeId = storeId;
        this.quantity = quantity;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
