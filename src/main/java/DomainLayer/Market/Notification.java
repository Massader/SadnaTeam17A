package DomainLayer.Market;

import java.util.UUID;

public class Notification {
    private String message;
    private UUID id;

    public Notification(String message) {
        this.message = message;
        this.id = UUID.randomUUID();
    }

    public String getMessage() {
        return message;
    }

    public UUID getId() {
        return id;
    }
}
