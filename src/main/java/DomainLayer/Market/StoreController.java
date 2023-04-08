package DomainLayer.Market;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.SaleHistory;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.*;
import DomainLayer.Security.SecurityController;
import ServiceLayer.Response;

import java.util.HashMap;
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
    private void checkExistStore(UUID storeId){
        if(!this.storeMap.containsKey(storeId))
            throw new IllegalArgumentException("no Store with id :" + storeId );}


    public Boolean closeStore(UUID clientCredentials ,UUID storeId ) {
        checkExistStore(storeId);
        return storeMap.get(storeId).closeStore();

        }

    public Boolean reopenStore(UUID clientCredentials ,UUID storeId ) {
        checkExistStore(storeId);
        return storeMap.get(storeId).reopenStore();
    }

    public Boolean shutdownStore(UUID clientCredentials , UUID storeId ) {
    checkExistStore(storeId);
    return storeMap.get(storeId).ShutDown();

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
    public Response<Item> addItem(String name, double price, UUID storeId, int quantity){

        if (!hasStore(storeId))
            return Response.getFailResponse("store doesn't exist");

        UUID id = UUID.randomUUID();
        Item item = new Item(id, name, price, storeId, 0, quantity);

        //add the item to the store
        Store store = getStore(storeId);
        store.addItem(item);

        return Response.getSuccessResponse(item);
    }


    public Response<Integer> getItemQuantity(UUID itemId){
        if (!itemExist(itemId))
            return Response.getFailResponse("item doesnt exist");
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
//        Client client = userController.getClientOrUser(clientId);
//        if(client == null)
//            return Response.getFailResponse("item not exist");
//

    }

    public Response<Store> getStore(UUID clientId, String storeName, String storeDescription){

    }

    public Response<UUID> postReview(UUID clientId, UUID itemId, String reviewBody){

    }

    //calculate new rating given a new one
    public Response<Boolean> addStoreRating(UUID storeId ,int rating){
        Store store = getStore(storeId);
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

    }

    public Boolean setItemQuantity(UUID clientId, UUID itemId, int newQuantity){

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

    public Response<List<User>> getStoreStaff(UUID clientId, UUID itemId){

    }

    public Response<List<Message>> getStoreMessages(UUID clientId, UUID itemId){

    }

    public Response<SaleHistory> getStoreSaleHistory(UUID clientId, UUID itemId){

    }
    public Response<Boolean> shutDownStore(UUID clientId, UUID itemId){

    }

    protected Store getStore(UUID storeId){
        return storeMap.get(storeId);
    }
    protected boolean hasStore(UUID storeId){
        return storeMap.contains(storeId);
    }

    //add a new store
    protected UUID addStore(Store store){
        UUID id = UUID.randomUUID();
        storeMap.put(id, store);
        return id;
    }
    protected void addStore(Store store, UUID id){
        storeMap.put(id, store);
    }





}

