package AcceptanceTests;
import DomainLayer.Market.Users.Roles.Role;
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

    public Response<Boolean> systemBoot() {
        return null;
    }

    public Response<Boolean> integrityTest() {
        return null;
    }

    public Response<Boolean> addService() {
        return null;
    }

    public Response<Boolean> updateService() {
        return null;
    }

    public Response<Boolean> payForShoppingCart() {
        return null;
    }

    public Response<Boolean> checkForSupply() {
        return null;
    }

    public Response<Boolean> systemRealTimeAlert() {
        return null;
    }

    public Response<Boolean> userRealTimeAlert() {
        return null;
    }

    public Response<Boolean> systemDelayedAlert() {
        return null;
    }

    public Response<Boolean> userDelayedAlert() {
        return null;
    }

    public Response<UUID> createClient() {
        return service.createClient();
    }

    public Response<Boolean> closeClient(UUID clientCredentials) {
        return service.closeClient(clientCredentials);
    }

    public Response<Boolean> register(String username, String password) {
        return service.register(username, password);
    }

    public Response<ServiceUser> login(UUID clientCredentials, String username, String password) {
        //return service.login(clientCredentials, username, password);
        return null;
    }

    public Response<ServiceStore> getStoreInformation(UUID storeId) {
        return service.getStoreInformation(storeId);
    }

    public Response<Boolean> searchStore() {
        return null;
    }

    public Response<List<ServiceItem>> searchItem(String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating) {
        return service.searchItem(keyword, category, minPrice, maxPrice, itemRating, storeRating);
    }

    public Response<Boolean> addItemToCart(UUID clientCredentials, UUID itemId, int quantity, UUID storeId) {
        return service.addItemToCart(clientCredentials, itemId, quantity, storeId);
    }

    public Response<List<ServiceShoppingBasket>> getCart(UUID clientCredentials) {
        return service.getCart(clientCredentials);
    }

    public Response<Boolean> purchaseCart(UUID clientCredentials, double expectedPrice, String address, String credit) {
        return service.purchaseCart(clientCredentials, expectedPrice, address, credit);
    }


    public Response<UUID> logout(UUID clientCredentials) {
        return service.logout(clientCredentials);
    }

    public Response<ServiceStore> createStore(UUID clientCredentials, String storeName, String storeDescription) {
        return service.createStore(clientCredentials, storeName, storeDescription);
    }

    public Response<ServiceItem> addItemToStore(UUID clientCredentials, String name, double price, UUID storeId, int quantity, String description) {
        return service.addItemToStore(clientCredentials, name, price, storeId, quantity, description);
    }

    public Response<Boolean> setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId) {
        return service.setItemQuantity(clientCredentials, storeId, itemId, 0);

    }

    public Response<Boolean> stockManagementChangeItemInfo(UUID clientCredentials, UUID storeId, UUID itemId, String name, String description) {
        Boolean check = false;
        if (description != null) {
            check = service.setItemDescription(clientCredentials, storeId, itemId, description).getValue();
        }
        if (name != null) {
            check = check && service.setItemName(clientCredentials, storeId, itemId, name).getValue();
        }
        return Response.getSuccessResponse(check);
    }

    public Response<Boolean> setStorePolicy() {
        return null;
    }

    public Response<Boolean> appointStoreOwner(UUID clientCredentials, UUID appointee, UUID storeId) {
        return service.appointStoreOwner(clientCredentials, appointee, storeId);
    }

    public Response<Boolean> appointStoreManager(UUID clientCredentials, UUID appointee, UUID storeId) {
        return service.appointStoreManager(clientCredentials, appointee, storeId);
    }

    public Response<Boolean> setStoreManagerPermissions(UUID clientCredentials, UUID manager,
                                              UUID storeId, List<Integer> permissions) {
        return service.setManagerPermissions(clientCredentials, manager, storeId, permissions);
    }

    public Response<Boolean> closeStore(UUID clientCredentials, UUID storeId) {
        return service.closeStore(clientCredentials, storeId);
    }

    public Response<List<ServiceUser>> getStoreStaffList(UUID clientCredentials, UUID storeId) {
        return service.getStoreStaff(clientCredentials, storeId);
    }

    public Response<List<ServiceSale>> getStoreSaleHistory(UUID clientCredentials, UUID storeId) {
        return service.getStoreSaleHistory(clientCredentials, storeId);
    }

    public Response<Boolean> storeManagerActions() {
        return null;
    }

    public Response<List<ServiceSale>> getStoreSaleHistorySystemAdmin(UUID clientCredentials, UUID storeId) {
        return service.getStoreSaleHistory(clientCredentials, storeId);
    }

    public Void resetService() {
        return service.resetService();
    }

    @Override
    public Response<Boolean> addItemCategory(UUID clientCredentials, UUID storeId, UUID itemId, String category) {
        return service.addItemCategory(clientCredentials, storeId, itemId, category);
    }

    public Response<ServiceItem> getItemInformation(UUID storeId, UUID itemId) {
        return service.getItemInformation(storeId, itemId);
    }

    @Override
    public Response<Boolean> validateOrder(UUID clientCredentials) {
        return service.validateOrder(clientCredentials);
    }

    @Override
    public Response<Boolean> validatePayment(UUID clientCredentials) {
        return service.validatePayment(clientCredentials);
    }

    @Override
    public Response<UUID> confirmOrder(UUID clientCredentials) {
        return service.confirmOrder(clientCredentials);
    }

    @Override
    public Response<Boolean> removeItemFromStore(UUID clientCredentials, UUID storeId, UUID itemId) {
        return service.removeItemFromStore(clientCredentials, storeId, itemId);
    }

    @Override
    public Response<Boolean> isLoggedIn(UUID userId) {
        return service.isLoggedIn(userId);
    }

    public Response<Integer> numOfUsers() {
        return service.numOfUsers();
    }

    public Response<Integer> numOfStores() {
        return service.numOfStores();
    }

    public Response<Integer> numOfOpenStores() {
        return service.numOfOpenStores();
    }

    public Response<Integer> numOfClients() {
        return service.numOfClients();
    }

    public Response<List<Role>> getUserRoles(UUID clientCredentials) {
        return service.getUserRoles(clientCredentials);
    }

    public Response<ServiceUser> getUserInfo(UUID clientCredentials) {
        return service.getUserInfo(clientCredentials);
    }

    public Response<Boolean> reopenStore(UUID clientCredentials, UUID storeId) {
        return service.reopenStore(clientCredentials, storeId);
    }

    public Response<UUID> getAdminCredentials() {
        return service.getAdminCredentials();
    }
}
