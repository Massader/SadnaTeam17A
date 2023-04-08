package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class StoreManagerActions extends ProjectTest {

    public StoreManagerActions(Bridge real) {
        super(real);
    }

    public boolean storeManagerActions() {
        return proxy.storeManagerActions();
    }
}
