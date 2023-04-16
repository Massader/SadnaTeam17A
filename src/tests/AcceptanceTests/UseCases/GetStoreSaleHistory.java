package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.ServiceSale;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.*;

import java.util.List;
import java.util.UUID;

public class GetStoreSaleHistory extends ProjectTest {
    UUID founder;
    UUID client;
    ServiceStore store;
    UUID storeId;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
    }

    @Before
    public void beforeEach()  {
        client = bridge.enterSystem();
    }

    @After
    public void tearDown() {
        bridge.exitSystem(client);
    }

    @AfterClass
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
    }
    @Test
    public void GetStoreSaleHistorySuccess() {
        List<ServiceSale> saleHistory = bridge.getStoreSaleHistory(founder,storeId);
        Assert.assertTrue(saleHistory.isEmpty());}
    @Test
    public void GetStoreSaleHistoryNotExistingStoreFail() {
        List<ServiceSale> saleHistory = bridge.getStoreSaleHistory(founder,storeId);
        Assert.assertNull(saleHistory);
    }






//    public GetStoreSaleHistory(Bridge real) {
//        super(real);
//    }
//
//    public boolean getStoreSaleHistory() {
//        return bridge.getStoreSaleHistory();
//    }
}
