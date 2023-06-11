package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import DomainLayer.Market.Users.Roles.StorePermissions;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetStoreStaffList extends ProjectTest {

    UUID storeFounderId;
    UUID storeOwnerId;
    UUID storeManagerWithPermissionId;
    UUID storeManagerNoPermissionId;
    UUID user1Id;
    UUID user2Id;
    ServiceStore store;
    UUID storeId;
    UUID item1Id;
    UUID item2Id;
    UUID item3Id;
    UUID item4Id;

    @BeforeAll
    public void beforeClass() {
        bridge.register("founder", "Aa1234");
        bridge.register("owner", "Aa1234");
        bridge.register("managerWithPermission", "Aa1234");
        bridge.register("managerNoPermission", "Aa1234");
        bridge.register("user1", "Aa1234");
        bridge.register("user2", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "Aa1234").getValue().getId();
        storeManagerWithPermissionId = bridge.login(bridge.createClient().getValue(), "managerWithPermission", "Aa1234").getValue().getId();
        storeManagerNoPermissionId = bridge.login(bridge.createClient().getValue(), "managerNoPermission", "Aa1234").getValue().getId();
        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "Aa1234").getValue().getId();
        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "Aa1234").getValue().getId();

        store = bridge.createStore(storeFounderId, "store", "test").getValue();
        storeId = store.getStoreId();

        bridge.appointStoreOwner(storeFounderId, storeOwnerId, storeId);
        bridge.appointStoreManager(storeFounderId, storeManagerWithPermissionId, storeId);
        bridge.appointStoreManager(storeFounderId, storeManagerNoPermissionId, storeId);

        List<Integer> permissions = new ArrayList<Integer>();
        permissions.add(6); // STORE_MANAGEMENT_INFORMATION = 1
        bridge.setStoreManagerPermissions(storeFounderId, storeManagerWithPermissionId, storeId, permissions);

        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManagerWithPermissionId);
        bridge.logout(storeManagerNoPermissionId);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "managerWithPermission", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "managerNoPermission", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user2", "Aa1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(storeManagerWithPermissionId);
        bridge.logout(storeManagerNoPermissionId);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }


    @Test
    public void getStoreStaffListFounderSuccess() {
        Response<List<ServiceUser>> staff = bridge.getStoreStaffList(storeFounderId, storeId);

        assertFalse(staff.isError(), String.format("bridge.getStoreStaffList(storeFounderId, storeId) => %s", staff.getValue()));
        assertNotNull(staff, "bridge.getStoreStaffList(storeFounderId, storeId) failed");
        assertEquals(4, staff.getValue().size(), "store staff list does not contain 4 members");
        assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeFounderId)), "store staff list does not contain founder");
        assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeOwnerId)), "store staff list does not contain owner");
        assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeManagerWithPermissionId)), "store staff list does not contain manager with permissions");
        assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeManagerNoPermissionId)), "store staff list does not contain manager without permissions");
    }

    @Test
    public void getStoreStaffListOwnerSuccess() {
        Response<List<ServiceUser>> staff = bridge.getStoreStaffList(storeOwnerId, storeId);

        assertFalse(staff.isError(), String.format("bridge.getStoreStaffList(storeOwnerId, storeId) => %s", staff.getValue()));
        assertNotNull(staff, "bridge.getStoreStaffList(storeOwnerId, storeId) failed");
        assertEquals(4, staff.getValue().size(), "store staff list does not contain 4 members");
        assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeFounderId)), "store staff list does not contain founder");
        assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeOwnerId)), "store staff list does not contain owner");
        assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeManagerWithPermissionId)), "store staff list does not contain manager with permissions");
        assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeManagerNoPermissionId)), "store staff list does not contain manager without permissions");
    }

    @Test
    public void getStoreStaffListManagerWithPermissionSuccess() {
        Response<List<ServiceUser>> staff = bridge.getStoreStaffList(storeManagerWithPermissionId, storeId);

        assertFalse(staff.isError(), String.format("bridge.getStoreStaffList(storeManagerWithPermissionId, storeId) => %s", staff.getMessage()));
        assertNotNull(staff, "bridge.getStoreStaffList(storeManagerWithPermissionId, storeId) failed");
        assertEquals(4, staff.getValue().size(), "store staff list does not contain 4 members");
        assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeFounderId)), "store staff list does not contain founder");
        assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeOwnerId)), "store staff list does not contain owner");
        assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeManagerWithPermissionId)), "store staff list does not contain manager with permissions");
        assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeManagerNoPermissionId)), "store staff list does not contain manager without permissions");
    }

    @Test
    public void getStoreStaffListManagerNoPermissionFail() {
        Response<List<ServiceUser>> staff = bridge.getStoreStaffList(storeManagerNoPermissionId, storeId);

        assertTrue(staff.isError(), "bridge.getStoreStaffList(storeManagerNoPermissionId, storeId) should have failed");
        assertEquals("User doesn't have permission.", staff.getMessage());
    }

    @Test
    public void getStoreStaffListUserFail() {
        Response<List<ServiceUser>> staff = bridge.getStoreStaffList(user1Id, storeId);

        assertTrue(staff.isError(), "bridge.getStoreStaffList(storeManagerNoPermissionId, storeId) should have failed");
        assertEquals("User doesn't have permission.", staff.getMessage(), "bridge.getStoreStaffList(user1Id, storeId) failed");
    }

    @Test
    public void getStoreStaffListStoreDoesntExistFail() {
        Response<UUID> admin = bridge.getAdminCredentials();
        assertFalse(admin.isError(), String.format("bridge.getAdminCredentials() => %s", admin.getMessage()));

        Response<Boolean> shutdown = bridge.shutdownStore(admin.getValue(), storeId);
        assertFalse(shutdown.isError(), String.format("bridge.shutdownStore(admin.getValue(), storeId) => %s", shutdown.getMessage()));
        assertTrue(shutdown.getValue(), "bridge.shutdownStore(admin.getValue(), storeId) failed");

        Response<List<ServiceUser>> staff = bridge.getStoreStaffList(storeFounderId, storeId);

        assertTrue(staff.isError(), "bridge.getStoreStaffList(user1Id, storeId) should have failed");
        assertEquals("Store does not exist", staff.getMessage(), "bridge.getStoreStaffList(user1Id, storeId) failed");
    }
}