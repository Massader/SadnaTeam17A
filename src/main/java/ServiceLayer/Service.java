package ServiceLayer;

import DomainLayer.Market.*;
import DomainLayer.Payment.PaymentController;
import DomainLayer.Security.SecurityController;
import DomainLayer.Supply.SupplyController;

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
            Response<Boolean> logoutResponse = userController.logout(clientCredentials);
            if (loginResponse.isError()) throw new RuntimeException("System startup - logging out default admin failed.");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage() + "\n" + e.getStackTrace());
            return false;
        }

        //Add Supply and Payment JSON config file read here

        LOG.log(Level.INFO, "System boot successful.");
        return true;
    }
}



