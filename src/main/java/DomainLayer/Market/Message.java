package DomainLayer.Market;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Market_Message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;
    @Column
    private String body;
    @Column
    private UUID sender;
    @Column
    private UUID recipient;
    @Column
    private Date timestamp;

    public Message(String body, UUID sender, UUID recipient){
        this.id = UUID.randomUUID();
        this.body = body;
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = Date.from(Instant.now());
    }

    public Message() {

    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public void setRecipient(UUID recipient) {
        this.recipient = recipient;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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
