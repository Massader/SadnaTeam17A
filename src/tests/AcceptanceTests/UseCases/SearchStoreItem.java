package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class SearchStoreItem extends ProjectTest {

    public SearchStoreItem(Bridge real) {
        super(real);
    }

    public boolean searchStore() {
        return bridge.searchStore();
    }

    public boolean searchItem() {
        return bridge.searchItem();
    }
}
