package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class OpenStore extends ProjectTest {

    public OpenStore(Bridge real) {
        super(real);
    }

    public boolean openStore() {
        return proxy.openStore();
    }
}
