package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class SetStorePolicy extends ProjectTest {

    public SetStorePolicy(Bridge real) {
        super(real);
    }

    public boolean setStorePolicy() {
        return bridge.setStorePolicy();
    }
}
