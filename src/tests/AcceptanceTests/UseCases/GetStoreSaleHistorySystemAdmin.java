package AcceptanceTests.UseCases;
import AcceptanceTests.*;
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
public class GetStoreSaleHistorySystemAdmin extends ProjectTest {

    UUID storeFounderId;
    UUID adminId;
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
        bridge.register("user1", "1234");
        bridge.register("user2", "1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "1234").getValue().getId();
        adminId = bridge.getAdminCredentials().getValue();
        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "1234").getValue().getId();
        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "1234").getValue().getId();

        store = bridge.createStore(storeFounderId, "test", "test").getValue();
        storeId = store.getStoreId();

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
        bridge.logout(user1Id);
        bridge.logout(user2Id);
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "1234");
        bridge.login(bridge.createClient().getValue(), "user1", "1234");
        bridge.login(bridge.createClient().getValue(), "user2", "1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(storeFounderId, storeId);
    }

    @Test
    //checks if the admin can get the store history
    public void GetStoreSaleHistoryAdminSuccess() {
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistorySystemAdmin(adminId, storeId);

        Assert.assertFalse(sales.isError());
        Assert.assertNotNull(sales.getValue());
        Assert.assertEquals(4, sales.getValue().size());
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item1Id) && sale.getQuantity() == 3));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item2Id) && sale.getQuantity() == 6));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item3Id) && sale.getQuantity() == 9));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item4Id) && sale.getQuantity() == 12));
    }

    @Test
    //checks if a logged out admin can get the store history
    public void GetStoreSaleHistoryLoggedOutFail() {
        bridge.logout(adminId);
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(adminId, storeId);
        bridge.login(bridge.createClient().getValue(), "admin", "Admin1");

        Assert.assertTrue(sales.isError());
    }
}

