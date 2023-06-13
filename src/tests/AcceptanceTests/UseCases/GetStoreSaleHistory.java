//package AcceptanceTests.UseCases;
//import AcceptanceTests.*;
//import ServiceLayer.Response;
//import ServiceLayer.ServiceObjects.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import org.junit.jupiter.api.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class GetStoreSaleHistory extends ProjectTest {//TODO: update purchase
//
//    UUID storeFounderId;
//    UUID storeOwnerId;
//    UUID storeManagerId;
//    UUID user1Id;
//    UUID user2Id;
//    ServiceStore store;
//    UUID storeId;
//    UUID item1Id;
//    UUID item2Id;
//    UUID item3Id;
//    UUID item4Id;
//
//    @BeforeAll
//    public void beforeClass() {
//        bridge.register("founder", "Aa1234");
//        bridge.register("owner", "Aa1234");
//        bridge.register("manager", "Aa1234");
//        bridge.register("user1", "Aa1234");
//        bridge.register("user2", "Aa1234");
//
//        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
//        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "Aa1234").getValue().getId();
//        storeManagerId = bridge.login(bridge.createClient().getValue(), "manager", "Aa1234").getValue().getId();
//        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "Aa1234").getValue().getId();
//        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "Aa1234").getValue().getId();
//
//        store = bridge.createStore(storeFounderId, "store", "test").getValue();
//        storeId = store.getStoreId();
//
//        bridge.appointStoreOwner(storeFounderId, storeOwnerId, storeId);
//        bridge.appointStoreManager(storeFounderId, storeManagerId, storeId);
//
//        item1Id = bridge.addItemToStore(storeFounderId, "item1", 10, storeId, 100, "test").getValue().getId();
//        item2Id = bridge.addItemToStore(storeFounderId, "item2", 20, storeId, 100, "test").getValue().getId();
//        item3Id = bridge.addItemToStore(storeFounderId, "item3", 30, storeId, 100, "test").getValue().getId();
//        item4Id = bridge.addItemToStore(storeFounderId, "item4", 40, storeId, 100, "test").getValue().getId();
//
//        bridge.addItemToCart(user1Id, item1Id, 3, storeId);
//        bridge.addItemToCart(user1Id, item2Id, 6, storeId);
//        bridge.purchaseCart(user1Id, bridge.getCartTotal(user1Id).getValue(), "address", "Aa12340000Aa12340000");
//
//        bridge.addItemToCart(user2Id, item3Id, 9, storeId);
//        bridge.addItemToCart(user2Id, item4Id, 12, storeId);
//        bridge.purchaseCart(user2Id, bridge.getCartTotal(user2Id).getValue(), "address", "Aa12340000Aa12340000");
//
//        bridge.logout(storeFounderId);
//        bridge.logout(storeOwnerId);
//        bridge.logout(storeManagerId);
//        bridge.logout(user1Id);
//        bridge.logout(user2Id);
//    }
//
//    @BeforeEach
//    public void setUp()  {
//        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
//        bridge.login(bridge.createClient().getValue(), "owner", "Aa1234");
//        bridge.login(bridge.createClient().getValue(), "manager", "Aa1234");
//        bridge.login(bridge.createClient().getValue(), "user1", "Aa1234");
//        bridge.login(bridge.createClient().getValue(), "user2", "Aa1234");
//    }
//
//    @AfterEach
//    public void tearDown() {
//        bridge.logout(storeFounderId);
//        bridge.logout(storeOwnerId);
//        bridge.logout(storeManagerId);
//        bridge.logout(user1Id);
//        bridge.logout(user2Id);
//    }
//
//    @AfterAll
//    public void afterClass() {
//        bridge.resetService();
//    }
//
//    @Test
//    //checks if a store founder can get the store history
//    public void GetStoreSaleHistoryFounderSuccess() {
//        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeFounderId, storeId);
//
//        assertFalse(sales.isError(), String.format("bridge.getStoreSaleHistory(storeFounderId, storeId) => %s", sales.getMessage()));
//        assertNotNull(sales.getValue(), "bridge.getStoreSaleHistory(storeFounderId, storeId) failed");
//        assertEquals(4, sales.getValue().size(), "list size is not equal 4");
//        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item1Id) && sale.getQuantity() == 3), "list does not contain item1");
//        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item2Id) && sale.getQuantity() == 6), "list does not contain item2");
//        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item3Id) && sale.getQuantity() == 9), "list does not contain item3");
//        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item4Id) && sale.getQuantity() == 12), "list does not contain item4");
//    }
//
//    @Test
//    //checks if a store owner can get the store history
//    public void GetStoreSaleHistoryOwnerSuccess() {
//        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeOwnerId, storeId);
//
//        assertFalse(sales.isError(), String.format("bridge.getStoreSaleHistory(storeFounderId, storeId) => %s", sales.getMessage()));
//        assertNotNull(sales.getValue(), "bridge.getStoreSaleHistory(storeOwnerId, storeId) failed");
//        assertEquals(4, sales.getValue().size(), "list size is not equal 4");
//        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item1Id) && sale.getQuantity() == 3), "list does not contain item1");
//        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item2Id) && sale.getQuantity() == 6), "list does not contain item2");
//        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item3Id) && sale.getQuantity() == 9), "list does not contain item3");
//        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item4Id) && sale.getQuantity() == 12), "list does not contain item4");
//    }
//
//    @Test
//    //checks if a store manager with permission can get the store history
//    public void GetStoreSaleHistoryManagerWithPermissionSuccess() {
//        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeManagerId, storeId);
//
//        assertFalse(sales.isError(), String.format("bridge.getStoreSaleHistory(storeFounderId, storeId) => %s", sales.getMessage()));
//        assertNotNull(sales.getValue(), "bridge.getStoreSaleHistory(storeManagerId, storeId) failed");
//        assertEquals(4, sales.getValue().size(), "list size is not equal 4");
//        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item1Id) && sale.getQuantity() == 3), "list does not contain item1");
//        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item2Id) && sale.getQuantity() == 6), "list does not contain item2");
//        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item3Id) && sale.getQuantity() == 9), "list does not contain item3");
//        assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user2Id) && sale.getItemId().equals(item4Id) && sale.getQuantity() == 12), "list does not contain item4");
//    }
//
//    @Test
//    //checks if a user can get the store history
//    public void GetStoreSaleHistoryUserFail() {
//        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(user1Id, storeId);
//
//        assertTrue(sales.isError(), "bridge.getStoreSaleHistory(user1Id, storeId) should have failed");
//    }
//
//    @Test
//    //checks if a logged out store owner/founder can get the store history
//    public void GetStoreSaleHistoryLoggedOutFail() {
//        bridge.logout(storeFounderId);
//        bridge.logout(storeOwnerId);
//        Response<List<ServiceSale>> sales1 = bridge.getStoreSaleHistory(storeFounderId, storeId);
//        Response<List<ServiceSale>> sales2 = bridge.getStoreSaleHistory(storeOwnerId, storeId);
//
//        assertTrue(sales1.isError(), "bridge.getStoreSaleHistory(storeFounderId, storeId) should have failed");
//        assertTrue(sales2.isError(), "bridge.getStoreSaleHistory(storeOwnerId, storeId) should have failed");
//    }
//}