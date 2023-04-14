package DomainLayer.Market.Stores;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Sale {
    private UUID id;
    private UUID userId;
    private UUID itemId;
    private Date date;
    private int quantity;


    public Sale(UUID userId, UUID itemId, int quantity) {
        this.id = UUID.randomUUID();
        this.date = Date.from(Instant.now());
        this.itemId = itemId;
        this.userId = userId;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getItemId() {
        return itemId;
    }

    public Date getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }
}
