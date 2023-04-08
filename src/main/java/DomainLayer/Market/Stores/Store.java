package DomainLayer.Market.Stores;

import DomainLayer.Market.Stores.Discounts.Discount;
import ServiceLayer.Response;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Store {
    private String name;
    private UUID storeID;
    private double rating;
    private boolean close;
    private boolean shutDown;
    private int ratingCounter;
    private ConcurrentHashMap<UUID, Item> items;
    private ConcurrentLinkedQueue<Discount> discounts;
    private Policy policy;
    private ConcurrentLinkedQueue<Sale> sales;


    public Store(String name) {
        this.name = name;
        this.storeID = UUID.randomUUID();
        this.rating = -1;
        this.close = false;
        this.shutDown = false;
        this.ratingCounter = 0;
        items = new ConcurrentHashMap<>();
        discounts = new ConcurrentLinkedQueue<>();
        policy = new Policy();
        sales = new ConcurrentLinkedQueue<>();
    }


    public void addRating(int newRating) {
        double x = rating * ratingCounter;
        x += newRating;
        ratingCounter++;
        rating = x / ratingCounter;

    }

    public boolean closeStore() {
        if(checkNotShutDown()&&checkNotClose()){
        this.close = true;
        return true;}
        return  false;
    }


    public boolean reopenStore() {
        ;
        if (!checkNotShutDown()&&!this.close) {
           // throw new IllegalArgumentException("the Store :" + this.getName() + " is already open ");
            return false;
        } else {
            this.close = false;
            return true;
        }

    }

    public Boolean ShutDown() {
        this.shutDown = true;
        return true;
    }

    private boolean checkNotShutDown() {
        if (isShutDown()) {
           // throw new IllegalArgumentException("the Store :" + this.getName() + " is already shutDown");
            return false;
        }
        return true;
    }

    private boolean checkNotClose() {
        if (isClose()) {
           // throw new IllegalArgumentException("the Store :" + this.getName() + " is already close");
            return false;
        }
        return  true;
    }





    //get +set function
    public boolean isClose() {
        return close;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public boolean isShutDown() {
        return shutDown;
    }

    public double getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }


    public int getRatingCounter() {
        return ratingCounter;
    }

    public void addItem(Item item) {
        UUID id = item.getId();
        items.put(id, item);
    }

    public Item getItem(UUID itemId) {
        if (!hasItem(itemId))
            return null;
        return items.get(itemId);
    }


    public boolean hasItem(UUID itemId) {
        return items.containsKey(itemId);
    }


    public ConcurrentLinkedQueue<Sale> getSales() {
        return sales;
    }
}
