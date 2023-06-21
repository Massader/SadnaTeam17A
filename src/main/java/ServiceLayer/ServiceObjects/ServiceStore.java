package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ServiceStore {
    private String name;
    private UUID storeId;
    private String description;
    private double rating;
    private boolean isShutdown;
    private boolean isClosed;

    public ServiceStore(Store store) {
        this.name = store.getName();
        this.storeId = store.getStoreId();
        this.description = store.getDescription();
        this.rating = store.getRating();
        this.isClosed = store.isClosed();
        this.isShutdown = store.isShutdown();
    }
    public ServiceStore(){}

    @Autowired
    public ServiceStore(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    public boolean getIsClosed() {
        return isClosed;
    }
    
    public boolean getIsShutdown() {
        return isShutdown;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public void setShutdown(boolean shutdown) {
        isShutdown = shutdown;
    }
    
    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
