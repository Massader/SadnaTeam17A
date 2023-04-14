package ServiceLayer.ServiceObjects;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServiceItem {
    private UUID id;
    private String name;
    private double price;
    private UUID storeId;
    private double rating;
    private int quantity;
    private String description;
    private List<String> categories;
    private String purchaseType;

    public ServiceItem(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.storeId = item.getStoreId();
        this.rating = item.getRating();
        this.quantity = item.getQuantity();
        this.description = item.getDescription();
        this.categories = new ArrayList<>();
        for (Category category : item.getCategories()) {
            categories.add(category.getCategoryName());
        }
        this.purchaseType = item.getPurchaseType().getClass().getName();
    }
}
