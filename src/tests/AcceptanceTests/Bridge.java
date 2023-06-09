package AcceptanceTests;

import DomainLayer.Market.Notification;
import DomainLayer.Market.Users.Roles.Role;
import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.*;


import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface Bridge {
    public void setReal();
    Response<Boolean> systemBoot();
    Response<Boolean> integrityTest();
    Response<Boolean> addService();
    Response<Boolean> updateService();
    Response<Boolean> payForShoppingCart();
    Response<Boolean> checkForSupply();
    Response<Boolean> systemRealTimeAlert();
    Response<Boolean> userRealTimeAlert();
    Response<Boolean> systemDelayedAlert();
    Response<Boolean> userDelayedAlert();
    Response<UUID> createClient();
    Response<Boolean> closeClient(UUID clientCredentials);
    Response<Boolean> register(String username, String password);
    Response<ServiceUser> login(UUID clientCredentials, String username, String password);
    Response<ServiceStore> getStoreInformation(UUID clientCredentials, UUID storeId);
    Response<Boolean> searchStore();
    Response<List<ServiceItem>> searchItem(String keyword, String category, Double minPrice, Double maxPrice, Integer itemRating, Integer storeRating, Integer number, Integer page, UUID storeId);
    Response<Boolean> addItemToCart(UUID clientCredentials, UUID itemId, int quantity, UUID storeId);
    Response<List<ServiceShoppingBasket>> getCart(UUID clientCredentials);
    Response<Boolean> purchaseCart(UUID clientCredentials, double expectedPrice, String address, String credit);
    Response<UUID> logout(UUID clientCredentials);
    Response<ServiceStore> createStore(UUID clientCredentials , String storeName , String storeDescription);
    Response<ServiceItem> addItemToStore(UUID clientCredentials, String name, double price, UUID storeId, int quantity, String description);
    Response<Boolean> setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId, int quantity);
    Response<Boolean> setItemName(UUID clientCredentials, UUID storeId, UUID itemId, String name);
    Response<Boolean> setItemDescription(UUID clientCredentials, UUID storeId, UUID itemId, String description);
    Response<Boolean> setItemPrice(UUID clientCredentials, UUID storeId, UUID itemId, double price);
    Response<Boolean> addPolicyTerm(UUID clientCredentials, UUID storeId, int rule, Boolean atLeast, int quantity, UUID itemId, String category);
    Response<Boolean> appointStoreOwner(UUID clientCredentials, UUID appointee, UUID storeId);
    Response<Boolean> appointStoreManager(UUID clientCredentials, UUID appointee, UUID storeId);
    Response<Boolean> setStoreManagerPermissions(UUID clientCredentials, UUID manager, UUID storeId, List<Integer> permissions);
    Response<Boolean> closeStore(UUID clientCredentials, UUID storeId);
    Response<List<ServiceUser>> getStoreStaffList(UUID clientCredentials, UUID storeId);
    Response<List<ServiceSale>> getStoreSaleHistory(UUID clientCredentials,UUID storeId);
    Response<Boolean> storeManagerActions();
    Response<List<ServiceSale>> getStoreSaleHistorySystemAdmin(UUID clientCredentials,UUID storeId);
    Void resetService();
    Response<Boolean> addItemCategory(UUID clientCredentials, UUID storeId, UUID itemId, String category);
    Response<ServiceItem>  getItemInformation(UUID storeId, UUID itemId);
    Response<Boolean> validateOrder(UUID clientCredentials);
    Response<Boolean> validatePayment(UUID clientCredentials);
    Response<UUID> confirmOrder(UUID clientCredentials);
    Response<Boolean> removeItemFromStore(UUID clientCredentials, UUID storeId, UUID itemId);
    Response<Boolean> isLoggedIn(UUID userId);
    Response<Integer> numOfUsers();
    Response<Long> numOfStores();
    Response<Long> numOfOpenStores();
    Response<Integer> numOfClients();
    Response<List<Role>> getUserRoles(UUID clientCredentials);
    Response<ServiceUser> getUserInfo(UUID clientCredentials);
    Response<Boolean> reopenStore(UUID clientCredentials, UUID storeId);
    Response<UUID> getAdminCredentials();
    Response<Boolean> shutdownStore(UUID clientCredentials, UUID storeId);
    Response<Integer> numOfLoggedInUsers();
    Response<ConcurrentHashMap<String, UUID>> getUserNames();
    Response<Double> getCartTotal(UUID clientCredentials);
    Response<Double> addStoreRating(UUID clientCredentials, UUID storeId ,int rating);
    Response<Boolean> addItemRating(UUID clientCredentials, UUID itemId, UUID storeId, int rating);
    Response<List<ServiceMessage>> getMessages(UUID clientCredentials, UUID recipient);
    Response<List<Notification>> getNotifications(UUID clientCredentials, UUID recipient);
    Response<Boolean> removeStoreRole(UUID clientCredentials, UUID roleToRemove, UUID storeId);
    Response<Boolean> addItemPolicyTerm(UUID clientCredentials, UUID storeId, UUID itemId, int quantity, boolean atLeast);
    Response<Boolean> addCategoryPolicyTerm(UUID clientCredentials, UUID storeId, String category, int quantity, boolean atLeast);
    Response<Boolean> addBasketPolicyTerm(UUID clientCredentials, UUID storeId, int quantity, boolean atLeast);
}
