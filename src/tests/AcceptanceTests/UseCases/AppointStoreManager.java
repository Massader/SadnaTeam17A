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

    UUID storeFounderId;
    UUID storeOwnerId;
    UUID storeManager1Id;
    UUID storeManager2Id;
    UUID user1Id;
    UUID user2Id;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void beforeClass() {
        bridge.register("founder", "1234");
        bridge.register("owner", "1234");
        bridge.register("manager1", "1234");
        bridge.register("manager2", "1234");
        bridge.register("user1", "1234");
        bridge.register("user2", "1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "1234").getValue().getId();
        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "1234").getValue().getId();
        storeManager1Id = bridge.login(bridge.createClient().getValue(), "manager1", "1234").getValue().getId();
        storeManager2Id = bridge.login(bridge.createClient().getValue(), "manager2", "1234").getValue().getId();
        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "1234").getValue().getId();
        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "1234").getValue().getId();

        store = bridge.createStore(storeFounderId, "store", "test").getValue();
        storeId = store.getStoreId();

        bridge.appointStoreOwner(storeFounderId, storeOwnerId, storeId);
        bridge.appointStoreManager(storeFounderId, storeManager1Id, storeId);

        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManager1Id);
        bridge.logout(storeManager2Id);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "1234");
        bridge.login(bridge.createClient().getValue(), "owner", "1234");
        bridge.login(bridge.createClient().getValue(), "manager1", "1234");
        bridge.login(bridge.createClient().getValue(), "manager2", "1234");
        bridge.login(bridge.createClient().getValue(), "user1", "1234");
        bridge.login(bridge.createClient().getValue(), "user2", "1234");
    }


    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManager1Id);
        bridge.logout(storeManager2Id);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    //  Tests that a store manager can be successfully appointed to a store by a store founder.
    public void AppointStoreManagerSuccess() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(storeManager2Id);
        Response<Boolean> appointStoreManager = bridge.appointStoreManager(storeFounderId,storeManager2Id,storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeManager2Id, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(storeManager2Id);
        Response<ServiceUser> storeManager2 = bridge.getUserInfo(storeManager2Id);

        Assert.assertFalse(storeStaff0.isError());
        Assert.assertFalse(userRoles0.isError());
        Assert.assertFalse(appointStoreManager.isError());
        Assert.assertFalse(storeStaff1.isError());
        Assert.assertFalse(userRoles1.isError());
        Assert.assertFalse(storeManager2.isError());
        //assert that the user didn't have a role in the store before appointment
        Assert.assertFalse(storeStaff0.getValue().contains(storeManager2.getValue()));
        Assert.assertTrue(userRoles0.getValue().stream().filter(role -> role.getStoreId() == storeId).toList().isEmpty());
        //assert that appointment was successful
        Assert.assertTrue(appointStoreManager.getValue());
        //assert that the user has a role in the store after appointment
        Assert.assertTrue(storeStaff1.getValue().contains(storeManager2.getValue()));
        Assert.assertFalse(userRoles1.getValue().stream().filter(role -> role.getStoreId() == storeId && role instanceof StoreManager).toList().isEmpty());
    }
    @Test
    //Tests that a store manager cannot be appointed by someone other than the store founder.
    public void AppointStoreManagerByOwnerFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<Boolean> appointFail = bridge.appointStoreManager(storeOwnerId, user1Id, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1Id);

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
    //Tests that a store manager cannot be appointed by someone other than the store founder.
    public void AppointStoreManagerByManagerFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<Boolean> appointFail = bridge.appointStoreManager(storeManager1Id, user1Id, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1Id);

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
    //Tests that a store manager cannot be appointed by someone other than the store founder.
    public void AppointStoreManagerByUserFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<Boolean> appointFail = bridge.appointStoreManager(user2Id, user1Id, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1Id);

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
    public void AppointStoreManagerByLoggedOutFounderFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<UUID> logout = bridge.logout(storeFounderId);
        Response<Boolean> appointFail = bridge.appointStoreManager(storeFounderId, user1Id, storeId);
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "founder", "1234");
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1Id);

        Assert.assertFalse(logout.isError());
        Assert.assertFalse(storeStaff0.isError());
        Assert.assertFalse(userRoles0.isError());
        //assert failure
        Assert.assertTrue(appointFail.isError());
        Assert.assertFalse(login.isError());
        Assert.assertFalse(storeStaff1.isError());
        Assert.assertFalse(userRoles1.isError());

        //assert no roles has been added
        Assert.assertEquals(userRoles0.getValue().size(), userRoles1.getValue().size());
        Assert.assertEquals(storeStaff0.getValue().size(), storeStaff1.getValue().size());
    }

    /*
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
     */
}

