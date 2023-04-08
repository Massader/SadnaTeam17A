package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class AppointStoreOwner extends ProjectTest {

    public AppointStoreOwner(Bridge real) {
        super(real);
    }

    public boolean appointStoreOwner() {
        return proxy.appointStoreOwner();
    }
}
