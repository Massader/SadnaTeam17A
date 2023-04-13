package DomainLayer.Market.Stores;

import DomainLayer.Market.Stores.Discounts.Discount;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreOwner;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.ShoppingCart;
import ServiceLayer.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Store {
    private String name;
    private UUID storeID;
    private String description;
    private double rating;
    private boolean close;
    private boolean shutdown;
    private int ratingCounter;
    private ConcurrentHashMap<UUID, Item> items;
    private ConcurrentLinkedQueue<Discount> discounts;
    private Policy policy;
    private ConcurrentLinkedQueue<Sale> sales;
    private ConcurrentHashMap<UUID, Role> rolesMap;


    public ConcurrentLinkedQueue<Discount> getDiscounts() {
        return discounts;
    }

    public Store(String name, String description) {
        this.name = name;
        this.storeID = UUID.randomUUID();
        this.description = description;
        this.rating = -1;
        this.close = false;
        this.shutdown = false;
        this.ratingCounter = 0;
        items = new ConcurrentHashMap<>();
        discounts = new ConcurrentLinkedQueue<>();
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

    public void addRole(UUID clientCredentials, Role role){
        rolesMap.put(clientCredentials, role);
    }

    public void removeRole(UUID idToRemove){
        rolesMap.remove(idToRemove);
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


    public ConcurrentLinkedQueue<Sale> getSales(UUID clientCredentials) throws Exception {
        if (rolesMap.containsKey(clientCredentials))
        {
            return sales;}
        throw new Exception("the user is not have permissions to get sale history of store "+this.name);
    }

    public double calculatePriceOfBasket( ConcurrentHashMap<UUID,Integer> itemsID) {// itemid, quantity
        double price =0;
        for(UUID key : itemsID.keySet()){
            int quantity =itemsID.get(key);
            double minPriceByDisscounts = getItem(key).getPrice()*quantity;//ite, price * quantity
            if(!getDiscounts().isEmpty()){
                for (Discount discount :getDiscounts()) {
                    double newPrice =discount.calculatePrice(key,quantity);// for this level we not Realize the assumptions
                    if(newPrice<minPriceByDisscounts){
                        minPriceByDisscounts = newPrice;
                    }
                }
            }
            price+=minPriceByDisscounts;}
            return price;

        };






}

