package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class UpdateExternalSystems extends ProjectTest {

    public UpdateExternalSystems(Bridge real) {
        super(real);
    }

    public boolean addService() {
        return proxy.addService();
    }

    public boolean updateService() {
        return proxy.updateService();
    }
}
