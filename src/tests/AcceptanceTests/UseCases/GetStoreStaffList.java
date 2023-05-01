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
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();

        bridge.register("storeOwner Lior", "Pass1");
        client2 = bridge.createClient().getValue();
        storeOwner2 = bridge.login(client2, "storeOwner Lior", "Pass1").getValue().getId();


        bridge.register("storeOwner Ido ", "Pass1");
        client3 = bridge.createClient().getValue();
        storeOwner3 = bridge.login(client3, "storeOwner Ido ", "Pass1").getValue().getId();

        check = bridge.appointStoreOwner(founder,storeOwner2,storeId).getValue();
        check = check&&  bridge.appointStoreOwner(founder,storeOwner3,storeId).getValue();

    }

    @BeforeEach
    public void beforeEach()  {
        client = bridge.createClient().getValue();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(client);
        bridge.closeClient(client2);
        bridge.closeClient(client3);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
        bridge.logout(storeOwner2);
        bridge.logout(storeOwner3);
    }

    @Test
    //Tests that a store owner can successfully get a list of staff members for their store.
    public void GetStoreStaffListSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();

        bridge.register("storeOwner Lior", "Pass1");
        client2 = bridge.createClient().getValue();
        storeOwner2 = bridge.login(client2, "storeOwner Lior", "Pass1").getValue().getId();


        bridge.register("storeOwner Ido ", "Pass1");
        client3 = bridge.createClient().getValue();
        storeOwner3 = bridge.login(client3, "storeOwner Ido ", "Pass1").getValue().getId();

        check = bridge.appointStoreOwner(founder,storeOwner2,storeId).getValue();
        check = check&&  bridge.appointStoreOwner(founder,storeOwner3,storeId).getValue();
        List<ServiceUser> staffList = bridge.getStoreStaffList(storeOwner2, storeId).getValue();
        Assert.assertNotNull(staffList);
        Assert.assertEquals(3, staffList.size());
    }

    @Test
    public void GetStoreStaffListFail() {
        //Tests that attempting to get a list of staff members for a store with an invalid store owner ID fails.
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();

        bridge.register("storeOwner Lior", "Pass1");
        client2 = bridge.createClient().getValue();
        storeOwner2 = bridge.login(client2, "storeOwner Lior", "Pass1").getValue().getId();


        bridge.register("storeOwner Ido ", "Pass1");
        client3 = bridge.createClient().getValue();
        storeOwner3 = bridge.login(client3, "storeOwner Ido ", "Pass1").getValue().getId();

        check = bridge.appointStoreOwner(founder,storeOwner2,storeId).getValue();
        check = check&&  bridge.appointStoreOwner(founder,storeOwner3,storeId).getValue();
        UUID randomId = UUID.randomUUID();
        List<ServiceUser> staffList = bridge.getStoreStaffList(randomId, storeId).getValue();
        Assert.assertNull(staffList);
    }
}
