package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreManager;
import DomainLayer.Market.Users.Roles.StoreOwner;
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
public class AppointStoreOwner extends ProjectTest {

    UUID storeFounderId;
    UUID storeOwner1Id;
    UUID storeOwner2Id;
    UUID storeManager1Id;
    UUID storeManager2Id;
    UUID user1Id;
    UUID user2Id;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void beforeClass() {
        bridge.register("founder", "Aa1234");
        bridge.register("owner1", "Aa1234");
        bridge.register("owner2", "Aa1234");
        bridge.register("manager1", "Aa1234");
        bridge.register("manager2", "Aa1234");
        bridge.register("user1", "Aa1234");
        bridge.register("user2", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
        storeOwner1Id = bridge.login(bridge.createClient().getValue(), "owner1", "Aa1234").getValue().getId();
        storeOwner2Id = bridge.login(bridge.createClient().getValue(), "owner2", "Aa1234").getValue().getId();
        storeManager1Id = bridge.login(bridge.createClient().getValue(), "manager1", "Aa1234").getValue().getId();
        storeManager2Id = bridge.login(bridge.createClient().getValue(), "manager2", "Aa1234").getValue().getId();
        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "Aa1234").getValue().getId();
        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "Aa1234").getValue().getId();

        store = bridge.createStore(storeFounderId, "store", "test").getValue();
        storeId = store.getStoreId();

        bridge.appointStoreOwner(storeFounderId, storeOwner1Id, storeId);
        bridge.appointStoreManager(storeFounderId, storeManager1Id, storeId);
        bridge.appointStoreManager(storeFounderId, storeManager2Id, storeId);

        bridge.logout(storeFounderId);
        bridge.logout(storeOwner1Id);
        bridge.logout(storeOwner2Id);
        bridge.logout(storeManager1Id);
        bridge.logout(storeManager2Id);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner2", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "manager1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "manager2", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user2", "Aa1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(storeOwner1Id);
        bridge.logout(storeOwner2Id);
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
    //  Tests that a store owner can be successfully appointed to a store by a store founder.
    public void AppointStoreOwnerSuccess() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(storeOwner2Id);
        Response<Boolean> appointStoreOwner = bridge.appointStoreOwner(storeFounderId, storeOwner2Id, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeOwner2Id, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(storeOwner2Id);
        Response<ServiceUser> storeOwner2 = bridge.getUserInfo(storeOwner2Id);

        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed",storeStaff0.isError());
        Assert.assertFalse("bridge.getUserRoles(storeOwner2Id) failed", userRoles0.isError());
        Assert.assertFalse("bridge.appointStoreOwner(storeFounderId, storeOwner2Id, storeId) failed", appointStoreOwner.isError());
        Assert.assertFalse("bridge.getStoreStaffList(storeOwner2Id, storeId) failed ", storeStaff1.isError());
        Assert.assertFalse("bridge.getUserRoles(storeOwner2Id) failed", userRoles1.isError());
        Assert.assertFalse("bridge.getUserInfo(storeOwner2Id) failed", storeOwner2.isError());
        //assert that the user didn't have a role in the store before appointment
        Assert.assertFalse(String.format("store staff list contained %s before the appointment", storeOwner2.getValue().getUsername()), storeStaff0.getValue().contains(storeOwner2.getValue()));
        Assert.assertTrue(String.format("%s had a role in the store before the appointment", storeOwner2.getValue().getUsername()), userRoles0.getValue().stream().filter(role -> role.getStoreId() == storeId).toList().isEmpty());
        //assert that appointment was successful
        Assert.assertTrue("bridge.appointStoreOwner(storeFounderId, storeOwner2Id, storeId) failed", appointStoreOwner.getValue());
        //assert that the user has a role in the store after appointment
        Assert.assertTrue(String.format("%s has not been added to the store staff", storeOwner2.getValue().getUsername()), storeStaff1.getValue().contains(storeOwner2.getValue()));
        Assert.assertFalse(String.format("%s has not been given a role in the store", storeOwner2.getValue().getUsername()), userRoles1.getValue().stream().filter(role -> role.getStoreId() == storeId && role instanceof StoreOwner).toList().isEmpty());
    }
    @Test
    //Tests that a store owner cannot be appointed by someone other than the store founder.
    public void AppointStoreOwnerByOwnerFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<Boolean> appointFail = bridge.appointStoreOwner(storeOwner1Id, user1Id, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1Id);
        Response<ServiceUser> user1 = bridge.getUserInfo(user1Id);

        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed",storeStaff0.isError());
        Assert.assertFalse("bridge.getUserRoles(storeOwner2Id) failed", userRoles0.isError());
        //assert failure
        Assert.assertTrue("bridge.appointStoreOwner(storeOwner1Id, user1Id, storeId) should have failed", appointFail.isError());
        Assert.assertFalse("bridge.getStoreStaffList(storeOwner2Id, storeId) failed ", storeStaff1.isError());
        Assert.assertFalse("bridge.getUserRoles(storeOwner2Id) failed", userRoles1.isError());
        Assert.assertFalse("bridge.getUserInfo(storeOwner2Id) failed", user1.isError());

