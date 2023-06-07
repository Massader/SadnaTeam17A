package AcceptanceTests;
import APILayer.Main;
import DomainLayer.Market.Notification;
import DomainLayer.Market.UserController;
import DomainLayer.Market.Users.Roles.Role;
import ServiceLayer.*;
import ServiceLayer.ServiceObjects.*;
import org.springframework.boot.SpringApplication;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RealBridge implements Bridge {

    Service service;
    public static boolean initielized = false;

    @Override
    public void setReal() {

    }

    public RealBridge() {
        if(!initielized){
            SpringApplication.run(Main.class);
            initielized = true;
        }
        service = Service.getInstance();
        service.init(UserController.repositoryFactory);
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
        return service.login(clientCredentials, username, password, null);
    }

    public Response<ServiceStore> getStoreInformation(UUID clientCredentials, UUID storeId) {
        return service.getStoreInformation(clientCredentials, storeId);
    }

    public Response<Boolean> searchStore() {
        return null;
    }

    public Response<List<ServiceItem>> searchItem(String keyword, String category, Double minPrice, Double maxPrice, Integer itemRating, Integer storeRating, Integer number, Integer page, UUID storeId) {
        return service.searchItem(keyword, category, minPrice, maxPrice, itemRating, storeRating, number, page, storeId);
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

    public Response<Boolean> setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId, int quantity) {
        return service.setItemQuantity(clientCredentials, storeId, itemId, quantity);
    }

    public Response<Boolean> setItemName(UUID clientCredentials, UUID storeId, UUID itemId, String name) {
        return service.setItemName(clientCredentials, storeId, itemId, name);
    }

    public Response<Boolean> setItemDescription(UUID clientCredentials, UUID storeId, UUID itemId, String description) {
        return service.setItemDescription(clientCredentials, storeId, itemId, description);
    }


    public Response<Boolean> setItemPrice(UUID clientCredentials, UUID storeId, UUID itemId, double price) {
        return service.setItemPrice(clientCredentials, storeId, itemId, price);
    }

    public Response<Boolean> addPolicyTerm(UUID clientCredentials, UUID storeId, int rule, Boolean atLeast, int quantity, UUID itemId, String category) {
        //return service.addPolicyTermByStoreOwner(clientCredentials, storeId, rule, atLeast, quantity, itemId, category);
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

    public Response<Boolean> shutdownStore(UUID clientCredentials, UUID storeId) {
        return service.shutdownStore(clientCredentials, storeId);
    }

    public Response<Integer> numOfLoggedInUsers() {
        return service.numOfLoggedInUsers();
    }

    public Response<ConcurrentHashMap<String, UUID>> getUserNames() {
        return service.getUserNames();
    }

    public Response<Double> getCartTotal(UUID clientCredentials) {
        return service.getCartTotal(clientCredentials);
    }

    public Response<Double> addStoreRating(UUID clientCredentials, UUID storeId ,int rating) {
        return service.addStoreRating(clientCredentials, storeId, rating);
    }

    public Response<Boolean> addItemRating(UUID clientCredentials, UUID itemId, UUID storeId, int rating) {
        return service.addItemRating(clientCredentials, itemId,storeId, rating);
    }

    public Response<List<ServiceMessage>> getMessages(UUID clientCredentials, UUID recipient) {
        return service.getMessages(clientCredentials, recipient);
    }

    public Response<List<Notification>> getNotifications(UUID clientCredentials, UUID recipient) {
        return service.getNotifications(clientCredentials, recipient);
    }

    public Response<Boolean> removeStoreRole(UUID clientCredentials, UUID roleToRemove, UUID storeId) {
        return service.removeStoreRole(clientCredentials, roleToRemove, storeId);
    }

    public Response<Boolean> addItemPolicyTerm(UUID clientCredentials, UUID storeId, UUID itemId, int quantity, boolean atLeast) {
        return service.addItemPolicyTerm(clientCredentials, storeId, itemId, quantity, atLeast);
    }
    public Response<Boolean> addCategoryPolicyTerm(UUID clientCredentials, UUID storeId, String category, int quantity, boolean atLeast) {
        return service.addCategoryPolicyTerm(clientCredentials, storeId, category, quantity, atLeast);
    }
    public Response<Boolean> addBasketPolicyTerm(UUID clientCredentials, UUID storeId, int quantity, boolean atLeast) {
        return service.addBasketPolicyTerm(clientCredentials, storeId, quantity, atLeast);
    }
}
