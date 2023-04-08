package AcceptanceTests;

public class ProxyBridge implements Bridge {

    Bridge real;

    public ProxyBridge(Bridge real) {
        this.real = real;
    }

    public Boolean systemBoot() {
        return real == null? null : real.systemBoot();
    }

    public Boolean integrityTest() {
        return real == null? null : real.integrityTest();
    }

    public Boolean addService() {
        return real == null? null : real.addService();
    }

    public Boolean updateService() {
        return real == null? null : real.updateService();
    }

    public Boolean payForShoppingCart() {
        return real == null? null : real.payForShoppingCart();
    }

    public Boolean checkForSupply() {
        return real == null? null : real.checkForSupply();
    }

    public Boolean systemRealTimeAlert() {
        return real == null? null : real.systemRealTimeAlert();
    }

    public Boolean userRealTimeAlert() {
        return real == null? null : real.userRealTimeAlert();
    }

    public Boolean systemDelayedAlert() {
        return real == null? null : real.systemDelayedAlert();
    }

    public Boolean userDelayedAlert() {
        return real == null? null : real.userDelayedAlert();
    }

    public Boolean enterSystem() {
        return real == null? null : real.enterSystem();
    }

    public Boolean exitSystem() {
        return real == null? null : real.exitSystem();
    }

    public Boolean register() {
        return real == null? null : real.register();
    }

    public Boolean login() {
        return real == null? null : real.login();
    }

    public Boolean receiveStoreInfo() {
        return real == null? null : real.receiveStoreInfo();
    }

    public Boolean searchStore() {
        return real == null? null : real.searchStore();
    }

    public Boolean searchItem() {
        return real == null? null : real.searchItem();
    }

    public Boolean saveItemInShoppingCart() {
        return real == null? null : real.saveItemInShoppingCart();
    }

    public Boolean viewShoppingCartItems() {
        return real == null? null : real.viewShoppingCartItems();
    }

    public Boolean purchaseShoppingCartPayment() {
        return real == null? null : real.purchaseShoppingCartPayment();
    }

    public Boolean purchaseShoppingCartSupply() {
        return real == null? null : real.purchaseShoppingCartSupply();
    }

    public Boolean logoutRegisterUser() {
        return real == null? null : real.logoutRegisterUser();
    }

    public Boolean openStore() {
        return real == null? null : real.openStore();
    }

    public Boolean stockManagementAddNewItem() {
        return real == null? null : real.stockManagementAddNewItem();
    }

    public Boolean stockManagementRemoveItem() {
        return real == null? null : real.stockManagementRemoveItem();
    }

    public Boolean stockManagementChangeItemInfo() {
        return real == null? null : real.stockManagementChangeItemInfo();
    }

    public Boolean setStorePolicy() {
        return real == null? null : real.setStorePolicy();
    }

    public Boolean appointStoreOwner() {
        return real == null? null : real.appointStoreOwner();
    }

    public Boolean appointStoreManager() {
        return real == null? null : real.appointStoreManager();
    }

    public Boolean setStoreManagerPermissions() {
        return real == null? null : real.setStoreManagerPermissions();
    }

    public Boolean closeStore() {
        return real == null? null : real.closeStore();
    }

    public Boolean getStoreStaffList() {
        return real == null? null : real.getStoreStaffList();
    }

    public Boolean getStoreSaleHistory() {
        return real == null? null : real.getStoreSaleHistory();
    }

    public Boolean storeManagerActions() {
        return real == null? null : real.storeManagerActions();
    }

    public Boolean getStoreSaleHistorySystemAdmin() {
        return real == null? null : real.getStoreSaleHistorySystemAdmin();
    }
}
