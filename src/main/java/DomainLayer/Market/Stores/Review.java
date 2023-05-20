package DomainLayer.Market.Stores;

import java.util.Date;
import java.time.Instant;
import java.util.UUID;

public class Review {
    private UUID id;
    private UUID itemId;
    private String text;
    private UUID reviewer;
    private Date timestamp;
    private int rating;

    public Review(UUID itemId, String text, UUID reviewer, int rating) {
        this.id = UUID.randomUUID();
        this.itemId = itemId;
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
    
    public UUID getItemId() {
        return itemId;
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
