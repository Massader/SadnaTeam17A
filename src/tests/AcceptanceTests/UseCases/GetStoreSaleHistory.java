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
public class GetStoreSaleHistory extends ProjectTest {

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

        store = bridge.createStore(storeFounderId, "test", "test").getValue();
        storeId = store.getStoreId();

        bridge.appointStoreOwner(storeFounderId, storeOwnerId, storeId);
        bridge.appointStoreManager(storeFounderId, storeManagerWithPermissionId, storeId);
        List<Integer> permissions = new ArrayList<Integer>();
        permissions.add(1); // STORE_SALE_HISTORY = 1
        bridge.setStoreManagerPermissions(storeFounderId, storeManagerWithPermissionId, storeId, permissions);
        bridge.appointStoreManager(storeFounderId, storeManagerNoPermissionId, storeId);

        item1Id = bridge.addItemToStore(storeFounderId, "item1", 10, storeId, 100, "test").getValue().getId();
        item2Id = bridge.addItemToStore(storeFounderId, "item2", 20, storeId, 100, "test").getValue().getId();
        item3Id = bridge.addItemToStore(storeFounderId, "item3", 30, storeId, 100, "test").getValue().getId();
        item4Id = bridge.addItemToStore(storeFounderId, "item4", 40, storeId, 100, "test").getValue().getId();

        bridge.addItemToCart(user1Id, item1Id, 3, storeId);
        bridge.addItemToCart(user1Id, item2Id, 6, storeId);
        bridge.purchaseCart(user1Id, 3*10 + 6*20, "address", "credit");

        bridge.addItemToCart(user2Id, item3Id, 9, storeId);
        bridge.addItemToCart(user2Id, item4Id, 12, storeId);
        bridge.purchaseCart(user2Id, 9*30 + 12*40, "address", "credit");

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
        bridge.closeStore(storeFounderId, storeId);
    }

    @Test
    //checks if a store founder can get the store history
    public void GetStoreSaleHistoryFounderSuccess() {
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeFounderId, storeId);

        Assert.assertFalse(sales.isError());
        Assert.assertNotNull(sales.getValue());
        Assert.assertEquals(4, sales.getValue().size());
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item1Id) && sale.getQuantity() == 3));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item2Id) && sale.getQuantity() == 6));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item3Id) && sale.getQuantity() == 9));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item4Id) && sale.getQuantity() == 12));
    }

    @Test
    //checks if a store owner can get the store history
    public void GetStoreSaleHistoryOwnerSuccess() {
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeOwnerId, storeId);

        Assert.assertFalse(sales.isError());
        Assert.assertNotNull(sales.getValue());
        Assert.assertEquals(4, sales.getValue().size());
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item1Id) && sale.getQuantity() == 3));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item2Id) && sale.getQuantity() == 6));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item3Id) && sale.getQuantity() == 9));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item4Id) && sale.getQuantity() == 12));
    }

    @Test
    //checks if a store manager with permission can get the store history
    public void GetStoreSaleHistoryManagerWithPermissionSuccess() {
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeManagerWithPermissionId, storeId);

        Assert.assertFalse(sales.isError());
        Assert.assertNotNull(sales.getValue());
        Assert.assertEquals(4, sales.getValue().size());
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item1Id) && sale.getQuantity() == 3));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item2Id) && sale.getQuantity() == 6));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item3Id) && sale.getQuantity() == 9));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item4Id) && sale.getQuantity() == 12));
    }

    @Test
    //checks if a user can get the store history
    public void GetStoreSaleHistoryManagerWithoutPermissionFail() {
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeManagerNoPermissionId, storeId);

        Assert.assertTrue(sales.isError());
    }
    @Test
    //checks if a user can get the store history
    public void GetStoreSaleHistoryUserFail() {
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(user1Id, storeId);

        Assert.assertTrue(sales.isError());
    }

    @Test
    //checks if a logged out store owner/founder can get the store history
    public void GetStoreSaleHistoryLoggedOutFail() {
        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        Response<List<ServiceSale>> sales1 = bridge.getStoreSaleHistory(storeFounderId, storeId);
        Response<List<ServiceSale>> sales2 = bridge.getStoreSaleHistory(storeOwnerId, storeId);

        Assert.assertTrue(sales1.isError());
        Assert.assertTrue(sales2.isError());
    }
}
