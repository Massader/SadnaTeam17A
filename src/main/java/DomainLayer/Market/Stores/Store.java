package DomainLayer.Market.Stores;

import DataAccessLayer.ItemRepository;
import DataAccessLayer.StoreRepository;
import DomainLayer.Market.Stores.Discounts.*;
import DomainLayer.Market.Stores.PurchaseRule.*;
import DomainLayer.Market.Stores.PurchaseRule.StorePurchasePolicy;
import DomainLayer.Market.Users.*;
import DataAccessLayer.RepositoryFactory;
import DataAccessLayer.UserRepository;
import DataAccessLayer.controllers.UserDalController;
//import DomainLayer.Market.Stores.Discounts.condition.*;
//import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.*;
import DomainLayer.Market.UserController;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.Purchase;
import DomainLayer.Market.Users.Roles.OwnerPetition;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreOwner;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.ShoppingBasket;
import DomainLayer.Market.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Entity
@Table(name = "Stores_Store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "storeId", nullable = false, unique = true)
    private UUID storeId;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private double rating;
    @Column
    private boolean closed;
    @Column
    private boolean shutdown;
    @Column
    private int ratingCounter;
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Item> items;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private StoreDiscount discounts;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private StorePurchasePolicy policy;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Sale> sales;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Role> roles;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<StoreReview> reviews;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<OwnerPetition> ownerPetitions;

    public StoreDiscount getDiscounts() {
        return discounts;
    }

    public StorePurchasePolicy getPolicy() {
        return policy;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public Store(String name, String description) {
        this.name = name;
        this.description = description;
        this.rating = 0;
        this.closed = false;
        this.shutdown = false;
        this.ratingCounter = 0;
        items = new ConcurrentLinkedQueue<>();
        discounts = new StoreDiscount(true);// always max until change
        policy = new StorePurchasePolicy();
        sales = new ConcurrentLinkedQueue<>();
        roles = new ConcurrentLinkedQueue<>();
        reviews = new ConcurrentLinkedQueue<>();
        ownerPetitions = new ArrayList<>();
    }

    public Store(){
        items = new ConcurrentLinkedQueue<>();
        sales = new ConcurrentLinkedQueue<>();
        roles = new ConcurrentLinkedQueue<>();
        reviews = new ConcurrentLinkedQueue<>();
        ownerPetitions = new ArrayList<>();
    }

    public StoreOwner getOwner(UUID ownerId) {
        for (Role role : roles) {
            if (role.getUser().getId().equals(ownerId) && role.getPermissions().contains(StorePermissions.STORE_OWNER)) {
                return (StoreOwner) role;
            }
        }
        return null;
    }

    public boolean checkPermission(UUID userId, StorePermissions permission) {
//        User user = UserController.getInstance().getUserById(userId);
//        Collection<Role> roles = user.getRoles();
        for(Role role : roles){
            if  (role.getUser().getId().equals(userId) && role.getPermissions().contains(permission) && role.getStore().getStoreId().equals(this.storeId))
                return true;
        }
        return false;
    }

    public Collection<Item> getItems() {
        return items;
    }

    public double addRating(int newRating) {
        if (newRating < 0 || newRating > 5)
            throw new RuntimeException("Rating can only be between 0 and 5.");
        double x = rating * ratingCounter;
        x += newRating;
        ratingCounter++;
        rating = x / ratingCounter;
        return rating;
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

    public void addRole(User user, Role role) throws Exception {
        if (roles.stream().anyMatch(r -> r.getUser().getId().equals(user.getId()))) {
            if (roles.stream().anyMatch(
                    r -> r.getUser().getId().equals(user.getId()) &&
                    !r.getPermissions().contains(StorePermissions.STORE_OWNER) &&
                    role.getPermissions().contains(StorePermissions.STORE_OWNER))) {
                roles.add(role);
                return;
            } else {
                throw new Exception("User is already a member of store staff.");
            }
        }
        roles.add(role);
        user.addStoreRole(role);
    }

    public Role removeRole(UUID userId, User user) {
        Role ret = null;
        for (Role role : roles) {
            if (role.getUser().getId().equals(userId)) {
                ret = role;
                role.setPermissions(new ArrayList<>());
                roles.remove(role);
                break;
            }
        }
        user.removeStoreRole(storeId);
        return ret;
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
            for (Item existingItem : items) {
                if (item.getName().equals(existingItem.getName()))
                    throw new Exception("Store already has item of this name.");
            }
//            UUID id = item.getId();
            items.add(item);
        }
    }

    public Item getItem(UUID itemId) {
        for (Item item : items) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    public boolean hasItem(UUID itemId) {
        return items.stream().anyMatch(item -> item.getId().equals(itemId));
    }

    public Collection<Sale> getSales(UUID clientCredentials) throws Exception {
        if (roles.stream().anyMatch(role -> role.getUser().getId().equals(clientCredentials))) {
            return sales;
        }
        throw new Exception("the user is not have permissions to get sale history of store " + this.name);
    }

    public Collection<Sale> getSales() {
        return sales;
    }

    public double calculatePriceOfBasket(Collection<CartItem> items) {
        double price = 0;
        for (CartItem cartItem : items) {
            price += cartItem.getPrice() * cartItem.getQuantity();
        }
        return price;
    }

    public  double calculatePriceOfBasketWithPolicyAndDiscount(ShoppingBasket shoppingBasket) throws Exception {
        if (policy.purchaseRuleOccurs(shoppingBasket, this)) {
            return discounts.calculateShoppingBasket(shoppingBasket, this);
        }
        throw new Exception("The shopping basket for store " + name + " is not accepted by store policy");
    }

    public Boolean purchaseRuleOccurs(ShoppingBasket shoppingBasket){
        return policy.purchaseRuleOccurs(shoppingBasket, this) ;
    }
    public int getRatingCounter() {
        return ratingCounter;
    }

    public boolean removeItem(Item item) {
        items.remove(item);
        item.setStore(null); // Set the store reference in the item to null
         // Delete the item from the database using the appropriate repository
        return true;
    }

    public ConcurrentLinkedQueue<Item> getUnavailableItems(ShoppingBasket shoppingBasket){
        Collection<CartItem> shoppingBasketItems = shoppingBasket.getItems();
        ConcurrentLinkedQueue<Item> missingItems = new ConcurrentLinkedQueue<>();
        synchronized (items) {
            for (CartItem cartItem : shoppingBasketItems) {
                int quantityToRemove = cartItem.getQuantity();
                int oldQuantity = cartItem.getItem().getQuantity();
                if (oldQuantity < quantityToRemove)
                    missingItems.add(cartItem.getItem());
            }
            return missingItems;
        }
    }
//    @Transactional
    public User purchaseBasket(Client client, ShoppingBasket shoppingBasket) throws Exception {
        Collection<CartItem> shoppingBasketItems = shoppingBasket.getItems();
        synchronized (items) {
            for (CartItem cartItem : shoppingBasketItems) {
                int quantityToRemove = cartItem.getQuantity();
                int oldQuantity =  cartItem.getItem().getQuantity();
                if (quantityToRemove <= oldQuantity){
                //update Store, history Sale Store, User purchase
                    cartItem.getItem().setQuantity(oldQuantity - quantityToRemove);
                    Sale sale = new Sale(client.getId(),shoppingBasket.getStoreId(), cartItem.getItemId(),quantityToRemove, this);
                    sales.add(sale);
                    if(client instanceof User){
                        User user = ((User) client);
                        Purchase purchase = new Purchase(user, cartItem.getItemId(), quantityToRemove, shoppingBasket.getStoreId());
                        user.addPurchase(purchase);
                        return user;
                    }
                }
                else throw new Exception("Quantity of item in store is lower than quantity to purchase.");
            }
        }
        return null;
    }

    public void unPurchaseBasket(Client client, ShoppingBasket shoppingBasket) throws Exception {
        synchronized (items) {
            for (CartItem cartItem : shoppingBasket.getItems()) {
                int quantityToRestore = cartItem.getQuantity();
                int oldQuantity =  cartItem.getItem().getQuantity() + quantityToRestore;
                cartItem.getItem().setQuantity(oldQuantity);

                // Remove the sale from the sales history
                Sale saleToRemove = null;
                for (Sale sale : sales) {
                    if (sale.getUserId().equals(client.getId()) && sale.getStore().getStoreId().equals(shoppingBasket.getStoreId())
                            && sale.getItemId().equals(cartItem.getItemId()) && sale.getQuantity() == quantityToRestore) {
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
                        if (purchase.getItemId().equals(cartItem.getItemId()) && purchase.getQuantity() == quantityToRestore
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

    public Boolean addPolicyTerm(PurchaseTerm term) throws Exception {
        this.policy.addPurchaseTerm(term);
        return true;
    }

    public Boolean removePolicyTerm(UUID termId) throws Exception {
        this.policy.removePurchaseTerm(termId);
        return true;
    }

    public Boolean addDiscount(Discount discount) throws Exception {
        this.discounts.addDiscount(discount);
        return true;
    }

    public Boolean removeDiscount(UUID discountId) throws Exception {
        this.discounts.removeDiscount(discountId);
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
                purchaseRule = new ShoppingBasketPurchaseRule();
                break;
            case  3://category
                purchaseRule = new CategoryPurchaseRule(category);
            default:
            { throw new Exception("can't Creating Purchase Term which is not a shopping basket item or category");}
        }
        if (atLeast){
            return new AtLeastPurchaseTerm(purchaseRule,quantity);
        }
        else return new AtMostPurchaseTerm(purchaseRule,quantity);
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
                OptioncalculateDiscount = new ShoppingBasketCalculateDiscount();
                break;
            case  3://category
                OptioncalculateDiscount = new CategoryCalculateDiscount(discountCategory);
            default:
                throw new Exception("can't Creating Discount Term which is not a shopping basket item or category");
        }
        Discount discount = new Discount(OptioncalculateDiscount,discountPercentage,purchaseTerm);
        return discount;
    }


    public List<UUID> getStoreManagers() {
        return roles.stream().filter(role -> !role.getPermissions().contains(StorePermissions.STORE_OWNER)).map(role -> role.getUser().getId()).toList();
    }

    public List<UUID> getStoreOwners() {
        return roles.stream().filter(role -> role.getPermissions().contains(StorePermissions.STORE_OWNER)).map(role -> role.getUser().getId()).toList();
    }
    
    public List<StoreReview> getReviews() {
        List<StoreReview> output = new ArrayList<>(reviews);
        output.sort(Comparator.comparing(StoreReview::getTimestamp));
        return output;
    }
    
    public StoreReview addReview(UUID clientCredentials, String body, int rating) {
        StoreReview review = new StoreReview(storeId, body, clientCredentials, rating);
        reviews.add(review);
        addRating(rating);
        return review;
    }

    public Collection<OwnerPetition> getOwnerPetitions() {
        return ownerPetitions;
    }
    
    public boolean removeOwnerPetitionApproval(UUID appointee, UUID owner) throws Exception {
        List<OwnerPetition> petitions = ownerPetitions.stream().filter(appointment -> appointment.getAppointeeId().equals(appointee)).toList();
        if (petitions.isEmpty())
            throw new Exception("Petition for this appointee does not exist");
        OwnerPetition petition = petitions.get(0);
        petition.removeApproval(owner);
        if (petition.getOwnersList().isEmpty())
            ownerPetitions.remove(petition);
        return true;
    }
    
    public double calculateItemDiscount(ShoppingBasket shoppingBasket, UUID itemId) {
        return discounts.calculateItemDiscount(shoppingBasket, this, itemId);
    }

    public boolean hasRole(UUID userId) {
        return roles.stream().anyMatch(role -> role.getUser().getId().equals(userId));
    }

    public Role getRoleByUserId(UUID userId) {
        for (Role role : roles) {
            if (role.getUser().getId().equals(userId)) {
                return role;
            }
        }
        return null;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public void setReviews(Collection<StoreReview> reviews) {
        this.reviews = reviews;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDiscounts(StoreDiscount discounts) {
        this.discounts = discounts;
    }

    public void setItems(Collection<Item> items) {
        this.items = items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerPetitions(Collection<OwnerPetition> ownerPetitions) {
        this.ownerPetitions = ownerPetitions;
    }

    public void setPolicy(StorePurchasePolicy policy) {
        this.policy = policy;
    }

    public void setRatingCounter(int ratingCounter) {
        this.ratingCounter = ratingCounter;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public void setSales(Collection<Sale> sales) {
        this.sales = sales;
    }

    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
    }


    public void addSale(Sale sale) {
        sales.add(sale);
    }

    public void removeOwnerPetition(OwnerPetition petition) {
        ownerPetitions.remove(petition);
    }
}