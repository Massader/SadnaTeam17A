package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import DomainLayer.Market.Users.Roles.StorePermissions;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.*;

import java.util.ArrayList;
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
        bridge.register("founder", "1234");
        bridge.register("owner", "1234");
        bridge.register("managerWithPermission", "1234");
        bridge.register("managerNoPermission", "1234");
        bridge.register("user1", "1234");
        bridge.register("user2", "1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "1234").getValue().getId();
        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "1234").getValue().getId();
        storeManagerWithPermissionId = bridge.login(bridge.createClient().getValue(), "managerWithPermission", "1234").getValue().getId();
        storeManagerNoPermissionId = bridge.login(bridge.createClient().getValue(), "managerNoPermission", "1234").getValue().getId();
        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "1234").getValue().getId();
        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "1234").getValue().getId();

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
        bridge.login(bridge.createClient().getValue(), "founder", "1234");
        bridge.login(bridge.createClient().getValue(), "owner", "1234");
        bridge.login(bridge.createClient().getValue(), "managerWithPermission", "1234");
        bridge.login(bridge.createClient().getValue(), "managerNoPermission", "1234");
        bridge.login(bridge.createClient().getValue(), "user1", "1234");
        bridge.login(bridge.createClient().getValue(), "user2", "1234");
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

        Assert.assertFalse(staff.isError());
        Assert.assertNotNull(staff);
        Assert.assertEquals(4, staff.getValue().size());
        Assert.assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeFounderId)));
        Assert.assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeOwnerId)));
        Assert.assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeManagerWithPermissionId)));
        Assert.assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeManagerNoPermissionId)));
    }

    @Test
    public void getStoreStaffListOwnerSuccess() {
        Response<List<ServiceUser>> staff = bridge.getStoreStaffList(storeOwnerId, storeId);

        Assert.assertFalse(staff.isError());
        Assert.assertNotNull(staff);
        Assert.assertEquals(4, staff.getValue().size());
        Assert.assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeFounderId)));
        Assert.assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeOwnerId)));
        Assert.assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeManagerWithPermissionId)));
        Assert.assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeManagerNoPermissionId)));
    }

    @Test
    public void getStoreStaffListManagerWithPermissionSuccess() {
        Response<List<ServiceUser>> staff = bridge.getStoreStaffList(storeManagerWithPermissionId, storeId);

        Assert.assertFalse(staff.isError());
        Assert.assertNotNull(staff);
        Assert.assertEquals(4, staff.getValue().size());
        Assert.assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeFounderId)));
        Assert.assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeOwnerId)));
        Assert.assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeManagerWithPermissionId)));
        Assert.assertTrue(staff.getValue().stream().anyMatch(user -> user.getId().equals(storeManagerNoPermissionId)));
    }

    @Test
    public void getStoreStaffListManagerNoPermissionFail() {
        Response<List<ServiceUser>> staff = bridge.getStoreStaffList(storeManagerNoPermissionId, storeId);

        Assert.assertTrue(staff.isError());
        Assert.assertEquals("User doesn't have permission.", staff.getMessage());
    }

    @Test
    public void getStoreStaffListUserFail() {
        Response<List<ServiceUser>> staff = bridge.getStoreStaffList(user1Id, storeId);

        Assert.assertTrue(staff.isError());
        Assert.assertEquals("User doesn't have permission.", staff.getMessage());
    }

    @Test
    public void getStoreStaffListStoreDoesntExistFail() {
        Response<UUID> admin = bridge.getAdminCredentials();
        Assert.assertFalse(admin.isError());

        Response<Boolean> shutdown = bridge.shutdownStore(admin.getValue(), storeId);
        Assert.assertFalse(shutdown.isError());
        Assert.assertTrue(shutdown.getValue());

        Response<List<ServiceUser>> staff = bridge.getStoreStaffList(user1Id, storeId);

        Assert.assertTrue(staff.isError());
        Assert.assertEquals("Store does not exist", staff.getMessage());
    }
}