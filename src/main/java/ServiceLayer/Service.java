package ServiceLayer;

import DomainLayer.Market.*;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Sale;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.Purchase;
import DomainLayer.Market.Users.ShoppingCart;
import DomainLayer.Market.Users.User;
import DomainLayer.Payment.PaymentController;
import DomainLayer.Security.SecurityController;
import DomainLayer.Supply.SupplyController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class Service {
    private static Service instance = null;
    private static Object instanceLock = new Object();
    private static final Logger LOG = getLogger("Service");
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
        LOG.log(Level.INFO, "Booting system");
        storeController = StoreController.getInstance();
        storeController.init();
        userController = UserController.getInstance();
        userController.init();
        securityController = SecurityController.getInstance();
        securityController.init();
        messageController = MessageController.getInstance();
        messageController.init();
        supplyController = SupplyController.getInstance();
        supplyController.init();
        paymentController = PaymentController.getInstance();
        paymentController.init();
        notificationController = NotificationController.getInstance();
        notificationController.init();
        searchController = SearchController.getInstance();
        searchController.init();

        //Creating default admin user
        try {
            Response<Boolean> registerResponse = userController.register("admin", "admin");
            if (registerResponse.isError()) throw new RuntimeException("System startup - registering admin failed.");
            Response<UUID> loginResponse = userController.login("admin", "admin", null);
            if (loginResponse.isError()) throw new RuntimeException("System startup - logging in as default admin failed.");
            UUID clientCredentials = loginResponse.getValue();
            Response<Boolean> setAdminResponse = userController.setAsAdmin(clientCredentials, clientCredentials);
            if (loginResponse.isError()) throw new RuntimeException("System startup - setting default admin role failed.");
            Response<UUID> logoutResponse = userController.logout(clientCredentials);
            if (loginResponse.isError()) throw new RuntimeException("System startup - logging out default admin failed.");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage() + "\n" + e.getStackTrace());
            return false;
        }

        //Add Supply and Payment JSON config file read here

        LOG.log(Level.INFO, "System boot successful.");
        return true;
    }
    public UUID createClient(){
        Response<UUID> response = userController.createClient();
        if(response.isError())
            return null;
        return response.getValue();
    }

    public UUID logout(UUID clientCredentials){
        Response<UUID> response =  userController.logout(clientCredentials);
        if(response.isError())
            return null;
        return response.getValue();
    }

    public boolean changePassword(UUID clientCredentials, String oldPassword, String newPassword){
        Response<Boolean> response = securityController.changePassword(clientCredentials, oldPassword, newPassword);
        return response.getValue()!=null ? response.getValue() : false;
    }

    public boolean closeClient(UUID clientCredentials){
        Response<Boolean> response = userController.closeClient(clientCredentials);
        return response.getValue()!=null ? response.getValue() : false;
    }

    public boolean closeStore(UUID clientCredentials, UUID storeID){
        Response<Boolean> response = storeController.closeStore(clientCredentials, storeID);
        return response.getValue()!=null ? response.getValue() : false;
    }

    public boolean reopenStore(UUID clientCredentials, UUID storeID){
        Response<Boolean> response = storeController.reopenStore(clientCredentials, storeID);
        return response.getValue()!=null ? response.getValue() : false;
    }

    public boolean shutdownStore(UUID clientCredentials, UUID storeID){
        Response<Boolean> response = storeController.shutdownStore(clientCredentials, storeID);
        return response.getValue()!=null ? response.getValue() : false;
    }

    public boolean deleteUser(UUID clientCredentials, UUID userToDelete){
        Response<Boolean> response1 = userController.deleteUser(clientCredentials, userToDelete);
        Response<Boolean> response2;
        if (!response1.isError())
            response2 = securityController.removeUser(clientCredentials, userToDelete);
        else
            return false;
        return response2.getValue()!=null ? response2.getValue() : false;
    }

    public boolean validateOrder(UUID clientCredentials/*, args*/){
        Response<Boolean> response = supplyController.validateOrder(/*args*/);
        return response.getValue()!=null ? response.getValue() : false;
    }

    public boolean validatePayment(UUID clientCredentials/*, args*/){
        Response<Boolean> response = paymentController.validatePaymentDetails(/*args*/);
        return response.getValue()!=null ? response.getValue() : false;
    }

    public int confirmOrder(UUID clientCredentials){
        Response<Integer> response1 = supplyController.sendOrder();
        Response<Integer> response2;
        if(!response1.isError()) {
            response2 = paymentController.requestPayment();
            int confirmationId = 1;
            if (!response2.isError())
                return confirmationId;
        }
        return 0;
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
        //ServiceStore constructor should get Store as an argument
    }

    public ServiceItem getItemInformation(UUID storeId, UUID itemId){
        Response<Item> response = storeController.getItemInformation(storeId, itemId);
        if(response.isError())
            return null;
        return new ServiceItem(response.getValue());
        //ServiceItem constructor should get Item as an argument
    }

    public ServiceStore createStore(UUID clientCredentials , String storeName , String storeDescription){
        Response<Store> response = storeController.createStore(clientCredentials, storeName, storeDescription);
        if(response.isError())
            return null;
        return new ServiceStore(response.getValue());
    }

    public ServiceShoppingCart viewCart(UUID clientCredentials){
        Response<ShoppingCart> response = userController.viewCart(clientCredentials);
        if(response.isError())
            return null;
        return new ServiceShoppingCart(response.getValue());
    }

    public UUID postReview(UUID clientCredentials, UUID itemId, String reviewBody){
        Response<UUID> response = storeController.postReview(clientCredentials, itemId, reviewBody);
        if(response.isError())
            return null;
        return response.getValue();
    }

    public boolean SetManagerPermissions(UUID clientCredentials, UUID manager,
                                         UUID storeId, List<Integer> permissions){
        Response<Boolean> response = userController.setManagerPermissions(clientCredentials, manager, storeId, permissions);
        if(response.isError())
            return false;
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
        if(response.isError())
            return false;
        return response.getValue();
    }

    public boolean appointStoreOwner(UUID clientCredentials, UUID appointee, UUID storeId){
        Response<Boolean> response = userController.appointStoreOwner(clientCredentials, appointee, storeId);
        if(response.isError())
            return false;
        return response.getValue();
    }

    public boolean removeStoreOwner(UUID clientCredentials, UUID ownerToRemove, UUID storeId){
        Response<Boolean> response = userController.removeStoreOwner(clientCredentials, ownerToRemove, storeId);
        if(response.isError())
            return false;
        return response.getValue();
    }

    public boolean removeStoreManager(UUID clientCredentials, UUID managerToRemove, UUID storeId){
        Response<Boolean> response = userController.removeStoreManager(clientCredentials, managerToRemove, storeId);
        if(response.isError())
            return false;
        return response.getValue();
    }


    public boolean setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId, int newQuantity){
        Response<Boolean> response = storeController.setItemQuantity(clientCredentials, storeId, itemId, newQuantity);
        if(response.isError())
            return false;
        return response.getValue();
    }

    public boolean setItemName(UUID clientCredentials, UUID storeId, UUID itemId, String name){
        Response<Boolean> response = storeController.setItemName(clientCredentials, storeId, itemId, name);
        if(response.isError())
            return false;
        return response.getValue();
    }

    public boolean setItemDescription(UUID clientCredentials, UUID storeId, UUID itemId, String description){
        Response<Boolean> response = storeController.setItemDescription(clientCredentials, storeId, itemId, description);
        if(response.isError())
            return false;
        return response.getValue();
    }

    public boolean setItemPrice(UUID clientCredentials, UUID storeId, UUID itemId, int price){
        Response<Boolean> response = storeController.setItemPrice(clientCredentials, storeId, itemId, price);
        if(response.isError())
            return false;
        return response.getValue();
    }

    public List<ServicePurchase> getPurchaseHistory(UUID clientCredentials, UUID user ){
        //if(clientCredentials!=user){if(!isAdmin(clientCredentials) return null); //TODO: check where we check admin
        Response<List<Purchase>> response = userController.getPurchaseHistory(clientCredentials,user);
        if(response.isError())
            return null;
        return new ServicePurchase(response.getValue());}


    public List<ServiceSale> getStoreSaleHistory(UUID clientCredentials,UUID storeId){
        Response<List<Sale>> response =storeController.getStoreSaleHistory(clientCredentials,storeId);
        if(response.isError())
            return null;
        return new ServiceSale(response.getValue());
    }

    public ServiceUser getUserInfo(UUID clientCredentials){
        Response<User> response = userController.getUser(clientCredentials);
        if(response.isError())
            return null;
        return new ServiceUser(response.getValue());
    }


    public Boolean addItemToCart(UUID clientCredentials, UUID itemId ,int  quantity, UUID storeID ) {
        Response<Boolean> response1 = userController.addItemToCart(clientCredentials,itemId,quantity, storeID);
        if(response1.isError())
            return false;
        Response<Boolean> response2 = storeController.removeItemQuantity(clientCredentials,itemId,quantity);
        return response2.getValue();
    }

    public Boolean removeItemFromCart(UUID clientCredentials, UUID itemId ,int  quantity, UUID storeID ) {
        Response<Boolean> response = userController.removeItemFromCart(clientCredentials,itemId,quantity, storeID);
        if(response.isError())
            return false;
        Response<Boolean> response2 = storeController.AddItemQuantity(clientCredentials,itemId,quantity);
        return response.getValue();
    }

    public Double getCartTotal(UUID clientCredentials){
        Response<ShoppingCart> response1 =userController.viewCart(clientCredentials);
        if(response1.isError())
            return null;
        Response<Double> response2 = storeController.calculatePriceOfCart(response1.getValue());
        if(response2.isError())
            return null;
        return response2.getValue();
    }

}




