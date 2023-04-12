package ServiceLayer;

import DomainLayer.Market.*;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.ShoppingCart;
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
}



