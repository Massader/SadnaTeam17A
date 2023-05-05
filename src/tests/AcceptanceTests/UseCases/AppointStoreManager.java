package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreManager;
import ServiceLayer.Response;
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
public class AppointStoreManager extends ProjectTest {

    UUID client;
    UUID client2;
    UUID client3;
    UUID client4;
    UUID founder;
    UUID storeManager;
    UUID user1;
    UUID user2;
    ServiceUser storeManagerUser;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void setUp() {
        bridge.resetService();
        bridge.register("founder", "Pass1");
        bridge.register("toManager", "Pass2");
        bridge.register("user1", "Pass3");
        bridge.register("user2", "Pass4");

        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();

        client2 = bridge.createClient().getValue();
        storeManager = bridge.login(client2, "toManager", "Pass2").getValue().getId();
        storeManagerUser = bridge.getUserInfo(storeManager).getValue();

        client3 = bridge.createClient().getValue();
        user1 = bridge.login(client3, "user1", "Pass3").getValue().getId();

        client4 = bridge.createClient().getValue();
        user2 = bridge.login(client4, "user2", "Pass4").getValue().getId();

        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
    }

    @BeforeEach
    public void beforeEach()  {
        client = bridge.createClient().getValue();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(client);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
        bridge.logout(storeManager);
    }

    @Test
    //  Tests that a store manager can be successfully appointed to a store by a store founder.
    public void AppointStoreManagerSuccess() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(founder, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(storeManager);
        Response<Boolean> appointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(founder, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(storeManager);

        Assert.assertFalse(storeStaff0.isError());
        Assert.assertFalse(userRoles0.isError());
        Assert.assertFalse(appointStoreManager.isError());
        Assert.assertFalse(storeStaff1.isError());
        Assert.assertFalse(userRoles1.isError());
        //assert that the user didn't have a role in the store before appointment
        Assert.assertFalse(storeStaff0.getValue().contains(storeManagerUser));
        Assert.assertTrue(userRoles0.getValue().stream().filter(role -> role.getStoreId() == storeId).toList().isEmpty());
        //assert that appointment was successful
        Assert.assertTrue(appointStoreManager.getValue());
        //assert that the user has a role in the store after appointment
        Assert.assertTrue(storeStaff1.getValue().contains(storeManagerUser));
        Assert.assertFalse(userRoles1.getValue().stream().filter(role -> role.getStoreId() == storeId && role instanceof StoreManager).toList().isEmpty());
    }
    @Test
    //Tests that a store manager cannot be appointed to a store by someone other than the store founder.
    public void AppointStoreManagerNotByFounderFail() {
        Response<Boolean> appointSuccess = bridge.appointStoreManager(founder,storeManager,storeId);

        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(founder, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1);
        Response<Boolean> appointFail = bridge.appointStoreManager(storeManager, user1, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(founder, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1);

        Assert.assertFalse(appointSuccess.isError());
        Assert.assertFalse(storeStaff0.isError());
        Assert.assertFalse(userRoles0.isError());
        //assert failure
        Assert.assertTrue(appointFail.isError());
        Assert.assertFalse(storeStaff1.isError());
        Assert.assertFalse(userRoles1.isError());

        //assert no roles has been added
        Assert.assertEquals(userRoles0.getValue().size(), userRoles1.getValue().size());
        Assert.assertEquals(storeStaff0.getValue().size(), storeStaff1.getValue().size());
    }


    @Test
    //Tests that two store managers cannot simultaneously appoint a third store manager to a store, and that only one appointment is successful.
    public void AppointStoreManagerBy2ManagerParallel(){
        Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId).getValue();
        Assert.assertTrue(AppointStoreManager);
        bridge.register("toBeManager", "Pass2");
        UUID newStoreManager = bridge.login(client3, "toBeManager", "Pass2").getValue().getId();
        List<ServiceUser> staffList = bridge.getStoreStaffList(founder, storeId).getValue();
        int stafSize= staffList.size();

        Thread thread1 = new Thread(()->{
            Boolean AppointStoreManager1 = bridge.appointStoreManager(founder,newStoreManager,storeId).getValue();
        });
        Thread thread2 = new Thread(()->{
            Boolean AppointStoreManager2 = bridge.appointStoreManager(storeManager,newStoreManager,storeId).getValue();
        });

        thread1.start();
        thread2.start();
        try{
            thread1.join();
            thread2.join();
        }catch(Exception ignored){
        }

        Assert.assertEquals(bridge.getStoreStaffList(founder, storeId).getValue().size(), stafSize + 1);
    }

}

