package APILayer.Requests;

import java.util.UUID;

public class ReviewRequest extends Request {

    private UUID targetId;
    private String body;
    private int rating;

    public ReviewRequest(UUID clientCredentials, UUID itemId, String body, int rating) {
        super(clientCredentials);
        this.targetId = itemId;
        this.body = body;
        this.rating = rating;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public void setTargetId(UUID targetId) {
        this.targetId = targetId;
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
