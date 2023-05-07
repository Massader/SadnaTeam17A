package DomainLayer.Market.Stores;

import DomainLayer.Market.Stores.Discounts.condition.Discount;
import DomainLayer.Market.Stores.Discounts.condition.StoreDiscount;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.PurchaseTerm;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.StorePurchasePolicies;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreOwner;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.ShoppingBasket;

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
    private final ConcurrentHashMap<UUID, Item> items;
    private final StoreDiscount discounts; // Map of Item ID -> Discount
    private final StorePurchasePolicies policy;
    private final ConcurrentLinkedQueue<Sale> sales;
    private final ConcurrentHashMap<UUID, Role> rolesMap;




    public Store(String name, String description) {
        this.name = name;
        this.storeId = UUID.randomUUID();
        this.description = description;
        this.rating = 0;
        this.closed = false;
        this.shutdown = false;
        this.ratingCounter = 0;
        items = new ConcurrentHashMap<>();
        discounts = new StoreDiscount(true);// always max until change
        policy = new StorePurchasePolicies();
        sales = new ConcurrentLinkedQueue<>();
        rolesMap = new ConcurrentHashMap<>();
    }

    public StoreDiscount getDiscounts() {
        return discounts;
    }

    public StorePurchasePolicies getPolicy() {
        return policy;
    }
    public ConcurrentHashMap<UUID, Role> getRolesMap() {
        return rolesMap;
    }

    public StoreOwner getOwner(UUID owner) {
        if (rolesMap.containsKey(owner))
            if (rolesMap.get(owner).getPermissions().contains(StorePermissions.STORE_OWNER))
                return (StoreOwner) rolesMap.get(owner);
        return null;
    }

    public boolean checkPermission(UUID clientCredentials, StorePermissions permission) {
        if (!rolesMap.containsKey(clientCredentials))
            return false;
        return (rolesMap.get(clientCredentials).getPermissions().contains(permission));
    }

    public ConcurrentHashMap<UUID, Item> getItems() {
        return items;
    }

    public void addRating(int newRating) {
        double x = rating * ratingCounter;
        x += newRating;
        ratingCounter++;
        rating = x / ratingCounter;
    }

    public boolean closeStore() {
        if (!shutdown && !closed) {
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

    public void addRole(UUID clientCredentials, Role role) {
        rolesMap.put(clientCredentials, role);
    }

    public void removeRole(UUID idToRemove) {
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

    public void addItem(Item item) throws Exception {
        if (item.getName() == null || item.getName().length() <= 0)
            throw new Exception("Item name must not be empty");
        if (item.getPrice() <= 0)
            throw new Exception("Item price must be larger than 0");
        synchronized (items) {
            for (Item existingItem : items.values()) {
                if (item.getName().equals(existingItem.getName()))
                    throw new Exception("Store already has item of this name.");
            }
            UUID id = item.getId();
            items.put(id, item);
        }
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
        if (rolesMap.containsKey(clientCredentials)) {
            return sales;
        }
        throw new Exception("the user is not have permissions to get sale history of store " + this.name);
    }

    public ConcurrentLinkedQueue<Sale> getSales() {
        return sales;
    }

    public double calculatePriceOfBasket(ConcurrentHashMap<UUID, Integer> items) { // Map of Item ID -> Quantity
        double price = 0;
        for (UUID key : items.keySet()) {
            int quantity = items.get(key);
            double basePrice = getItem(key).getPrice() * quantity;
            price += basePrice;
        }
        return price;

    }

    public  double calculatePriceOfBasketWithPolicyAndDiscount(ShoppingBasket shoppingBasket) throws Exception { // Map of Item ID -> Quantity)
        if(policy.purchaseRuleOccurs(shoppingBasket,this)){
            return discounts.CalculateShoppingBasket(shoppingBasket,this);
        }
        throw new Exception("The shopping Basket is not accepted by Store Policy");
    }
    

    public int getRatingCounter() {
        return ratingCounter;
    }

    public boolean removeItem(UUID itemId) {
        if (!items.containsKey(itemId))
            return false;
        items.remove(itemId);
        return true;
    }

    public ConcurrentLinkedQueue<Item> getUnavailableItems(ShoppingBasket shoppingBasket){
        ConcurrentHashMap<UUID, Integer> shoppingBasketItems = shoppingBasket.getItems();
        ConcurrentLinkedQueue<Item> missingItems = new ConcurrentLinkedQueue<>();
        synchronized (items) {
            for (UUID itemId : shoppingBasketItems.keySet()) {
                int quantityToRemove = shoppingBasketItems.get(itemId);
                int oldQuantity = items.get(itemId).getQuantity();
                if (oldQuantity < quantityToRemove)
                    missingItems.add(items.get(itemId));
            }
            return missingItems;
        }
    }

    public void purchaseBasket(ShoppingBasket shoppingBasket) throws Exception {
        ConcurrentHashMap<UUID, Integer> shoppingBasketItems = shoppingBasket.getItems();
        synchronized (items) {
            for (UUID itemId : shoppingBasketItems.keySet()) {
                int quantityToRemove = shoppingBasketItems.get(itemId);
                int oldQuantity = items.get(itemId).getQuantity();
                if (quantityToRemove <= oldQuantity)
                    items.get(itemId).setQuantity(oldQuantity - quantityToRemove);
                else throw new Exception("Quantity of item in store is lower than quantity to purchase.");
            }
        }
    }


    public Boolean addPolicyTermByStoreOwner( PurchaseTerm term) throws Exception {
        this.policy.addPurchaseTerm(term);
        return true;
    }

    public Boolean removePolicyTermByStoreOwner( PurchaseTerm term) throws Exception {
        this.policy.removePurchaseTerm(term);
        return true;
    }

    public Boolean addDiscountByStoreOwner(Discount discount) throws Exception {
        this.discounts.addDiscount(discount);
        return true;
    }

    public Boolean removeDiscountByStoreOwner(Discount discount) throws Exception {
        this.discounts.removeDiscount(discount);
        return true;
    }


    public int numOfItems() {
        return items.size();
    }

}



