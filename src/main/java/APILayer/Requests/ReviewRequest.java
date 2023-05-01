package APILayer.Requests;

import java.util.UUID;

public class ReviewRequest extends Request {

    private UUID itemId;
    private String body;

    public ReviewRequest(UUID clientCredentials, UUID itemId, String body) {
        super(clientCredentials);
        this.itemId = itemId;
        this.body = body;
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
}
