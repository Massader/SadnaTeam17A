package APILayer.Requests;

import java.util.UUID;

public class ReviewRequest extends Request {

    private UUID itemId;
    private String body;
    private int rating;

    public ReviewRequest(UUID clientCredentials, UUID itemId, String body, int rating) {
        super(clientCredentials);
        this.itemId = itemId;
        this.body = body;
        this.rating = rating;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
}
