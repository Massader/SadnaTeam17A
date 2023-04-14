package DomainLayer.Market.Stores;

import DomainLayer.Market.Stores.PurchaseTypes.PurchaseType;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Item {
    private UUID id;
    private String name;
    private double price;
    private UUID storeId;
    private double rating;
    private int ratesCount;
    private int quantity;
    private  String description;
    private ConcurrentLinkedQueue<PurchaseType> purchaseTypes;
    private ConcurrentLinkedQueue<Category> categories;
    private PolicyRules policyRules;
    private ConcurrentHashMap<UUID, Review> reviews;


//constructor:




    public Item(UUID id, String name, double price, UUID storeId, double rating, int quantity, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.storeId = storeId;
        this.rating = rating;
        this.quantity = quantity;
        purchaseTypes = new ConcurrentLinkedQueue<>();
        categories = new ConcurrentLinkedQueue<>();
        policyRules = null;
        reviews = new ConcurrentHashMap<>();
    }

    //getters & setters :

    public Collection<Category> getCategories(){
        return categories;
    }
    public boolean containsCategory(String category){
        for (Category category1 : this.getCategories()){
            if (category1.getCategoryName().equals(category))
                    return true;
        }
        return false;
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
    public void setDescription(String description) {
        this.description = description;
    }

    //methods:


    public UUID addReview(UUID clientCredentials, String body){
        Review review = new Review(body, clientCredentials);
        reviews.put(review.getId(), review);
        return review.getId();
    }

    // make avarage of the old ratings with the new one
    public void addRating(int newRating){
        double x = rating * ratesCount;
        x += newRating;
        ratesCount++;
        rating = x/ratesCount;
    }
    public Boolean addQuantity(int quantityToAdd){
        this.quantity+=quantityToAdd;
        return  true;
    }

    public Boolean removeFromQuantity(int quantityToRemove) throws Exception {
        if(this.quantity<quantityToRemove){
            throw new Exception(" can't remove "+ quantityToRemove+ " items, the store have less");
        }
        else {this.quantity-=quantityToRemove;}
        return  true;
    }



}
