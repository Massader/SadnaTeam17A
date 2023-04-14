package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class ReceiveStoreInfo extends ProjectTest {

    public ReceiveStoreInfo(Bridge real) {
        super(real);
    }

    public boolean receiveStoreInfo() {
        return bridge.receiveStoreInfo();
    }
}
