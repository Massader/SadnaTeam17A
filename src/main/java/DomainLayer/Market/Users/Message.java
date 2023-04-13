package DomainLayer.Market.Users;

import java.util.UUID;

public class Message {

    private UUID messageId;
    private String body;
    private UUID sender;
    private UUID recipient;

    public Message(String body, UUID sender, UUID recipient){
        this.messageId = UUID.randomUUID();
        this.body = body;
        this.sender = sender;
        this.recipient = recipient;
    }

    public UUID getMessageId() {
        return messageId;
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
