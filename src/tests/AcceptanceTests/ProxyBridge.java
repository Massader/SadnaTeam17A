package AcceptanceTests;

import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;

public class ProxyBridge implements Bridge {

    Bridge real = null;

    public void setReal() {
        real = new RealBridge();
    }

    public Response<Boolean> systemBoot() {
        return real == null ? null : real.systemBoot();
    }

    public Response<Boolean> integrityTest() {
        return real == null ? null : real.integrityTest();
    }

    public Response<Boolean> addService() {
        return real == null ? null : real.addService();
    }

    public Response<Boolean> updateService() {
        return real == null ? null : real.updateService();
    }

    public Response<Boolean> payForShoppingCart() {
        return real == null ? null : real.payForShoppingCart();
    }

    public Response<Boolean> checkForSupply() {
        return real == null ? null : real.checkForSupply();
    }

    public Response<Boolean> systemRealTimeAlert() {
        return real == null ? null : real.systemRealTimeAlert();
    }

    public Response<Boolean> userRealTimeAlert() {
        return real == null ? null : real.userRealTimeAlert();
    }

    public Response<Boolean> systemDelayedAlert() {
        return real == null ? null : real.systemDelayedAlert();
    }

    public Response<Boolean> userDelayedAlert() {
        return real == null ? null : real.userDelayedAlert();
    }

    public Response<UUID> createClient() {
        return real == null? null : real.createClient();
    }

    public Response<Boolean> closeClient(UUID clientCredentials) {
        return real == null? null : real.closeClient(clientCredentials);
    }

    public Response<Boolean> register(String username, String password) {
        return real == null ? null : real.register(username, password);
    }

    public Response<ServiceUser> login(UUID clientCredentials, String username, String password) {
        return real == null ? null : real.login(clientCredentials, username, password);
    }

    public Response<ServiceStore> getStoreInformation(UUID storeId) {
        return real == null ? null : real.getStoreInformation(storeId);
    }

    public Response<Boolean> searchStore() {
        return real == null ? null : real.searchStore();
    }

    public Response<List<ServiceItem>> searchItem(String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating) {
        return real == null ? null : real.searchItem(keyword, category, minPrice, maxPrice, itemRating, storeRating);
    }

    public Response<Boolean> addItemToCart(UUID clientCredentials, UUID itemId, int quantity, UUID storeId) {
        return real == null ? null : real.addItemToCart(clientCredentials, itemId, quantity, storeId);
    }

    public Response<List<ServiceShoppingBasket>> getCart(UUID clientCredentials) {
        return real == null ? null : real.getCart(clientCredentials);
    }

    public Response<Boolean> purchaseShoppingCart() {
        return real == null ? null : real.purchaseShoppingCart();
    }


    public Response<UUID> logout(UUID clientCredentials) {
        return real == null ? null : real.logout(clientCredentials);
    }

    public Response<ServiceStore> createStore(UUID clientCredentials , String storeName , String storeDescription) {
        return real == null? null : real.createStore(clientCredentials , storeName , storeDescription);
    }

    public Response<ServiceItem> addItemToStore(UUID clientCredentials, String name, double price, UUID storeId, int quantity, String description) {
        return real == null ? null : real.addItemToStore(clientCredentials, name, price, storeId, quantity, description);
    }

    public Response<Boolean> setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId) {
        return real == null ? null : real.setItemQuantity(clientCredentials, storeId, itemId);
    }

    public Response<Boolean> stockManagementChangeItemInfo(UUID clientCredentials, UUID storeId, UUID itemId, String name, String description) {
        return real == null ? null : real.stockManagementChangeItemInfo(clientCredentials, storeId, itemId, name, description);
    }

    public Response<Boolean> setStorePolicy() {
        return real == null ? null : real.setStorePolicy();
    }

    public Response<Boolean> appointStoreOwner(UUID clientCredentials, UUID appointee, UUID storeId) {
        return real == null ? null : real.appointStoreOwner(clientCredentials, appointee, storeId
        );
    }

    public Response<Boolean> appointStoreManager(UUID clientCredentials, UUID appointee, UUID storeId) {
        return real == null ? null : real.appointStoreManager(clientCredentials, appointee, storeId);
    }

    public Response<Boolean> setStoreManagerPermissions(UUID clientCredentials, UUID manager,
                                              UUID storeId, List<Integer> permissions) {
        return real == null ? null : real.setStoreManagerPermissions(clientCredentials, manager, storeId, permissions);
    }

    public Response<Boolean> closeStore(UUID clientCredentials, UUID storeId) {
        return real == null ? null : real.closeStore(clientCredentials, storeId);
    }

    public Response<List<ServiceUser>> getStoreStaffList(UUID clientCredentials, UUID storeId) {
        return real == null ? null : real.getStoreStaffList(clientCredentials, storeId);
    }

    public Response<List<ServiceSale>> getStoreSaleHistory(UUID clientCredentials, UUID storeId) {
        return real == null ? null : real.getStoreSaleHistory(clientCredentials, storeId);
    }

    public Response<Boolean> storeManagerActions() {
        return real == null ? null : real.storeManagerActions();
    }

    public Response<List<ServiceSale>> getStoreSaleHistorySystemAdmin(UUID clientCredentials, UUID storeId) {
        return real == null ? null : real.getStoreSaleHistorySystemAdmin(clientCredentials, storeId);
    }

    public Void resetService() {
        return real == null ? null : real.resetService();
    }

    @Override
    public Response<Boolean> addItemCategory(UUID clientCredentials, UUID storeId, UUID itemId, String category) {
        return real == null ? null : real.addItemCategory(clientCredentials, storeId, itemId, category);
    }

    public Response<ServiceItem> getItemInformation(UUID storeId, UUID itemId) {
        return real == null ? null : real.getItemInformation(storeId, itemId);
    }

    @Override
    public Response<Boolean> validateOrder(UUID clientCredentials) {
        return real == null ? null : real.validateOrder(clientCredentials);
    }

    @Override
    public Response<Boolean> validatePayment(UUID clientCredentials) {
        return real == null ? null : real.validatePayment(clientCredentials);
    }

    @Override
    public Response<UUID> confirmOrder(UUID clientCredentials) {
        return real == null ? null : real.confirmOrder(clientCredentials);
    }

    @Override
    public Response<Boolean> removeItemFromStore(UUID clientCredentials, UUID storeId, UUID itemId) {
        return real == null ? null : real.removeItemFromStore(clientCredentials, storeId, itemId);
    }

    @Override
    public Response<Boolean> isLoggedIn(UUID userId) {
        return real == null ? null : real.isLoggedIn(userId);
    }
}

