package ServiceLayer;

import DomainLayer.Market.*;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Sale;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.Purchase;
import DomainLayer.Market.Users.ShoppingBasket;
import DomainLayer.Market.Users.ShoppingCart;
import DomainLayer.Market.Users.User;
import DomainLayer.Payment.PaymentController;
import DomainLayer.Security.SecurityController;
import DomainLayer.Supply.SupplyController;
import ServiceLayer.Loggers.ErrorLogger;
import ServiceLayer.Loggers.EventLogger;
import ServiceLayer.ServiceObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import static java.util.logging.Logger.getLogger;

public class Service {
    private static Service instance = null;
    private static final Object instanceLock = new Object();
    private final EventLogger eventLogger = EventLogger.getInstance();
    private final ErrorLogger errorLogger = ErrorLogger.getInstance();
    private StoreController storeController;
    private UserController userController;
    private SecurityController securityController;
    private MessageController messageController;
    private SupplyController supplyController;
    private PaymentController paymentController;
    private NotificationController notificationController;
    private SearchController searchController;

    private Service() {
    }

    public static Service getInstance() {
        synchronized (instanceLock) {
            if (instance == null) {
                instance = new Service();
            }
        }
        return instance;
    }

    public boolean init() {
        errorLogger.log(Level.INFO, "Booting system");
        storeController = StoreController.getInstance();
        storeController.init();
        userController = UserController.getInstance();
        userController.init();  // Creates default admin
        securityController = SecurityController.getInstance();
        //securityController.init();
        messageController = MessageController.getInstance();
        //messageController.init();
        supplyController = SupplyController.getInstance();
        //supplyController.init();
        paymentController = PaymentController.getInstance();
        //paymentController.init();
        notificationController = NotificationController.getInstance();
        notificationController.init();
        searchController = SearchController.getInstance();
        //searchController.init();

        //Add Supply and Payment JSON config file read here


        eventLogger.log(Level.INFO, "System boot successful.");
        return true;
    }
    public UUID createClient(){
        Response<UUID> response = userController.createClient();
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return null;
        }
        eventLogger.log(Level.INFO, "Successfully created client " + response.getValue());
        return response.getValue();
    }

    public UUID login(UUID clientCredentials, String username, String password) {
        Response<UUID> response = userController.login(clientCredentials, username, password);
        if (response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return null;
        }
        eventLogger.log(Level.INFO, "Successfully logged in user " + username);
        return response.getValue();
    }

    public UUID logout(UUID clientCredentials){
        Response<UUID> response =  userController.logout(clientCredentials);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return null;
        }
        eventLogger.log(Level.INFO, "Successfully logged out user " + clientCredentials);
        return response.getValue();
    }

    public boolean changePassword(UUID clientCredentials, String oldPassword, String newPassword){
        Response<Boolean> response = securityController.changePassword(clientCredentials, oldPassword, newPassword);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully changed user " + clientCredentials + " password");
        return response.getValue();
    }

    public boolean closeClient(UUID clientCredentials){
        Response<Boolean> response = userController.closeClient(clientCredentials);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully closed client " + clientCredentials);
        return response.getValue();
    }

    public boolean closeStore(UUID clientCredentials, UUID storeId){
        Response<Boolean> response = storeController.closeStore(clientCredentials, storeId);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully closed store " + storeId);
        return response.getValue();
    }

    public boolean reopenStore(UUID clientCredentials, UUID storeId){
        Response<Boolean> response = storeController.reopenStore(clientCredentials, storeId);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully reopened store " + storeId);
        return response.getValue();
    }

    public boolean shutdownStore(UUID clientCredentials, UUID storeId){
        Response<Boolean> response = storeController.shutdownStore(clientCredentials, storeId);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully shutdown store " + storeId);
        return response.getValue();
    }

    public boolean deleteUser(UUID clientCredentials, UUID userToDelete){
        Response<Boolean> response = userController.deleteUser(clientCredentials, userToDelete);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully deleted user " + userToDelete);
        return response.getValue();
    }

    public boolean validateOrder(UUID clientCredentials/*, args*/){
        Response<Boolean> response = supplyController.validateOrder(/*args*/);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully validated order details for user " + clientCredentials);
        return response.getValue();
    }

    public boolean validatePayment(UUID clientCredentials/*, args*/){
        Response<Boolean> response = paymentController.validatePaymentDetails(/*args*/);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully validated payment details for user " + clientCredentials);
        return response.getValue();
    }

    public UUID confirmOrder(UUID clientCredentials){
        Response<Integer> response = supplyController.sendOrder();
        if(!response.isError()) {
            response = paymentController.requestPayment();
            UUID confirmationId = UUID.randomUUID();
            if (!response.isError())
                userController.clearCart(clientCredentials);
                eventLogger.log(Level.INFO, "Successfully sent order for user " + clientCredentials);
                return confirmationId;
        }
        errorLogger.log(Level.SEVERE, response.getMessage());
        return null;
    }

    public List<ServiceItem> searchItem(String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating){
        Response<List<Item>> response = searchController.searchItem(keyword, category, minPrice, maxPrice, itemRating, storeRating);
        if(response.isError())
            return null;
        List<ServiceItem> list = new ArrayList<ServiceItem>();
        for (Item item : response.getValue()) {
            list.add(new ServiceItem(item));
        }
        return list;
    }

    public ServiceStore getStoreInformation(UUID storeId){
        Response<Store> response = storeController.getStoreInformation(storeId);
        if(response.isError())
            return null;
        return new ServiceStore(response.getValue());
    }

    public ServiceItem getItemInformation(UUID storeId, UUID itemId){
        Response<Item> response = storeController.getItemInformation(storeId, itemId);
        if(response.isError())
            return null;
        return new ServiceItem(response.getValue());
    }

    public ServiceStore createStore(UUID clientCredentials , String storeName , String storeDescription){
        Response<User> userResponse = userController.getUser(clientCredentials);
        if(userResponse.isError()) {
            errorLogger.log(Level.SEVERE, userResponse.getMessage());
            return null;
        }
        Response<Store> storeResponse = storeController.createStore(clientCredentials, storeName, storeDescription);
        if(storeResponse.isError()) {
            errorLogger.log(Level.SEVERE, storeResponse.getMessage());
            return null;
        }
        eventLogger.log(Level.INFO, "Successfully created new store " + storeResponse.getValue().getName());
        return new ServiceStore(storeResponse.getValue());
    }

    public List<ServiceShoppingBasket> viewCart(UUID clientCredentials){
        Response<ShoppingCart> response = userController.viewCart(clientCredentials);
        if(response.isError())
            return null;
        List<ServiceShoppingBasket> cart = new ArrayList<>();
        for (ShoppingBasket basket : response.getValue().getShoppingBaskets().values()) {
            cart.add(new ServiceShoppingBasket(basket));
        }
        return cart;
    }

    public UUID postReview(UUID clientCredentials, UUID itemId, String reviewBody){
        Response<User> userResponse = userController.getUser(clientCredentials);
        if(userResponse.isError()) {
            errorLogger.log(Level.SEVERE, userResponse.getMessage());
            return null;
        }
        Response<UUID> reviewResponse = storeController.postReview(clientCredentials, itemId, reviewBody);
        if(reviewResponse.isError()) {
            errorLogger.log(Level.SEVERE, reviewResponse.getMessage());
            return null;
        }
        eventLogger.log(Level.INFO, "Successfully posted review by " + userResponse.getValue().getUsername() + " for item "
                + itemId);
        return reviewResponse.getValue();
    }

    public boolean SetManagerPermissions(UUID clientCredentials, UUID manager,
                                         UUID storeId, List<Integer> permissions){
        Response<Boolean> response = userController.setManagerPermissions(clientCredentials, manager, storeId, permissions);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully set manager permissions for user " + manager + " in store " + storeId);
        return response.getValue();
    }

    public List<ServiceUser> getStoreStaff(UUID clientCredentials, UUID storeId){
        Response<List<User>> response = storeController.getStoreStaff(clientCredentials, storeId);
        if(response.isError())
            return null;
        List<ServiceUser> serviceUsers = new ArrayList<ServiceUser>();
        for(User user : response.getValue())
            serviceUsers.add(new ServiceUser(user));
        return serviceUsers;
    }

    public boolean appointStoreManager(UUID clientCredentials, UUID appointee, UUID storeId){
        Response<Boolean> response = userController.appointStoreManager(clientCredentials, appointee, storeId);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully appointed user " + appointee + " as manager in store " + storeId);
        return response.getValue();
    }

    public boolean appointStoreOwner(UUID clientCredentials, UUID appointee, UUID storeId){
        Response<Boolean> response = userController.appointStoreOwner(clientCredentials, appointee, storeId);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully appointed user " + appointee + " as owner in store " + storeId);
        return response.getValue();
    }

    public boolean removeStoreRole(UUID clientCredentials, UUID roleToRemove, UUID storeId){
        Response<Boolean> response = userController.removeStoreRole(clientCredentials, roleToRemove, storeId);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully removed appointment of user " + roleToRemove + " as staff in store " + storeId);
        return response.getValue();
    }

    public boolean setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId, int newQuantity){
        Response<Boolean> response = storeController.setItemQuantity(clientCredentials, storeId, itemId, newQuantity);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully set quantity of item " + itemId + " to " + newQuantity);
        return response.getValue();
    }

    public boolean setItemName(UUID clientCredentials, UUID storeId, UUID itemId, String name){
        Response<Boolean> response = storeController.setItemName(clientCredentials, storeId, itemId, name);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully set name of item " + itemId + " to " + name);
        return response.getValue();
    }

    public boolean setItemDescription(UUID clientCredentials, UUID storeId, UUID itemId, String description){
        Response<Boolean> response = storeController.setItemDescription(clientCredentials, storeId, itemId, description);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully set description of item " + itemId);
        return response.getValue();
    }

    public boolean setItemPrice(UUID clientCredentials, UUID storeId, UUID itemId, int price){
        Response<Boolean> response = storeController.setItemPrice(clientCredentials, storeId, itemId, price);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully set price of item " + itemId + " to " + price);
        return response.getValue();
    }

    public List<ServicePurchase> getPurchaseHistory(UUID clientCredentials, UUID user ){
        Response<List<Purchase>> response = userController.getPurchaseHistory(clientCredentials,user);
        if(response.isError())
            return null;
        List<ServicePurchase> output = new ArrayList<ServicePurchase>();
        for (Purchase purchase : response.getValue()) {
            output.add(new ServicePurchase(purchase));
        }
        return output;
    }


    public List<ServiceSale> getStoreSaleHistory(UUID clientCredentials,UUID storeId) {
        Response<List<Sale>> response = storeController.getStoreSaleHistory(clientCredentials, storeId);
        if (response.isError())
            return null;
        List<ServiceSale> output = new ArrayList<>();
        for (Sale sale : response.getValue()) {
            output.add(new ServiceSale(sale));
        }
        return output;
    }

    public ServiceUser getUserInfo(UUID clientCredentials){
        Response<User> response = userController.getUser(clientCredentials);
        if(response.isError())
            return null;
        return new ServiceUser(response.getValue());
    }

    public Boolean addItemToCart(UUID clientCredentials, UUID itemId, int quantity, UUID storeId) {
        Response<Boolean> response = userController.addItemToCart(clientCredentials,itemId,quantity, storeId);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully added item " + itemId + " to " + clientCredentials + " cart");
        return response.getValue();
    }

    public Boolean removeItemFromCart(UUID clientCredentials, UUID itemId ,int  quantity, UUID storeID ) {
        Response<Boolean> response = userController.removeItemFromCart(clientCredentials,itemId,quantity, storeID);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully removed item " + itemId + " from " + clientCredentials + " cart");
        return response.getValue();
    }

    public Double getCartTotal(UUID clientCredentials){
        Response<Double> response = storeController.getCartTotal(clientCredentials);
        if(response.isError())
            return null;
        return response.getValue();
    }

    public Boolean validateSecurityQuestion(UUID clientCredentials, String answer ){
        Response<Boolean> response = securityController.ValidateSecurityQuestion(clientCredentials,answer);
        if(response.isError()|| response.getValue()==null)
            return null;
        return response.getValue();
    }

    public Boolean addSecurityQuestion(UUID clientCredentials,String question ,String answer ) {
        Response<Boolean> response = securityController.addSecurityQuestion(clientCredentials,question,answer);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully added security question to " + clientCredentials);
        return response.getValue();
    }

    public  String getSecurityQuestion(UUID clientCredentials ) {
        Response<String> response = securityController.getSecurityQuestion(clientCredentials);
        if(response.isError())
            return null;
        return response.getValue();
    }


    public Boolean addStoreRating(UUID clientCredentials, UUID storeId ,int rating){
        Response<Boolean> userResponse = userController.isUser(clientCredentials);
        if(userResponse.isError()) {
            errorLogger.log(Level.SEVERE, userResponse.getMessage());
            return false;
        }
        Response<Boolean> ratingResponse = storeController.addStoreRating(storeId,rating);
        if(ratingResponse.isError()){
            errorLogger.log(Level.SEVERE, ratingResponse.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully added rating to store " + storeId);
        return ratingResponse.getValue();
    }


    public Boolean addItemRating(UUID clientCredentials, UUID itemId, UUID storeId, int rating){
        Response<Boolean> response1 = userController.isUser(clientCredentials);
        if(response1.isError())
            return false;
        Response<Boolean> ratingResponse = storeController.addItemRating(itemId, storeId, rating);
        if(ratingResponse.isError()){
            errorLogger.log(Level.SEVERE, ratingResponse.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully added rating to item " + itemId);
        return ratingResponse.getValue();
    }

    public Boolean register(String username,String password) {
        Response<Boolean> response = userController.register(username,password);
        if(response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully registered user " + username);
        return response.getValue();
    }

    public boolean registerAdmin(UUID clientCredentials, String username, String password) {
        Response<Boolean> response = userController.registerAsAdmin(clientCredentials,username,password);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return false;
        }
        eventLogger.log(Level.INFO, "Successfully registered admin " + username);
        return response.getValue();
    }

    public ServiceItem addItemToStore(UUID clientCredentials,String name, double price, UUID storeId, int quantity, String description){
        Response<Item> response = storeController.addItemToStore(clientCredentials,name,price,storeId,quantity,description);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return null;
        }
        eventLogger.log(Level.INFO, "Successfully add "+quantity+" Item: "+name+" to store ");
        return new ServiceItem(response.getValue());
    }

    public Void resetService() {
        messageController.resetController();
        notificationController.resetController();
        searchController.resetController();
        storeController.resetController();
        userController.resetController();
        paymentController.resetController();
        securityController.resetController();
        supplyController.resetController();
        init();
        return null;
    }
}

