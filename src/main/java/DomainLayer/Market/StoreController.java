package DomainLayer.Market;

import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Discounts.condition.Discount;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseRule.PurchaseTerm;
import DomainLayer.Market.Stores.Sale;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.*;
import DomainLayer.Market.Users.Roles.StoreFounder;
import DomainLayer.Market.Users.Roles.StoreOwner;
import DomainLayer.Market.Users.Roles.StorePermissions;
import ServiceLayer.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StoreController {
    private static StoreController instance = null;
    private static final Object instanceLock = new Object();
    private ConcurrentHashMap<UUID, Store> storeMap;
    private UserController userController;

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
    }

    public List<Store> getStores() {
        return new ArrayList<>(storeMap.values());
    }

    public Response<List<Store>> getStoresPage(int number, int page) {
        if (number <= 0) return Response.getFailResponse("Number of stores per page can't be lower than 1.");
        if (storeMap == null || storeMap.size() == 0) return Response.getSuccessResponse(new ArrayList<Store>());
        if (page == 0 || page > (storeMap.size() / number) + 1)
            return Response.getFailResponse("Page number can't be 0 or larger than available stores.");
        List<Store> stores = new ArrayList<>(storeMap.values());
        int start = (page - 1) * number;
        int end = Math.min(start + number, stores.size());
        return Response.getSuccessResponse(stores.subList(start, end));
    }


    public boolean storeExist(UUID storeId) {
        return this.storeMap.containsKey(storeId);
    }

    public Response<Store> getStoreInformation(UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            if (getStore(storeId).isClosed())
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
            if (!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_FOUNDER))
                return Response.getFailResponse("User doesn't have permission.");
            if (getStore(storeId).closeStore())
                return Response.getSuccessResponse(true);
            return Response.getFailResponse("Store already closed.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> reopenStore(UUID clientCredentials, UUID storeId) {
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if (!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_FOUNDER))
                return Response.getFailResponse("User doesn't have permission.");
            if (storeMap.get(storeId).reopenStore()) {
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
                return Response.getFailResponse("Store does not exist");
            Response<User> response = userController.getUser(clientCredentials);
            if (response.isError())
                return Response.getFailResponse(response.getMessage());
            if (!response.getValue().isAdmin())
                return Response.getFailResponse("User doesn't have permission.");
            if (storeMap.get(storeId).shutdownStore()) {
                return Response.getSuccessResponse(true);
            }
            return Response.getFailResponse("Cannot shutdown store.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Item> getItem(UUID itemId) {
        Item item = null;
        for (Store currentStore : storeMap.values()) {
            if (currentStore.hasItem(itemId)) {
                item = currentStore.getItem(itemId);
                return Response.getSuccessResponse(item);
            }
        }
        return Response.getFailResponse("item doesn't exist");
    }

    // create item and add it to a store
    public Response<Item> addItem(String name, double price, UUID storeId, int quantity, String description) {
        try {
            if (!storeMap.containsKey(storeId))
                return Response.getFailResponse("store doesn't exist");

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
        if (!itemExist(itemId))
            return Response.getFailResponse("item doesn't exist");
        Item item = getItem(itemId).getValue();
        int quantity = item.getQuantity();
        return Response.getSuccessResponse(quantity);
    }

    // check id the item id exist
    protected boolean itemExist(UUID itemId) {
        for (Store currentStore : storeMap.values())
            if (currentStore.hasItem(itemId))
                return true;
        return false;
    }

    public Response<ShoppingCart> getShoppingCart(UUID clientId) {
        Client client = userController.getClientOrUser(clientId);
        if (client == null)
            return Response.getFailResponse("client not exist");
        return Response.getSuccessResponse(client.getCart());
    }

    public Store getStore(UUID storeId) {
        return storeMap.get(storeId);
    }

    public Response<UUID> postReview(UUID clientCredentials, UUID itemId, String reviewBody) {
        try {
            if (!itemExist(itemId))
                return Response.getFailResponse("Item doesn't exist");
            Item item = getItem(itemId).getValue();
            UUID reviewId = item.addReview(clientCredentials, reviewBody);
            return Response.getSuccessResponse(reviewId);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    //calculate new rating given a new one
    public Response<Boolean> addStoreRating(UUID storeId, int rating) {
        try {
            Store store = getStore(storeId);
            store.addRating(rating);
            return Response.getSuccessResponse(true);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }

    }

    //calculate new rating given a new one
    public Response<Boolean> addItemRating(UUID itemId, UUID storeId, int rating) {
        try {
            if (!storeMap.containsKey(storeId)) return Response.getFailResponse("Store not found.");
            Item item = storeMap.get(storeId).getItem(itemId);
            if (item == null) return Response.getFailResponse("Item not found");
            item.addRating(rating);
            return Response.getSuccessResponse(true);
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
            store.addRole(clientCredentials, new StoreOwner(clientCredentials));
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
        if (!storeMap.containsKey(storeId))
            return Response.getFailResponse("Store does not exist.");
        Store store = storeMap.get(storeId);
        if (!store.checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT) && !(store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER)))
            return Response.getFailResponse("User does not have item management permissions for this store.");
        Item item = store.getItem(itemId);
        if (item == null)
            return Response.getFailResponse("Item does not exist.");
        item.addCategory(new Category(category));
        return Response.getSuccessResponse(true);
    }

    public Response<Boolean> removeItemFromStore(UUID clientCredentials, UUID storeId, UUID itemId) {
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
    }

    public Response<List<Item>> getItemsPage(int number, int page, UUID storeId) {
        List<Item> output = new ArrayList<>();
        if (storeId == null) {
            List<Item> allItems = new ArrayList<>();
            for (Store store : getStores()) {
                allItems.addAll(store.getItems().values());
            }
            int start = (page - 1) * number;
            int end = Math.min(start + number, allItems.size());
            output.addAll(allItems.subList(start, end));
        } else {
            Store store = getStore(storeId);
            if (store == null)
                return Response.getFailResponse("Store does not exist");
            List<Item> allItems = new ArrayList<>(store.getItems().values());
            int start = (page - 1) * number;
            int end = Math.min(start + number, allItems.size());
            output.addAll(allItems.subList(start, end));
        }
        return Response.getSuccessResponse(output);
    }

    public Response<Integer> numOfStores() {
        int num = storeMap.size();
        return Response.getSuccessResponse(num);
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

}