        //assert no roles has been added
        Assert.assertEquals(String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()), userRoles0.getValue().size(), userRoles1.getValue().size());
        Assert.assertEquals("the store staff list has been changed although the appointment failed", storeStaff0.getValue().size(), storeStaff1.getValue().size());
    }

    @Test
    //Tests that a store Owner cannot be appointed by someone other than the store founder.
    public void AppointStoreOwnerByManagerFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<Boolean> appointFail = bridge.appointStoreOwner(storeManager1Id, user1Id, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1Id);
        Response<ServiceUser> user1 = bridge.getUserInfo(user1Id);

        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed",storeStaff0.isError());
        Assert.assertFalse("bridge.getUserRoles(storeOwner2Id) failed", userRoles0.isError());
        //assert failure
        Assert.assertTrue("bridge.appointStoreOwner(storeManager1Id, user1Id, storeId) should have failed", appointFail.isError());
        Assert.assertFalse("bridge.getStoreStaffList(storeOwner2Id, storeId) failed ", storeStaff1.isError());
        Assert.assertFalse("bridge.getUserRoles(storeOwner2Id) failed", userRoles1.isError());
        Assert.assertFalse("bridge.getUserInfo(storeOwner2Id) failed", user1.isError());
        //assert no roles has been added
        Assert.assertEquals(String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()), userRoles0.getValue().size(), userRoles1.getValue().size());
        Assert.assertEquals("the store staff list has been changed although the appointment failed", storeStaff0.getValue().size(), storeStaff1.getValue().size());
    }

    @Test
    //Tests that a store owner cannot be appointed by someone other than the store founder.
    public void AppointStoreOwnerByUserFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<Boolean> appointFail = bridge.appointStoreOwner(user2Id, user1Id, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1Id);
        Response<ServiceUser> user1 = bridge.getUserInfo(user1Id);

        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed",storeStaff0.isError());
        Assert.assertFalse("bridge.getUserRoles(storeOwner2Id) failed", userRoles0.isError());
        //assert failure
        Assert.assertTrue("bridge.appointStoreOwner(user2Id, user1Id, storeId) should have failed", appointFail.isError());
        Assert.assertFalse("bridge.getStoreStaffList(storeOwner2Id, storeId) failed ", storeStaff1.isError());
        Assert.assertFalse("bridge.getUserRoles(storeOwner2Id) failed", userRoles1.isError());
        Assert.assertFalse("bridge.getUserInfo(storeOwner2Id) failed", user1.isError());
        //assert no roles has been added
        Assert.assertEquals(String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()), userRoles0.getValue().size(), userRoles1.getValue().size());
        Assert.assertEquals("the store staff list has been changed although the appointment failed", storeStaff0.getValue().size(), storeStaff1.getValue().size());
    }

    @Test
    //Tests that a store owner cannot be appointed by the store founder when the founder is logged-out.
    public void AppointStoreOwnerByLoggedOutFounderFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<UUID> logout = bridge.logout(storeFounderId);
        Response<Boolean> appointFail = bridge.appointStoreOwner(storeFounderId, user1Id, storeId);
        Response<ServiceUser> login = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(user1Id);
        Response<ServiceUser> user1 = bridge.getUserInfo(user1Id);

        Assert.assertFalse("bridge.logout(storeFounderId) failed", logout.isError());
        Assert.assertFalse("bridge.getStoreStaffList(storeFounderId, storeId) failed",storeStaff0.isError());
        Assert.assertFalse("bridge.getUserRoles(storeOwner2Id) failed", userRoles0.isError());
        //assert failure
        Assert.assertTrue("bridge.appointStoreOwner(storeFounderId, user1Id, storeId) should have failed", appointFail.isError());
        Assert.assertFalse("bridge.login(bridge.createClient().getValue(), \"founder\", \"Aa1234\") failed", login.isError());
        Assert.assertFalse("bridge.getStoreStaffList(storeOwner2Id, storeId) failed ", storeStaff1.isError());
        Assert.assertFalse("bridge.getUserRoles(storeOwner2Id) failed", userRoles1.isError());
        Assert.assertFalse("bridge.getUserInfo(storeOwner2Id) failed", user1.isError());

        //assert no roles has been added
        Assert.assertEquals(String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()), userRoles0.getValue().size(), userRoles1.getValue().size());
        Assert.assertEquals("the store staff list has been changed although the appointment failed", storeStaff0.getValue().size(), storeStaff1.getValue().size());
    }
}