package DomainLayer.Market;

import DomainLayer.Market.Stores.*;
import DomainLayer.Market.Stores.Discounts.condition.Discount;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.PurchaseTerm;
import DomainLayer.Market.Users.*;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreFounder;
import DomainLayer.Market.Users.Roles.StorePermissions;
import ServiceLayer.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StoreController {
    private static StoreController instance = null;
    private static final Object instanceLock = new Object();
    private ConcurrentHashMap<UUID, Store> storeMap;
    private UserController userController;
    private NotificationController notificationController;
    private ConcurrentHashMap<String, Category> itemCategories;

    private StoreController() {
    }

    public static StoreController getInstance() {
        synchronized (instanceLock) {
            if (instance == null)
                instance = new StoreController();
        }
        return instance;
    }

    public void init() {
        storeMap = new ConcurrentHashMap<>();
        userController = UserController.getInstance();
        notificationController = NotificationController.getInstance();
        itemCategories = new ConcurrentHashMap<>();
    }

    public List<Store> getStores() {
        return new ArrayList<>(storeMap.values());
    }

    public Response<List<Store>> getStoresPage(int number, int page) {
        try {
            if (number <= 0) return Response.getFailResponse("Number of stores per page can't be lower than 1.");
            if (storeMap == null || storeMap.size() == 0) return Response.getSuccessResponse(new ArrayList<Store>());
            if (page == 0 || page > (storeMap.size() / number) + 1)
                return Response.getFailResponse("Page number can't be 0 or larger than available stores.");
            List<Store> stores = new ArrayList<>(storeMap.values().stream().filter(store -> !store.isClosed() && !store.isShutdown()).toList());
            int start = (page - 1) * number;
            int end = Math.min(start + number, stores.size());
            return Response.getSuccessResponse(stores.subList(start, end));
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }


    public boolean storeExist(UUID storeId) {
        return this.storeMap.containsKey(storeId);
    }

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

    public Response<Item> getItemInformation(UUID storeId, UUID itemId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Item item = getStore(storeId).getItem(itemId);
            if (item != null)
                return Response.getSuccessResponse(item);
            return Response.getFailResponse("Item does not exist.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> closeStore(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_FOUNDER))
                return Response.getFailResponse("User doesn't have permission.");
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

    public Response<Boolean> reopenStore(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            Store store = getStore(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_FOUNDER))
                return Response.getFailResponse("User doesn't have permission.");
            if (store.reopenStore()) {
                for (Map.Entry<UUID, Role> role : store.getRolesMap().entrySet()) {
                    List<StorePermissions> rolePermissions = role.getValue().getPermissions();
                    if (rolePermissions.contains(StorePermissions.STORE_OWNER)
                            && !rolePermissions.contains(StorePermissions.STORE_FOUNDER)) {
                        User user = userController.getUserById(role.getKey());
                        if (user != null) notificationController.sendNotification(user.getId(),
                                "Owned store " + store.getName() + " has been reopened by founder.");
                    }
                }
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
            if (storeMap.get(storeId).shutdownStore()) {
                for (Map.Entry<UUID, Role> role : store.getRolesMap().entrySet()) {
                    List<StorePermissions> rolePermissions = role.getValue().getPermissions();
                    if (rolePermissions.contains(StorePermissions.STORE_OWNER)
                            && !rolePermissions.contains(StorePermissions.STORE_FOUNDER)) {
                        user = userController.getUserById(role.getKey());
                        if (user != null) {
                            notificationController.sendNotification(user.getId(),
                                    "Owned store " + store.getName() + " has been shut down by admin.");
                            user.removeStoreRole(storeId);
                            // TODO: Remove role from store as well? --Nitzan
                        }
                    }
                }
                return Response.getSuccessResponse(true);
            }
            return Response.getFailResponse("Cannot shutdown store.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Item> getItem(UUID itemId) {
        try {
            Item item = null;
            for (Store currentStore : storeMap.values()) {
                if (currentStore.hasItem(itemId)) {
                    item = currentStore.getItem(itemId);
                    return Response.getSuccessResponse(item);
                }
            }
            return Response.getFailResponse("item doesn't exist");
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<Item> addItemToStore(UUID clientCredentials, String name, double price, UUID storeId, int quantity, String description) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if (!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    && !getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User doesn't have permission.");

            UUID id = UUID.randomUUID();
            Item item = new Item(id, name, price, storeId, 0, quantity, description);


            //add the item to the store
            Store store = getStore(storeId);
            store.addItem(item);

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
        for (Store currentStore : storeMap.values())
            if (currentStore.hasItem(itemId))
                return true;
        return false;
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
        return storeMap.get(storeId);
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
                    return Response.getSuccessResponse(reviewId);
                }
            }
            return Response.getFailResponse("An item can only be reviewed once.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
    
    public Response<List<ItemReview>> getReviews(UUID storeId, UUID itemId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Item item = getStore(storeId).getItem(itemId);
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
            return Response.getSuccessResponse(store.addRating(rating));
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    //calculate new rating given a new one
    public Response<Boolean> addItemRating(UUID clientCredentials, UUID itemId, UUID storeId, int rating) {
        try {
            if (!storeMap.containsKey(storeId)) return Response.getFailResponse("Store not found.");
            Item item = storeMap.get(storeId).getItem(itemId);
            if (item == null) return Response.getFailResponse("Item not found");
            if (!userController.isRegisteredUser(clientCredentials))
                return Response.getFailResponse("User not found.");
            ConcurrentLinkedQueue<Purchase> purchases = userController.getUserById(clientCredentials).getPurchases();
            for (Purchase purchase : purchases) {
                if (purchase.getItemId().equals(itemId)) {
                    if (purchase.isRated())
                        return Response.getFailResponse("Item has already been rated by user.");
                    item.addRating(rating);
                    purchase.setRated(true);
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
            if (!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            List<User> staffList = new ArrayList<User>();
            for (UUID id : getStore(storeId).getRolesMap().keySet())
                staffList.add(userController.getUser(id).getValue());
            return Response.getSuccessResponse(staffList);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<List<Sale>> getStoreSaleHistory(UUID clientCredentials, UUID storeId) {
        try {
            if (!userController.isRegisteredUser(clientCredentials)) {
                return Response.getFailResponse("this client not user doesn't have the permissions ");
            }
            if (userController.getUser(clientCredentials).getValue().isAdmin()) {
                return Response.getSuccessResponse(new ArrayList<>(getStore(storeId).getSales()));
            }
            return Response.getSuccessResponse(new ArrayList<>(getStore(storeId).getSales(clientCredentials)));
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }


    //why do we need clientCredentials here? we call the setAsFounder function from Service.
    public Response<Store> createStore(UUID clientCredentials, String storeName, String storeDescription) {
        try {
            if (storeName == null || storeName.length() == 0)
                return Response.getFailResponse("Store must have a name.");
            for (Store store : storeMap.values())
                if (store.getName().equals(storeName))
                    return Response.getFailResponse("A Store with this name already exists.");
            Store store = new Store(storeName, storeDescription);
            store.addRole(clientCredentials, new StoreFounder(clientCredentials));
            Response<Boolean> response = userController.setAsFounder(clientCredentials, store.getStoreId());
            if (response.isError())
                return Response.getFailResponse(response.getMessage());
            storeMap.put(store.getStoreId(), store);
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
            if (!getStore(storeId).getItems().containsKey(itemId))
                return Response.getFailResponse("Item does not exist");
            getStore(storeId).getItems().get(itemId).setQuantity(newQuantity);
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
            if (!getStore(storeId).getItems().containsKey(itemId))
                return Response.getFailResponse("Item does not exist");
            if (name == "")
                return Response.getFailResponse("illegal name!");
            getStore(storeId).getItems().get(itemId).setName(name);
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
            if (!getStore(storeId).getItems().containsKey(itemId))
                return Response.getFailResponse("Item does not exist");
            getStore(storeId).getItems().get(itemId).setDescription(description);
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
            if (!getStore(storeId).getItems().containsKey(itemId))
                return Response.getFailResponse("Item does not exist");
            if (price <=0)
                return Response.getFailResponse("illegal price");
            getStore(storeId).getItems().get(itemId).setPrice(price);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> removeItemQuantity(UUID storeId, UUID itemId, int quantity) {
        try {
            if (!storeMap.containsKey(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = storeMap.get(storeId);
            Item item = store.getItem(itemId);
            if (item == null) return Response.getFailResponse("Item does not exist.");
            synchronized (item) {
                if (item.removeFromQuantity(quantity))
                    return Response.getSuccessResponse(true);
                else return Response.getFailResponse("Failed to remove quantity from item.");
            }
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());

        }
    }

    public Response<Boolean> addItemQuantity(UUID clientId, UUID itemId, int quantity) {
        try {
            if (!itemExist(itemId))
                return Response.getFailResponse("item not exist");
            Response<Item> itemResponse = getItem(itemId);
            if (itemResponse.isError())
                return Response.getFailResponse(itemResponse.getMessage());
            synchronized (itemResponse.getValue()) {
                if (itemResponse.getValue().addQuantity(quantity)) {
                    return Response.getSuccessResponse(true);
                }
            }
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());

        }
        return Response.getFailResponse("cant remove item quantity");
    }


    public Response<Double> getCartTotal(UUID clientCredentials) {
        try {
            double price = 0;
            Client client = userController.getClientOrUser(clientCredentials);
            if (client == null)
                return Response.getFailResponse("User does not exist.");
            ShoppingCart cart = client.getCart();
            for (UUID storeId : cart.getShoppingBaskets().keySet()) {// iterator on the storeId
                price += storeMap.get(storeId).calculatePriceOfBasketWithPolicyAndDiscount(cart.getShoppingBaskets().get(storeId));
            }
            return Response.getSuccessResponse(price);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public double verifyCartPrice(ShoppingCart shoppingCart) throws Exception {
        double price = 0;
        for (UUID storeId : shoppingCart.getShoppingBaskets().keySet()) {// iterator on the storeId
            price += storeMap.get(storeId).calculatePriceOfBasketWithPolicyAndDiscount(shoppingCart.getShoppingBaskets().get(storeId));
        }
        return price;

    }

    //add a new store
    // for unit tests
    public UUID addStore(Store store) {
        UUID id = UUID.randomUUID();
        storeMap.put(id, store);
        return id;
    }

    public void resetController() {
        instance = new StoreController();
    }

    public Response<Boolean> addItemCategory(UUID clientCredentials, UUID storeId, UUID itemId, String category) {
        try {
            if (!storeMap.containsKey(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = storeMap.get(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT) && !(store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER)))
                return Response.getFailResponse("User does not have item management permissions for this store.");
            Item item = store.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist.");
            itemCategories.putIfAbsent(category, new Category(category));
            item.addCategory(itemCategories.get(category));
            return Response.getSuccessResponse(true);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<Boolean> removeItemFromStore(UUID clientCredentials, UUID storeId, UUID itemId) {
        try {
            if (!storeMap.containsKey(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = storeMap.get(storeId);
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT) && !(store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER)))
                return Response.getFailResponse("User does not have item management permissions for this store.");
            Item item = store.getItem(itemId);
            if (item == null)
                return Response.getFailResponse("Item does not exist.");
            synchronized (item) {
                userController.removeItemFromCarts(storeId, item);
                if (!store.removeItem(itemId))
                    return Response.getFailResponse("Failed to remove item.");
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
                List<Item> allItems = new ArrayList<>();
                for (Store store : getStores()) {
                    if (!store.isClosed() && !store.isShutdown())
                        allItems.addAll(store.getItems().values());
                }
                int start = (page - 1) * number;
                int end = Math.min(start + number, allItems.size());
                output.addAll(allItems.subList(start, end));
            } else {
                Store store = getStore(storeId);
                if (store == null || store.isShutdown())
                    return Response.getFailResponse("Store does not exist");
                if (store.isClosed())
                    return Response.getFailResponse("Store is temporarily closed.");
                List<Item> allItems = new ArrayList<>(store.getItems().values());
                int start = (page - 1) * number;
                int end = Math.min(start + number, allItems.size());
                output.addAll(allItems.subList(start, end));
            }
            return Response.getSuccessResponse(output);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<Integer> numOfStores() {
        try {
            int num = storeMap.size();
            return Response.getSuccessResponse(num);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }


    public Response<Boolean> addPolicyTermByStoreOwner(UUID clientCredentials, UUID storeId, PurchaseTerm term) {
        try {
            if (!storeMap.containsKey(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = storeMap.get(storeId);
            if (!(store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER)))
                return Response.getFailResponse("User does not have STORE OWNER permissions for add policy term.");
            store.addPolicyTermByStoreOwner(term);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> removePolicyTermByStoreOwner(UUID clientCredentials, UUID storeId, PurchaseTerm term)  {
        try {
            if (!storeMap.containsKey(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = storeMap.get(storeId);
            if (!(store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER)))
                return Response.getFailResponse("User does not have STORE OWNER permissions for add policy term.");
            store.removePolicyTermByStoreOwner(term);
            return Response.getSuccessResponse(true);
    } catch(
    Exception exception)
    {
        return Response.getFailResponse(exception.getMessage());
    }
}

    public Response<Boolean> addDiscountByStoreOwner(UUID clientCredentials, UUID storeId, Discount discount) {
        try {
            if (!storeMap.containsKey(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = storeMap.get(storeId);
            if (!(store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER)))
                return Response.getFailResponse("User does not have STORE OWNER permissions for add policy term.");
            store.addDiscountByStoreOwner(discount);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> removeDiscountByStoreOwner(UUID clientCredentials, UUID storeId, Discount discount) {
        try {
            if (!storeMap.containsKey(storeId))
                return Response.getFailResponse("Store does not exist.");
            Store store = storeMap.get(storeId);
            if (!(store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER)))
                return Response.getFailResponse("User does not have STORE OWNER permissions for add policy term.");
            store.removeDiscountByStoreOwner(discount);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Integer> numOfItems(UUID storeId) {
        if (storeId == null)
            return Response.getSuccessResponse(numOfItems());

        if (!storeExist(storeId))
            return Response.getFailResponse("store no exist");
        int num = getStore(storeId).numOfItems();
        return Response.getSuccessResponse(num);
    }

    private Integer numOfItems() {
        int num = 0;
        for (Store store : storeMap.values())
            num+= store.numOfItems();
        return num;

    }

    public Response<Integer> numOfOpenStores() {
        try {
            int openStores = storeMap.values().stream().filter(store -> !store.isClosed()).toList().size();
            return Response.getSuccessResponse(openStores);
        }
        catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Category getCategory(String categoryName) throws Exception {
        if(categoryName.length()==0){throw new Exception("the category name can not be empty"); }
        Category category = itemCategories.get(categoryName);
        if (category == null) {
            category = new Category(categoryName);
            itemCategories.put(categoryName, category);
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
                    UUID reviewId = store.addReview(clientCredentials, body, rating);
                    return Response.getSuccessResponse(reviewId);
                }
            }
            return Response.getFailResponse("An item can only be reviewed once.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
}