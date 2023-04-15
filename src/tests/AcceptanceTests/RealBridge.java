package AcceptanceTests;
import ServiceLayer.*;

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

    public UUID enterSystem() {
        return service.createClient();
    }

    public Boolean exitSystem(UUID clientCredentials) {
        return service.closeClient(clientCredentials);
    }

    public Boolean register(String username, String password) {
        return service.register(username, password);
    }

    public UUID login(UUID clientCredentials, String username, String password) {
        return service.login(clientCredentials, username, password);
    }

    public Boolean receiveStoreInfo() {
        return false;
    }

    public Boolean searchStore() {
        return false;
    }

    public Boolean searchItem() {
        return false;
    }

    public Boolean saveItemInShoppingCart() {
        return false;
    }

    public Boolean viewShoppingCartItems() {
        return false;
    }

    public Boolean purchaseShoppingCartPayment() {
        return false;
    }

    public Boolean purchaseShoppingCartSupply() {
        return false;
    }

    public UUID logout(UUID clientCredentials) {
        return service.logout(clientCredentials);
    }

    public Boolean openStore() {
        return false;
    }

    public Boolean stockManagementAddNewItem() {
        return false;
    }

    public Boolean stockManagementRemoveItem() {
        return false;
    }

    public Boolean stockManagementChangeItemInfo() {
        return false;
    }

    public Boolean setStorePolicy() {
        return false;
    }

    public Boolean appointStoreOwner() {
        return false;
    }

    public Boolean appointStoreManager() {
        return false;
    }

    public Boolean setStoreManagerPermissions() {
        return false;
    }

    public Boolean closeStore() {
        return false;
    }

    public Boolean getStoreStaffList() {
        return false;
    }

    public Boolean getStoreSaleHistory() {
        return false;
    }

    public Boolean storeManagerActions() {
        return false;
    }

    public Boolean getStoreSaleHistorySystemAdmin() {
        return false;
    }
}
