package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.Review;

import java.util.Date;
import java.util.UUID;

public class ServiceReview {
    private UUID id;
    private UUID itemId;
    private String text;
    private UUID reviewer;
    private Date timestamp;
    
    public ServiceReview(Review review) {
        this.id = review.getId();
        this.itemId = review.getItemId();
        this.text = review.getText();
        this.reviewer = review.getReviewer();
        this.timestamp = review.getTimestamp();
    }
    
    public UUID getId() {
        return id;
    }
    
    public String getText() {
        return text;
    }
    
    public UUID getReviewer() {
        return reviewer;
    }
    
    public UUID getItemId() {
        return itemId;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
}
