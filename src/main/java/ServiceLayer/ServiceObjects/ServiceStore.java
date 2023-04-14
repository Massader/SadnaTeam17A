package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.Store;

import java.util.UUID;

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
}
