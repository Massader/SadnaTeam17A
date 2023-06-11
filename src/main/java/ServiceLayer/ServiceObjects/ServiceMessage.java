package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Message;

import java.util.Date;
import java.util.UUID;

public class ServiceMessage {
    private UUID id;
    private String body;
    private UUID sender;
    private UUID recipient;
    private Date timestamp;

    public ServiceMessage(Message message){
        this.id = message.getId();
        this.body = message.getBody();
        this.sender = message.getSender();
        this.recipient = message.getRecipient();
        this.timestamp = message.getTimestamp();
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
    
    public Date getTimestamp() {
        return timestamp;
    }
}
