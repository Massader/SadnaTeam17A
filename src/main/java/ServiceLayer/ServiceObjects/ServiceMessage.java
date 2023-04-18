package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Users.Message;

import java.util.UUID;

public class ServiceMessage {
    private UUID id;
    private String body;
    private UUID sender;
    private UUID recipient;

    public ServiceMessage(Message message){
        this.id = message.getId();
        this.body = message.getBody();
        this.sender = message.getSender();
        this.recipient = message.getRecipient();
    }

    public UUID getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getRecipient() {
        return recipient;
    }
}
