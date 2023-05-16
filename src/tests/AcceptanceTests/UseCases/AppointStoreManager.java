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
        bridge.register("founder", "Aa1234");
        bridge.register("owner", "Aa1234");
        bridge.register("manager1", "Aa1234");
        bridge.register("manager2", "Aa1234");
        bridge.register("user1", "Aa1234");
        bridge.register("user2", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "Aa1234").getValue().getId();
        storeManager1Id = bridge.login(bridge.createClient().getValue(), "manager1", "Aa1234").getValue().getId();
        storeManager2Id = bridge.login(bridge.createClient().getValue(), "manager2", "Aa1234").getValue().getId();
        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "Aa1234").getValue().getId();
        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "Aa1234").getValue().getId();

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
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "manager1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "manager2", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user2", "Aa1234");
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

        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed",storeStaff0.isError());
        Assert.assertFalse("bridge.getUserRoles(storeManager2Id) failed", userRoles0.isError());
        Assert.assertFalse("bridge.appointStoreManager(storeFounderId,storeManager2Id,storeId) failed", appointStoreManager.isError());
        Assert.assertFalse("bridge.getStoreStaffList(storeManager2Id, storeId) failed", storeStaff1.isError());
        Assert.assertFalse("bridge.getUserRoles(storeManager2Id) failed", userRoles1.isError());
        Assert.assertFalse("bridge.getUserInfo(storeManager2Id) failed", storeManager2.isError());
        //assert that the user didn't have a role in the store before appointment
        Assert.assertFalse(String.format("store staff list contained %s before the appointment", storeManager2.getValue().getUsername()), storeStaff0.getValue().contains(storeManager2.getValue()));
        Assert.assertTrue(String.format("%s had a role in the store before the appointment", storeManager2.getValue().getUsername()), userRoles0.getValue().stream().filter(role -> role.getStoreId() == storeId).toList().isEmpty());
        //assert that appointment was successful
        Assert.assertTrue("bridge.appointStoreManager(storeFounderId,storeManager2Id,storeId) failed", appointStoreManager.getValue());
        //assert that the user has a role in the store after appointment
        Assert.assertTrue(String.format("%s has not been added to the store staff", storeManager2.getValue().getUsername()), storeStaff1.getValue().contains(storeManager2.getValue()));
        Assert.assertFalse(String.format("%s has not been given a role in the store", storeManager2.getValue().getUsername()), userRoles1.getValue().stream().filter(role -> role.getStoreId() == storeId && role instanceof StoreManager).toList().isEmpty());
    }
    @Test
    //Tests that a store manager cannot be appointed by someone other than the store founder.
    public void AppointStoreManagerByOwnerFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<Boolean> appointFail = bridge.appointStoreManager(storeOwnerId, user1Id, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1Id);
        Response<ServiceUser> user1 = bridge.getUserInfo(user1Id);

        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed", storeStaff0.isError());
        Assert.assertFalse("bridge.getUserRoles(user1Id) failed", userRoles0.isError());
        //assert failure
        Assert.assertTrue("bridge.appointStoreManager(storeOwnerId, user1Id, storeId) should have failed", appointFail.isError());
        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed", storeStaff1.isError());
        Assert.assertFalse("bridge.getUserRoles(user1Id) failed", userRoles1.isError());
        Assert.assertFalse("bridge.getUserInfo(user1Id) failed", user1.isError());

        //assert no roles has been added
        Assert.assertEquals(String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()), userRoles0.getValue().size(), userRoles1.getValue().size());
        Assert.assertEquals("the store staff list has been changed although the appointment failed", storeStaff0.getValue().size(), storeStaff1.getValue().size());
    }

    @Test
    //Tests that a store manager cannot be appointed by someone other than the store founder.
    public void AppointStoreManagerByManagerFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<Boolean> appointFail = bridge.appointStoreManager(storeManager1Id, user1Id, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1Id);
        Response<ServiceUser> user1 = bridge.getUserInfo(user1Id);

        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed", storeStaff0.isError());
        Assert.assertFalse("bridge.getUserRoles(user1Id) failed", userRoles0.isError());
        //assert failure
        Assert.assertTrue("bridge.appointStoreManager(storeManager1Id, user1Id, storeId) should have failed", appointFail.isError());
        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed", storeStaff1.isError());
        Assert.assertFalse("bridge.getUserRoles(user1Id) failed", userRoles1.isError());
        Assert.assertFalse("bridge.getUserInfo(user1Id) failed", user1.isError());

        //assert no roles has been added
        Assert.assertEquals(String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()), userRoles0.getValue().size(), userRoles1.getValue().size());
        Assert.assertEquals("the store staff list has been changed although the appointment failed", storeStaff0.getValue().size(), storeStaff1.getValue().size());
    }

    @Test
    //Tests that a store manager cannot be appointed by someone other than the store founder.
    public void AppointStoreManagerByUserFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<Boolean> appointFail = bridge.appointStoreManager(user2Id, user1Id, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1Id);
        Response<ServiceUser> user1 = bridge.getUserInfo(user1Id);

        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed", storeStaff0.isError());
        Assert.assertFalse("bridge.getUserRoles(user1Id) failed", userRoles0.isError());
        //assert failure
        Assert.assertTrue("bridge.appointStoreManager(user2Id, user1Id, storeId) should have failed", appointFail.isError());
        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed", storeStaff1.isError());
        Assert.assertFalse("bridge.getUserRoles(user1Id) failed", userRoles1.isError());
        Assert.assertFalse("bridge.getUserInfo(user1Id) failed", user1.isError());

        //assert no roles has been added
        Assert.assertEquals(String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()), userRoles0.getValue().size(), userRoles1.getValue().size());
        Assert.assertEquals("the store staff list has been changed although the appointment failed", storeStaff0.getValue().size(), storeStaff1.getValue().size());
    }

    @Test
    public void AppointStoreManagerByLoggedOutFounderFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<UUID> logout = bridge.logout(storeFounderId);
        Response<Boolean> appointFail = bridge.appointStoreManager(storeFounderId, user1Id, storeId);
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1Id);
        Response<ServiceUser> user1 = bridge.getUserInfo(user1Id);

        Assert.assertFalse("bridge.logout(storeFounderId) failed", logout.isError());
        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed", storeStaff0.isError());
        Assert.assertFalse("bridge.getUserRoles(user1Id) failed", userRoles0.isError());
        //assert failure
        Assert.assertTrue("bridge.appointStoreManager(storeFounderId, user1Id, storeId) should have failed", appointFail.isError());
        Assert.assertFalse("bridge.login(bridge.createClient().getValue(), \"founder\", \"Aa1234\") failed", login.isError());
        Assert.assertFalse("bridge.getUserRoles(user1Id) failed", userRoles1.isError());
        Assert.assertFalse("bridge.getUserInfo(user1Id) failed", user1.isError());
        Assert.assertFalse("bridge.getUserInfo(user1Id) failed", user1.isError());

        //assert no roles has been added
        Assert.assertEquals(String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()), userRoles0.getValue().size(), userRoles1.getValue().size());
        Assert.assertEquals("the store staff list has been changed although the appointment failed", storeStaff0.getValue().size(), storeStaff1.getValue().size());
    }
}