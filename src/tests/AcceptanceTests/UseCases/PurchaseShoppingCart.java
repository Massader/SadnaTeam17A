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
        bridge.addItemToCart(user1Id, item11Id, 2, store1Id);
        bridge.addItemToCart(user1Id, item12Id, 4, store1Id);

        Response<Boolean> purchase = bridge.purchaseCart(user1Id, bridge.getCartTotal(user1Id).getValue(), "address", "Aa12340000Aa12340000");
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeFounderId, store1Id);
        Response<ServiceItem> item1 = bridge.getItemInformation(store1Id, item11Id);
        Response<ServiceItem> item2 = bridge.getItemInformation(store1Id, item12Id);

        Assert.assertFalse(purchase.isError());
        Assert.assertFalse(sales.isError());
        Assert.assertFalse(item1.isError());
        Assert.assertFalse(item2.isError());

        Assert.assertTrue(purchase.getValue());
        Assert.assertNotNull(sales.getValue());
        Assert.assertEquals(2, sales.getValue().size());
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item11Id) && sale.getQuantity() == 2));
        Assert.assertTrue(sales.getValue().stream().anyMatch(sale -> sale.getUserId().equals(user1Id) && sale.getItemId().equals(item12Id) && sale.getQuantity() == 4));
        Assert.assertEquals(100 - 2, item1.getValue().getQuantity());
        Assert.assertEquals(100 - 4, item1.getValue().getQuantity());   //TODO: Add check for empty cart after purchase
    }

    @Test
    public void purchaseOverQuantityFail() {
        bridge.addItemToCart(user1Id, item21Id, 200, store2Id);

        Response<Boolean> purchase = bridge.purchaseCart(user1Id, bridge.getCartTotal(user1Id).getValue(), "address", "Aa12340000Aa12340000");
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeFounderId, store2Id);

        Assert.assertTrue(purchase.isError());
        Assert.assertFalse(sales.isError());

        Assert.assertNotNull(sales.getValue());
        Assert.assertEquals(0, sales.getValue().size());    //TODO: Check store item quantity hasn't changed
    }

    @Test
    public void purchaseInvalidCreditFail() {
        bridge.addItemToCart(user2Id, item21Id, 1, store2Id);

        Response<Boolean> purchase = bridge.purchaseCart(user2Id, bridge.getCartTotal(user2Id).getValue(), "address", "Aa12340000Aa1234FAIL");
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeFounderId, store2Id);

        Assert.assertTrue(purchase.isError());
        Assert.assertFalse(sales.isError());

        Assert.assertNotNull(sales.getValue());
        Assert.assertEquals(0, sales.getValue().size());//TODO: Check store item quantity hasn't changed
    }

    @Test
    public void purchaseWrongPriceFail() {
        bridge.addItemToCart(user3Id, item21Id, 1, store2Id);

        Response<Boolean> purchase = bridge.purchaseCart(user2Id, bridge.getCartTotal(user2Id).getValue() - 1, "address", "Aa12340000Aa12340000");
        Response<List<ServiceSale>> sales = bridge.getStoreSaleHistory(storeFounderId, store2Id);

        Assert.assertTrue(purchase.isError());
        Assert.assertFalse(sales.isError());

        Assert.assertNotNull(sales.getValue());
        Assert.assertEquals(0, sales.getValue().size());//TODO: Check store item quantity hasn't changed
    }

    @Test void purchaseConcurrently() {
        Response<ServiceItem> item22_0 = bridge.getItemInformation(store2Id, item22Id);

        UUID[] ids = new UUID[1000];
        for (int i = 0; i < 1000; i++) {
            bridge.register("user_" + i, "Aa1234");
            ids[i] = bridge.login(bridge.createClient().getValue(), "user_" + i, "Aa1234").getValue().getId();
        }

        Response<Boolean>[] purchases = new Response[1000];
        Thread[] threads = new Thread[1000];
        try {
            for (int i = 0; i < 1000; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    bridge.addItemToCart(ids[index], item22Id, 1, store2Id);
                    purchases[index] = bridge.purchaseCart(ids[index], bridge.getCartTotal(ids[index]).getValue(), "address", "Aa12340000Aa12340000");
                });
                threads[i].start();
            }
            for (Thread t : threads) {
                t.join();
            }
        }
        catch (Exception ignore) {}

        Response<ServiceItem> item22_1 = bridge.getItemInformation(store2Id, item22Id);

        int successPurchases = 0;
        for (Response<Boolean> p : purchases) {
            if (p != null && !p.isError() && p.getValue())
                successPurchases++;
        }

        Assert.assertFalse(item22_0.isError());
        Assert.assertFalse(item22_1.isError());

        Assert.assertEquals(100, item22_0.getValue().getQuantity());
        Assert.assertEquals(0, item22_1.getValue().getQuantity());
        Assert.assertEquals(100, successPurchases);
    }

    @Test void purchaseConcurrently2() {
        Response<ServiceItem> item23_0 = bridge.getItemInformation(store2Id, item22Id);

        UUID[] ids = new UUID[1000];
        for (int i = 0; i < 1000; i++) {
            bridge.register("user_" + i, "Aa1234");
            ids[i] = bridge.login(bridge.createClient().getValue(), "user__" + i, "Aa1234").getValue().getId();
        }

        Response<Boolean>[] purchases = new Response[1000];
        Thread[] threads = new Thread[1000];
        try {
            for (int i = 0; i < 1000; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    int amount = (int)(Math.random() * 5);
                    bridge.addItemToCart(ids[index], item22Id, amount, store2Id);
                    purchases[index] = bridge.purchaseCart(ids[index], bridge.getCartTotal(ids[index]).getValue(), "address", "Aa12340000Aa12340000");
                });
                threads[i].start();
            }
            for (Thread t : threads) {
                t.join();
            }
        }
        catch (Exception ignore) {}

        Response<ServiceItem> item23_1 = bridge.getItemInformation(store2Id, item22Id);

        int successPurchases = 0;
        for (Response<Boolean> p : purchases) {
            if (p != null && !p.isError() && p.getValue())
                successPurchases++;
        }

        Assert.assertFalse(item23_0.isError());
        Assert.assertFalse(item23_1.isError());

        Assert.assertEquals(100, item23_0.getValue().getQuantity());
        Assert.assertTrue(item23_1.getValue().getQuantity() >= 0 && item23_1.getValue().getQuantity() <= 4);
        Assert.assertTrue(successPurchases >= 20 && successPurchases <= 100);
    }
}