package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class EnterSystem extends ProjectTest {

    public EnterSystem(Bridge real) {
        super(real);
    }

    public boolean enterSystem() {
        return bridge.enterSystem();
    }
}
