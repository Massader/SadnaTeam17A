package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class GetStoreSaleHistory extends ProjectTest {

    public GetStoreSaleHistory(Bridge real) {
        super(real);
    }

    public boolean getStoreSaleHistory() {
        return proxy.getStoreSaleHistory();
    }
}
