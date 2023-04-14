package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class Supply extends ProjectTest {

    public Supply(Bridge real) {
        super(real);
    }

    public boolean checkForSupply() {
        return bridge.checkForSupply();
    }
}
