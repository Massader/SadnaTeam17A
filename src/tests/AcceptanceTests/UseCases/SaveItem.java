package AcceptanceTests.UseCases;
import APILayer.Main;
import AcceptanceTests.*;
import DomainLayer.Market.Users.ShoppingBasket;
import DomainLayer.Market.Users.ShoppingCart;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveItem extends ProjectTest {

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

        item21Id = bridge.addItemToStore(storeFounderId, "item21", 30, store2Id, 1000, "test").getValue().getId();
        item22Id = bridge.addItemToStore(storeFounderId, "item22", 40, store2Id, 1000, "test").getValue().getId();

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
    public void saveItemSuccess() {
        Response<List<ServiceShoppingBasket>> cart0 = bridge.getCart(user1Id);
        Response<Boolean> save = bridge.addItemToCart(user1Id, item11Id, 10, store1Id);
        Response<List<ServiceShoppingBasket>> cart1 = bridge.getCart(user1Id);

        assertFalse(cart0.isError(), String.format("bridge.getCart(user1Id) => %s", cart0.getMessage()));
        assertFalse(save.isError(), String.format("bridge.addItemToCart(user1Id, item11Id, 10, store1Id) => %s", save.getMessage()));
        assertFalse(cart1.isError(), String.format("bridge.getCart(user1Id) => %s", cart1.getMessage()));

        assertTrue(save.getValue(), "bridge.addItemToCart(user1Id, item11Id, 10, store1Id) failed");
        assertEquals(1, cart1.getValue().size() - cart0.getValue().size(), "cart size did not increased by 1");
        assertTrue(cart1.getValue().stream().anyMatch(basket -> basket.getStoreId().equals(store1Id) && basket.getItems().get(item11Id) != null && basket.getItems().get(item11Id).getQuantity() == 10), "cart does not contain a basket with item11");
    }

    @Test
    public void saveItemOverQuantityFail() {
        Response<List<ServiceShoppingBasket>> cart0 = bridge.getCart(user1Id);
        Response<Boolean> save = bridge.addItemToCart(user1Id, item12Id, 101, store1Id);
        Response<List<ServiceShoppingBasket>> cart1 = bridge.getCart(user1Id);

        assertFalse(cart0.isError(), String.format("bridge.getCart(user1Id) => %s", cart0.getMessage()));
        assertTrue(save.isError(), "bridge.addItemToCart(user1Id, item12Id, 101, store1Id) should have failed");
        assertFalse(cart1.isError(), String.format("bridge.getCart(user1Id) => %s", cart1.getMessage()));

        assertEquals(cart1.getValue().size(), cart0.getValue().size(), "cart size has changed");
    }

    @Test
    public void saveItemConcurrently() {
        UUID[] ids = new UUID[1000];
        List<ServiceShoppingBasket>[] carts0 = new List[1000];
        for (int i = 0; i < 1000; i++) {
            bridge.register("user_" + i, "Aa1234");
            ids[i] = bridge.login(bridge.createClient().getValue(), "user_" + i, "Aa1234").getValue().getId();
            carts0[i] = bridge.getCart(ids[i]).getValue();
        }

        Response<Boolean>[] saves = new Response[1000];
        Thread[] threads = new Thread[1000];
        try {
            for (int i = 0; i < 1000; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    saves[index] = bridge.addItemToCart(ids[index], item22Id, 1, store2Id);
                });
                threads[i].start();
            }
            for (Thread t : threads) {
                t.join();
            }
        }
        catch (Exception ignore) {}

        List<ServiceShoppingBasket>[] carts1 = new List[1000];

        for (int i = 0; i < 1000; i++) {
            carts1[i] = bridge.getCart(ids[i]).getValue();

            assertFalse(saves[i].isError(), String.format("bridge.addItemToCart(ids[%d], item22Id, 1, store2Id) => %s", i, saves[i].getMessage()));
            assertTrue(saves[i].getValue(), String.format("bridge.addItemToCart(ids[%d], item22Id, 1, store2Id) failed", i));
            assertEquals(1, carts1[i].size() - carts0[i].size(), String.format("cart[%d] size did not increased by 1", i));
            assertTrue(carts1[i].stream().anyMatch(basket -> basket.getStoreId().equals(store1Id) && basket.getItems().get(item22Id) != null && basket.getItems().get(item22Id).getQuantity() == 1), String.format("cart[%d] does not contain a basket with item22", i));
        }
    }
}