package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class CloseStore extends ProjectTest {

    public CloseStore(Bridge real) {
        super(real);
    }

    public boolean closeStore() {
        return bridge.closeStore();
    }
}
