package DomainLayer.Market.Stores;

import DomainLayer.Market.Stores.Discounts.condition.*;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.*;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.Purchase;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreOwner;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.ShoppingBasket;
import DomainLayer.Market.Users.User;

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
        if (newRating < 0 || newRating > 5)
            throw new RuntimeException("Rating can only be between 0 and 5.");
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
        closed = true;
        return true;
    }


    public String getDescription() {
        return description;
    }

    public void addRole(UUID clientCredentials, Role role) throws Exception {
        if (rolesMap.containsKey(clientCredentials)) {
            Role existingRole = rolesMap.get(clientCredentials);
            if (role.getPermissions().contains(StorePermissions.STORE_OWNER)
                    && !existingRole.getPermissions().contains(StorePermissions.STORE_OWNER)) {
                rolesMap.put(clientCredentials, role);
                return;
            } else {
                throw new Exception("User is already a member of store staff.");
            }
        }
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

    public void purchaseBasket(Client client, ShoppingBasket shoppingBasket) throws Exception {
        ConcurrentHashMap<UUID, Integer> shoppingBasketItems = shoppingBasket.getItems();
        synchronized (items) {
            for (UUID itemId : shoppingBasketItems.keySet()) {
                int quantityToRemove = shoppingBasketItems.get(itemId);
                int oldQuantity = items.get(itemId).getQuantity();
                if (quantityToRemove <= oldQuantity){
                //update Store, history Sale Store, User purchase
                    items.get(itemId).setQuantity(oldQuantity - quantityToRemove);
                    Sale sale = new Sale(client.getId(),shoppingBasket.getStoreId(), itemId,quantityToRemove);
                    sales.add(sale);
                    if(client instanceof User){
                        Purchase purchase = new Purchase(client.getId(),itemId,quantityToRemove, shoppingBasket.getStoreId());
                        ((User) client).addPurchase(purchase);
                    }}
                else throw new Exception("Quantity of item in store is lower than quantity to purchase.");
            }
        }
    }

    public void unPurchaseBasket(Client client, ShoppingBasket shoppingBasket) throws Exception {
        synchronized (items) {
            for (UUID itemId : shoppingBasket.getItems().keySet()) {
                int quantityToRestore = shoppingBasket.getItems().get(itemId);
                int oldQuantity = items.get(itemId).getQuantity() + quantityToRestore;
                items.get(itemId).setQuantity(oldQuantity);

                // Remove the sale from the sales history
                Sale saleToRemove = null;
                for (Sale sale : sales) {
                    if (sale.getUserId().equals(client.getId()) && sale.getStoreId().equals(shoppingBasket.getStoreId())
                            && sale.getItemId().equals(itemId) && sale.getQuantity() == quantityToRestore) {
                        saleToRemove = sale;
                        break;
                    }
                }
                if (saleToRemove != null) {
                    sales.remove(saleToRemove);
                }

                // Remove the purchase from the user's purchase history
                if (client instanceof User) {
                    User user = (User) client;
                    Purchase purchaseToRemove = null;
                    for (Purchase purchase : user.getPurchases()) {
                        if (purchase.getItemId().equals(itemId) && purchase.getQuantity() == quantityToRestore
                                && purchase.getStoreId().equals(shoppingBasket.getStoreId())) {
                            purchaseToRemove = purchase;
                            break;
                        }
                    }
                    if (purchaseToRemove != null) {
                        user.getPurchases().remove(purchaseToRemove);
                    }
                }
            }
        }
    }






    public Boolean addPolicyTermByStoreOwner( int rule, Boolean atLeast, int quantity, UUID itemId, Category category) throws Exception {
        PurchaseTerm term = creatingPurchaseTerm(rule,  atLeast,  quantity,  itemId,  category);
        this.policy.addPurchaseTerm(term);
        return true;
    }

    public Boolean removePolicyTermByStoreOwner(  int rule, Boolean atLeast, int quantity, UUID itemId, Category category) throws Exception {
        PurchaseTerm term = creatingPurchaseTerm(rule,  atLeast,  quantity,  itemId,  category);
        this.policy.removePurchaseTerm(term);
        return true;
    }

    public Boolean addDiscountByStoreOwner(int PurchaseRule,int DiscountRule, Boolean atLeast, int quantity, UUID itemId, Category category,Double discountPercentage,UUID DiscountItemId,Category discountCategory) throws Exception {
        Discount discount = creatingDiscountTerm(PurchaseRule,DiscountRule,atLeast,quantity,itemId,category,discountPercentage,DiscountItemId,discountCategory);
        this.discounts.addDiscount(discount);
        return true;
    }

    public Boolean removeDiscountByStoreOwner(int PurchaseRule,int DiscountRule, Boolean atLeast, int quantity, UUID itemId, Category category,Double discountPercentage,UUID DiscountItemId,Category discountCategory) throws Exception {
        Discount discount = creatingDiscountTerm(PurchaseRule,DiscountRule,atLeast,quantity,itemId,category,discountPercentage,DiscountItemId,discountCategory);
        this.discounts.removeDiscount(discount);
        return true;
    }


    public int numOfItems() {
        return items.size();
    }

    public PurchaseTerm creatingPurchaseTerm(int rule, Boolean atLeast, int quantity, UUID itemId, Category category) throws Exception {
        PurchaseRule purchaseRule;
        switch (rule){
            case 1://Item
                if(itemId==null){ throw new Exception("can't Creating Purchase Term of Item Purchase Rule if item id is null");}
                purchaseRule = new ItemPurchaseRule(itemId);
                break;
            case  2://ShoppingBasket
                purchaseRule = new ShopingBasketPurchaseRule();
                break;
            case  3://category
                purchaseRule = new CategoryPurchaseRule(category);
            default:
            { throw new Exception("can't Creating Purchase Term which is not a shopping basket item or category");}
        }
        if (atLeast){
            return new atLeastPurchaseRule(purchaseRule,quantity);
        }
        else return new AtMostPurchaseRule(purchaseRule,quantity);

    }


    public Discount creatingDiscountTerm(int PurchaseRule,int DiscountRule, Boolean atLeast, int quantity, UUID itemId, Category category,Double discountPercentage,UUID DiscountItemId,Category discountCategory) throws Exception {
        PurchaseTerm purchaseTerm = creatingPurchaseTerm(PurchaseRule,  atLeast,  quantity,  itemId,  category);
        CalculateDiscount OptioncalculateDiscount;
        switch (DiscountRule){
            case 1://Item
                if(DiscountItemId==null){ throw new Exception("can't Creating discount Term of Item discount Rule if item id is null");}
                OptioncalculateDiscount = new ItemCalculateDiscount(itemId);
                break;
            case  2://ShoppingBasket
                OptioncalculateDiscount = new ShopingBasketCalculateDiscount();
                break;
            case  3://category
                OptioncalculateDiscount = new CategoryCalculateDiscount(discountCategory);
            default:
            { throw new Exception("can't Creating Discount Term which is not a shopping basket item or category");}
        }
        Discount discount = new Discount(OptioncalculateDiscount,discountPercentage,purchaseTerm);
        return discount;

    }

    }



