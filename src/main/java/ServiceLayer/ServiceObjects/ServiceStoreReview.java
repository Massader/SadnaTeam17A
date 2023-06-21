package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.StoreReview;

import java.util.Date;
import java.util.UUID;

public class ServiceStoreReview {
    private UUID id;
    private UUID storeId;
    private String text;
    private UUID reviewer;
    private Date timestamp;
    private int rating;
    
    public ServiceStoreReview(StoreReview storeReview) {
        this.id = storeReview.getId();
        this.storeId = storeReview.getStoreId();
        this.text = storeReview.getText();
        this.reviewer = storeReview.getReviewer();
        this.timestamp = storeReview.getTimestamp();
        this.rating = storeReview.getRating();
    }
    public ServiceStoreReview(){}
    
    public UUID getId() {
        return id;
    }
    
    public String getText() {
        return text;
    }
    
    public UUID getReviewer() {
        return reviewer;
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
}
