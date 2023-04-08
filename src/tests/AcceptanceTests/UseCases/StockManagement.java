package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class StockManagement extends ProjectTest {

    public StockManagement(Bridge real) {
        super(real);
    }

    public boolean stockManagementAddNewItem() {
        return proxy.stockManagementAddNewItem();
    }

    public boolean stockManagementRemoveItem() {
        return proxy.stockManagementRemoveItem();
    }

    public boolean stockManagementChangeItemInfo() {
        return proxy.stockManagementChangeItemInfo();
    }
}
