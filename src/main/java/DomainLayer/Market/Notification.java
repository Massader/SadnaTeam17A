package DomainLayer.Market;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Notification {
    private String message;
    private UUID id;
    private Date timestamp;

    public Notification(String message) {
        this.message = message;
        this.id = UUID.randomUUID();
        this.timestamp = Date.from(Instant.now());
    }

    public String getMessage() {
        return message;
    }

    public UUID getId() {
        return id;
    }
}
