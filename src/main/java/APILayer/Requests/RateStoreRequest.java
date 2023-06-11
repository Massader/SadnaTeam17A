package APILayer.Requests;

import java.util.UUID;

public class RateStoreRequest extends Request {
    private UUID storeId;
    private int rating;
    
    public RateStoreRequest(UUID clientCredentials, UUID storeId, int rating) {
        super(clientCredentials);
        this.storeId = storeId;
        this.rating = rating;
    }
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
}
