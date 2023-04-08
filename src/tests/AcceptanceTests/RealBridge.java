package AcceptanceTests;
import ServiceLayer.*;

public class RealBridge implements Bridge {

    Service service;

    public RealBridge(Service service) {
        this.service = service;
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

    public Boolean enterSystem() {
        return false;
    }

    public Boolean exitSystem() {
        return false;
    }

    public Boolean register() {
        return false;
    }

    public Boolean login() {
        return false;
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

    public Boolean logoutRegisterUser() {
        return false;
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
