package AcceptanceTests;

import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;

public class ProxyBridge implements Bridge {

    Bridge real = null;

    public void setReal() {
        real = new RealBridge();
    }

    public Boolean systemBoot() {
        return real == null ? null : real.systemBoot();
    }

    public Boolean integrityTest() {
        return real == null ? null : real.integrityTest();
    }

    public Boolean addService() {
        return real == null ? null : real.addService();
    }

    public Boolean updateService() {
        return real == null ? null : real.updateService();
    }

    public Boolean payForShoppingCart() {
        return real == null ? null : real.payForShoppingCart();
    }

    public Boolean checkForSupply() {
        return real == null ? null : real.checkForSupply();
    }

    public Boolean systemRealTimeAlert() {
        return real == null ? null : real.systemRealTimeAlert();
    }

    public Boolean userRealTimeAlert() {
        return real == null ? null : real.userRealTimeAlert();
    }

    public Boolean systemDelayedAlert() {
        return real == null ? null : real.systemDelayedAlert();
    }

    public Boolean userDelayedAlert() {
        return real == null ? null : real.userDelayedAlert();
    }

    public UUID enterSystem() {
        return real == null ? null : real.enterSystem();
    }

    public Boolean exitSystem(UUID clientCredentials) {
        return real == null ? null : real.exitSystem(clientCredentials);
    }

    public Boolean register(String username, String password) {
        return real == null ? null : real.register(username, password);
    }

    public UUID login(UUID clientCredentials, String username, String password) {
        return real == null ? null : real.login(clientCredentials, username, password);
    }

    public ServiceStore receiveStoreInfo(UUID storeId) {
        return real == null ? null : real.receiveStoreInfo(storeId);
    }

    public Boolean searchStore() {
        return real == null ? null : real.searchStore();
    }

    public List<ServiceItem> searchItem(String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating) {
        return real == null ? null : real.searchItem(keyword, category, minPrice, maxPrice, itemRating, storeRating);
    }

    public Boolean saveItemInShoppingCart(UUID clientCredentials, UUID itemId, int quantity, UUID storeId) {
        return real == null ? null : real.saveItemInShoppingCart(clientCredentials, itemId, quantity, storeId);
    }

    public List<ServiceShoppingBasket> viewShoppingCartItems(UUID clientCredentials) {
        return real == null ? null : real.viewShoppingCartItems(clientCredentials);
    }

    public Boolean purchaseShoppingCart() {
        return real == null ? null : real.purchaseShoppingCart();
    }


    public UUID logout(UUID clientCredentials) {
        return real == null ? null : real.logout(clientCredentials);
    }

    public ServiceStore openStore(UUID clientCredentials, String storeName, String storeDescription) {
        return real == null ? null : real.openStore(clientCredentials, storeName, storeDescription);
    }

    public ServiceItem stockManagementAddNewItem(UUID clientCredentials, String name, double price, UUID storeId, int quantity, String description) {
        return real == null ? null : real.stockManagementAddNewItem(clientCredentials, name, price, storeId, quantity, description);
    }

    public Boolean stockManagementRemoveItem(UUID clientCredentials, UUID storeId, UUID itemId) {
        return real == null ? null : real.stockManagementRemoveItem(clientCredentials, storeId, itemId);
    }

    public Boolean stockManagementChangeItemInfo(UUID clientCredentials, UUID storeId, UUID itemId, String name, String description) {
        return real == null ? null : real.stockManagementChangeItemInfo(clientCredentials, storeId, itemId, name, description);
    }

    public Boolean setStorePolicy() {
        return real == null ? null : real.setStorePolicy();
    }

    public Boolean appointStoreOwner(UUID clientCredentials, UUID appointee, UUID storeId) {
        return real == null ? null : real.appointStoreOwner(clientCredentials, appointee, storeId
        );
    }

    public Boolean appointStoreManager(UUID clientCredentials, UUID appointee, UUID storeId) {
        return real == null ? null : real.appointStoreManager(clientCredentials, appointee, storeId);
    }

    public Boolean setStoreManagerPermissions(UUID clientCredentials, UUID manager,
                                              UUID storeId, List<Integer> permissions) {
        return real == null ? null : real.setStoreManagerPermissions(clientCredentials, manager, storeId, permissions);
    }

    public Boolean closeStore(UUID clientCredentials, UUID storeId) {
        return real == null ? null : real.closeStore(clientCredentials, storeId);
    }

    public List<ServiceUser> getStoreStaffList(UUID clientCredentials, UUID storeId) {
        return real == null ? null : real.getStoreStaffList(clientCredentials, storeId);
    }

    public List<ServiceSale> getStoreSaleHistory(UUID clientCredentials, UUID storeId) {
        return real == null ? null : real.getStoreSaleHistory(clientCredentials, storeId);
    }

    public Boolean storeManagerActions() {
        return real == null ? null : real.storeManagerActions();
    }

    public List<ServiceSale> getStoreSaleHistorySystemAdmin(UUID clientCredentials, UUID storeId, String userName, String password) {
        return real == null ? null : real.getStoreSaleHistorySystemAdmin(clientCredentials, storeId, userName, password);
    }

    public Void resetService() {
        return real == null ? null : real.resetService();
    }

    public ServiceItem getItemInformation(UUID storeId, UUID itemId) {
        return real == null ? null : real.getItemInformation(storeId, itemId);
    }
}