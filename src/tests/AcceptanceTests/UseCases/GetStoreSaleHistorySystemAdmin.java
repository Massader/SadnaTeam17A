package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class GetStoreSaleHistorySystemAdmin extends ProjectTest {

    public GetStoreSaleHistorySystemAdmin(Bridge real) {
        super(real);
    }

    public boolean getStoreSaleHistorySystemAdmin() {
        return bridge.getStoreSaleHistorySystemAdmin();
    }
}
