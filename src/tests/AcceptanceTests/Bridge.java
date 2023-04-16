package AcceptanceTests;

import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceStore;
import ServiceLayer.ServiceObjects.ServiceSale;

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
    UUID enterSystem();
    Boolean exitSystem(UUID clientCredentials);
    Boolean register(String username, String password);
    UUID login(UUID clientCredentials, String username, String password);
    ServiceStore receiveStoreInfo(UUID storeId);
    Boolean searchStore();
    Boolean searchItem();
    Boolean saveItemInShoppingCart();
    Boolean viewShoppingCartItems();
    Boolean purchaseShoppingCartPayment();
    Boolean purchaseShoppingCartSupply();
    UUID logout(UUID clientCredentials);
    ServiceStore openStore(UUID clientCredentials , String storeName , String storeDescription);
    Boolean stockManagementAddNewItem();
    Boolean stockManagementRemoveItem();
    Boolean stockManagementChangeItemInfo();
    Boolean setStorePolicy();
    Boolean appointStoreOwner(UUID clientCredentials, UUID appointee, UUID storeId);
    Boolean appointStoreManager(UUID clientCredentials, UUID appointee, UUID storeId);
    Boolean setStoreManagerPermissions();
    Boolean closeStore(UUID clientCredentials, UUID storeId);
    Boolean getStoreStaffList();
    List<ServiceSale> getStoreSaleHistory(UUID clientCredentials,UUID storeId);
    Boolean storeManagerActions();
    Boolean getStoreSaleHistorySystemAdmin();
}
