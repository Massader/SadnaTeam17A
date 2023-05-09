package DomainLayer.Market.Users;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Purchase {
    private UUID id;
    private UUID userId;
    private UUID itemId;
    private Date date;
    private int quantity;
    private boolean rated;
    private UUID storeId;


    public Purchase(UUID userId, UUID itemId, int quantity, UUID storeId) {
        this.id = UUID.randomUUID();
        this.date = Date.from(Instant.now());
        this.itemId = itemId;
        this.userId = userId;
        this.quantity = quantity;
        this.storeId = storeId;
        rated = false;
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

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }


    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
}
