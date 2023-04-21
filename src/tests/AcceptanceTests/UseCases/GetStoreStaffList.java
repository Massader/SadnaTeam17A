package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();

        bridge.register("storeOwner Lior", "Pass1");
        client2 = bridge.enterSystem();
        storeOwner2 = bridge.login(client2, "storeOwner Lior", "Pass1");


        bridge.register("storeOwner Ido ", "Pass1");
        client3 = bridge.enterSystem();
        storeOwner3 = bridge.login(client3, "storeOwner Ido ", "Pass1");

        check = bridge.appointStoreOwner(founder,storeOwner2,storeId);
        check = check&&  bridge.appointStoreOwner(founder,storeOwner3,storeId);

    }

    @BeforeEach
    public void beforeEach()  {
        client = bridge.enterSystem();
    }

    @AfterEach
    public void tearDown() {
        bridge.exitSystem(client);
        bridge.exitSystem(client2);
        bridge.exitSystem(client3);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
        bridge.logout(storeOwner2);
        bridge.logout(storeOwner3);
    }

    @Test
    public void GetStoreStaffListSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();

        bridge.register("storeOwner Lior", "Pass1");
        client2 = bridge.enterSystem();
        storeOwner2 = bridge.login(client2, "storeOwner Lior", "Pass1");


        bridge.register("storeOwner Ido ", "Pass1");
        client3 = bridge.enterSystem();
        storeOwner3 = bridge.login(client3, "storeOwner Ido ", "Pass1");

        check = bridge.appointStoreOwner(founder,storeOwner2,storeId);
        check = check&&  bridge.appointStoreOwner(founder,storeOwner3,storeId);
        List<ServiceUser> staffList = bridge.getStoreStaffList(storeOwner2, storeId);
        Assert.assertNotNull(staffList);
        Assert.assertEquals(3, staffList.size());
    }

    @Test
    public void GetStoreStaffListFail() {
        bridge.register("founder", "Pass1");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();

        bridge.register("storeOwner Lior", "Pass1");
        client2 = bridge.enterSystem();
        storeOwner2 = bridge.login(client2, "storeOwner Lior", "Pass1");


        bridge.register("storeOwner Ido ", "Pass1");
        client3 = bridge.enterSystem();
        storeOwner3 = bridge.login(client3, "storeOwner Ido ", "Pass1");

        check = bridge.appointStoreOwner(founder,storeOwner2,storeId);
        check = check&&  bridge.appointStoreOwner(founder,storeOwner3,storeId);
        UUID randomId = UUID.randomUUID();
        List<ServiceUser> staffList = bridge.getStoreStaffList(randomId, storeId);
        Assert.assertNull(staffList);
    }
}
