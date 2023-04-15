package DomainLayer.Market;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Sale;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.*;
import DomainLayer.Market.Users.Roles.StoreOwner;
import DomainLayer.Market.Users.Roles.StorePermissions;
import ServiceLayer.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StoreController {
    private static StoreController instance = null;
    private static final Object instanceLock = new Object();
    private ConcurrentHashMap<UUID, Store> storeMap;
    private UserController userController;

    private StoreController() { }

    public static StoreController getInstance() {
        synchronized(instanceLock) {
            if (instance == null)
                instance = new StoreController();
        }
        return instance;
    }

    public void init() {
        storeMap = new ConcurrentHashMap<>();
        userController = UserController.getInstance();
    }

    public Collection<Store> getStores(){
        return storeMap.values();
    }

    public boolean storeExist(UUID storeId){
        return this.storeMap.containsKey(storeId);
    }

    public Response<Store> getStoreInformation(UUID storeId){
        try{
            if(!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            if(getStore(storeId).isClosed())
                return Response.getFailResponse("Store is closed.");
            return Response.getSuccessResponse(getStore(storeId));
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Item> getItemInformation(UUID storeId, UUID itemId){
        try{
            if(!storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            Item item = getStore(storeId).getItem(itemId);
                if(item!=null)
                    return Response.getSuccessResponse(item);
                return Response.getFailResponse("Item does not exist.");
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> closeStore(UUID clientCredentials ,UUID storeId ) {
        try {
            if(!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if(!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_FOUNDER))
                return Response.getFailResponse("User doesn't have permission.");
            if(getStore(storeId).closeStore())
                return Response.getSuccessResponse(true);
            return Response.getFailResponse("Store already closed.");
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> reopenStore(UUID clientCredentials ,UUID storeId ) {
        try {
            if(!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if(!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_FOUNDER))
                return Response.getFailResponse("User doesn't have permission.");
            if (storeMap.get(storeId).reopenStore()) {
                return Response.getSuccessResponse(true);
            }
            return Response.getFailResponse("can not reopen store");
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> shutdownStore(UUID clientCredentials , UUID storeId ) {
        try {
            if(!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
//            if(!isAdmin(clientCredentials))
//                return Response.getFailResponse("User doesn't have permission.");
            if (storeMap.get(storeId).shutdownStore()) {
                return Response.getSuccessResponse(true);
            }
            return Response.getFailResponse("Cannot shutdown store.");
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Item> getItem(UUID itemId){
        Item item = null;
        for (Store currentStore : storeMap.values()){
            if (currentStore.hasItem(itemId)){
                item = currentStore.getItem(itemId);
                return Response.getSuccessResponse(item);
            }
        }
        return Response.getFailResponse("item doesn't exist");
    }

    // create item and add it to a store
    public Response<Item> addItem(String name, double price, UUID storeId, int quantity, String description){

        if (!storeMap.containsKey(storeId))
            return Response.getFailResponse("store doesn't exist");

        UUID id = UUID.randomUUID();
        Item item = new Item(id, name, price, storeId, 0, quantity,description);


        //add the item to the store
        Store store = getStore(storeId);
        store.addItem(item);

        return Response.getSuccessResponse(item);
    }

    public Response<Integer> getItemQuantity(UUID itemId){
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

    public Response<ShoppingCart> getShoppingCart(UUID clientId){
        Client client = userController.getClientOrUser(clientId);
        if(client == null)
            return Response.getFailResponse("client not exist");
        return  Response.getSuccessResponse(client.getCart());
    }

    public Store getStore(UUID storeId){
        return storeMap.get(storeId);
    }

    public Response<UUID> postReview(UUID clientCredentials, UUID itemId, String reviewBody){
        try{
            if (!itemExist(itemId))
                return Response.getFailResponse("Item doesn't exist");
            Item item = getItem(itemId).getValue();
            UUID reviewId = item.addReview(clientCredentials,reviewBody);
            return Response.getSuccessResponse(reviewId);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    //calculate new rating given a new one
    public Response<Boolean> addStoreRating(UUID storeId ,int rating){
        try {
            Store store = getStore(storeId);
            store.addRating(rating);
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }

    }

    //calculate new rating given a new one
    public Response<Boolean> addItemRating(UUID itemId, UUID storeId, int rating){
        try{
            if (!storeMap.containsKey(storeId)) return Response.getFailResponse("Store not found.");
            Item item = storeMap.get(storeId).getItem(itemId);
            if (item == null) return Response.getFailResponse("Item not found");
            item.addRating(rating);
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<List<User>> getStoreStaff(UUID clientCredentials, UUID storeId){
        try {
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if (!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            List<User> staffList = new ArrayList<User>();
            for (UUID id : getStore(storeId).getRolesMap().keySet())
                staffList.add(userController.getUser(id).getValue());
            return Response.getSuccessResponse(staffList);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<List<Message>> getStoreMessages(UUID clientId, UUID itemId){
        return  null;
    }

    public Response<List<Sale>> getStoreSaleHistory(UUID clientCredentials , UUID storeId ) {// TODO: after we will have sale class -> the return  String
        try{
            if(!userController.isRegisteredUser(clientCredentials)){return Response.getFailResponse("this client not user יe doesn't have the permissions ");}
            return Response.getSuccessResponse(getStore(storeId).getSales(clientCredentials).stream().toList());
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
//        Store store = getStore(storeId).getValue();
//        ConcurrentLinkedQueue<Sale> saleHistory = store.getSales();
//        return Response.getSuccessResponse(saleHistory.toArray().toString());
//    }

    //why do we need clientCredentials here? we call the setAsFounder function from Service.
    public Response<Store> createStore(UUID clientCredentials , String storeName , String storeDescription ) {
        try {
            Store store = new Store(storeName, storeDescription);
            store.addRole(clientCredentials, new StoreOwner(clientCredentials));
            Response<Boolean> response = userController.setAsFounder(clientCredentials, store.getStoreId());
            if (response.isError())
                return Response.getFailResponse(response.getMessage());
            storeMap.put(store.getStoreId(), store);
            return Response.getSuccessResponse(store);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId, int newQuantity){
        try{
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if(!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                && !getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                    return Response.getFailResponse("User doesn't have permission.");
            if(!getStore(storeId).getItems().containsKey(itemId))
                return Response.getFailResponse("Item does not exist");
            getStore(storeId).getItems().get(itemId).setQuantity(newQuantity);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setItemName(UUID clientCredentials, UUID storeId, UUID itemId, String name){
        try{
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if(!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    && !getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User doesn't have permission.");
            if(!getStore(storeId).getItems().containsKey(itemId))
                return Response.getFailResponse("Item does not exist");
            getStore(storeId).getItems().get(itemId).setName(name);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setItemDescription(UUID clientCredentials, UUID storeId, UUID itemId, String description){
        try{
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if(!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    && !getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User doesn't have permission.");
            if(!getStore(storeId).getItems().containsKey(itemId))
                return Response.getFailResponse("Item does not exist");
            getStore(storeId).getItems().get(itemId).setDescription(description);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setItemPrice(UUID clientCredentials, UUID storeId, UUID itemId, double price){
        try{
            if (!storeExist(storeId))
                return Response.getFailResponse("Store does not exist");
            if(!getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    && !getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User doesn't have permission.");
            if(!getStore(storeId).getItems().containsKey(itemId))
                return Response.getFailResponse("Item does not exist");
            getStore(storeId).getItems().get(itemId).setPrice(price);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
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
            if (item.removeFromQuantity(quantity))
                return Response.getSuccessResponse(true);
            else return Response.getFailResponse("Failed to remove quantity from item.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());

        }
    }

    public Response<Boolean> addItemQuantity(UUID clientId, UUID itemId, int quantity){
        try {
            if(!itemExist(itemId))
                return Response.getFailResponse("item not exist");
            if(getItem(itemId).getValue().addQuantity(quantity)){
                return Response.getSuccessResponse(true);}}
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());

        }
        return Response.getFailResponse("cant remove item quantity");}


    public Response<Double> getCartTotal(UUID clientCredentials){
        try {
            double price = 0;
            Client client = userController.getClientOrUser(clientCredentials);
            if (client == null)
                return Response.getFailResponse("User does not exist.");
            ShoppingCart cart = client.getCart();
            for (UUID storeId : cart.getShoppingBaskets().keySet()) {// iterator on the storeId
                price += storeMap.get(storeId).calculatePriceOfBasket(cart.getShoppingBaskets().get(storeId).getItems());
            }
            return Response.getSuccessResponse(price);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    //add a new store
    //TODO: guy why we need this?
    protected UUID addStore(Store store){
        UUID id = UUID.randomUUID();
        storeMap.put(id, store);
        return id;
    }
    protected void addStore(Store store, UUID id){
        storeMap.put(id, store);
    }
}