package DomainLayer.Market.Stores;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class StoreReview {
    private UUID id;
    private UUID storeId;
    private String text;
    private UUID reviewer;
    private Date timestamp;
    private int rating;

    public StoreReview(UUID storeId, String text, UUID reviewer, int rating) {
        this.id = UUID.randomUUID();
        this.storeId = storeId;
        this.text = text;
        this.reviewer = reviewer;
        this.timestamp = Date.from(Instant.now());
        this.rating = rating;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getReviewer() {
        return reviewer;
    }

    public void setReviewer(UUID reviewer) {
        this.reviewer = reviewer;
    }
    
    public UUID getStoreId() {
        return storeId;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
}
