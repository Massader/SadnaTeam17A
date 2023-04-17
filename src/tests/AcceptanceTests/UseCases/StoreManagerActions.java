package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.ServiceItem;
import ServiceLayer.ServiceObjects.ServiceSale;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoreManagerActions extends ProjectTest {


    UUID storeManager;


    UUID storeId;
    List<Integer> permissions;
    UUID founder;
    UUID client;
    UUID client2;

    ServiceStore store;


    @BeforeClass
    public void setUp() {
        bridge.setReal();

        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();

        bridge.register("Manager1", "pass");
        client2 = bridge.enterSystem();
        storeManager = bridge.login(client2, "Manager1", "pass");
        Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId);
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
        bridge.logout(storeManager);
        bridge.exitSystem(client2);
    }

    @Test
    public void StoreManagerActionsSuccess() {
        List<ServiceSale> saleHistory = bridge.getStoreSaleHistory(storeManager,storeId);
        Assert.assertTrue(saleHistory.isEmpty());}


    @Test
    public void StoreManagerActionsFail() {
        ServiceItem serviceItem = bridge.stockManagementAddNewItem(storeManager,"bannana",5.5,storeId,20,"yellow fruit");
        Assert.assertNull(serviceItem);

}


}
