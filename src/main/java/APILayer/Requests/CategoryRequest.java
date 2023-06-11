package APILayer.Requests;

import java.util.UUID;

public class CategoryRequest extends Request{
    private UUID storeId;
    private UUID itemId;
    private String category;

    public CategoryRequest(UUID clientCredentials, UUID storeId, UUID itemId, String category) {
        super(clientCredentials);
        this.storeId = storeId;
        this.itemId = itemId;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
