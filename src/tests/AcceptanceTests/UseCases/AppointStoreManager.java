package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class AppointStoreManager extends ProjectTest {

    public AppointStoreManager(Bridge real) {
        super(real);
    }

    public boolean appointStoreManager() {
        return bridge.appointStoreManager();
    }
}
