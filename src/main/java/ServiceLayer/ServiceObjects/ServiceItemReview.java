package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.ItemReview;

import java.util.Date;
import java.util.UUID;

public class ServiceItemReview {
    private UUID id;
    private UUID itemId;
    private String text;
    private UUID reviewer;
    private Date timestamp;
    private int rating;
    
    public ServiceItemReview(ItemReview itemReview) {
        this.id = itemReview.getId();
        this.itemId = itemReview.getItem().getId();
        this.text = itemReview.getText();
        this.reviewer = itemReview.getReviewer();
        this.timestamp = itemReview.getTimestamp();
        this.rating = itemReview.getRating();
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
    
    public int getRating() {
        return rating;
    }
}
