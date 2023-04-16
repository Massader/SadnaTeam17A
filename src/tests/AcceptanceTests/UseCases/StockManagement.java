package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class StockManagement extends ProjectTest {

    public StockManagement(Bridge real) {
        super(real);
    }

    public boolean stockManagementAddNewItem() {
       // return bridge.stockManagementAddNewItem();
    }

    public boolean stockManagementRemoveItem() {
        return bridge.stockManagementRemoveItem();
    }

    public boolean stockManagementChangeItemInfo() {
        return bridge.stockManagementChangeItemInfo();
    }
}
