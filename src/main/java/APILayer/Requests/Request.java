package APILayer.Requests;

import java.util.UUID;

public class Request {
    private UUID clientCredentials;

    public Request(UUID clientCredentials) {
        this.clientCredentials = clientCredentials;
    }

    public UUID getClientCredentials() {
        return clientCredentials;
    }

    public void setClientCredentials(UUID clientCredentials) {
        this.clientCredentials = clientCredentials;
    }
}
