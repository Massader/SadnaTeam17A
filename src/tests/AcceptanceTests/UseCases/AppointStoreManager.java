package AcceptanceTests.UseCases;

import AcceptanceTests.ProjectTest;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreManager;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.ServiceStore;
import ServiceLayer.ServiceObjects.ServiceUser;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

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

        assertFalse(storeStaff0.isError(),"bridge.getStoreStaffList(storeFounderId, storeId) failed");
        assertFalse(userRoles0.isError(), "bridge.getUserRoles(storeManager2Id) failed");
        assertFalse(appointStoreManager.isError(), "bridge.appointStoreManager(storeFounderId,storeManager2Id,storeId) failed");
        assertFalse(storeStaff1.isError(), "bridge.getStoreStaffList(storeManager2Id, storeId) failed");
        assertFalse(userRoles1.isError(), "bridge.getUserRoles(storeManager2Id) failed");
        assertFalse(storeManager2.isError(), "bridge.getUserInfo(storeManager2Id) failed");
        //assert that the user didn't have a role in the store before appointment
        assertFalse(storeStaff0.getValue().contains(storeManager2.getValue()), String.format("store staff list contained %s before the appointment", storeManager2.getValue().getUsername()));
        assertTrue(userRoles0.getValue().stream().filter(role -> role.getStoreId() == storeId).toList().isEmpty(), String.format("%s had a role in the store before the appointment", storeManager2.getValue().getUsername()));
        //assert that appointment was successful
        assertTrue(appointStoreManager.getValue(), "bridge.appointStoreManager(storeFounderId,storeManager2Id,storeId) failed");
        //assert that the user has a role in the store after appointment
        assertTrue(storeStaff1.getValue().contains(storeManager2.getValue()),
                String.format("%s has not been added to the store staff", storeManager2.getValue().getUsername()));
        assertFalse(userRoles1.getValue().stream().filter(role -> role.getStoreId() == storeId
                        && role instanceof StoreManager).toList().isEmpty(),
                String.format("%s has not been given a role in the store", storeManager2.getValue().getUsername()));
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

        assertFalse(storeStaff0.isError(), "bridge.getStoreStaffList(storeFounderId, storeId) failed");
        assertFalse(userRoles0.isError(), "bridge.getUserRoles(user1Id) failed");
        //assert failure
        assertTrue(appointFail.isError(), "bridge.appointStoreManager(storeOwnerId, user1Id, storeId) should have failed");
        assertFalse(storeStaff1.isError(), "bridge.getStoreStaffList(storeFounderId, storeId) failed");
        assertFalse(userRoles1.isError(), "bridge.getUserRoles(user1Id) failed");
        assertFalse(user1.isError(), "bridge.getUserInfo(user1Id) failed");

        //assert no roles has been added
        assertEquals(userRoles0.getValue().size(), userRoles1.getValue().size(),
                String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()));
        assertEquals(storeStaff0.getValue().size(), storeStaff1.getValue().size(),
                "the store staff list has been changed although the appointment failed");
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

        assertFalse(storeStaff0.isError(), "bridge.getStoreStaffList(storeFounderId, storeId) failed");
        assertFalse(userRoles0.isError(), "bridge.getUserRoles(user1Id) failed");
        //assert failure
        assertTrue(appointFail.isError(), "bridge.appointStoreManager(storeManager1Id, user1Id, storeId) should have failed");
        assertFalse(storeStaff1.isError(), "bridge.getStoreStaffList(storeFounderId, storeId) failed");
        assertFalse(userRoles1.isError(), "bridge.getUserRoles(user1Id) failed");
        assertFalse(user1.isError(), "bridge.getUserInfo(user1Id) failed");

        //assert no roles has been added
        assertEquals(userRoles0.getValue().size(), userRoles1.getValue().size(),
                String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()));
        assertEquals(storeStaff0.getValue().size(), storeStaff1.getValue().size(),
                "the store staff list has been changed although the appointment failed");
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

        assertFalse(storeStaff0.isError(), "bridge.getStoreStaffList(storeFounderId, storeId) failed");
        assertFalse(userRoles0.isError(), "bridge.getUserRoles(user1Id) failed");
        //assert failure
        assertTrue(appointFail.isError(), "bridge.appointStoreManager(user2Id, user1Id, storeId) should have failed");
        assertFalse(storeStaff1.isError(), "bridge.getStoreStaffList(storeFounderId, storeId) failed");
        assertFalse(userRoles1.isError(), "bridge.getUserRoles(user1Id) failed");
        assertFalse(user1.isError(), "bridge.getUserInfo(user1Id) failed");

        //assert no roles has been added
        assertEquals(userRoles0.getValue().size(), userRoles1.getValue().size(),
                String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()));
        assertEquals(storeStaff0.getValue().size(), storeStaff1.getValue().size(),
                "the store staff list has been changed although the appointment failed");
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

        assertFalse(logout.isError(), "bridge.logout(storeFounderId) failed");
        assertFalse(storeStaff0.isError(), "bridge.getStoreStaffList(storeFounderId, storeId) failed");
        assertFalse(userRoles0.isError(), "bridge.getUserRoles(user1Id) failed");
        //assert failure
        assertTrue(appointFail.isError(), "bridge.appointStoreManager(storeFounderId, user1Id, storeId) should have failed");
        assertFalse(login.isError(), "bridge.login(bridge.createClient().getValue(), \"founder\", \"Aa1234\") failed");
        assertFalse(userRoles1.isError(), "bridge.getUserRoles(user1Id) failed");
        assertFalse(user1.isError(), "bridge.getUserInfo(user1Id) failed");
        assertFalse(user1.isError(), "bridge.getUserInfo(user1Id) failed");

        //assert no roles has been added
        assertEquals(userRoles0.getValue().size(), userRoles1.getValue().size(), String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()));
        assertEquals(storeStaff0.getValue().size(), storeStaff1.getValue().size(), "the store staff list has been changed although the appointment failed");
    }
}