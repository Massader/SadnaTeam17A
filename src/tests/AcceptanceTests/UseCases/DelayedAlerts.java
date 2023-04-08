package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class DelayedAlerts extends ProjectTest {

    public DelayedAlerts(Bridge real) {
        super(real);
    }

    public boolean systemDelayedAlert() {
        return proxy.systemDelayedAlert();
    }

    public boolean userDelayedAlert() {
        return proxy.userDelayedAlert();
    }
}
