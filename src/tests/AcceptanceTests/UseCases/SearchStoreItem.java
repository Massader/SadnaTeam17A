package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class SearchStoreItem extends ProjectTest {

    public SearchStoreItem(Bridge real) {
        super(real);
    }

    public boolean searchStore() {
        return proxy.searchStore();
    }

    public boolean searchItem() {
        return proxy.searchItem();
    }
}
