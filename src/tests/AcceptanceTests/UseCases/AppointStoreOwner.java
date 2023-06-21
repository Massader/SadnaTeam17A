package AcceptanceTests.UseCases;

import AcceptanceTests.ProjectTest;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StoreOwner;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.ServiceStore;
import ServiceLayer.ServiceObjects.ServiceUser;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import java.util.List;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppointStoreOwner extends ProjectTest {

    UUID storeFounderId;
    UUID storeOwner1Id;
    UUID storeOwner2Id;
    UUID storeOwner3Id;
    UUID storeManager1Id;
    UUID storeManager2Id;
    UUID user1Id;
    UUID user2Id;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void beforeClass() {

    }

    @BeforeEach
    public void setUp()  {
        bridge.register("founder", "Aa1234");
        bridge.register("owner1", "Aa1234");
        bridge.register("owner2", "Aa1234");
        bridge.register("owner3", "Aa1234");
        bridge.register("manager1", "Aa1234");
        bridge.register("manager2", "Aa1234");
        bridge.register("user1", "Aa1234");
        bridge.register("user2", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
        storeOwner1Id = bridge.login(bridge.createClient().getValue(), "owner1", "Aa1234").getValue().getId();
        storeOwner2Id = bridge.login(bridge.createClient().getValue(), "owner2", "Aa1234").getValue().getId();
        storeOwner3Id = bridge.login(bridge.createClient().getValue(), "owner3", "Aa1234").getValue().getId();
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
        bridge.logout(storeOwner3Id);
        bridge.logout(storeManager1Id);
        bridge.logout(storeManager2Id);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner2", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner3", "Aa1234");
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
        bridge.logout(storeOwner3Id);
        bridge.logout(storeManager1Id);
        bridge.logout(storeManager2Id);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
        deleteDB();
        bridge.resetService();
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    //  Tests that a store owner can be successfully appointed to a store by a store founder.
    public void AppointStoreOwnerByFounderSuccess() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(storeOwner2Id);
        Response<Boolean> appointStoreOwner = bridge.appointStoreOwner(storeFounderId, storeOwner2Id, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeOwner2Id, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(storeOwner2Id);
        Response<ServiceUser> storeOwner2 = bridge.getUserInfo(storeOwner2Id);

        assertFalse(storeStaff0.isError(), String.format("bridge.getStoreStaffList(storeFounderId, storeId) => %s", storeStaff0.getMessage()));
        assertFalse(userRoles0.isError(), String.format("bridge.getUserRoles(storeOwner2Id) => %s", userRoles0.getMessage()));
        assertFalse(appointStoreOwner.isError(), String.format("bridge.appointStoreOwner(storeFounderId, storeOwner2Id, storeId) => %s", appointStoreOwner.getMessage()));
        assertFalse(storeStaff1.isError(), String.format("bridge.getStoreStaffList(storeOwner2Id, storeId) => %s", storeStaff1.getMessage()));
        assertFalse(userRoles1.isError(), String.format("bridge.getUserRoles(storeOwner2Id) => %s", userRoles1.getMessage()));
        assertFalse(storeOwner2.isError(), String.format("bridge.getUserInfo(storeOwner2Id) => %s", storeOwner2.getMessage()));
        //assert that the user didn't have a role in the store before appointment
        assertFalse(storeStaff0.getValue().contains(storeOwner2.getValue()), String.format("store staff list contained %s before the appointment", storeOwner2.getValue().getUsername()));
        assertTrue(userRoles0.getValue().stream().filter(role -> role.getStore().getStoreId() == storeId).toList().isEmpty(), String.format("%s had a role in the store before the appointment", storeOwner2.getValue().getUsername()));
        //assert that appointment was successful
        assertTrue(appointStoreOwner.getValue(), "bridge.appointStoreOwner(storeFounderId, storeOwner2Id, storeId) failed");
        //assert that the user has a role in the store after appointment
        assertTrue(storeStaff1.getValue().stream().anyMatch(staff -> staff.getId().equals(storeOwner2.getValue().getId())), String.format("%s has not been added to the store staff", storeOwner2.getValue().getUsername()));
        assertFalse(userRoles1.getValue().stream().filter(role -> role.getStore().getStoreId() == storeId && role instanceof StoreOwner).toList().isEmpty(), String.format("%s has not been given a role in the store", storeOwner2.getValue().getUsername()));
    }
    @Test
    //  Tests that a store owner can be successfully appointed to a store by a store owner.
    public void AppointStoreOwnerByOwnerSuccess() {
        Response<List<ServiceUser>> storeStaff0 = bridge.getStoreStaffList(storeFounderId, storeId);
        Response<List<Role>> userRoles0 = bridge.getUserRoles(storeOwner3Id);
        Response<Boolean> appointStoreOwner = bridge.appointStoreOwner(storeOwner1Id, storeOwner3Id, storeId);
        Response<List<ServiceUser>> storeStaff1 = bridge.getStoreStaffList(storeOwner3Id, storeId);
        Response<List<Role>> userRoles1 = bridge.getUserRoles(storeOwner3Id);
        Response<ServiceUser> storeOwner3 = bridge.getUserInfo(storeOwner3Id);

        assertFalse(storeStaff0.isError(), String.format("bridge.getStoreStaffList(storeFounderId, storeId) => %s", storeStaff0.getMessage()));
        assertFalse(userRoles0.isError(), String.format("bridge.getUserRoles(storeOwner3Id) => %s", userRoles0.getMessage()));
        assertFalse(appointStoreOwner.isError(), String.format("bridge.appointStoreOwner(storeOwner1Id, storeOwner3Id, storeId) => %s", appointStoreOwner.getMessage()));
        assertFalse(storeStaff1.isError(), String.format("bridge.getStoreStaffList(storeOwner3Id, storeId) => %s", storeStaff1.getMessage()));
        assertFalse(userRoles1.isError(), String.format("bridge.getUserRoles(storeOwner3Id) => %s", userRoles1.getMessage()));
        assertFalse(storeOwner3.isError(), String.format("bridge.getUserInfo(storeOwner3Id) => %s", storeOwner3.getMessage()));
        //assert that the user didn't have a role in the store before appointment
        assertFalse(storeStaff0.getValue().contains(storeOwner3.getValue()), String.format("store staff list contained %s before the appointment", storeOwner3.getValue().getUsername()));
        assertTrue(userRoles0.getValue().stream().filter(role -> role.getStore().getStoreId() == storeId).toList().isEmpty(), String.format("%s had a role in the store before the appointment", storeOwner3.getValue().getUsername()));
        //assert that appointment was successful
        assertTrue(appointStoreOwner.getValue(), "bridge.appointStoreOwner(storeFounderId, storeOwner2Id, storeId) failed");
        //assert that the user has a role in the store after appointment
        assertTrue(storeStaff1.getValue().stream().anyMatch(staff -> staff.getId().equals(storeOwner3.getValue().getId())), String.format("%s has not been added to the store staff", storeOwner3.getValue().getUsername()));
        assertFalse(userRoles1.getValue().stream().filter(role -> role.getStore().getStoreId() == storeId && role instanceof StoreOwner).toList().isEmpty(), String.format("%s has not been given a role in the store", storeOwner3.getValue().getUsername()));
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

        assertFalse(storeStaff0.isError(), String.format("bridge.getStoreStaffList(storeFounderId, storeId) => %s", storeStaff0.getMessage()));
        assertFalse(userRoles0.isError(), String.format("bridge.getUserRoles(storeOwner2Id) => %s", userRoles0.getMessage()));
        //assert failure
        assertTrue(appointFail.isError(), "bridge.appointStoreOwner(storeManager1Id, user1Id, storeId) should have failed");
        assertFalse(storeStaff1.isError(), String.format("bridge.getStoreStaffList(storeOwner2Id, storeId) => %s", storeStaff1.getMessage()));
        assertFalse(userRoles1.isError(), String.format("bridge.getUserRoles(storeOwner2Id) => %s", userRoles1.getMessage()));
        assertFalse(user1.isError(), String.format("bridge.getUserInfo(storeOwner2Id) => %s", user1.getMessage()));
        //assert no roles has been added
        assertEquals(userRoles0.getValue().size(), userRoles1.getValue().size(), String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()));
        assertEquals(storeStaff0.getValue().size(), storeStaff1.getValue().size(), "the store staff list has been changed although the appointment failed");
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

        assertFalse(storeStaff0.isError(), String.format("bridge.getStoreStaffList(storeFounderId, storeId) => %s", storeStaff0.getMessage()));
        assertFalse(userRoles0.isError(), String.format("bridge.getUserRoles(storeOwner2Id) => %s", userRoles0.getMessage()));
        //assert failure
        assertTrue(appointFail.isError(), "bridge.appointStoreOwner(user2Id, user1Id, storeId) should have failed");
        assertFalse(storeStaff1.isError(), String.format("bridge.getStoreStaffList(storeOwner2Id, storeId) => %s", storeStaff1.getMessage()));
        assertFalse(userRoles1.isError(), String.format("bridge.getUserRoles(storeOwner2Id) => %s", userRoles1.getMessage()));
        assertFalse(user1.isError(), String.format("bridge.getUserInfo(storeOwner2Id) => %s", user1.getMessage()));
        //assert no roles has been added
        assertEquals(userRoles0.getValue().size(), userRoles1.getValue().size(), String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()));
        assertEquals(storeStaff0.getValue().size(), storeStaff1.getValue().size(), "the store staff list has been changed although the appointment failed");
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

        assertFalse(logout.isError(), String.format("bridge.logout(storeFounderId) => %s", logout.getMessage()));
        assertFalse(storeStaff0.isError(), String.format("bridge.getStoreStaffList(storeFounderId, storeId) => %s", storeStaff0.getMessage()));
        assertFalse(userRoles0.isError(), String.format("bridge.getUserRoles(storeOwner2Id) => %s", userRoles0.getMessage()));
        //assert failure
        assertTrue(appointFail.isError(), "bridge.appointStoreOwner(storeFounderId, user1Id, storeId) should have failed");
        assertFalse(login.isError(), String.format("bridge.login(bridge.createClient().getValue(), \"founder\", \"Aa1234\") => %s", login.getMessage()));
        assertFalse(storeStaff1.isError(), String.format("bridge.getStoreStaffList(storeOwner2Id, storeId) => %s", storeStaff1.getMessage()));
        assertFalse(userRoles1.isError(), String.format("bridge.getUserRoles(storeOwner2Id) => %s", userRoles1.getMessage()));
        assertFalse(user1.isError(), String.format("bridge.getUserInfo(storeOwner2Id) => %s", user1.getMessage()));

        //assert no roles has been added
        assertEquals(userRoles0.getValue().size(), userRoles1.getValue().size(), String.format("%s roles list has been changed although the appointment failed", user1.getValue().getUsername()));
        assertEquals(storeStaff0.getValue().size(), storeStaff1.getValue().size(), "the store staff list has been changed although the appointment failed");
    }
}