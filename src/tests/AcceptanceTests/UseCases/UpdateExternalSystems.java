package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class UpdateExternalSystems extends ProjectTest {

    public UpdateExternalSystems(Bridge real) {
        super(real);
    }

    public boolean addService() {
        return bridge.addService();
    }

    public boolean updateService() {
        return bridge.updateService();
    }
}
