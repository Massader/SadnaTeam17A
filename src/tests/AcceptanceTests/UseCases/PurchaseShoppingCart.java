package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import DomainLayer.Market.Users.ShoppingBasket;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PurchaseShoppingCart extends ProjectTest {

    UUID storeFounderId;
    UUID user1Id;
    UUID user2Id;
    UUID user3Id;
    ServiceStore store1;
    ServiceStore store2;
    UUID store1Id;
    UUID store2Id;
    UUID item11Id;
    UUID item12Id;
    UUID item21Id;
    UUID item22Id;
    UUID item23Id;
    //creditCard
    double price;
    String cardNumber;
    String month;
    String year;
    String holder;
    String ccv;
    String idCard;

    //shipping address
    String name;
    String address;
    String city;
    String country;
    int zip;



    @BeforeAll
    public void beforeClass() {
        bridge.register("founder", "Aa1234");
        bridge.register("user1", "Aa1234");
        bridge.register("user2", "Aa1234");
        bridge.register("user3", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "Aa1234").getValue().getId();
        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "Aa1234").getValue().getId();
        user3Id = bridge.login(bridge.createClient().getValue(), "user3", "Aa1234").getValue().getId();

        store1 = bridge.createStore(storeFounderId, "store1", "test").getValue();
        store1Id = store1.getStoreId();

        store2 = bridge.createStore(storeFounderId, "store2", "test").getValue();
        store2Id = store2.getStoreId();

        item11Id = bridge.addItemToStore(storeFounderId, "item11", 10, store1Id, 100, "test").getValue().getId();
        item12Id = bridge.addItemToStore(storeFounderId, "item12", 20, store1Id, 100, "test").getValue().getId();

        item21Id = bridge.addItemToStore(storeFounderId, "item21", 30, store2Id, 100, "test").getValue().getId();
        item22Id = bridge.addItemToStore(storeFounderId, "item22", 40, store2Id, 100, "test").getValue().getId();
        item23Id = bridge.addItemToStore(storeFounderId, "item23", 50, store2Id, 100, "test").getValue().getId();

        bridge.logout(storeFounderId);
        bridge.logout(user1Id);

    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user2", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user3", "Aa1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
        bridge.logout(user3Id);
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    public void purchaseCartSuccess() {
        price = 100.0;
        cardNumber = "12345";
        month = "12";
        year = "2027";
        holder = "Lior Levy";
        ccv = "123";
        idCard = "123456789";
        name = "lior levy";
        address = "heshkolit";
        city = "beer sheva";
        country = "Israel";
        zip = 6092000;
        bridge.addItemToCart(user1Id, item11Id, 2, store1Id);
        bridge.addItemToCart(user1Id, item12Id, 4, store1Id);

        Response<List<ServiceShoppingBasket>> cart0 = bridge.getCart(user1Id);
        Response<Boolean> purchase = bridge.purchaseCart(user1Id, bridge.getCartTotal(user1Id).getValue(),address, city, country, zip, cardNumber, month, year, holder, ccv, idCard);
       // Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeFounderId, store1Id);
        Response<ServiceItem> item11 = bridge.getItemInformation(store1Id, item11Id);
        Response<ServiceItem> item22 = bridge.getItemInformation(store1Id, item12Id);
        Response<List<ServiceShoppingBasket>> cart1 = bridge.getCart(user1Id);

        assertFalse(cart0.isError(), String.format("bridge.getCart(user1Id) => %s", cart0.getMessage()));
        assertFalse(purchase.isError(), String.format("bridge.purchaseCart(user1Id, bridge.getCartTotal(user1Id).getValue(), \"address\", \"1234000012340000\") => %s", purchase.getMessage()));
      //  assertFalse(sales.isError(), String.format("bridge.getStoreSaleHistory(storeFounderId, store1Id) => %s", sales.getMessage()));
        assertFalse(item11.isError(), String.format("bridge.getItemInformation(store1Id, item11Id) => %s", item11.getMessage()));
        assertFalse(item22.isError(), String.format("bridge.getItemInformation(store1Id, item12Id) => %s", item22.getMessage()));
        assertFalse(cart1.isError(), String.format("bridge.getCart(user1Id) => %s", cart1.getMessage()));

        assertFalse(cart0.getValue().isEmpty(), "cart is empty before purchase");
        assertTrue(purchase.getValue(), "bridge.purchaseCart(user1Id, bridge.getCartTotal(user1Id).getValue(), \"address\", \"1234000012340000\") failed");
  //      assertNotNull(sales.getValue(), "bridge.getStoreSaleHistory(storeFounderId, store1Id) failed");
   //     assertEquals(2, sales.getValue().size(), "sales list size is not 2");
  //      assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item11Id) && sale.getQuantity() == 2), "sales list does not contain item11");
  //      assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item12Id) && sale.getQuantity() == 4), "sales list does not contain item12");
        assertEquals(100 - 2, item11.getValue().getQuantity(), "item11 quantity did not decreased by 2");
        assertEquals(100 - 4, item22.getValue().getQuantity(), "item12 quantity did not decreased by 4");
        assertTrue(cart1.getValue().isEmpty(), "cart is not empty after purchase");
    }

    @Test
    public void purchaseOverQuantityFail() {
        price = 100.0;
        cardNumber = "12345";
        month = "12";
        year = "2027";
        holder = "Lior Levy";
        ccv = "123";
        idCard = "123456789";
        name = "lior levy";
        address = "heshkolit";
        city = "beer sheva";
        country = "Israel";
        zip = 6092000;


        bridge.addItemToCart(user1Id, item21Id, 200, store2Id);
//cant happen cart will have 0 items
//        Response<List<ServiceShoppingBasket>> cart0 = bridge.getCart(user1Id);
//        Response<Boolean> purchase = bridge.purchaseCart(user1Id, bridge.getCartTotal(user1Id).getValue(), address, city, country, zip, cardNumber, month, year, holder, ccv, idCard);
//        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeFounderId, store2Id);
//        Response<List<ServiceShoppingBasket>> cart1 = bridge.getCart(user1Id);
//        Response<ServiceItem> item21 = bridge.getItemInformation(store2Id, item21Id);
//
//        assertFalse(cart0.isError(), String.format("bridge.getCart(user1Id) => %s", cart0.getMessage()));
//        assertTrue(purchase.isError(), "bridge.purchaseCart(user1Id, bridge.getCartTotal(user1Id).getValue(), \"address\", \"1234000012340000\") should have failed");
//        assertFalse(sales.isError(), String.format("bridge.getStoreSaleHistory(storeFounderId, store2Id) => %s", sales.getMessage()));
//        assertFalse(cart1.isError(), String.format("bridge.getCart(user1Id) => %s", cart1.getMessage()));
//        assertFalse(item21.isError(), String.format("bridge.getItemInformation(store2Id, item21Id) => %s", item21.getMessage()));
//
//
//        assertNotNull(sales.getValue(), "bridge.getStoreSaleHistory(storeFounderId, store2Id) failed");
//        assertTrue(sales.getValue().isEmpty(), "sales list is not empty");
//        assertEquals(cart0.getValue().size(), cart1.getValue().size(), "cart size changed although purchase failed");
//        assertEquals(30, item21.getValue().getQuantity(), "item quantity changed although purchase failed");

    }

//    @Test
//    public void purchaseInvalidCreditFail() {
//        bridge.addItemToCart(user2Id, item21Id, 1, store2Id);
//
//        Response<List<ServiceShoppingBasket>> cart0 = bridge.getCart(user2Id);
//        Response<Boolean> purchase = bridge.purchaseCart(user2Id, bridge.getCartTotal(user2Id).getValue(), "address", "Aa12340000Aa1234FAIL");
//        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeFounderId, store2Id);
//        Response<List<ServiceShoppingBasket>> cart1 = bridge.getCart(user2Id);
//        Response<ServiceItem> item21 = bridge.getItemInformation(store2Id, item21Id);
//
//        assertFalse(cart0.isError(), String.format("bridge.getCart(user2Id) => %s", cart0.getMessage()));
//        assertTrue(purchase.isError(), "bridge.purchaseCart(user2Id, bridge.getCartTotal(user2Id).getValue(), \"address\", \"Aa12340000Aa1234FAIL\") should have failed");
//        assertFalse(sales.isError(), String.format("bridge.getStoreSaleHistory(storeFounderId, store2Id) => %s", sales.getMessage()));
//        assertFalse(cart1.isError(), String.format("bridge.getCart(user2Id) => %s", cart1.getMessage()));
//        assertFalse(item21.isError(), String.format("bridge.getItemInformation(store2Id, item21Id) => %s", item21.getMessage()));
//
//        assertNotNull(sales.getValue(), "bridge.getStoreSaleHistory(storeFounderId, store2Id) failed");
//        assertTrue(sales.getValue().isEmpty(), "sales list is not empty");
//        assertEquals(cart0.getValue().size(), cart1.getValue().size(), "cart size changed although purchase failed");
//        assertEquals(30, item21.getValue().getQuantity(), "item quantity changed although purchase failed");
//    }

//    @Test
//    public void purchaseWrongPriceFail() {
//        bridge.addItemToCart(user3Id, item21Id, 1, store2Id);
//
//        Response<List<ServiceShoppingBasket>> cart0 = bridge.getCart(user3Id);
//        Response<Boolean> purchase = bridge.purchaseCart(user2Id, bridge.getCartTotal(user2Id).getValue() - 1, "address", "1234000012340000");
//        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeFounderId, store2Id);
//        Response<List<ServiceShoppingBasket>> cart1 = bridge.getCart(user3Id);
//        Response<ServiceItem> item21 = bridge.getItemInformation(store2Id, item21Id);
//
//        assertFalse(cart0.isError(), String.format("bridge.getCart(user3Id) => %s", cart0.getMessage()));
//        assertTrue(purchase.isError(), "bridge.purchaseCart(user2Id, bridge.getCartTotal(user2Id).getValue() - 1, \"address\", \"1234000012340000\") should have failed");
//        assertFalse(sales.isError(), String.format("bridge.getStoreSaleHistory(storeFounderId, store2Id) => %s", sales.getMessage()));
//        assertFalse(cart1.isError(), String.format("bridge.getCart(user3Id) => %s", cart1.getMessage()));
//        assertFalse(item21.isError(), String.format("bridge.getItemInformation(store2Id, item21Id) => %s", item21.getMessage()));
//
//        assertNotNull(sales.getValue(), "bridge.getStoreSaleHistory(storeFounderId, store2Id) failed");
//        assertTrue(sales.getValue().isEmpty(), "sales list is not empty");
//        assertEquals(cart0.getValue().size(), cart1.getValue().size(), "cart size changed although purchase failed");
//        assertEquals(30, item21.getValue().getQuantity(), "item quantity changed although purchase failed");
//    }
//
//    @Test void purchaseConcurrently() {
//        Response<ServiceItem> item22_0 = bridge.getItemInformation(store2Id, item22Id);
//
//        UUID[] ids = new UUID[1000];
//        Response<List<ServiceShoppingBasket>>[] carts = new Response[1000];
//        for (int i = 0; i < 1000; i++) {
//            bridge.register("user_" + i, "Aa1234");
//            ids[i] = bridge.login(bridge.createClient().getValue(), "user_" + i, "Aa1234").getValue().getId();
//        }
//
//        Response<Boolean>[] purchases = new Response[1000];
//        Thread[] threads = new Thread[1000];
//        try {
//            for (int i = 0; i < 1000; i++) {
//                final int index = i;
//                threads[i] = new Thread(() -> {
//                    bridge.addItemToCart(ids[index], item22Id, 1, store2Id);
//                    purchases[index] = bridge.purchaseCart(ids[index], bridge.getCartTotal(ids[index]).getValue(), "address", "1234000012340000");
//                    carts[index] = bridge.getCart(ids[index]);
//                });
//                threads[i].start();
//            }
//            for (Thread t : threads) {
//                t.join();
//            }
//        }
//        catch (Exception ignore) {}
//
//        Response<ServiceItem> item22_1 = bridge.getItemInformation(store2Id, item22Id);
//
//        int successPurchases = 0;
//        int emptyCartsForSuccessPurchase = 0;
//        for (int i = 0; i < 1000; i++) {
//            if (purchases[i] != null && !purchases[i].isError() && purchases[i].getValue()) {
//                successPurchases++;
//                if (carts[i] != null && !carts[i].isError() && carts[i].getValue().isEmpty()) {
//                    emptyCartsForSuccessPurchase++;
//                }
//            }
//        }
//
//        assertFalse(item22_0.isError(), String.format("bridge.getItemInformation(store2Id, item22Id) => %s", item22_0.getMessage()));
//        assertFalse(item22_1.isError(), String.format("bridge.getItemInformation(store2Id, item22Id) => %s", item22_1.getMessage()));
//
//        assertEquals(100, item22_0.getValue().getQuantity(), "item22 quantity is not equal 100 before purchases");
//        assertEquals(0, item22_1.getValue().getQuantity(), "item22 quantity is not equal 0 after purchases");
//        assertEquals(100, successPurchases, "there is no 100 successful purchases");
//        assertEquals(100, emptyCartsForSuccessPurchase, "not all carts of successful purchases are empty");
//    }
//
//    @Test void purchaseConcurrently2() {
//        Response<ServiceItem> item23_0 = bridge.getItemInformation(store2Id, item22Id);
//
//        UUID[] ids = new UUID[1000];
//        Response<List<ServiceShoppingBasket>>[] carts = new Response[1000];
//        for (int i = 0; i < 1000; i++) {
//            bridge.register("user_" + i, "Aa1234");
//            ids[i] = bridge.login(bridge.createClient().getValue(), "user__" + i, "Aa1234").getValue().getId();
//        }
//
//        Response<Boolean>[] purchases = new Response[1000];
//        Thread[] threads = new Thread[1000];
//        try {
//            for (int i = 0; i < 1000; i++) {
//                final int index = i;
//                threads[i] = new Thread(() -> {
//                    int amount = (int)(Math.random() * 5);
//                    bridge.addItemToCart(ids[index], item22Id, amount, store2Id);
//                    purchases[index] = bridge.purchaseCart(ids[index], bridge.getCartTotal(ids[index]).getValue(), "address", "1234000012340000");
//                    carts[index] = bridge.getCart(ids[index]);
//                });
//                threads[i].start();
//            }
//            for (Thread t : threads) {
//                t.join();
//            }
//        }
//        catch (Exception ignore) {}
//
//        Response<ServiceItem> item23_1 = bridge.getItemInformation(store2Id, item22Id);
//
//        int successPurchases = 0;
//        int emptyCartsForSuccessPurchase = 0;
//        for (int i = 0; i < 1000; i++) {
//            if (purchases[i] != null && !purchases[i].isError() && purchases[i].getValue()) {
//                successPurchases++;
//                if (carts[i] != null && !carts[i].isError() && carts[i].getValue().isEmpty()) {
//                    emptyCartsForSuccessPurchase++;
//                }
//            }
//        }
//
//        assertFalse(item23_0.isError(), String.format("bridge.getItemInformation(store2Id, item22Id) => %s", item23_0.getMessage()));
//        assertFalse(item23_1.isError(), String.format("bridge.getItemInformation(store2Id, item22Id) => %s", item23_1.getMessage()));
//
//        assertEquals(100, item23_0.getValue().getQuantity(), "item23 quantity is not equal 100 before purchases");
//        assertTrue(item23_1.getValue().getQuantity() >= 0 && item23_1.getValue().getQuantity() <= 4, "item23 quantity is not between 0 to 4 after purchases");
//        assertTrue(successPurchases >= 20 && successPurchases <= 100, "there was not between 20 to 100 successful purchases");
//        assertEquals(successPurchases, emptyCartsForSuccessPurchase, "not all carts of successful purchases are empty");
//    }
}