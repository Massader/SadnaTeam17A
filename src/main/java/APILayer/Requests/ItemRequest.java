package APILayer.Requests;

import ServiceLayer.ServiceObjects.ServiceItem;

import java.util.UUID;

public class ItemRequest extends Request {

    private UUID id;
    private String name;
    private double price;
    private UUID storeId;
    private double rating;
    private int quantity;
    private String description;


    public ItemRequest(UUID clientCredentials, ServiceItem item) {
        super(clientCredentials);
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.storeId = item.getStoreId();
        this.rating = item.getRating();
        this.quantity = item.getQuantity();
        this.description = item.getDescription();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
