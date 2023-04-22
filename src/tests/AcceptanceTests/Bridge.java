package AcceptanceTests;

import ServiceLayer.ServiceObjects.*;


import java.util.List;
import java.util.UUID;

public interface Bridge {
    public void setReal();
    Boolean systemBoot();
    Boolean integrityTest();
    Boolean addService();
    Boolean updateService();
    Boolean payForShoppingCart();
    Boolean checkForSupply();
    Boolean systemRealTimeAlert();
    Boolean userRealTimeAlert();
    Boolean systemDelayedAlert();
    Boolean userDelayedAlert();
    UUID createClient();
    Boolean closeClient(UUID clientCredentials);
    Boolean register(String username, String password);
    UUID login(UUID clientCredentials, String username, String password);
    ServiceStore getStoreInformation(UUID storeId);
    Boolean searchStore();
    List<ServiceItem> searchItem(String keyword, String category, double minPrice, double maxPrice, int itemRating, int storeRating);
    Boolean addItemToCart(UUID clientCredentials, UUID itemId, int quantity, UUID storeId);
    List<ServiceShoppingBasket> getCart(UUID clientCredentials);
    Boolean purchaseShoppingCart();
    UUID logout(UUID clientCredentials);
    ServiceStore createStore(UUID clientCredentials , String storeName , String storeDescription);
    ServiceItem addItemToStore(UUID clientCredentials, String name, double price, UUID storeId, int quantity, String description);
    Boolean setItemQuantity(UUID clientCredentials, UUID storeId, UUID itemId);
    Boolean stockManagementChangeItemInfo(UUID clientCredentials, UUID storeId, UUID itemId, String name,String description);
    Boolean setStorePolicy();
    Boolean appointStoreOwner(UUID clientCredentials, UUID appointee, UUID storeId);
    Boolean appointStoreManager(UUID clientCredentials, UUID appointee, UUID storeId);
    Boolean setStoreManagerPermissions(UUID clientCredentials, UUID manager, UUID storeId, List<Integer> permissions);
    Boolean closeStore(UUID clientCredentials, UUID storeId);
    List<ServiceUser> getStoreStaffList(UUID clientCredentials, UUID storeId);
    List<ServiceSale> getStoreSaleHistory(UUID clientCredentials,UUID storeId);
    Boolean storeManagerActions();
    List<ServiceSale> getStoreSaleHistorySystemAdmin(UUID clientCredentials,UUID storeId,String userName, String password);
    Void resetService();
    Boolean addItemCategory(UUID clientCredentials, UUID storeId, UUID itemId, String category);
    ServiceItem  getItemInformation(UUID storeId, UUID itemId);
}
