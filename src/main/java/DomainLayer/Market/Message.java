package DomainLayer.Market;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Message {

    private UUID id;
    private String body;
    private UUID sender;
    private UUID recipient;
    private Date timestamp;

    public Message(String body, UUID sender, UUID recipient){
        this.id = UUID.randomUUID();
        this.body = body;
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = Date.from(Instant.now());
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
