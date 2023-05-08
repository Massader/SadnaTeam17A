package AcceptanceTests;

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
    Response<List<ServiceItem>> searchItem(String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating);
    Response<Boolean> addItemToCart(UUID clientCredentials, UUID itemId, int quantity, UUID storeId);
    Response<List<ServiceShoppingBasket>> getCart(UUID clientCredentials);
    Response<Boolean> purchaseCart(UUID clientCredentials, double expectedPrice, String address, String credit);
    Response<UUID> logout(UUID clientCredentials);
    Response<ServiceStore> createStore(UUID clientCredentials , String storeName , String storeDescription);
    Response<ServiceItem> addItemToStore(UUID clientCredentials, String name, double price, UUID storeId, int quantity, String description);
    Response<Boolean> setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId);
    Response<Boolean> stockManagementChangeItemInfo(UUID clientCredentials, UUID storeId, UUID itemId, String name,String description);
    Response<Boolean> setStorePolicy();
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
    Response<Integer> numOfStores();
    Response<Integer> numOfOpenStores();
    Response<Integer> numOfClients();
    Response<List<Role>> getUserRoles(UUID clientCredentials);
    Response<ServiceUser> getUserInfo(UUID clientCredentials);
    Response<Boolean> reopenStore(UUID clientCredentials, UUID storeId);
    Response<UUID> getAdminCredentials();
    Response<Boolean> shutdownStore(UUID clientCredentials, UUID storeId);

    Response<Integer> numOfLoggedInUsers();
    Response<ConcurrentHashMap<String, UUID>> getUserNames();
    Response<Double> getCartTotal(UUID clientCredentials);
}
