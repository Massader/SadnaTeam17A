package APILayer.Requests;

import java.util.UUID;

public class MessageRequest extends Request{
    private java.util.UUID clientCredentials;
    private UUID sender;
    private UUID recipient;
    private String body;

    public MessageRequest(UUID clientCredentials, UUID clientCredentials1, UUID sender, UUID recipient, String body) {
        super(clientCredentials);
        this.clientCredentials = clientCredentials1;
        this.sender = sender;
        this.recipient = recipient;
        this.body = body;
    }

    @Override
    public UUID getClientCredentials() {
        return clientCredentials;
    }

    @Override
    public void setClientCredentials(UUID clientCredentials) {
        this.clientCredentials = clientCredentials;
    }

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public UUID getRecipient() {
        return recipient;
    }

    public void setRecipient(UUID recipient) {
        this.recipient = recipient;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
