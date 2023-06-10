package DomainLayer.Market.Stores;

import DomainLayer.Market.Stores.PurchaseTypes.Bid;
import DomainLayer.Market.Stores.PurchaseTypes.BidPurchase;
import DomainLayer.Market.Stores.PurchaseTypes.DirectPurchase;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseType;
import jakarta.persistence.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
@Entity
@Table(name = "Items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ItemId", nullable = false, unique = true)
    private UUID id;
    @Column
    private String name;
    @Column
    private double price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column
    private double rating;
    @Column
    private int ratesCount;
    @Column
    private int quantity;
    @Column
    private String description;
    @OneToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)// cascade
    private PurchaseType purchaseType;


    @Transient
    private Collection<Category> categories;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Map<UUID, ItemReview> reviewsMap;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<ItemReview> reviews;

    public Item(String name, double price, Store store, double rating, int quantity, String description) {
        this.name = name;
        this.price = price;
        this.store = store;
        this.rating = rating;
        this.quantity = quantity;
        this.description = description;
        purchaseType = new DirectPurchase(PurchaseType.DIRECT_PURCHASE);
        categories = new ConcurrentLinkedQueue<>();
        //reviewsMap = new ConcurrentHashMap<>();
        reviews = new ConcurrentLinkedQueue<>();
    }

    public Item(Item otherItem) {
        this.id = otherItem.getId();
        this.name = otherItem.getName();
        this.price = otherItem.getPrice();
        this.store = otherItem.getStore();
        this.rating = otherItem.getRating();
        this.quantity = otherItem.getQuantity();
        this.description = otherItem.getDescription();
        purchaseType = otherItem.getPurchaseType();
        categories = otherItem.getCategories();
        //reviewsMap = otherItem.getReviewsMap();
        reviews = otherItem.getReviews();
    }
    public Item(){
        purchaseType = new DirectPurchase(PurchaseType.DIRECT_PURCHASE);
        categories = new ConcurrentLinkedQueue<>();
        //reviewsMap = new ConcurrentHashMap<>();
        reviews = new ConcurrentLinkedQueue<>();
    }
    
    public void setPurchaseType(PurchaseType purchaseType) {
        this.purchaseType = purchaseType;
    }
    
    public Collection<Category> getCategories() {
        return categories;
    }

    public boolean containsCategory(String category) {
        for (Category category1 : this.getCategories()) {
            if (category1.getCategoryName().equalsIgnoreCase(category))
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

    public void setName(String name) throws Exception {
        if (name == null || name.length() <= 0)
            throw new RuntimeException("Item name cannot be empty.");
        this.name = name;
    }

    public double getPrice() {
        return price;
    }
    
    public double getPrice(UUID clientCredentials) {
        if (purchaseType.getType().equals(PurchaseType.BID_PURCHASE) &&
                ((BidPurchase)purchaseType).isBidAccepted(clientCredentials))
            return ((BidPurchase)purchaseType).getBids().get(clientCredentials).getPrice();
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0)
            throw new RuntimeException("Item price must be positive.");
        this.price = price;
    }

    public Store getStore() {
        return store;
    }

    public void setStoreId(Store store) {
        this.store = store;
    }

    public double getRating() {
        return rating;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0)
            throw new RuntimeException("Item quantity cannot be negative.");
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }

    public List<ItemReview> getReviews() {
        List<ItemReview> output = new ArrayList<>(reviews);
        output.sort(Comparator.comparing(ItemReview::getTimestamp));
        return output;
    }

    /*
    private Map<UUID, ItemReview> getReviewsMap() {
        return this.reviewsMap;
    }
     */

    /*
    public UUID addReview(UUID clientCredentials, String body, int rating) {
        ItemReview itemReview = new ItemReview(this, body, clientCredentials, rating);
        reviewsMap.put(itemReview.getId(), itemReview);
        addRating(rating);
        return itemReview.getId();
    }

     */

    public UUID addReview(UUID clientCredentials, String body, int rating) {
        ItemReview itemReview = new ItemReview(this, body, clientCredentials, rating);
        reviews.add(itemReview);
        addRating(rating);
        return itemReview.getId();
    }

    // make average of the old ratings with the new one
    public void addRating(int newRating) {
        if (newRating < 0 || newRating > 5)
            throw new RuntimeException("Rating can only be between 0 and 5.");
        double x = rating * ratesCount;
        x += newRating;
        ratesCount++;
        rating = x / ratesCount;
    }

    public Boolean addQuantity(int quantityToAdd) {
        this.quantity += quantityToAdd;
        return true;
    }

    public Boolean removeFromQuantity(int quantityToRemove) throws Exception {
        if (this.quantity < quantityToRemove) {
            throw new Exception("Passed quantity to remove is less than is available in stock.");
        } else {
            this.quantity -= quantityToRemove;
        }
        return true;
    }

    public void addCategory(Category category) {
        if (containsCategory(category.getCategoryName())) {
            throw new RuntimeException("Item is already assigned to category " + category.getCategoryName());
        }
        categories.add(category);
    }
    
    public boolean addBid(UUID clientCredentials, double amount, int quantity) {
        if (!getPurchaseType().getType().equals(PurchaseType.BID_PURCHASE))
            throw new RuntimeException("Bidding is only available on Bid Purchase type items.");
        if (amount * quantity < price)
            throw new RuntimeException("Bidding price can only be equal or larger than item base price.");
        ((BidPurchase)purchaseType).addBid(clientCredentials, amount, quantity);
        return true;
    }
    
    public boolean removeBid(UUID clientCredentials) {
        if (!getPurchaseType().getType().equals(PurchaseType.BID_PURCHASE))
            throw new RuntimeException("Bidding is only available on Bid Purchase type items.");
        ((BidPurchase)purchaseType).removeBid(clientCredentials);
        return true;
    }
    
    public Bid acceptBid(UUID clientCredentials, UUID bidderId, double amount) {
        if (!getPurchaseType().getType().equals(PurchaseType.BID_PURCHASE))
            throw new RuntimeException("Bidding is only available on Bid Purchase type items.");
        return ((BidPurchase)purchaseType).acceptBid(clientCredentials, bidderId, amount);
        
    }
    
    public Bid getBid(UUID clientCredentials) {
        if (purchaseType.getType().equals(PurchaseType.BID_PURCHASE) &&
                ((BidPurchase) purchaseType).getBids().containsKey(clientCredentials)) {
            return ((BidPurchase) purchaseType).getBids().get(clientCredentials);
        }
        else if (!purchaseType.getType().equals(PurchaseType.BID_PURCHASE))
            throw new RuntimeException("This item is not a bid purchase type item.");
        throw new RuntimeException("This item has no bids by this user.");
    }
    
    public List<Bid> getBids() {
        if (purchaseType.getType().equals(PurchaseType.BID_PURCHASE)) {
            return ((BidPurchase) purchaseType).getBids().values().stream().toList();
        }
        throw new RuntimeException("This item is not a bid purchase type item.");
    }

    public void setStore(Store store) {
        this.store = store;

    }
}
