package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class RealTimeAlerts extends ProjectTest {

    public RealTimeAlerts(Bridge real) {
        super(real);
    }

    public boolean systemRealTimeAlert() {
        return bridge.systemRealTimeAlert();
    }

    public boolean userRealTimeAlert() {
        return bridge.userRealTimeAlert();
    }
}
