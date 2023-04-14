package AcceptanceTests.UseCases;
import AcceptanceTests.*;

public class GetStoreStaffList extends ProjectTest {

    public GetStoreStaffList(Bridge real) {
        super(real);
    }

    public boolean getStoreStaffList() {
        return bridge.getStoreStaffList();
    }
}
