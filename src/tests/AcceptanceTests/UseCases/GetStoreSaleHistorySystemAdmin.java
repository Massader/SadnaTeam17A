package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

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
        bridge.register("founder", "Aa1234");
        bridge.register("user1", "Aa1234");
        bridge.register("user2", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
        adminId = bridge.getAdminCredentials().getValue();
        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "Aa1234").getValue().getId();
        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "Aa1234").getValue().getId();

        store = bridge.createStore(storeFounderId, "store", "test").getValue();
        storeId = store.getStoreId();

        item1Id = bridge.addItemToStore(storeFounderId, "item1", 10, storeId, 100, "test").getValue().getId();
        item2Id = bridge.addItemToStore(storeFounderId, "item2", 20, storeId, 100, "test").getValue().getId();
        item3Id = bridge.addItemToStore(storeFounderId, "item3", 30, storeId, 100, "test").getValue().getId();
        item4Id = bridge.addItemToStore(storeFounderId, "item4", 40, storeId, 100, "test").getValue().getId();

        bridge.addItemToCart(user1Id, item1Id, 3, storeId);
        bridge.addItemToCart(user1Id, item2Id, 6, storeId);
        bridge.purchaseCart(user1Id, bridge.getCartTotal(user1Id).getValue(), "address", "Aa12340000Aa12340000");

        bridge.addItemToCart(user2Id, item3Id, 9, storeId);
        bridge.addItemToCart(user2Id, item4Id, 12, storeId);
        bridge.purchaseCart(user2Id, bridge.getCartTotal(user2Id).getValue(), "address", "Aa12340000Aa12340000");

        bridge.logout(storeFounderId);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user2", "Aa1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }


    @Test
    //checks if the admin can get the store history
    public void GetStoreSaleHistoryAdminSuccess() {
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistorySystemAdmin(adminId, storeId);

        assertFalse(sales.isError(), String.format("bridge.getStoreSaleHistorySystemAdmin(adminId, storeId) => %s", sales.getMessage()));
        assertNotNull(sales.getValue(), "bridge.getStoreSaleHistorySystemAdmin(adminId, storeId) failed");
        assertEquals(4, sales.getValue().size(), "list size is not equal 4");
        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item1Id) && sale.getQuantity() == 3), "list does not contain item1");
        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item2Id) && sale.getQuantity() == 6), "list does not contain item2");
        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item3Id) && sale.getQuantity() == 9), "list does not contain item3");
        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item4Id) && sale.getQuantity() == 12),"list does not contain item4");
    }

    @Test
    //checks if a logged out admin can get the store history
    public void GetStoreSaleHistoryLoggedOutFail() {
        bridge.logout(adminId);
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(adminId, storeId);
        bridge.login(bridge.createClient().getValue(), "admin", "Admin1");

        assertTrue(sales.isError(), "bridge.getStoreSaleHistory(adminId, storeId) should have failed");
    }
}