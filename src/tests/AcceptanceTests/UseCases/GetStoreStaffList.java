package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.ServiceStore;
import ServiceLayer.ServiceObjects.ServiceUser;
import org.junit.*;

import java.util.List;
import java.util.UUID;

public class GetStoreStaffList extends ProjectTest {

    UUID founder;
    UUID client;
    UUID storeOwner2;
    UUID client2;

    UUID storeOwner3;
    UUID client3;
    ServiceStore store;
    UUID storeId;
    Boolean check;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();

        bridge.register("storeOwner Lior", "pass");
        client2 = bridge.enterSystem();
        storeOwner2 = bridge.login(client2, "storeOwner Lior", "pass");


        bridge.register("storeOwner Ido ", "pass");
        client3 = bridge.enterSystem();
        storeOwner3 = bridge.login(client3, "storeOwner Ido ", "pass");

        check = bridge.appointStoreOwner(founder,storeOwner2,storeId);
        check = check&&  bridge.appointStoreOwner(founder,storeOwner3,storeId);

    }

    @Before
    public void beforeEach()  {
        client = bridge.enterSystem();
    }

    @After
    public void tearDown() {
        bridge.exitSystem(client);
        bridge.exitSystem(client2);
        bridge.exitSystem(client3);
    }

    @AfterClass
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
        bridge.logout(storeOwner2);
        bridge.logout(storeOwner3);
    }

    @Test
    public void GetStoreStaffListSuccess() {
        List<ServiceUser> staffList = bridge.getStoreStaffList(storeOwner2, storeId);
        Assert.assertNotNull(staffList);
        Assert.assertTrue(staffList.size()==3);

    }

    @Test
    public void GetStoreStaffListFail() {
        UUID randomId = UUID.randomUUID();
        List<ServiceUser> staffList = bridge.getStoreStaffList(randomId, storeId);
        Assert.assertNull(staffList);
    }


}
