package APILayer.Requests;

import java.util.UUID;

public class ItemRatingRequest extends Request {
    private int rating;
    private UUID itemId;
    private UUID storeId;

    public ItemRatingRequest(UUID clientCredentials, int rating, UUID itemId, UUID storeId) {
        super(clientCredentials);
        this.rating = rating;
        this.itemId = itemId;
        this.storeId = storeId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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
}
