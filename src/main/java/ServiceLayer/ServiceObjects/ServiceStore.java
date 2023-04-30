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

    public ServiceStore(Store store) {
        this.name = store.getName();
        this.storeId = store.getStoreId();
        this.description = store.getDescription();
        this.rating = store.getRating();
    }

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
}
