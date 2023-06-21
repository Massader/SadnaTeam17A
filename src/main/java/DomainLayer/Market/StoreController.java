package DomainLayer.Market;

import DataAccessLayer.RepositoryFactory;
import DataAccessLayer.UserRepository;
import DataAccessLayer.controllers.StoreDalController;
import DataAccessLayer.controllers.UserDalController;
import DomainLayer.Market.Stores.*;
import DomainLayer.Market.Stores.Discounts.Discount;
import DomainLayer.Market.Stores.PurchaseRule.PurchaseTerm;
import DomainLayer.Market.Stores.PurchaseRule.StorePurchasePolicy;
import DomainLayer.Market.Stores.PurchaseTypes.*;
import DomainLayer.Market.Users.*;
import DomainLayer.Market.Users.Roles.OwnerPetition;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreFounder;
import DomainLayer.Market.Users.Roles.StorePermissions;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.ServiceDiscount;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StoreController {
    private static StoreController instance = null;
    private static final Object instanceLock = new Object();
    private UserController userController;
    private NotificationController notificationController;
    private RepositoryFactory repositoryFactory;
    private StoreDalController storeDalController;

    private StoreController() {
    }

    public static StoreController getInstance() {
        synchronized (instanceLock) {
            if (instance == null)
                instance = new StoreController();
        }
        return instance;
    }

    public void init(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
//        storeMap = new ConcurrentHashMap<>();
        userController = UserController.getInstance();
        notificationController = NotificationController.getInstance();
//        itemCategories = new ConcurrentHashMap<>();
        storeDalController = StoreDalController.getInstance(repositoryFactory);
    }

    private void initCategories() {

    }

    public void init() {
//        storeMap = new ConcurrentHashMap<>();
        userController = UserController.getInstance();
        notificationController = NotificationController.getInstance();
//        itemCategories = new ConcurrentHashMap<>();
    }


    public List<Store> getStores() {

        return storeDalController.getAllStores();
    }

    public Response<List<Store>> getStoresPage(int number, int page) {
        try {
            if (number <= 0) return Response.getFailResponse("Number of stores per page can't be lower than 1.");
            List<Store> stores = new ArrayList<>(storeDalController.getAllStores().stream().filter(store -> !store.isClosed() && !store.isShutdown()).toList());
            if (stores.isEmpty()) return Response.getSuccessResponse(new ArrayList<Store>());
            if (page == 0 || page > (stores.size() / number) + 1) {
                return Response.getFailResponse("Page number can't be 0 or larger than available stores.");
            }
            int start = (page - 1) * number;
            int end = Math.min(start + number, stores.size());
            return Response.getSuccessResponse(stores.subList(start, end));
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }


    public boolean storeExist(UUID storeId) {
        return storeDalController.isStoreExists(storeId);
    }

    /*
    public Response<Store> getStoreInformation(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            if (getStore(storeId).isClosed() && !getStore(storeId).getRolesMap().containsKey(clientCredentials))
                return Response.getFailResponse("Store is closed.");
            return Response.getSuccessResponse(getStore(storeId));
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
     */

    public Response<Store> getStoreInformation(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Response<Boolean> response =userController.isLoggedInUser(clientCredentials);
            if(response.isError())
                return Response.getFailResponse("user does not logged in");
//            boolean found = false;
//            Store store = getStore(storeId);
//            if (store.isClosed())
//                for (Role role : store.getRoles())
//                    if (role.getUser().getId().equals(clientCredentials)) {
//                        found = true;
//
//                    }
//            if(!found)
            Store store =getStore(storeId);
            if (store.isClosed() && !getStore(storeId).getRoles().stream().anyMatch(role -> role.getUser().getId().equals(clientCredentials)))
                return Response.getFailResponse("Store is closed.");
            return Response.getSuccessResponse(getStore(storeId));
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Item> getItemInformation(UUID storeId, UUID itemId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Item item = storeDalController.getItem(itemId);;
            if (item != null)
                return Response.getSuccessResponse(item);
            return Response.getFailResponse("Item does not exist.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    /*
    public Response<Boolean> closeStore(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_FOUNDER))
                return Response.getFailResponse("User doesn't have permission.");
            if (!userController.isUserLoggedIn(clientCredentials))
                return Response.getFailResponse("Appointing user is not logged in.");
            if (store.closeStore()) {
                for (Map.Entry<UUID, Role> role : store.getRolesMap().entrySet()) {
                    List<StorePermissions> rolePermissions = role.getValue().getPermissions();
                    if (rolePermissions.contains(StorePermissions.STORE_OWNER)
                            && !rolePermissions.contains(StorePermissions.STORE_FOUNDER)) {
                        User user = userController.getUserById(role.getKey());
                        if (user != null) notificationController.sendNotification(user.getId(),
                                "Owned store " + store.getName() + " has been closed by founder.");
                    }
                }
                return Response.getSuccessResponse(true);
            }
            return Response.getFailResponse("Store already closed.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
     */

    public Response<Boolean> closeStore(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_FOUNDER))
                return Response.getFailResponse("User doesn't have permission.");
            if (!userController.isUserLoggedIn(clientCredentials))
                return Response.getFailResponse("Appointing user is not logged in.");
            if (store.closeStore()) {
                for (Role role : store.getRoles()) {
                    List<StorePermissions> rolePermissions = role.getPermissions();
                    if (rolePermissions.contains(StorePermissions.STORE_OWNER)
                            && !rolePermissions.contains(StorePermissions.STORE_FOUNDER)) {
                        User user = userController.getUserById(role.getUser().getId());
                        if (user != null) notificationController.sendNotification(user.getId(),
                                "Owned store " + store.getName() + " has been closed by founder.");
                    }
                }
                storeDalController.saveStore(store);
                return Response.getSuccessResponse(true);
            }
            return Response.getFailResponse("Store already closed.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> reopenStore(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_FOUNDER))
                return Response.getFailResponse("User doesn't have permission.");
            if (store.reopenStore()) {
                for (Role role : store.getRoles()) {
                    List<StorePermissions> rolePermissions = role.getPermissions();
                    if (rolePermissions.contains(StorePermissions.STORE_OWNER)
                            && !rolePermissions.contains(StorePermissions.STORE_FOUNDER)) {
                        User user = userController.getUserById(role.getUser().getId());
                        if (user != null) notificationController.sendNotification(user.getId(),
                                "Owned store " + store.getName() + " has been reopened by founder.");
                    }
                }
                storeDalController.saveStore(store);
                return Response.getSuccessResponse(true);
            }
            return Response.getFailResponse("can not reopen store");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> shutdownStore(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = getStore(storeId);
            User user = userController.getUserById(clientCredentials);
            if (user == null)
                return Response.getFailResponse("User does not exist.");
            if (!user.isAdmin())
                return Response.getFailResponse("Only admins can shutdown stores.");
            if (store.shutdownStore()) {
                for (Role role : store.getRoles()) {
                    List<StorePermissions> rolePermissions = role.getPermissions();
                    if (rolePermissions.contains(StorePermissions.STORE_OWNER)
                            && !rolePermissions.contains(StorePermissions.STORE_FOUNDER)) {
                        user = userController.getUserById(role.getUser().getId());
                        if (user != null) {
                            notificationController.sendNotification(user.getId(),
                                    "Owned store " + store.getName() + " has been shut down by admin.");
                            user.removeStoreRole(storeId);
                            // TODO: Remove role from store as well? --Nitzan
                        }
                    }
                }
                storeDalController.saveStore(store);
                return Response.getSuccessResponse(true);
            }
            return Response.getFailResponse("Cannot shutdown store.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Item> getItem(UUID itemId) {
        try {
            Item item = storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("item doesn't exist");
            else return Response.getSuccessResponse(item);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    @Transactional
    public Response<Item> addItemToStore(UUID clientCredentials, String name, double price, UUID storeId, int quantity, String description) {
        try {
            Store store = storeDalController.getStore(storeId);
            if (store == null)
                return Response.getFailResponse("Store does not exist");
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    && !store.checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User doesn't have permission.");

            Item item = new Item(name, price, store, 0, quantity, description);
            store.addItem(item);
            storeDalController.saveItem(item);
//            UUID id3 = storeDalController.saveStore(store);
            UUID id2 = item.getId();
            return Response.getSuccessResponse(item);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }

    }

    public Response<Integer> getItemQuantity(UUID itemId) {
        try {
            if (!itemExist(itemId))
                return Response.getFailResponse("item doesn't exist");
            Item item = getItem(itemId).getValue();
            int quantity = item.getQuantity();
            return Response.getSuccessResponse(quantity);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    // check id the item id exist
    protected boolean itemExist(UUID itemId) {
        return storeDalController.isItemExists(itemId);
    }

    public Response<ShoppingCart> getShoppingCart(UUID clientId) {
        try {
            Client client = userController.getClientOrUser(clientId);
            if (client == null)
                return Response.getFailResponse("client not exist");
            return Response.getSuccessResponse(client.getCart());
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Store getStore(UUID storeId) {
        return storeDalController.getStore(storeId);
    }

    public Response<UUID> postReview(UUID clientCredentials, UUID itemId, String reviewBody, int rating) {
        try {
            if (!itemExist(itemId))
                return Response.getFailResponse("Item doesn't exist");
            if (rating > 5 || rating < 0)
                return Response.getFailResponse("Rating must be between 0 and 5.");
            Item item = getItem(itemId).getValue();
            synchronized (item.getReviews()) {
                if (item.getReviews()
                        .stream().filter((itemReview) -> itemReview.getReviewer().equals(clientCredentials))
                        .toList()
                        .isEmpty()
                        && userController.hasUserPurchasedItem(clientCredentials, itemId)) {
                    UUID reviewId = item.addReview(clientCredentials, reviewBody, rating);
                    storeDalController.saveItem(item);
                    return Response.getSuccessResponse(reviewId);
                }
            }
            return Response.getFailResponse("An item can only be reviewed once.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
    
    public Response<List<ItemReview>> getItemReviews(UUID storeId, UUID itemId) {
        try {
            Store store = storeDalController.getStore(storeId);
            if (store == null) {
                return Response.getFailResponse("Store does not exist.");
            }
            Item item = storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist.");
            return Response.getSuccessResponse(item.getReviews());
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<Boolean> isReviewableByUser(UUID clientCredentials, UUID storeId, UUID itemId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            if (!itemExist(itemId))
                return Response.getFailResponse("Item does not exist");
            Item item = getItem(itemId).getValue();
            if (item.getReviews()
                    .stream().filter((itemReview) -> itemReview.getReviewer().equals(clientCredentials))
                    .toList()
                    .isEmpty()
                && userController.hasUserPurchasedItem(clientCredentials, itemId)) {
                return Response.getSuccessResponse(true);
            } else return Response.getSuccessResponse(false);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<Boolean> isReviewableByUser(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = getStore(storeId);
            if (store.getReviews()
                    .stream().filter((storeReview) -> storeReview.getReviewer().equals(clientCredentials))
                    .toList()
                    .isEmpty()) {
                return Response.getSuccessResponse(true);
            } else return Response.getSuccessResponse(false);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    //calculate new rating given a new one
    public Response<Double> addStoreRating(UUID storeId, int rating) {
        try {
            Store store = getStore(storeId);
            double d  = store.addRating(rating);
            storeDalController.saveStore(store);
            return Response.getSuccessResponse(d);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    //calculate new rating given a new one
    public Response<Boolean> addItemRating(UUID clientCredentials, UUID itemId, UUID storeId, int rating) {
        try {
            if (!storeDalController.isStoreExists(storeId)) return Response.getFailResponse("Store not found.");
            Item item = storeDalController.getItem(itemId);
            if (item == null) return Response.getFailResponse("Item not found");
            if (!userController.isRegisteredUser(clientCredentials))
                return Response.getFailResponse("User not found.");
            Collection<Purchase> purchases = userController.getUserById(clientCredentials).getPurchases();
            for (Purchase purchase : purchases) {
                if (purchase.getItemId().equals(itemId)) {
                    if (purchase.isRated())
                        return Response.getFailResponse("Item has already been rated by user.");
                    item.addRating(rating);
                    purchase.setRated(true);
                    storeDalController.saveItem(item);
                    return Response.getSuccessResponse(true);
                }
            }
            return Response.getFailResponse("Only purchased items can be rated.");

        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<List<User>> getStoreStaff(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if (!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER) &&
                    !getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_MANAGEMENT_INFORMATION))
                return Response.getFailResponse("User doesn't have permission.");
            List<User> staffList = new ArrayList<User>();
            for (UUID id : getStore(storeId).getRoles().stream().map(role -> role.getUser().getId()).toList())
                staffList.add(userController.getUser(id).getValue());
            return Response.getSuccessResponse(staffList);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<List<Sale>> getStoreSaleHistory(UUID clientCredentials, UUID storeId) {
        try {
            if (!userController.isRegisteredUser(clientCredentials)) {
                return Response.getFailResponse("Only registered users can view a store's sale history.");
            }
            if(!userController.isUserLoggedIn(clientCredentials)){
                return Response.getFailResponse("User is not logged in.");
            }
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = getStore(storeId);
            User user = userController.getUserById(clientCredentials);
            if (user != null && store != null
                    && (user.isAdmin()
                    || store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    || store.checkPermission(clientCredentials,StorePermissions.STORE_SALE_HISTORY))) {
                return Response.getSuccessResponse(new ArrayList<>(getStore(storeId).getSales()));
            }
            return Response.getFailResponse("Action failed.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    @Transactional
    public Response<Store> createStore(UUID clientCredentials, String storeName, String storeDescription) {
        try {
            if (storeName == null || storeName.length() == 0)
                return Response.getFailResponse("Store must have a name.");
            if (storeDalController.isStoreExists(storeName))
                    return Response.getFailResponse("A Store with this name already exists.");
            Store store = new Store(storeName, storeDescription);
            Role role = new StoreFounder();
            User user = userController.getUserById(clientCredentials);
            store.addRole(user, role);
            role.setStore(store);
            storeDalController.saveStore(store);
            return Response.getSuccessResponse(store);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId, int newQuantity) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if (!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    && !getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User doesn't have permission.");
            Item item =  storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist");
            item.setQuantity(newQuantity);
            storeDalController.saveItem(item);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setItemName(UUID clientCredentials, UUID storeId, UUID itemId, String name) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if (!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    && !getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User doesn't have permission.");
            Item item =  storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist");
            if (name == "")
                return Response.getFailResponse("illegal name!");
            item.setName(name);
            storeDalController.saveItem(item);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setItemDescription(UUID clientCredentials, UUID storeId, UUID itemId, String description) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if (!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    && !getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User doesn't have permission.");
            Item item =  storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist");
            item.setDescription(description);
            storeDalController.saveItem(item);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setItemPrice(UUID clientCredentials, UUID storeId, UUID itemId, double price) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if (!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    && !getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User doesn't have permission.");
            Item item =  storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist");
            if (price <=0)
                return Response.getFailResponse("illegal price");
            item.setPrice(price);
            storeDalController.saveItem(item);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Double> getCartTotal(UUID clientCredentials) {
        try {
            double price = 0;
            Client client = userController.getClientOrUser(clientCredentials);
            if (client == null)
                return Response.getFailResponse("User does not exist.");
            ShoppingCart cart = client.getCart();
            for (UUID storeId : cart.getShoppingBaskets().stream().map(ShoppingBasket::getStoreId).toList()) {// iterator on the storeId
                price += storeDalController.getStore(storeId)
                        .calculatePriceOfBasketWithPolicyAndDiscount(cart.getBasketByStoreId(storeId));
            }
            return Response.getSuccessResponse(price);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public double verifyCartPrice(ShoppingCart shoppingCart) throws Exception {
        double price = 0;
        for (UUID storeId : shoppingCart.getShoppingBaskets().stream().map(ShoppingBasket::getStoreId).toList()) {// iterator on the storeId
            price += storeDalController.getStore(storeId)
                    .calculatePriceOfBasketWithPolicyAndDiscount(shoppingCart.getBasketByStoreId(storeId));
        }
        return price;

    }

    //add a new store
    // for unit tests
    public UUID addStore(Store store) {
        UUID id = UUID.randomUUID();
        storeDalController.saveStore(store);
        return id;
    }

    public void resetController() {
        init(repositoryFactory);
    }

    public Response<Boolean> addItemCategory(UUID clientCredentials, UUID storeId, UUID itemId, String category) {
        try {
            Store store = storeDalController.getStore(storeId);
            if (store == null) {
                return Response.getFailResponse("Store does not exist.");
            }
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT) && !(store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER)))
                return Response.getFailResponse("User does not have item management permissions for this store.");
            Item item = storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist.");
            List<Category> categories =  repositoryFactory.categoryRepository.findByCategoryName(category);
            Category categoryOdj = null;
            if(!categories.isEmpty())
                categoryOdj = categories.get(0);
            else
                categoryOdj = new Category(category);
            item.addCategory(categoryOdj);
            storeDalController.saveItem(item);
            return Response.getSuccessResponse(true);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<Boolean> removeItemFromStore(UUID clientCredentials, UUID storeId, UUID itemId) {
        try {
            Store store = storeDalController.getStore(storeId);
            if (store == null) {
                return Response.getFailResponse("Store does not exist.");
            }
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT) && !(store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER)))
                return Response.getFailResponse("User does not have item management permissions for this store.");
            Item item = storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist.");
            synchronized (item) {
                userController.removeItemFromCarts(storeId, item);
                if (!store.removeItem(item))
                    return Response.getFailResponse("Failed to remove item.");
                else {
                    storeDalController.deleteItem(item);
                    storeDalController.saveStore(store);
                }
                return Response.getSuccessResponse(true);
            }
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<List<Item>> getItemsPage(int number, int page, UUID storeId) {
        try {
            List<Item> output = new ArrayList<>();
            if (storeId == null) {
                List<Item> allItems = repositoryFactory.itemRepository.findAll();
                int start = (page - 1) * number;
                int end = Math.min(start + number, allItems.size());
                output.addAll(allItems.subList(start, end));
            } else {
                Store store = getStore(storeId);
                if (store == null || store.isShutdown())
                    return Response.getFailResponse("Store does not exist");
                if (store.isClosed())
                    return Response.getFailResponse("Store is temporarily closed.");
                List<Item> allItems = repositoryFactory.itemRepository.findAllByStore(store);
                int start = (page - 1) * number;
                int end = Math.min(start + number, allItems.size());
                output.addAll(allItems.subList(start, end));
            }
            return Response.getSuccessResponse(output);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<Long> numOfStores() {
        try {
            long num = storeDalController.storesCount();;
            return Response.getSuccessResponse(num);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }


    public Response<Boolean> addPolicyTerm(UUID clientCredentials, UUID storeId, PurchaseTerm term) {
        try {
            Store store = storeDalController.getStore(storeId);
            if (store == null)
                return Response.getFailResponse("Store does not exist.");
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER) &&
                    !store.checkPermission(clientCredentials, StorePermissions.STORE_POLICY_MANAGEMENT))
                return Response.getFailResponse("User does not have STORE OWNER permissions for add policy term.");
            store.addPolicyTerm(term);
            storeDalController.saveStore(store);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> removePolicyTerm(UUID clientCredentials, UUID storeId, UUID termId)  {
        try {
            Store store = storeDalController.getStore(storeId);
            if (store == null)
                return Response.getFailResponse("Store does not exist.");
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER) &&
                    !store.checkPermission(clientCredentials, StorePermissions.STORE_POLICY_MANAGEMENT))
                return Response.getFailResponse("User does not have STORE OWNER permissions for add policy term.");
            return Response.getSuccessResponse(store.removePolicyTerm(termId));
        } catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> addDiscount(UUID clientCredentials, UUID storeId, ServiceDiscount serviceDiscount) {
        try {
            Store store = storeDalController.getStore(storeId);
            if (store == null)
                return Response.getFailResponse("Store does not exist.");
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER) &&
                    !store.checkPermission(clientCredentials, StorePermissions.STORE_DISCOUNT_MANAGEMENT))
                return Response.getFailResponse("User does not have STORE OWNER permissions for add policy term.");
            Discount discount = new Discount(serviceDiscount);
            store.addDiscount(discount);;
            storeDalController.saveStore(store);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> removeDiscount(UUID clientCredentials, UUID storeId, UUID discountId) {
        try {
            Store store = storeDalController.getStore(storeId);
            if (store == null)
                return Response.getFailResponse("Store does not exist.");
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER) &&
                    !store.checkPermission(clientCredentials, StorePermissions.STORE_DISCOUNT_MANAGEMENT))
                return Response.getFailResponse("User does not have STORE OWNER permissions for add policy term.");
            store.removeDiscount(discountId);;
            storeDalController.saveStore(store);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Long> numOfItems(UUID storeId) {
        if (storeId == null)
            return Response.getSuccessResponse(numOfItems());

        if (!storeExist(storeId))
            return Response.getFailResponse("store no exist");
        int num = getStore(storeId).numOfItems();
        return Response.getSuccessResponse((long)num);
    }

    private Long numOfItems() {
        return storeDalController.itemsCount();

    }

    public Response<Long> numOfOpenStores() {
        try {
            long count = storeDalController.openStoresCount();
            return Response.getSuccessResponse(count);
        }
        catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Category getCategory(String categoryName) throws Exception {
        if(categoryName.length()==0){throw new Exception("the category name can not be empty"); }
        List<Category> categories = repositoryFactory.categoryRepository.findByCategoryName(categoryName);
        Category category = null;
        if (!categories.isEmpty()) {
            category = categories.get(0);
        }
        return category;
    }
    
    
    public Response<List<User>> getStoreManagers(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            if (userController.getUserById(clientCredentials) == null)
                return Response.getFailResponse("Only registered users may get store manager list.");
            Response<Boolean> loggedInUserResponse = userController.isLoggedInUser(clientCredentials);
            if (loggedInUserResponse.isError())
                return Response.getFailResponse("Passed user is not logged in.");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_MANAGEMENT_INFORMATION)
                    && !store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER)) {
                return Response.getFailResponse("User does not have permission to get store managers list.");
            }
            List<User> managers = new ArrayList<>();
            List<UUID> managersIds = store.getStoreManagers();
            for (UUID id : managersIds) {
                managers.add(userController.getUserById(id));
            }
            return Response.getSuccessResponse(managers);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    
    public Response<UUID> postStoreReview(UUID clientCredentials, UUID storeId, String body, int rating) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store doesn't exist");
            if (rating > 5 || rating < 0)
                return Response.getFailResponse("Rating must be between 0 and 5.");
            Store store = getStore(storeId);
            synchronized (store.getReviews()) {
                if (store.getReviews()
                        .stream().filter((storeReview) -> storeReview.getReviewer().equals(clientCredentials))
                        .toList()
                        .isEmpty()) {
                    StoreReview storeReview = store.addReview(clientCredentials, body, rating);
                    storeDalController.saveStore(store); //unatached entity
                    return Response.getSuccessResponse(storeReview.getId());
                }
            }
            return Response.getFailResponse("An item can only be reviewed once.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
    
    public Response<List<StoreReview>> getStoreReviews(UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = getStore(storeId);
            return Response.getSuccessResponse(store.getReviews());
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<PurchaseType> getItemPurchaseType(UUID storeId, UUID itemId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Item item = storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist.");
            PurchaseType purchaseType = item.getPurchaseType();
            if (purchaseType == null)
                return Response.getFailResponse("Item purchase type was null.");
            return Response.getSuccessResponse(purchaseType);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<Boolean> setItemPurchaseType(UUID clientCredentials, UUID storeId, UUID itemId, String purchaseTypeName) {
        try {
            if (!userController.isRegisteredUser(clientCredentials))
                return Response.getFailResponse("User does not exist.");
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            if (!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT) &&
                !getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User does not have permission to change item purchase type.");
            Item item = storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist.");
            PurchaseType purchaseType;
            switch (purchaseTypeName) {
                case "DIRECT":
                    purchaseType = new DirectPurchase(purchaseTypeName);
                    break;
                case "BID":
                    purchaseType = new BidPurchase(purchaseTypeName);
                    break;
                case "AUCTION":
                    purchaseType = new AuctionPurchase(purchaseTypeName);
                    break;
                case "LOTTERY":
                    purchaseType = new LotteryPurchase(purchaseTypeName);
                    break;
                default:
                    return Response.getFailResponse("Purchase type name is invalid");
            }
            PurchaseType prevType = item.setPurchaseType(purchaseType);
            storeDalController.saveItem(item);
            storeDalController.deletePurchaseType(prevType.getId());
            return Response.getSuccessResponse(true);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<Boolean> addBidToItem(UUID clientCredentials, UUID storeId, UUID itemId, double bidPrice,
                                          int quantity) {
        try {
            if (userController.getClientOrUser(clientCredentials) == null)
                return Response.getFailResponse("User does not exist.");
            if (!userController.isRegisteredUser(clientCredentials))
                return Response.getFailResponse("Only registered users can add bids to items.");
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Item item = storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist.");
            item.addBid(clientCredentials, bidPrice, quantity);
            storeDalController.saveItem(item);
            return Response.getSuccessResponse(true);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<Boolean> removeBidOnItem(UUID clientCredentials, UUID storeId, UUID itemId) {
        try {
            if (!userController.isRegisteredUser(clientCredentials))
                return Response.getFailResponse("User does not exist.");
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Item item = storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist.");
            item.removeBid(clientCredentials);
            storeDalController.saveItem(item);
            return Response.getSuccessResponse(true);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<Boolean> acceptItemBid(UUID clientCredentials, UUID storeId, UUID itemId,
                                           UUID bidderId, double bidPrice) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("Only store owners can accept bids");
            Item item = storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist.");
            if (item.getQuantity() <= 0)
                return Response.getFailResponse("Can't accept bid on item when item does not have quantity.");
            Bid bid = item.acceptBid(clientCredentials, bidderId, bidPrice);
            item.removeBid(clientCredentials);
            if (bid.getOwnersAccepted().containsAll(getStore(storeId).getStoreOwners())) {
                bid.setAccepted(true);
                notificationController.sendNotification(bid.getBidderId(), "Your bid on " + item.getName() + " from " + store.getName()
                + " has been accepted. You can now add it to your cart.");
            }

            return Response.getSuccessResponse(true);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<List<Bid>> getItemBids(UUID clientCredentials, UUID storeId, UUID itemId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER) &&
                !store.checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User does not have permission to see item bids.");
            Item item = storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist.");
            List<Bid> output = item.getBids();
            if (output == null)
                return Response.getFailResponse("Failed to get bids.");
            return Response.getSuccessResponse(output);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<List<Bid>> getStoreBids(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER) &&
                    !store.checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User does not have permission to see item bids.");
            List<Bid> bids = new ArrayList<>();
            for (Item item : store.getItems()) {
                bids.addAll(item.getBids());
            }
            storeDalController.saveStore(store);
            return Response.getSuccessResponse(bids);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<List<PurchaseTerm>> getStorePurchaseTerms(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER) &&
                    !store.checkPermission(clientCredentials, StorePermissions.STORE_POLICY_MANAGEMENT))
                return Response.getFailResponse("User does not have permission to see item bids.");
            StorePurchasePolicy policy = store.getPolicy();
            if (policy == null)
                return Response.getFailResponse("Store policy was null");
            List<PurchaseTerm> terms = policy.getPurchasePolicies().stream().toList();
            if (terms == null)
                return Response.getFailResponse("Store policy terms were null");
            return Response.getSuccessResponse(terms);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<List<Discount>> getStoreDiscounts(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER) &&
                    !store.checkPermission(clientCredentials, StorePermissions.STORE_DISCOUNT_MANAGEMENT))
                return Response.getFailResponse("User does not have permission to see item bids.");
            return Response.getSuccessResponse(store.getDiscounts().getDiscountsAssembly().getDiscounts().stream().toList());
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<Collection<OwnerPetition>> getStoreOwnerPetitions(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User does not have permission to view owner petitions");
            return Response.getSuccessResponse(store.getOwnerPetitions());
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<Boolean> removeOwnerPetitionApproval(UUID clientCredentials, UUID storeId, UUID appointee){
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User does not have permission to view owner petitions");
            boolean success = store.removeOwnerPetitionApproval(appointee, clientCredentials);
            storeDalController.saveStore(store);
            return Response.getSuccessResponse(success);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<Bid> getUserItemBid(UUID clientCredentials, UUID storeId, UUID itemId, UUID bidderId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = getStore(storeId);
            Item item = storeDalController.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist");
            if (!item.getPurchaseType().getType().equals(PurchaseType.BID_PURCHASE))
                return Response.getFailResponse("Item is not sold by bids");
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER) && !clientCredentials.equals(bidderId))
                return Response.getFailResponse("User does not have permission to view bid");
            Bid bid = item.getBid(clientCredentials);
            if (bid == null)
                return Response.getFailResponse("User does not have a bid on this item.");
            return Response.getSuccessResponse(bid);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    private List<Bid> getStoreBids(UUID storeId) throws Exception {
        if (!storeExist(storeId))
            throw new Exception("Store does not exist.");
        Store store = getStore(storeId);
        List<Bid> bids = new ArrayList<>();
        for (Item item : store.getItems()) {
            bids.addAll(item.getBids());
        }
        return bids;
    }
    
    public Response<List<Bid>> getUserBids(UUID clientCredentials) {
        try {
            User user = userController.getUserById(clientCredentials);
            if (user == null)
                return Response.getFailResponse("User does not exist.");
            List<Bid> userBids = new ArrayList<>();
            for (Store store : storeDalController.getAllStores()) {
                List<Bid> storeBids = getStoreBids(store.getStoreId());
                userBids.addAll(storeBids.stream().filter(bid -> bid.getBidderId().equals(clientCredentials)).toList());
            }
            return Response.getSuccessResponse(userBids);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

//    public Response<List<CartItem>> getCartItem(UUID itemId) {
//
//    }

//    public Response<CartItem> getCartItem(UUID itemId, ShoppingBasket shoppingBasket) {
//
//    }
}