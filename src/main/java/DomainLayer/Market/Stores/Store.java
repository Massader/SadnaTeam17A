package DomainLayer.Market.Stores;

import DomainLayer.Market.Stores.Discounts.Discount;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreOwner;
import DomainLayer.Market.Users.Roles.StorePermissions;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Store {
    private String name;
    private UUID storeId;
    private String description;
    private double rating;
    private boolean closed;
    private boolean shutdown;
    private int ratingCounter;
    private ConcurrentHashMap<UUID, Item> items;
    private ConcurrentHashMap<UUID,Discount> discounts; // Map of Item ID -> Discount
    private Policy policy;
    private ConcurrentLinkedQueue<Sale> sales;
    private ConcurrentHashMap<UUID, Role> rolesMap;


    public ConcurrentHashMap<UUID,Discount> getDiscounts() {
        return discounts;
    }

    public Store(String name, String description) {
        this.name = name;
        this.storeId = UUID.randomUUID();
        this.description = description;
        this.rating = -1;
        this.closed = false;
        this.shutdown = false;
        this.ratingCounter = 0;
        items = new ConcurrentHashMap<>();
        discounts = new ConcurrentHashMap<>();
        policy = new Policy();
        sales = new ConcurrentLinkedQueue<>();
        rolesMap = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<UUID, Role> getRolesMap() {
        return rolesMap;
    }

    public StoreOwner getOwner(UUID owner){
        if(rolesMap.containsKey(owner))
            if(rolesMap.get(owner).getPermissions().contains(StorePermissions.STORE_OWNER))
                return (StoreOwner) rolesMap.get(owner);
        return null;
    }

    public boolean checkPermission(UUID clientCredentials, StorePermissions permission){
        if(!rolesMap.containsKey(clientCredentials))
            return false;
        return (rolesMap.get(clientCredentials).getPermissions().contains(permission));
    }

    public Map<UUID, Item> getItems(){
        return items;
    }

    public void addRating(int newRating) {
        double x = rating * ratingCounter;
        x += newRating;
        ratingCounter++;
        rating = x / ratingCounter;
    }

    public boolean closeStore() {
        if(!shutdown && !closed){
            this.closed = true;
            return true;
        }
        return false;
    }

    public boolean reopenStore() {
        if (shutdown || !closed) {
            return false;
        } else {
            closed = false;
            return true;
        }
    }

    public boolean shutdownStore() {
        shutdown = true;
        return true;
    }

    public String getDescription() {
        return description;
    }

    public void addRole(UUID clientCredentials, Role role){
        rolesMap.put(clientCredentials, role);
    }

    public void removeRole(UUID idToRemove){
        rolesMap.remove(idToRemove);
    }

    public boolean isClosed() {
        return closed;
    }

    public UUID getStoreId() {
        return storeId;
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

    public ConcurrentLinkedQueue<Sale> getSales(UUID clientCredentials) throws Exception {
        if (rolesMap.containsKey(clientCredentials))
        {
            return sales;}
        throw new Exception("the user is not have permissions to get sale history of store "+this.name);
    }

    public double calculatePriceOfBasket(ConcurrentHashMap<UUID,Integer> items) { // Map of Item ID -> Quantity
        double price = 0;
        for(UUID key : items.keySet()){
            int quantity = items.get(key);
            double basePrice = getItem(key).getPrice()*quantity;
            for (UUID itemId : items.keySet()) {
                if (discounts.containsKey(itemId)) {
                    price += discounts.get(itemId).calculatePrice(basePrice);
                }
            }
        }
        return price;

    };

    public Object getRatingCounter() {
        return ratingCounter;
    }


}

