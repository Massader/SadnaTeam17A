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
    public void setUp() {
        bridge.register("founder", "1234");
        bridge.register("owner1", "1234");
        bridge.register("owner2", "1234");
        bridge.register("manager1", "1234");
        bridge.register("manager2", "1234");
        bridge.register("user1", "1234");
        bridge.register("user2", "1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "1234").getValue().getId();
        storeOwner1Id = bridge.login(bridge.createClient().getValue(), "owner1", "1234").getValue().getId();
        storeOwner2Id = bridge.login(bridge.createClient().getValue(), "owner2", "1234").getValue().getId();
        storeManager1Id = bridge.login(bridge.createClient().getValue(), "manager1", "1234").getValue().getId();
        storeManager2Id = bridge.login(bridge.createClient().getValue(), "manager2", "1234").getValue().getId();
        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "1234").getValue().getId();
        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "1234").getValue().getId();

        store = bridge.createStore(storeFounderId, "test", "test").getValue();
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
    public void beforeEach()  {
        bridge.login(bridge.createClient().getValue(), "founder", "1234");
        bridge.login(bridge.createClient().getValue(), "owner1", "1234");
        bridge.login(bridge.createClient().getValue(), "owner2", "1234");
        bridge.login(bridge.createClient().getValue(), "manager1", "1234");
        bridge.login(bridge.createClient().getValue(), "manager2", "1234");
        bridge.login(bridge.createClient().getValue(), "user1", "1234");
        bridge.login(bridge.createClient().getValue(), "user2", "1234");
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
        bridge.closeStore(storeFounderId, storeId);
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

        Assert.assertFalse(storeStaff0.isError());
        Assert.assertFalse(userRoles0.isError());
        Assert.assertFalse(appointStoreOwner.isError());
        Assert.assertFalse(storeStaff1.isError());
        Assert.assertFalse(userRoles1.isError());
        Assert.assertFalse(storeOwner2.isError());
        //assert that the user didn't have a role in the store before appointment
        Assert.assertFalse(storeStaff0.getValue().contains(storeOwner2.getValue()));
        Assert.assertTrue(userRoles0.getValue().stream().filter(role -> role.getStoreId() == storeId).toList().isEmpty());
        //assert that appointment was successful
        Assert.assertTrue(appointStoreOwner.getValue());
        //assert that the user has a role in the store after appointment
        Assert.assertTrue(storeStaff1.getValue().contains(storeOwner2.getValue()));
        Assert.assertFalse(userRoles1.getValue().stream().filter(role -> role.getStoreId() == storeId && role instanceof StoreOwner).toList().isEmpty());
    }
    @Test
    //Tests that a store owner cannot be appointed by someone other than the store founder.
    public void AppointStoreOwnerByOwnerFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<Boolean> appointFail = bridge.appointStoreOwner(storeOwner1Id, user1Id, storeId);
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
    //Tests that a store Owner cannot be appointed by someone other than the store founder.
    public void AppointStoreOwnerByManagerFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<Boolean> appointFail = bridge.appointStoreOwner(storeManager1Id, user1Id, storeId);
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
    //Tests that a store owner cannot be appointed by someone other than the store founder.
    public void AppointStoreOwnerByUserFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<Boolean> appointFail = bridge.appointStoreOwner(user2Id, user1Id, storeId);
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
    //Tests that a store owner cannot be appointed by the store founder when the founder is logged-out.
    public void AppointStoreOwnerByLoggedOutFounderFail() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(user1Id);
        Response<UUID> logout = bridge.logout(storeFounderId);
        Response<Boolean> appointFail = bridge.appointStoreOwner(storeFounderId, user1Id, storeId);
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
}


