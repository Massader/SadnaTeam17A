package ServiceLayer;

import DataAccessLayer.RepositoryFactory;
import DomainLayer.Market.*;
import DomainLayer.Market.Stores.*;
import DomainLayer.Market.Stores.Discounts.Discount;
import DomainLayer.Market.Stores.PurchaseRule.*;
import DomainLayer.Market.Stores.PurchaseTypes.Bid;
import DomainLayer.Market.Users.*;
import DomainLayer.Market.Users.Roles.OwnerPetition;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Payment.PaymentController;
import DomainLayer.Payment.PaymentProxy;
import DomainLayer.Security.SecurityController;
import DomainLayer.Supply.SupplyController;
import DomainLayer.Supply.SupplyProxy;
import ServiceLayer.Loggers.ErrorLogger;
import ServiceLayer.Loggers.EventLogger;
import ServiceLayer.ServiceObjects.*;
import ServiceLayer.StateFileRunner.StateFileRunner;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.image.ReplicateScaleFilter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.logging.Level;

import static java.lang.System.exit;

@org.springframework.stereotype.Service
//@Transactional
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
    private RepositoryFactory repositoryFactory;

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

    public boolean init(RepositoryFactory repositoryFactory, StateFileRunner stateFileRunner) {
        try {
            synchronized (this) {
                if (initialized) return true;
                initialized = true;
            }
            eventLogger.log(Level.INFO, "Booting system");
            this.repositoryFactory = repositoryFactory;
            storeController = StoreController.getInstance();
            storeController.init(repositoryFactory);
            securityController = SecurityController.getInstance();
            securityController.init(repositoryFactory);
            userController = UserController.getInstance();
            userController.init(repositoryFactory);  // Creates default admin
            purchaseController = PurchaseController.getInstance(repositoryFactory);
            purchaseController.init();

            messageController = MessageController.getInstance();
            messageController.init(repositoryFactory);
            supplyController = SupplyController.getInstance();
            supplyController.init();
            paymentController = PaymentController.getInstance();
            paymentController.init();
            notificationController = NotificationController.getInstance();
            notificationController.init(repositoryFactory);
            searchController = SearchController.getInstance();
            searchController.init(repositoryFactory);
            registerAdmin(UUID.randomUUID(), "Admin", "Admin1");

    
            eventLogger.log(Level.INFO, "Reading state file.");
            try {
                stateFileRunner.run();
                eventLogger.log(Level.INFO, "State loaded.");
            } catch (Exception e) {
                errorLogger.log(Level.WARNING, "Failed to load state - " + e.getMessage());
            }

            eventLogger.log(Level.INFO, "System boot successful.");
            return true;
        } catch (Exception e) {
            eventLogger.log(Level.SEVERE, "Failed to load service - " + e.getMessage());
            return false;
        }
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

    public Response<ServiceUser> login(UUID clientCredentials, String username, String password,
                                       BiConsumer<UUID, Notification> notificationSender) {
        Response<User> response = userController.login(clientCredentials, username, password);
        if (response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        notificationController.addNotifier(response.getValue().getId(), notificationSender);
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
            notificationController.removeNotifier(clientCredentials);
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



    public Response<ServiceStore> getStoreInformation(UUID clientCredentials, UUID storeId){
        Response<Store> response = storeController.getStoreInformation(clientCredentials, storeId);
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
        Response<ShoppingCart> response = userController.getCart(clientCredentials);
        if(response.isError())
            return Response.getFailResponse(response.getMessage());
        List<ServiceShoppingBasket> cart = new ArrayList<>();
        for (ShoppingBasket basket : response.getValue().getShoppingBaskets()) {
            cart.add(new ServiceShoppingBasket(basket));
        }
        return Response.getSuccessResponse(cart);
    }

    public Response<UUID> postItemReview(UUID clientCredentials, UUID itemId, String reviewBody, int rating){
        Response<User> userResponse = userController.getUser(clientCredentials);
        if(userResponse.isError()) {
            errorLogger.log(Level.SEVERE, userResponse.getMessage());
            return Response.getFailResponse(userResponse.getMessage());
        }
        Response<UUID> reviewResponse = storeController.postReview(clientCredentials, itemId, reviewBody, rating);
        if(reviewResponse.isError()) {
            errorLogger.log(Level.SEVERE, reviewResponse.getMessage());
            return Response.getFailResponse(reviewResponse.getMessage());
        }
        eventLogger.log(Level.INFO, "Successfully posted review by " + userResponse.getValue().getUsername() + " for item "
                + itemId);
        return reviewResponse;
    }
    
    public Response<List<ServiceItemReview>> getItemReviews(UUID storeId, UUID itemId) {
        Response<List<ItemReview>> reviewsResponse = storeController.getItemReviews(storeId, itemId);
        if (reviewsResponse.isError()) {
            errorLogger.log(Level.WARNING, reviewsResponse.getMessage());
            return Response.getFailResponse(reviewsResponse.getMessage());
        }
        List<ServiceItemReview> output = new ArrayList<>();
        for (ItemReview itemReview : reviewsResponse.getValue()) {
            output.add(new ServiceItemReview(itemReview));
        }
        return Response.getSuccessResponse(output);
    }
    
    public Response<Boolean> isReviewableByUser(UUID clientCredentials, UUID storeId, UUID itemId) {
        Response<Boolean> response = storeController.isReviewableByUser(clientCredentials, storeId, itemId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
        }
        return response;
    }
    
    public Response<Boolean> isReviewableByUser(UUID clientCredentials, UUID storeId) {
        Response<Boolean> response = storeController.isReviewableByUser(clientCredentials, storeId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
        }
        return response;
    }

    // Sets manager's permissions to the list, i.e adds and removes.
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
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
        }
        return response;
    }

    public Response<ServiceUser> validateSecurityQuestion(UUID clientCredentials, String answer, BiConsumer<UUID, Notification> notificationSender){
        Response<Boolean> response = securityController.validateSecurityQuestion(clientCredentials, answer);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        Response<User> userResponse = userController.getUser(clientCredentials);
        if (userResponse.isError()) {
            errorLogger.log(Level.SEVERE, userResponse.getMessage());
            return Response.getFailResponse(userResponse.getMessage());
        }
        notificationController.addNotifier(clientCredentials, notificationSender);
        return Response.getSuccessResponse(new ServiceUser(userResponse.getValue()));
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

    public Response<Double> addStoreRating(UUID clientCredentials, UUID storeId ,int rating){
        Response<Boolean> userResponse = userController.isUser(clientCredentials);
        if(userResponse.isError()) {
            errorLogger.log(Level.SEVERE, userResponse.getMessage());
            return Response.getFailResponse(userResponse.getMessage());
        }
        Response<Double> ratingResponse = storeController.addStoreRating(storeId,rating);
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
        Response<Boolean> ratingResponse = storeController.addItemRating(clientCredentials, itemId, storeId, rating);
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
            return Response.getFailResponse(response.getMessage());
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

    public Response<UUID> sendComplaint(UUID clientCredentials, UUID purchaseId, UUID storeId, UUID itemId, String body) {
        Response<UUID> response = messageController.sendComplaint(clientCredentials, purchaseId, storeId, itemId, body);
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
        //init(repositoryFactory, new StateFileRunner(new ObjectMapper(), this));
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

    public Response<Boolean> purchaseCart(UUID clientCredentials, double expectedPrice, String address, String city, String country, int zip,String card_number, String month, String year, String holder, String cvv, String idCard){
        Response<ShoppingCart> response1 = userController.getCart(clientCredentials);
        if(response1.isError()){
            errorLogger.log(Level.WARNING, response1.getMessage());
            return Response.getFailResponse(response1.getMessage());
        }
        Response<Boolean> response2 = purchaseController.purchaseCart(clientCredentials,response1.getValue(),expectedPrice,address, city, country, zip, card_number, month, year, holder, cvv, idCard);
        if(response2.isError()){
            errorLogger.log(Level.WARNING, response2.getMessage());
            return Response.getFailResponse(response2.getMessage());
        }
        return response2;
    }


    public Response<List<ServiceStore>> getStoresPage(int number, int page){
        Response<List<Store>> response = storeController.getStoresPage(number, page);
        if(response.isError()){
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        List<ServiceStore> output = new ArrayList<>();
        for (Store store : response.getValue()) {
            output.add(new ServiceStore(store));
        }
        return Response.getSuccessResponse(output);
    }

    public Response<Boolean> isLoggedIn(UUID userId) {
        Response<Boolean> loggedIn = userController.isLoggedInUser(userId);
        if (loggedIn.isError()) {
            errorLogger.log(Level.WARNING, loggedIn.getMessage());
            return Response.getFailResponse(loggedIn.getMessage());
        }
        return Response.getSuccessResponse(loggedIn.getValue());
    }

    public Response<List<ServiceItem>> getItemsPage(int number, int page, UUID storeId) {
        Response<List<Item>> response = storeController.getItemsPage(number, page, storeId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        List<ServiceItem> output = new ArrayList<>();
        for (Item item : response.getValue()) {
            output.add(new ServiceItem(item));
        }
        return Response.getSuccessResponse(output);
    }

    public Response<Long> numOfStores(){
        return storeController.numOfStores();
    }

    public Response<Boolean> addItemPolicyTermByStoreOwner(UUID clientCredentials, UUID storeId, PurchaseTerm term) {
        Response<Boolean> response = storeController.addPolicyTerm(clientCredentials, storeId, term);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return response;
        }
        eventLogger.log(Level.INFO, "Successfully add Policy to store " + storeId);
        return response;
    }

    public Response<Boolean> removePolicyTerm(UUID clientCredentials, UUID storeId, UUID termId) {
        Response<Boolean> response = storeController.removePolicyTerm(clientCredentials, storeId, termId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return response;
        }
        eventLogger.log(Level.INFO, "Successfully removed policy term from store " + storeId);
        return response;
    }
    
    public Response<Boolean> addDiscount(UUID clientCredentials, UUID storeId, ServiceDiscount serviceDiscount) {
        Response<Boolean> response = storeController.addDiscount(clientCredentials, storeId, serviceDiscount);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return response;
        }
        eventLogger.log(Level.INFO, "Successfully add discount to store " + storeId);
        return response;
    }
    
    public Response<Boolean> removeDiscount(UUID clientCredentials, UUID storeId, UUID discountId) {
        Response<Boolean> response = storeController.removeDiscount(clientCredentials, storeId, discountId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return response;
        }
        eventLogger.log(Level.INFO, "Successfully remove discount to store " + storeId);
        return response;
    }

    public Response<Long> numOfItems(UUID storeId) {
        Response<Long> response = storeController.numOfItems(storeId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        eventLogger.log(Level.INFO, "return the number of items"  +  response.getValue());
        return response;
    }

    public Response<List<ServiceUser>> getNotLoginUser(UUID clientCredentials){
        Response<List<User>> response =userController.getNotLoggedInUsers(clientCredentials);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return Response.getFailResponse(response.getMessage());}
        eventLogger.log(Level.INFO, "Successfully get all user that not Login");
        List<ServiceUser> serviceUsers = new ArrayList<ServiceUser>();
        for(User user : response.getValue())
            serviceUsers.add(new ServiceUser(user));
        return Response.getSuccessResponse(serviceUsers);
    }

    public Response<List<ServiceUser>> getLoginUser(UUID clientCredentials){
        Response<List<User>> response =userController.getAllLoggedInUsers(clientCredentials);
        if(response.isError()){
            errorLogger.log(Level.SEVERE, response.getMessage());
            return Response.getFailResponse(response.getMessage());}
        eventLogger.log(Level.INFO, "Successfully get all LoginUser");
        List<ServiceUser> serviceUsers = new ArrayList<ServiceUser>();
        for(User user : response.getValue())
            serviceUsers.add(new ServiceUser(user));
        return Response.getSuccessResponse(serviceUsers);
    }

    public Response<Boolean> CancelSubscriptionNotRole(UUID adminCredentials, UUID clientCredentials){
        Response<Boolean> response = userController.CancelSubscriptionNotRole(adminCredentials, clientCredentials);
        if(response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return Response.getFailResponse(response.getMessage());}
        eventLogger.log(Level.INFO, "Successfully  Cancel subscription  of user " + clientCredentials);
        return response;
    }


    public Response<List<ServiceItem>> searchItem(String keyword, String category, Double minPrice, Double maxPrice, Integer itemRating, Integer storeRating, Integer number, Integer page, UUID storeId) {
        Response<List<Item>> response = searchController.searchItem(keyword,
                category, minPrice, maxPrice, itemRating, storeRating, number, page, storeId);
        if(response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        List<ServiceItem> list = new ArrayList<ServiceItem>();
        for (Item item : response.getValue()) {
            list.add(new ServiceItem(item));
        }
        return Response.getSuccessResponse(list);
    }

    public Response<List<Role>> getUserRoles(UUID clientCredentials) {
        Response<List<Role>> rolesResponse = userController.getUserRoles(clientCredentials);
        if (rolesResponse.isError()) {
            errorLogger.log(Level.WARNING, rolesResponse.getMessage());
        }
        return rolesResponse;
    }

    public Response<Integer> searchItemNum(String keyword, String category, Double minPrice, Double maxPrice, Integer itemRating, Integer storeRating, Integer number, Integer page, UUID storeId) {
        Response<List<Item>> response = searchController.searchItem(keyword,
                category, minPrice, maxPrice, itemRating, storeRating, number, page, storeId);
        if(response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        List<ServiceItem> list = new ArrayList<ServiceItem>();
        for (Item item : response.getValue()) {
            list.add(new ServiceItem(item));
        }
        return Response.getSuccessResponse(list.size());
    }

    public Response<Integer> numOfUsers() {
        Response<Integer> response = userController.numOfUsers();
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        return response;
    }

    public Response<Integer> numOfClients() {
        Response<Integer> response = userController.numOfClients();
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        return response;
    }

    public Response<List<Notification>> getNotifications(UUID clientCredentials, UUID recipient) {
        Response<List<Notification>> notificationResponse = notificationController.getNotifications(clientCredentials, recipient);
        if (notificationResponse.isError()) {
            errorLogger.log(Level.WARNING, notificationResponse.getMessage());
        }
        return notificationResponse;
    }

    public Response<List<ServiceUser>> searchUser(String username) {
        Response<List<User>> usersResponse = searchController.searchUser(username);
        if (usersResponse.isError()) {
            errorLogger.log(Level.WARNING, usersResponse.getMessage());
            return Response.getFailResponse(usersResponse.getMessage());
        }
        List<User> users = usersResponse.getValue();
        List<ServiceUser> serviceUsers = new ArrayList<>();
        for (User user : users) {
            serviceUsers.add(new ServiceUser(user));
        }
        return Response.getSuccessResponse(serviceUsers);
    }

    public Response<Long> numOfOpenStores() {
        Response<Long> openStoresResponse = storeController.numOfOpenStores();
        if (openStoresResponse.isError()) {
            errorLogger.log(Level.WARNING, openStoresResponse.getMessage());
        }
        return openStoresResponse;
    }

    public Response<UUID> getAdminCredentials() {
        Response<UUID> adminResponse = userController.getAdminCredentials();
        if (adminResponse.isError()) {
            errorLogger.log(Level.WARNING, adminResponse.getMessage());
        }
        return adminResponse;
    }

    public Response<Integer> numOfLoggedInUsers() {
        Response<Integer> loggedInUsers = userController.numOfLoggedInUsers();
        if (loggedInUsers.isError()) {
            errorLogger.log(Level.WARNING, loggedInUsers.getMessage());
        }
        return loggedInUsers;
    }

//    public Response<ConcurrentHashMap<String, UUID>> getUserNames() {
//        return Response.getSuccessResponse(userController.getUsernames());
//    }
    
    public Response<List<ServiceUser>> getStoreManagers(UUID clientCredentials, UUID storeId) {
        Response<List<User>> response = storeController.getStoreManagers(clientCredentials, storeId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
        }
        else {
            eventLogger.log(Level.INFO, "Returned store " + storeId + " managers to " + clientCredentials);
        }
        List<ServiceUser> users = new ArrayList<>();
        for (User user : response.getValue()) {
            users.add(new ServiceUser(user));
        }
        return Response.getSuccessResponse(users);
    }
    
    public Response<ServiceUser> getUserByUsername(String username) {
        Response<User> response = userController.getUserByUsername(username);
        if (response.isError()){
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        return Response.getSuccessResponse(new ServiceUser(response.getValue()));
    }
    
    public Response<Boolean> addItemPolicyTerm(UUID clientCredentials, UUID storeId, UUID itemId, int quantity, boolean atLeast) {
        ItemPurchaseRule rule = new ItemPurchaseRule(itemId);
        PurchaseTerm term = atLeast ? new AtLeastPurchaseTerm(rule, quantity) : new AtMostPurchaseTerm(rule, quantity);
        Response<Boolean> response = storeController.addPolicyTerm(clientCredentials, storeId, term);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return response;
        }
        eventLogger.log(Level.INFO, "Successfully added policy to store " + storeId);
        return response;
    }
    
    public Response<Boolean> addCategoryPolicyTerm(UUID clientCredentials, UUID storeId, String category, int quantity, boolean atLeast) {
        CategoryPurchaseRule rule = new CategoryPurchaseRule(new Category(category));
        PurchaseTerm term = atLeast ? new AtLeastPurchaseTerm(rule, quantity) : new AtMostPurchaseTerm(rule, quantity);
        Response<Boolean> response = storeController.addPolicyTerm(clientCredentials, storeId, term);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return response;
        }
        eventLogger.log(Level.INFO, "Successfully added policy to store " + storeId);
        return response;
    }
    
    public Response<Boolean> addBasketPolicyTerm(UUID clientCredentials, UUID storeId, int quantity, boolean atLeast) {
        ShoppingBasketPurchaseRule rule = new ShoppingBasketPurchaseRule();
        PurchaseTerm term = atLeast ? new AtLeastPurchaseTerm(rule, quantity) : new AtMostPurchaseTerm(rule, quantity);
        Response<Boolean> response = storeController.addPolicyTerm(clientCredentials, storeId, term);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return response;
        }
        eventLogger.log(Level.INFO, "Successfully added policy to store " + storeId);
        return response;
    }
    
    public Response<List<ServiceComplaint>> getAssignedComplaints(UUID clientCredentials) {
        Response<List<Complaint>> response = messageController.getAssignedComplaints(clientCredentials);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        List<ServiceComplaint> output = new ArrayList<>();
        for (Complaint complaint : response.getValue())
            output.add(new ServiceComplaint(complaint));
        return Response.getSuccessResponse(output);
    }

    public Response<Boolean> closeComplaint(UUID clientCredentials, UUID complaintId) {
        Response<Boolean> response = messageController.closeComplaint(clientCredentials, complaintId);
        if (response.isError()){
            errorLogger.log(Level.WARNING, response.getMessage());
        }
        return response;
    }
    
    public Response<Boolean> reopenComplaint(UUID clientCredentials, UUID complaintId) {
        Response<Boolean> response = messageController.reopenComplaint(clientCredentials, complaintId);
        if (response.isError()){
            errorLogger.log(Level.WARNING, response.getMessage());
        }
        return response;
    }
    
    public Response<UUID> addStoreReview(UUID clientCredentials, UUID storeId, String body, int rating) {
        Response<User> userResponse = userController.getUser(clientCredentials);
        if(userResponse.isError()) {
            errorLogger.log(Level.SEVERE, userResponse.getMessage());
            return Response.getFailResponse(userResponse.getMessage());
        }
        Response<UUID> reviewResponse = storeController.postStoreReview(clientCredentials, storeId, body, rating);
        if(reviewResponse.isError()) {
            errorLogger.log(Level.SEVERE, reviewResponse.getMessage());
            return Response.getFailResponse(reviewResponse.getMessage());
        }
        eventLogger.log(Level.INFO, "Successfully posted review by " + userResponse.getValue().getUsername() + " for store "
                + storeId);
        return reviewResponse;
    }

    
    public Response<List<ServiceStoreReview>> getStoreReviews(UUID storeId) {
        Response<List<StoreReview>> response = storeController.getStoreReviews(storeId);
        if (response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        List<ServiceStoreReview> output = new ArrayList<>();
        for (StoreReview review : response.getValue()) {
            output.add(new ServiceStoreReview(review));
        }
        return Response.getSuccessResponse(output);
    }
    
    public Response<Boolean> addConditionalPurchaseTerm(UUID clientCredentials, UUID storeId,
                                                        ServiceConditionalPurchaseTerm term) {
        ConditionalPurchaseTerm termToAdd = createConditionalPurchaseTerm(term);
        if (termToAdd == null)
            return Response.getFailResponse("Failed to create conditional purchase term");
        Response<Boolean> response = storeController.addPolicyTerm(clientCredentials, storeId, termToAdd);
        if (response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
        }
        else eventLogger.log(Level.INFO, "Successfully added purchase term to " + storeId);
        return response;
    }
    
    private ConditionalPurchaseTerm createConditionalPurchaseTerm(ServiceConditionalPurchaseTerm conditionalServiceTerm) {
        ConditionalPurchaseTerm termToAdd = new ConditionalPurchaseTerm(null, null, null);
        ServicePurchaseTerm serviceIfTerm = conditionalServiceTerm.getIfPurchaseTerm();
        PurchaseRule ifRule = createPurchaseRule(serviceIfTerm.getRule());
        PurchaseTerm ifTerm;
        if (serviceIfTerm.getAtLeast())
            ifTerm = new AtLeastPurchaseTerm(ifRule, serviceIfTerm.getQuantity());
        else
            ifTerm = new AtMostPurchaseTerm(ifRule, serviceIfTerm.getQuantity());
        
        termToAdd.setPurchaseTermIf(ifTerm);
        
        ServicePurchaseTerm serviceThenTerm = conditionalServiceTerm.getThenPurchaseTerm();
        PurchaseRule thenRule = createPurchaseRule(serviceThenTerm.getRule());
        PurchaseTerm thenTerm;
        
        if (serviceThenTerm.getAtLeast())
            thenTerm = new AtLeastPurchaseTerm(thenRule, serviceThenTerm.getQuantity());
        else
            thenTerm = new AtMostPurchaseTerm(thenRule, serviceThenTerm.getQuantity());
        
        termToAdd.setPurchaseTermThen(thenTerm);
        return termToAdd;
    }
    
    private PurchaseRule createPurchaseRule(ServicePurchaseRule rule) {
        PurchaseRule outputRule = null;
        switch (rule.getType()) {
            case "ITEM":
                outputRule = new ItemPurchaseRule(UUID.fromString(rule.getItemIdOrCategoryOrNull()));
                break;
            case "CATEGORY":
                outputRule = new CategoryPurchaseRule(new Category(rule.getItemIdOrCategoryOrNull()));
                break;
            case "BASKET":
                outputRule = new ShoppingBasketPurchaseRule();
                break;
            default:
                return null;
        }
        return outputRule;
    }
    
    public Response<Boolean> addCompositePolicyTerm(UUID clientCredentials, UUID storeId,
                                                    ServiceCompositePurchaseTerm term) {
        CompositePurchaseTerm termToAdd = createCompositePurchaseTerm(term);
        if (termToAdd == null)
            return Response.getFailResponse("Failed to create composite purchase term.");
        Response<Boolean> response = storeController.addPolicyTerm(clientCredentials, storeId, termToAdd);
        if (response.isError()) {
            errorLogger.log(Level.SEVERE, response.getMessage());
        }
        else eventLogger.log(Level.INFO, "Successfully added purchase term to " + storeId);
        return response;
    }
    
    private CompositePurchaseTerm createCompositePurchaseTerm(ServiceCompositePurchaseTerm compositeServiceTerm) {
        CompositePurchaseTerm termToAdd;
        switch (compositeServiceTerm.getType()) {
            case "AND":
                termToAdd = new CompositePurchaseTermAnd(null, new ConcurrentLinkedQueue<>());
                break;
            case "OR":
                termToAdd = new CompositePurchaseTermOr(null, new ConcurrentLinkedQueue<>());
                break;
            case "XOR":
                termToAdd = new CompositePurchaseTermXor(null, new ConcurrentLinkedQueue<>());
                break;
            default:
                return null;
        }
        List<ServicePurchaseTerm> serviceTerms = compositeServiceTerm.getPurchaseTerms();
        ConcurrentLinkedQueue<PurchaseTerm> terms = new ConcurrentLinkedQueue<>();
        for (ServicePurchaseTerm serviceTerm : serviceTerms) {
            PurchaseRule rule = createPurchaseRule(serviceTerm.getRule());
            PurchaseTerm term;
            if (serviceTerm.getAtLeast())
                term = new AtLeastPurchaseTerm(rule, serviceTerm.getQuantity());
            else
                term = new AtMostPurchaseTerm(rule, serviceTerm.getQuantity());
            terms.add(term);
        }
        
        termToAdd.setPurchaseTerms(terms);
        return termToAdd;
    }
    
    public Response<Boolean> addBidToItem(UUID clientCredentials, UUID storeId, UUID itemId, double bidPrice, int quantity) {
        Response<Boolean> response = storeController.addBidToItem(clientCredentials, storeId, itemId, bidPrice, quantity);
        if (response.isError())
            errorLogger.log(Level.WARNING, response.getMessage());
        else
            eventLogger.log(Level.INFO, "Successfully bid on item " + itemId);
        return response;
    }
    
    public Response<Boolean> acceptItemBid(UUID clientCredentials, UUID storeId, UUID itemId, UUID bidderId, double bidPrice) {
        Response<Boolean> response = storeController.acceptItemBid(clientCredentials, storeId, itemId, bidderId, bidPrice);
        if (response.isError())
            errorLogger.log(Level.WARNING, response.getMessage());
        else
            eventLogger.log(Level.INFO, "Successfully accepted bid on item " + itemId);
        return response;
    }
    
    public Response<List<Bid>> getItemBids(UUID clientCredentials, UUID storeId, UUID itemId) {
        Response<List<Bid>> response = storeController.getItemBids(clientCredentials, storeId, itemId);
        if (response.isError())
            errorLogger.log(Level.WARNING, response.getMessage());
        return response;
    }
    
    public Response<List<Bid>> getStoreBids(UUID clientCredentials, UUID storeId) {
        Response<List<Bid>> response = storeController.getStoreBids(clientCredentials, storeId);
        if (response.isError())
            errorLogger.log(Level.WARNING, response.getMessage());
        return response;
    }
    
    public Response<List<Object>> getStorePurchaseTerms(UUID clientCredentials, UUID storeId) {
        Response<List<PurchaseTerm>> response = storeController.getStorePurchaseTerms(clientCredentials, storeId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        List<Object> output = new ArrayList<>();
        for (PurchaseTerm term : response.getValue()) {
            if (term instanceof CompositePurchaseTerm)
                output.add(new ServiceCompositePurchaseTerm((CompositePurchaseTerm) term));
            else if (term instanceof ConditionalPurchaseTerm)
                output.add(new ServiceConditionalPurchaseTerm((ConditionalPurchaseTerm) term));
            else
                output.add(new ServicePurchaseTerm(term));
        }
        return Response.getSuccessResponse(output);
        
    }
    
    public Response<List<ServiceDiscount>> getStoreDiscounts(UUID clientCredentials, UUID storeId) {
        Response<List<Discount>> response = storeController.getStoreDiscounts(clientCredentials, storeId);
        if (response.isError()) {
            errorLogger.log(Level.WARNING, response.getMessage());
            return Response.getFailResponse(response.getMessage());
        }
        List<ServiceDiscount> output = new ArrayList<>();
        for (Discount discount : response.getValue()) {
            output.add(new ServiceDiscount(discount));
        }
        return Response.getSuccessResponse(output);
    }
    
    public Response<Collection<OwnerPetition>> getStoreOwnerPetitions(UUID clientCredentials, UUID storeId) {
        Response<Collection<OwnerPetition>> response = storeController.getStoreOwnerPetitions(clientCredentials, storeId);
        if (response.isError())
            errorLogger.log(Level.WARNING, response.getMessage());
        return response;
    }
    
    public Response<Boolean> removeOwnerPetitionApproval(UUID clientCredentials, UUID storeId, UUID appointee) {
        Response<Boolean> response = storeController.removeOwnerPetitionApproval(clientCredentials, storeId, appointee);
        if (response.isError())
            errorLogger.log(Level.WARNING, response.getMessage());
        return response;
    }

    
    public Response<Boolean> setItemPurchaseType(UUID clientCredentials, UUID storeId, UUID itemId, String purchaseType) {
        Response<Boolean> response = storeController.setItemPurchaseType(clientCredentials, storeId, itemId, purchaseType);
        if (response.isError())
            errorLogger.log(Level.WARNING, response.getMessage());
        return response;
    }
    
    public Response<Bid> getUserItemBid(UUID clientCredentials, UUID storeId, UUID itemId, UUID bidderId) {
        Response<Bid> response = storeController.getUserItemBid(clientCredentials, storeId, itemId, bidderId);
        if (response.isError())
            errorLogger.log(Level.WARNING, response.getMessage());
        return response;
    }
    
    public Response<List<Bid>> getUserBids(UUID clientCredentials) {
        Response<List<Bid>> response = storeController.getUserBids(clientCredentials);
        if (response.isError())
            errorLogger.log(Level.WARNING, response.getMessage());
        return response;
    }
    
    public Response<Double> getItemDiscount(UUID clientCredentials, UUID storeId, UUID itemId) {
        Response<Double> response = userController.getItemDiscount(clientCredentials, storeId, itemId);
        if (response.isError())
            errorLogger.log(Level.WARNING, response.getMessage());
        return response;
    }

    public Response<ConcurrentHashMap<String, UUID>> getUserNames() {
        List<User> users = repositoryFactory.userRepository.findAll();
        ConcurrentHashMap<String, UUID> userNames = new ConcurrentHashMap<>();
        for(User user : users){
            userNames.put(user.getUsername(), user.getId());
        }
        return Response.getSuccessResponse(userNames);
    }
}

