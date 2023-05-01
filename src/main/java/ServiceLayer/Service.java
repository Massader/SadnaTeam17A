package ServiceLayer;

import DomainLayer.Market.*;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Sale;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.*;
import DomainLayer.Payment.PaymentController;
import DomainLayer.Payment.PaymentProxy;
import DomainLayer.Security.SecurityController;
import DomainLayer.Supply.SupplyController;
import DomainLayer.Supply.SupplyProxy;
import ServiceLayer.Loggers.ErrorLogger;
import ServiceLayer.Loggers.EventLogger;
import ServiceLayer.ServiceObjects.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

@org.springframework.stereotype.Service
public class Service {
    private static ServiceLayer.Service instance = null;
    private static final Object instanceLock = new Object();
    private final EventLogger eventLogger = EventLogger.getInstance();
    private final ErrorLogger errorLogger = ErrorLogger.getInstance();
    private StoreController storeController;
    private UserController userController;
    private PurchaseController purchaseController;
    private SecurityController securityController;
    private MessageController messageController;
    private SupplyController supplyController;
    private PaymentController paymentController;
    private SupplyProxy supplyProxy;
    private PaymentProxy paymentProxy;
    private NotificationController notificationController;
    private SearchController searchController;

    private boolean initialized;

    private Service() {
        initialized = false;
    }

    public static ServiceLayer.Service getInstance() {
        synchronized (instanceLock) {
            if (instance == null) {
                instance = new ServiceLayer.Service();
            }
        }
        return instance;
    }

