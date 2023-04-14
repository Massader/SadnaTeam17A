package AcceptanceTests;

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
    Boolean receiveStoreInfo();
    Boolean searchStore();
    Boolean searchItem();
    Boolean saveItemInShoppingCart();
    Boolean viewShoppingCartItems();
    Boolean purchaseShoppingCartPayment();
    Boolean purchaseShoppingCartSupply();
    Boolean logoutRegisterUser();
    Boolean openStore();
    Boolean stockManagementAddNewItem();
    Boolean stockManagementRemoveItem();
    Boolean stockManagementChangeItemInfo();
    Boolean setStorePolicy();
    Boolean appointStoreOwner();
    Boolean appointStoreManager();
    Boolean setStoreManagerPermissions();
    Boolean closeStore();
    Boolean getStoreStaffList();
    Boolean getStoreSaleHistory();
    Boolean storeManagerActions();
    Boolean getStoreSaleHistorySystemAdmin();
}
