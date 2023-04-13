package DomainLayer.Market;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Sale;
import DomainLayer.Market.Stores.SaleHistory;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.*;
import DomainLayer.Market.Users.Roles.StorePermissions;
import ServiceLayer.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StoreController {
    private static StoreController singleton = null;
    private ConcurrentHashMap<UUID, Store> storeMap;
    private UserController userController;

    private StoreController() {
        storeMap = new ConcurrentHashMap<>();
        userController = UserController.getInstance();

    }

    public static synchronized StoreController getInstance() {
        if (singleton == null)
            singleton = new StoreController();
        return singleton;
    }

    public Collection<Store> getStores(){
        return storeMap.values();
    }


    private Response<Boolean> checkExistStore(UUID storeId){
        if(!this.storeMap.containsKey(storeId)){ return Response.getFailResponse("item not exist");}
        return Response.getSuccessResponse(true);

    }

    public Response<Store> getStoreInformation(UUID storeId){
        try{
            Store store = storeMap.get(storeId);
            if(store!=null)
                return Response.getSuccessResponse(store);
            return Response.getFailResponse("Store does not exist");
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Item> getItemInformation(UUID storeId, UUID itemId){
        try{
            Store store = storeMap.get(storeId);
            if(store!=null){
                Item item = store.getItem(itemId);
                if(item!=null)
                    return Response.getSuccessResponse(item);
                return Response.getFailResponse("Item does not exist");
            }
            return Response.getFailResponse("Store does not exist");
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }


    public Response<Boolean> closeStore(UUID clientCredentials ,UUID storeId ) {
        try {
            checkExistStore(storeId);
            if (storeMap.get(storeId).closeStore()) {
                return Response.getSuccessResponse(true);
            }
            return Response.getFailResponse("store already close");
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> reopenStore(UUID clientCredentials ,UUID storeId ) {
        try {
            checkExistStore(storeId);
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
            checkExistStore(storeId);
            if (storeMap.get(storeId).shutdownStore()) {
                return Response.getSuccessResponse(true);
            }
            return Response.getFailResponse("can not shutdown store");
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Item> getItem(UUID itemId){

                //check search for the store of these item, and return the item
        Item item = null;
        for (Store cuurentStore : storeMap.values()){
            if (cuurentStore.hasItem(itemId)){
                item = cuurentStore.getItem(itemId);
                return Response.getSuccessResponse(item);
            }
        }

                // if got here, no store contains these item ID
        return Response.getFailResponse("item doesn't exist");

    }

    // create item and add it to a store
    public Response<Item> addItem(String name, double price, UUID storeId, int quantity, String description){

        if (!hasStore(storeId))
            return Response.getFailResponse("store doesn't exist");

        UUID id = UUID.randomUUID();
        Item item = new Item(id, name, price, storeId, 0, quantity,description);


        //add the item to the store
        Store store = getStore(storeId).getValue();
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
        Item item = null;
        for (Store cuurentStore : storeMap.values()){
            if (cuurentStore.hasItem(itemId)){
                return true;
            }
        }

        // if got here, no store contains these item ID
        return false;
    }



    public Response<ShoppingCart> getShoppingCart(UUID clientId){
        Client client = userController.getClientOrUser(clientId);
        if(client == null)
            return Response.getFailResponse("client not exist");
        return  Response.getSuccessResponse(client.getCart());


    }
//TODO : guy why we need this?
    public Response<Store> getStore(UUID clientId, String storeName, String storeDescription){return  null;}
    public Response<Store> getStore(UUID storeId){
        checkExistStore(storeId);
        Store store = storeMap.get(storeId);
        return Response.getSuccessResponse(store);
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
        Store store = getStore(storeId).getValue();
        store.addRating(rating);
        return Response.getSuccessResponse(true);
    }

    //calculate new rating given a new one
    public Response<Boolean> addItemRating(UUID itemId, int rating){

        Item item = getItem(itemId).getValue();
        item.addRating(rating);
        return Response.getSuccessResponse(true);
    }

    public Response<Boolean> setItemDescription(UUID clientId, UUID itemId, String description){
        if(!itemExist(itemId))
            return Response.getFailResponse("item not exist");
        Item item = getItem(itemId).getValue();
        item.setDescription(description);
        return Response.getSuccessResponse(true);

    }

    public Response<Boolean> setItemQuantity(UUID clientId, UUID itemId, int newQuantity){
        if(!itemExist(itemId))
            return Response.getFailResponse("item not exist");
        Item item = getItem(itemId).getValue();
        item.setQuantity(newQuantity);
        return Response.getSuccessResponse(true);
    }

    //change the name of an item
    public Response<Boolean> setItemName(UUID clientId, UUID itemId, String newName){
        if(!itemExist(itemId))
            return Response.getFailResponse("item not exist");
        Item item = getItem(itemId).getValue();
        item.setName(newName);
        return Response.getSuccessResponse(true);
    }
    //change the Price of an item
    public Response<Boolean> setItemPrice(UUID clientId, UUID itemId, double newPrice){
        if(!itemExist(itemId))
            return Response.getFailResponse("item not exist");
        Item item = getItem(itemId).getValue();
        item.setPrice(newPrice);
        return Response.getSuccessResponse(true);
    }

    public Response<List<User>> getStoreStaff(UUID clientCredentials, UUID storeId){
        try {
            Response<Store> response = this.getStore(storeId);
            if (!response.isError())
                return Response.getFailResponse("Store does not exist");
            if (!response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            List<User> staffList = new ArrayList<User>();
            for (UUID id : response.getValue().getRolesMap().keySet())
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
            return Response.getSuccessResponse(getStore(storeId).getValue().getSales(clientCredentials).stream().toList());
        }catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
//        Store store = getStore(storeId).getValue();
//        ConcurrentLinkedQueue<Sale> saleHistory = store.getSales();
//        return Response.getSuccessResponse(saleHistory.toArray().toString());
//    }

    //why do we need clientCredentials here? we call the setAsFounder function from Service.
    public  Response<Store> createStore(UUID clientCredentials , String storeName , String storeDescription ) {
        try {
            Store store = new Store(storeName, storeDescription);
            Response<Boolean> response = userController.setAsFounder(clientCredentials, store.getStoreID());
            if (response.isError())
                return Response.getFailResponse(response.getMessage());
            storeMap.put(store.getStoreID(), store);
            return Response.getSuccessResponse(store);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId, int newQuantity){
        try{
            Response<Store> response = this.getStore(storeId);
            if (!response.isError())
                return Response.getFailResponse("Store does not exist");
            if(!response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                && !response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                    return Response.getFailResponse("User doesn't have permission.");
            if(!response.getValue().getItems().containsKey(itemId))
                return Response.getFailResponse("Item does not exist");
            response.getValue().getItems().get(itemId).setQuantity(newQuantity);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setItemName(UUID clientCredentials, UUID storeId, UUID itemId, String name){
        try{
            Response<Store> response = this.getStore(storeId);
            if (!response.isError())
                return Response.getFailResponse("Store does not exist");
            if(!response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    && !response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User doesn't have permission.");
            if(!response.getValue().getItems().containsKey(itemId))
                return Response.getFailResponse("Item does not exist");
            response.getValue().getItems().get(itemId).setName(name);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setItemDescription(UUID clientCredentials, UUID storeId, UUID itemId, String description){
        try{
            Response<Store> response = this.getStore(storeId);
            if (!response.isError())
                return Response.getFailResponse("Store does not exist");
            if(!response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    && !response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User doesn't have permission.");
            if(!response.getValue().getItems().containsKey(itemId))
                return Response.getFailResponse("Item does not exist");
            response.getValue().getItems().get(itemId).setDescription(description);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setItemPrice(UUID clientCredentials, UUID storeId, UUID itemId, double price){
        try{
            Response<Store> response = this.getStore(storeId);
            if (!response.isError())
                return Response.getFailResponse("Store does not exist");
            if(!response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                    && !response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_ITEM_MANAGEMENT))
                return Response.getFailResponse("User doesn't have permission.");
            if(!response.getValue().getItems().containsKey(itemId))
                return Response.getFailResponse("Item does not exist");
            response.getValue().getItems().get(itemId).setPrice(price);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }





//    protected Store getStore(UUID storeId){
//        return storeMap.get(storeId);
//    }
    protected boolean hasStore(UUID storeId){
        return storeMap.containsKey(storeId);
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

