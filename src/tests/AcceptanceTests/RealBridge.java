package AcceptanceTests;
import ServiceLayer.*;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;

public class RealBridge implements Bridge {

    Service service;

    @Override
    public void setReal() {

    }

    public RealBridge() {
        service = Service.getInstance();
        service.init();
    }

    public Boolean systemBoot() {
        return false;
    }

    public Boolean integrityTest() {
        return false;
    }

    public Boolean addService() {
        return false;
    }

    public Boolean updateService() {
        return false;
    }

    public Boolean payForShoppingCart() {
        return false;
    }

    public Boolean checkForSupply() {
        return false;
    }

    public Boolean systemRealTimeAlert() {
        return false;
    }

    public Boolean userRealTimeAlert() {
        return false;
    }

    public Boolean systemDelayedAlert() {
        return false;
    }

    public Boolean userDelayedAlert() {
        return false;
    }

    public UUID createClient() {
        return service.createClient();
    }

    public Boolean closeClient(UUID clientCredentials) {
        return service.closeClient(clientCredentials);
    }

    public Boolean register(String username, String password) {
        return service.register(username, password);
    }

    public ServiceUser login(UUID clientCredentials, String username, String password) {
        return service.login(clientCredentials, username, password);
    }

    public ServiceStore getStoreInformation(UUID storeId) {
        return service.getStoreInformation(storeId);
    }

    public Boolean searchStore() {
        return false;
    }

    public List<ServiceItem> searchItem(String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating) {
        return service.searchItem(keyword, category, minPrice, maxPrice, itemRating, storeRating);
    }

    public Boolean addItemToCart(UUID clientCredentials, UUID itemId, int quantity, UUID storeId) {
        return service.addItemToCart(clientCredentials, itemId, quantity, storeId);
    }

    public List<ServiceShoppingBasket> getCart(UUID clientCredentials) {
        return service.getCart(clientCredentials);
    }

    public Boolean purchaseShoppingCart() {
        return false;
    }


    public UUID logout(UUID clientCredentials) {
        return service.logout(clientCredentials);
    }

    public ServiceStore createStore(UUID clientCredentials, String storeName, String storeDescription) {
        return service.createStore(clientCredentials, storeName, storeDescription);
    }

    public ServiceItem addItemToStore(UUID clientCredentials, String name, double price, UUID storeId, int quantity, String description) {
        return service.addItemToStore(clientCredentials, name, price, storeId, quantity, description);
    }

    public Boolean setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId) {
        return service.setItemQuantity(clientCredentials, storeId, itemId, 0);

    }

    public Boolean stockManagementChangeItemInfo(UUID clientCredentials, UUID storeId, UUID itemId, String name, String description) {
        Boolean check = false;
        if (description != null) {
            check = service.setItemDescription(clientCredentials, storeId, itemId, description);
        }
        if (name != null) {
            check = check && service.setItemName(clientCredentials, storeId, itemId, name);
        }
        return check;
    }

    public Boolean setStorePolicy() {
        return false;
    }

    public Boolean appointStoreOwner(UUID clientCredentials, UUID appointee, UUID storeId) {
        return service.appointStoreOwner(clientCredentials, appointee, storeId);
    }

    public Boolean appointStoreManager(UUID clientCredentials, UUID appointee, UUID storeId) {
        return service.appointStoreManager(clientCredentials, appointee, storeId);
    }

    public Boolean setStoreManagerPermissions(UUID clientCredentials, UUID manager,
                                              UUID storeId, List<Integer> permissions) {
        return service.SetManagerPermissions(clientCredentials, manager, storeId, permissions);
    }

    public Boolean closeStore(UUID clientCredentials, UUID storeId) {
        return service.closeStore(clientCredentials, storeId);
    }

    public List<ServiceUser> getStoreStaffList(UUID clientCredentials, UUID storeId) {
        return service.getStoreStaff(clientCredentials, storeId);
    }

    public List<ServiceSale> getStoreSaleHistory(UUID clientCredentials, UUID storeId) {
        return service.getStoreSaleHistory(clientCredentials, storeId);
    }

    public Boolean storeManagerActions() {
        return false;
    }

    public List<ServiceSale> getStoreSaleHistorySystemAdmin(UUID clientCredentials, UUID storeId) {
        return service.getStoreSaleHistory(clientCredentials, storeId);
    }

    public Void resetService() {
        return service.resetService();
    }

    @Override
    public Boolean addItemCategory(UUID clientCredentials, UUID storeId, UUID itemId, String category) {
        return service.addItemCategory(clientCredentials, storeId, itemId, category);
    }

    public ServiceItem getItemInformation(UUID storeId, UUID itemId) {
        return service.getItemInformation(storeId, itemId);
    }

    @Override
    public Boolean validateOrder(UUID clientCredentials) {
        return service.validateOrder(clientCredentials);
    }

    @Override
    public Boolean validatePayment(UUID clientCredentials) {
        return service.validatePayment(clientCredentials);
    }

    @Override
    public UUID confirmOrder(UUID clientCredentials) {
        return service.confirmOrder(clientCredentials);
    }

    @Override
    public Boolean removeItemFromStore(UUID clientCredentials, UUID storeId, UUID itemId) {
        return service.removeItemFromStore(clientCredentials, storeId, itemId);
    }


}
