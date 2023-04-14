package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class DelayedAlerts extends ProjectTest {

    public DelayedAlerts(Bridge real) {
        super(real);
    }

    public boolean systemDelayedAlert() {
        return bridge.systemDelayedAlert();
    }

    public boolean userDelayedAlert() {
        return bridge.userDelayedAlert();
    }
}