    public boolean init() {
        synchronized (this) {
            if (initialized) return true;
            initialized = true;
        }
        eventLogger.log(Level.INFO, "Booting system");
        storeController = StoreController.getInstance();
        storeController.init();
        userController = UserController.getInstance();
        userController.init();  // Creates default admin
        purchaseController = PurchaseController.getInstance();
        purchaseController.init();
        securityController = SecurityController.getInstance();
        //securityController.init();
        messageController = MessageController.getInstance();
        //messageController.init();
        supplyController = SupplyController.getInstance();
        //supplyController.init();
        paymentController = PaymentController.getInstance();
//        supplyProxy.setReal();;
//        paymentProxy.setReal();

        //paymentController.init();
        notificationController = NotificationController.getInstance();
        notificationController.init();
        searchController = SearchController.getInstance();
        //searchController.init();

        //Add Supply and Payment JSON config file read here


        eventLogger.log(Level.INFO, "System boot successful.");
        return true;
    }
    public Response<UUID> createClient(){
        Response<UUID> response = userController.createClient();
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
        }
        else{
            eventLogger.log(Level.INFO, "Successfully created client " + response.getValue());
        }
        return response;
    }

    public Response<ServiceUser> login(UUID clientCredentials, String username, String password) {
        Response<User> response = userController.login(clientCredentials, username, password);
        if (response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        eventLogger.log(Level.INFO, "Successfully logged in user " + username);
        return Response.getSuccessResponse(new ServiceUser(response.getValue()));
    }

    public Response<UUID> logout(UUID clientCredentials){
        Response<UUID> response =  userController.logout(clientCredentials);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
        }
        else{
            eventLogger.log(Level.INFO, "Successfully logged out user " + clientCredentials);
        }
        return response;
    }

    public Response<Boolean> changePassword(UUID clientCredentials, String oldPassword, String newPassword){
        Response<Boolean> response = securityController.changePassword(clientCredentials, oldPassword, newPassword);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully changed user " + clientCredentials + " password");
        }
        return response;
    }

    public Response<Boolean> closeClient(UUID clientCredentials){
        Response<Boolean> response = userController.closeClient(clientCredentials);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully closed client " + clientCredentials);
        }
        return response;
    }

    public Response<Boolean> closeStore(UUID clientCredentials, UUID storeId){
        Response<Boolean> response = storeController.closeStore(clientCredentials, storeId);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully closed store " + storeId);
        }
        return response;
    }

    public Response<Boolean> reopenStore(UUID clientCredentials, UUID storeId){
        Response<Boolean> response = storeController.reopenStore(clientCredentials, storeId);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully reopened store " + storeId);
        }
        return response;
    }

    public Response<Boolean> shutdownStore(UUID clientCredentials, UUID storeId){
        Response<Boolean> response = storeController.shutdownStore(clientCredentials, storeId);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully shutdown store " + storeId);
        }
        return response;
    }

    public Response<Boolean> deleteUser(UUID clientCredentials, UUID userToDelete){
        Response<Boolean> response = userController.deleteUser(clientCredentials, userToDelete);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully deleted user " + userToDelete);
        }
        return response;
    }

    public Response<Boolean> validateOrder(UUID clientCredentials/*, args*/){
        Response<Boolean> response = supplyController.validateOrder(/*args*/);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully validated order details for user " + clientCredentials);
        }
        return response;
    }

    public Response<Boolean> validatePayment(UUID clientCredentials/*, args*/){
        Response<Boolean> response = paymentController.validatePaymentDetails(/*args*/);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
        }
        else{
            eventLogger.log(Level.INFO, "Successfully validated payment details for user " + clientCredentials);
        }
        return response;
    }

    public Response<UUID> confirmOrder(UUID clientCredentials){
        Response<Integer> response = supplyController.sendOrder();
        if(!response.isError()) {
            response = paymentController.requestPayment();
            UUID confirmationId = UUID.randomUUID();
            if (!response.isError())
                userController.clearCart(clientCredentials);
            eventLogger.log(Level.INFO, "Successfully sent order for user " + clientCredentials);
            return Response.getSuccessResponse(confirmationId);
        }
        errorLogger.log(Level.SEVERE, response.getMessage());
        return Response.getFailResponse(response.getMessage());
    }

    public Response<List<ServiceItem>> searchItem(String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating){
        Response<List<Item>> response = searchController.searchItem(keyword, category, minPrice, maxPrice, itemRating, storeRating);
        if(response.isError())
            return Response.getFailResponse(response.getMessage());
        List<ServiceItem> list = new ArrayList<ServiceItem>();
        for (Item item : response.getValue()) {
            list.add(new ServiceItem(item));
        }
        return Response.getSuccessResponse(list);
    }

    public Response<ServiceStore> getStoreInformation(UUID storeId){
        Response<Store> response = storeController.getStoreInformation(storeId);
        if(response.isError())
            return Response.getFailResponse(response.getMessage());
        return Response.getSuccessResponse(new ServiceStore(response.getValue()));
    }

    public Response<ServiceItem> getItemInformation(UUID storeId, UUID itemId){
        Response<Item> response = storeController.getItemInformation(storeId, itemId);
        if(response.isError())
            return Response.getFailResponse(response.getMessage());
        return Response.getSuccessResponse(new ServiceItem(response.getValue()));
    }

    public Response<ServiceStore> createStore(UUID clientCredentials , String storeName , String storeDescription){
        Response<User> userResponse = userController.getUser(clientCredentials);
        if(userResponse.isError()) {
            errorLogger.log(Level.SEVERE, userResponse.getMessage());
            return Response.getFailResponse(userResponse.getMessage());
        }
        Response<Store> storeResponse = storeController.createStore(clientCredentials, storeName, storeDescription);
        if(storeResponse.isError()) {
            errorLogger.log(Level.SEVERE, storeResponse.getMessage());
            return Response.getFailResponse(storeResponse.getMessage());
        }
        eventLogger.log(Level.INFO, "Successfully created new store " + storeResponse.getValue().getName());
        return Response.getSuccessResponse(new ServiceStore(storeResponse.getValue()));
    }

    public Response<List<ServiceShoppingBasket>> getCart(UUID clientCredentials){
        Response<ShoppingCart> response = userController.viewCart(clientCredentials);
        if(response.isError())
            return Response.getFailResponse(response.getMessage());
        List<ServiceShoppingBasket> cart = new ArrayList<>();
        for (ShoppingBasket basket : response.getValue().getShoppingBaskets().values()) {
            cart.add(new ServiceShoppingBasket(basket));
        }
        return Response.getSuccessResponse(cart);
    }

    public Response<UUID> postReview(UUID clientCredentials, UUID itemId, String reviewBody){
        Response<User> userResponse = userController.getUser(clientCredentials);
        if(userResponse.isError()) {
            errorLogger.log(Level.SEVERE, userResponse.getMessage());
            return Response.getFailResponse(userResponse.getMessage());
        }
        Response<UUID> reviewResponse = storeController.postReview(clientCredentials, itemId, reviewBody);
        if(reviewResponse.isError()) {
            errorLogger.log(Level.SEVERE, reviewResponse.getMessage());
            return Response.getFailResponse(reviewResponse.getMessage());
        }
        eventLogger.log(Level.INFO, "Successfully posted review by " + userResponse.getValue().getUsername() + " for item "
                + itemId);
        return reviewResponse;
    }


    public Response<Boolean> setManagerPermissions(UUID clientCredentials, UUID manager,
                                                   UUID storeId, List<Integer> permissions){
        Response<Boolean> response = userController.setManagerPermissions(clientCredentials, manager, storeId, permissions);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        eventLogger.log(Level.INFO, "Successfully set manager permissions for user " + manager + " in store " + storeId);
        return response;
    }

    public Response<List<ServiceUser>> getStoreStaff(UUID clientCredentials, UUID storeId){
        Response<List<User>> response = storeController.getStoreStaff(clientCredentials, storeId);
        if(response.isError())
            return Response.getFailResponse(response.getMessage());
        List<ServiceUser> serviceUsers = new ArrayList<ServiceUser>();
        for(User user : response.getValue())
            serviceUsers.add(new ServiceUser(user));
        return Response.getSuccessResponse(serviceUsers);
    }

    public Response<Boolean> appointStoreManager(UUID clientCredentials, UUID appointee, UUID storeId){
        Response<Boolean> response = userController.appointStoreManager(clientCredentials, appointee, storeId);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        eventLogger.log(Level.INFO, "Successfully appointed user " + appointee + " as manager in store " + storeId);
        return response;
    }

    public Response<Boolean> appointStoreOwner(UUID clientCredentials, UUID appointee, UUID storeId){
        Response<Boolean> response = userController.appointStoreOwner(clientCredentials, appointee, storeId);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully appointed user " + appointee + " as owner in store " + storeId);
        }
        return response;
    }

    public Response<Boolean> removeStoreRole(UUID clientCredentials, UUID roleToRemove, UUID storeId){
        Response<Boolean> response = userController.removeStoreRole(clientCredentials, roleToRemove, storeId);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
        }
        else{
            eventLogger.log(Level.INFO, "Successfully removed appointment of user " + roleToRemove + " as staff in store " + storeId);
        }
        return response;
    }

    public Response<Boolean> setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId, int newQuantity){
        Response<Boolean> response = storeController.setItemQuantity(clientCredentials, storeId, itemId, newQuantity);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully set quantity of item " + itemId + " to " + newQuantity);
        }
        return response;
    }

    public Response<Boolean> setItemName(UUID clientCredentials, UUID storeId, UUID itemId, String name){
        Response<Boolean> response = storeController.setItemName(clientCredentials, storeId, itemId, name);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully set name of item " + itemId + " to " + name);
        }
        return response;
    }

    public Response<Boolean> setItemDescription(UUID clientCredentials, UUID storeId, UUID itemId, String description){
        Response<Boolean> response = storeController.setItemDescription(clientCredentials, storeId, itemId, description);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully set description of item " + itemId);
        }
        return response;
    }

    public Response<Boolean> setItemPrice(UUID clientCredentials, UUID storeId, UUID itemId, double price){
        Response<Boolean> response = storeController.setItemPrice(clientCredentials, storeId, itemId, price);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully set price of item " + itemId + " to " + price);
        }
        return response;
    }

    public Response<List<ServicePurchase>> getPurchaseHistory(UUID clientCredentials, UUID user ){
        Response<List<Purchase>> response = userController.getPurchaseHistory(clientCredentials,user);
        if(response.isError())
            return Response.getFailResponse(response.getMessage());
        List<ServicePurchase> output = new ArrayList<ServicePurchase>();
        for (Purchase purchase : response.getValue()) {
            output.add(new ServicePurchase(purchase));
        }
        return Response.getSuccessResponse(output);
    }


    public Response<List<ServiceSale>> getStoreSaleHistory(UUID clientCredentials,UUID storeId) {
        Response<List<Sale>> response = storeController.getStoreSaleHistory(clientCredentials, storeId);
        if (response.isError())
            return Response.getFailResponse(response.getMessage());
        List<ServiceSale> output = new ArrayList<>();
        for (Sale sale : response.getValue()) {
            output.add(new ServiceSale(sale));
        }
        return Response.getSuccessResponse(output);
    }

    public Response<ServiceUser> getUserInfo(UUID clientCredentials){
        Response<User> response = userController.getUser(clientCredentials);
        if(response.isError())
            return Response.getFailResponse(response.getMessage());
        return Response.getSuccessResponse(new ServiceUser(response.getValue()));
    }

    public Response<Boolean> addItemToCart(UUID clientCredentials, UUID itemId, int quantity, UUID storeId) {
        Response<Boolean> response = userController.addItemToCart(clientCredentials,itemId,quantity, storeId);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully added item " + itemId + " to " + clientCredentials + " cart");
        }
        return response;
    }

    public Response<Boolean> removeItemFromCart(UUID clientCredentials, UUID itemId ,int  quantity, UUID storeID ) {
        Response<Boolean> response = userController.removeItemFromCart(clientCredentials,itemId,quantity, storeID);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully removed item " + itemId + " from " + clientCredentials + " cart");
        }
        return response;
    }

    public  Response<Double> getCartTotal(UUID clientCredentials){
        Response<Double> response = storeController.getCartTotal(clientCredentials);
        return response;
    }

    public Response<Boolean> validateSecurityQuestion(UUID clientCredentials, String answer ){
        Response<Boolean> response = securityController.ValidateSecurityQuestion(clientCredentials,answer);
        if(response.isError()|| response.getValue()==null)
            return Response.getFailResponse(response.getMessage());
        return response;
    }

    public Response<Boolean> addSecurityQuestion(UUID clientCredentials,String question ,String answer ) {
        Response<Boolean> response = securityController.addSecurityQuestion(clientCredentials,question,answer);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully added security question to " + clientCredentials);
        }
        return response;
    }

    public Response<String> getSecurityQuestion(UUID clientCredentials ) {
        Response<String> response = securityController.getSecurityQuestion(clientCredentials);
        return response;
    }

    public Response<Boolean> addStoreRating(UUID clientCredentials, UUID storeId ,int rating){
        Response<Boolean> userResponse = userController.isUser(clientCredentials);
        if(userResponse.isError()) {
            errorLogger.log(Level.SEVERE, userResponse.getMessage());
            return userResponse;
        }
        Response<Boolean> ratingResponse = storeController.addStoreRating(storeId,rating);
        if(ratingResponse.isError()){
            errorLogger.log(Level.SEVERE, ratingResponse.getMessage());
            return ratingResponse;
        }
        eventLogger.log(Level.INFO, "Successfully added rating to store " + storeId);
        return ratingResponse;
    }


    public Response<Boolean> addItemRating(UUID clientCredentials, UUID itemId, UUID storeId, int rating){
        Response<Boolean> response1 = userController.isUser(clientCredentials);
        if(response1.isError())
            return response1;
        Response<Boolean> ratingResponse = storeController.addItemRating(itemId, storeId, rating);
        if(ratingResponse.isError()){
            errorLogger.log(Level.SEVERE, ratingResponse.getMessage());
            return ratingResponse;
        }
        eventLogger.log(Level.INFO, "Successfully added rating to item " + itemId);
        return ratingResponse;
    }

    public Response<Boolean> register(String username,String password) {
        Response<Boolean> response = userController.register(username,password);
        if(response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully registered user " + username);
        }
        return response;
    }

    public Response<Boolean> registerAdmin(UUID clientCredentials, String username, String password) {
        Response<Boolean> response = userController.registerAsAdmin(clientCredentials,username,password);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully registered admin " + username);
        }
        return response;
    }

    public Response<ServiceItem> addItemToStore(UUID clientCredentials,String name, double price, UUID storeId, int quantity, String description){
        Response<Item> response = storeController.addItemToStore(clientCredentials,name,price,storeId,quantity,description);
        if(response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            Response.getFailResponse(response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Successfully add "+quantity+" Item: "+name+" to store ");
        }
        return Response.getSuccessResponse(new ServiceItem(response.getValue()));
    }

    public Response<UUID> sendMessage(UUID clientCredentials, UUID sender, UUID recipient, String body) {
        Response<UUID> response = messageController.sendMessage(clientCredentials, sender, recipient, body);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
        }else{
            eventLogger.log(Level.INFO, "Message sent from " + sender + " to " + recipient);
        }
        return response;
    }

    public Response<List<ServiceMessage>> getMessages(UUID clientCredentials, UUID recipient) {
        Response<List<Message>> response = messageController.getMessages(clientCredentials, recipient);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        List<ServiceMessage> output = new ArrayList<>();
        for (Message message : response.getValue())
            output.add(new ServiceMessage(message));
        return Response.getSuccessResponse(output);
    }

    public Response<ServiceMessage> getMessage(UUID clientCredentials, UUID recipient, UUID messageId) {
        Response<Message> response = messageController.getMessage(clientCredentials, recipient, messageId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        return Response.getSuccessResponse(new ServiceMessage(response.getValue()));
    }

    public Response<UUID> sendComplaint(UUID clientCredentials, UUID purchaseId, String body) {
        Response<UUID> response = messageController.sendComplaint(clientCredentials, purchaseId, body);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return response;
        }
        eventLogger.log(Level.INFO, "Complaint made by " + clientCredentials + " for purchase " + purchaseId + " sent successfully.");
        return response;
    }

    public Response<List<ServiceComplaint>> getComplaints(UUID clientCredentials) {
        Response<List<Complaint>> response = messageController.getComplaints(clientCredentials);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        List<ServiceComplaint> output = new ArrayList<>();
        for (Complaint complaint : response.getValue())
            output.add(new ServiceComplaint(complaint));
        return Response.getSuccessResponse(output);
    }

    public Response<ServiceComplaint> getComplaint(UUID clientCredentials, UUID complaintId) {
        Response<Complaint> response = messageController.getComplaint(clientCredentials, complaintId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        return Response.getSuccessResponse(new ServiceComplaint(response.getValue()));
    }

    public Response<Boolean> assignAdminToComplaint(UUID clientCredentials, UUID complaintId) {
        Response<Boolean> response = messageController.assignAdminToComplaint(clientCredentials, complaintId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
        }
        return response;
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

    public Response<Boolean> addItemCategory(UUID clientCredentials, UUID storeId, UUID itemId, String category) {
        Response<Boolean> response = storeController.addItemCategory(clientCredentials, storeId, itemId, category);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return response;
        }
        eventLogger.log(Level.INFO, "Successfully added category " + category + " to item " + itemId);
        return response;
    }

    public Response<Boolean> removeItemFromStore(UUID clientCredentials, UUID storeId, UUID itemId) {
        Response<Boolean> response = storeController.removeItemFromStore(clientCredentials, storeId, itemId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return response;
        }
        eventLogger.log(Level.INFO, "Successfully removed item " + itemId + " from store " + storeId);
        return response;
    }

    public Response<Boolean> purchaseCart(UUID clientCredentials,  double expectedPrice, String address, int credit){
        Response<ShoppingCart> response1 = userController.viewCart(clientCredentials);
        if(response1.isError()){
            errorLogger.log(Level.WARNING, response1.getMessage());
            return Response.getFailResponse(response1.getMessage());
        }
        Response<Boolean> response2 = purchaseController.purchaseCart(userController.getClientOrUser(clientCredentials),response1.getValue(),expectedPrice,address,credit);
        if(response2.isError()){
            errorLogger.log(Level.WARNING, response2.getMessage());
            return Response.getFailResponse(response2.getMessage());
        }
        return response2;
    }
}

