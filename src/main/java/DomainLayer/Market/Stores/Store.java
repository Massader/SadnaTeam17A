package DomainLayer.Market.Stores;

import DomainLayer.Market.Stores.Discounts.Discount;
import ServiceLayer.Response;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Store {
    private String name;
    private UUID storeID;
    private double rating;
    private boolean close;
    private boolean shutdown;
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
        this.shutdown = false;
        this.ratingCounter = 0;
        items = new ConcurrentHashMap<>();
        discounts = new ConcurrentLinkedQueue<>();
        policy = new Policy();
        sales = new ConcurrentLinkedQueue<>();
    }

    public Collection<Item> getItems(){
        return items.values();
    }

    public void addRating(int newRating) {
        double x = rating * ratingCounter;
        x += newRating;
        ratingCounter++;
        rating = x / ratingCounter;

    }

    public boolean closeStore() {
        if(checkNotShutdown()&&checkNotClose()){
        this.close = true;
        return true;}
        return  false;
    }


    public boolean reopenStore() {
        ;
        if (!checkNotShutdown()&&!this.close) {
           // throw new IllegalArgumentException("the Store :" + this.getName() + " is already open ");
            return false;
        } else {
            this.close = false;
            return true;
        }

    }

    public Boolean shutdownStore() {
        this.shutdown = true;
        return true;
    }

    private boolean checkNotShutdown() {
        if (isShutdown()) {
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

    public boolean isShutdown() {
        return shutdown;
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
