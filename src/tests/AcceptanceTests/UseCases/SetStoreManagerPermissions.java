package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class SetStoreManagerPermissions extends ProjectTest {

    public SetStoreManagerPermissions(Bridge real) {
        super(real);
    }

    public boolean setStoreManagerPermissions() {
        return bridge.setStoreManagerPermissions();
    }
}
