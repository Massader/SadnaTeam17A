package DomainLayer.Market;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Market_Notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;
    @Column
    private UUID recipient;
    @Column
    private String message;
    @Column
    private Date timestamp;

    public Notification(String message, UUID recipient) {
        this.message = message;
        this.recipient = recipient;
        this.timestamp = Date.from(Instant.now());
    }

    public Notification() {
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public UUID getId() {
        return id;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }

    public UUID getRecipient() {
        return recipient;
    }

    public void setRecipient(UUID recipient) {
        this.recipient = recipient;
    }
}
