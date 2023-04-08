package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class ExitSystem extends ProjectTest {

    public ExitSystem(Bridge real) {
        super(real);
    }

    public boolean exitSystem() {
        return proxy.exitSystem();
    }
}
