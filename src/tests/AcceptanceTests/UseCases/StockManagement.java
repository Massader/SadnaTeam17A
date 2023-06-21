package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StockManagement extends ProjectTest {

    UUID storeFounderId;
    UUID storeOwnerId;
    UUID managerId;
    UUID userId;
    ServiceStore fruitStore;
    UUID fruitStoreId;
    UUID lemonId;
    UUID appleId;
    UUID tomatoId;
    UUID watermelonId;

    /*
    -----fruit store-----
    store rating: 2
    items:
            ---lemon---
            price: 1
            rating: 5
            quantity: 100
            desc: "price per 1kg"

            ---apple---
            price: 1.5
            rating: 4
            quantity: 100
            desc: "price per 1kg"

            ---tomato---
            price: 2
            rating: 3
            quantity: 100
            desc: "price per 1kg"

            ---watermelon---
            price: 2.5
            rating: 2
            quantity: 100
            desc: "price per 1kg"

    ----------Store Permissions----------
    0   STORE_COMMUNICATION     (default)
    1   STORE_SALE_HISTORY      (default)
    2   STORE_STOCK_MANAGEMENT  (default)
    3   STORE_ITEM_MANAGEMENT
    4   STORE_POLICY_MANAGEMENT
    5   STORE_DISCOUNT_MANAGEMENT
    6   STORE_MANAGEMENT_INFORMATION
    7   STORE_OWNER
    8   STORE_FOUNDER
    -------------------------------------

    */

    @BeforeAll
    public void beforeClass() {

    }

    @BeforeEach
    public void setUp()  {
        bridge.register("founder", "Aa1234");
        bridge.register("owner", "Aa1234");
        bridge.register("manager", "Aa1234");
        bridge.register("user", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "Aa1234").getValue().getId();
        managerId = bridge.login(bridge.createClient().getValue(), "manager", "Aa1234").getValue().getId();
        managerId = bridge.login(bridge.createClient().getValue(), "user", "Aa1234").getValue().getId();


        fruitStore = bridge.createStore(storeFounderId, "Fruit Store", "fruits and vegetables").getValue();
        fruitStoreId = fruitStore.getStoreId();
        bridge.addStoreRating(bridge.createClient().getValue(), fruitStoreId, 2);

        bridge.appointStoreOwner(storeFounderId, storeOwnerId, fruitStoreId);
        bridge.appointStoreManager(storeFounderId, managerId, fruitStoreId);


        lemonId = bridge.addItemToStore(storeFounderId, "Lemon", 1, fruitStoreId, 100, "price per 1kg").getValue().getId();
        appleId = bridge.addItemToStore(storeFounderId, "Apple pink lady", 1.5, fruitStoreId, 100, "price per 1kg").getValue().getId();
        tomatoId = bridge.addItemToStore(storeFounderId, "Cherry Tomato", 2, fruitStoreId, 100, "price per 1kg").getValue().getId();
        watermelonId = bridge.addItemToStore(storeFounderId, "watermelon", 2.5, fruitStoreId, 100, "price per 1kg").getValue().getId();

        bridge.addItemCategory(storeFounderId, fruitStoreId, lemonId, "fruits");
        bridge.addItemCategory(storeFounderId, fruitStoreId, appleId, "fruits");
        bridge.addItemCategory(storeFounderId, fruitStoreId, tomatoId, "vegetables");
        bridge.addItemCategory(storeFounderId, fruitStoreId, watermelonId, "fruits");

        bridge.addItemRating(bridge.createClient().getValue(), lemonId, fruitStoreId, 5);
        bridge.addItemRating(bridge.createClient().getValue(), appleId, fruitStoreId, 4);
        bridge.addItemRating(bridge.createClient().getValue(), tomatoId, fruitStoreId, 3);
        bridge.addItemRating(bridge.createClient().getValue(), watermelonId, fruitStoreId, 2);

        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(managerId);
        bridge.logout(userId);
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "manager", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user", "Aa1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(managerId);
        bridge.logout(userId);
        deleteDB();
        bridge.resetService();
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    public void setQuantitySuccess() {
        Response<ServiceItem> lemon0 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple0 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato0 = bridge.getItemInformation(fruitStoreId, tomatoId);

        Response<Boolean> byFounder = bridge.setItemQuantity(storeFounderId, fruitStoreId, lemonId, 111);
        Response<Boolean> byOwner = bridge.setItemQuantity(storeOwnerId, fruitStoreId, appleId, 222);
        Response<Boolean> byManager = bridge.setItemQuantity(managerId, fruitStoreId, tomatoId, 333);

        Response<ServiceItem> lemon1 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple1 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato1 = bridge.getItemInformation(fruitStoreId, tomatoId);

        assertFalse(lemon0.isError(), String.format("bridge.getItemInformation(fruitStoreId, lemonId) => %s", lemon0.getMessage()));
        assertFalse(apple0.isError(), String.format("bridge.getItemInformation(fruitStoreId, appleId) => %s", apple0.getMessage()));
        assertFalse(tomato0.isError(), String.format("bridge.getItemInformation(fruitStoreId, tomatoId) => %s", tomato0.getMessage()));
        assertFalse(byFounder.isError(), String.format("bridge.setItemQuantity(storeFounderId, fruitStoreId, lemonId, 111) => %s", byFounder.getMessage()));
        assertFalse(byOwner.isError(), String.format("bridge.setItemQuantity(storeOwnerId, fruitStoreId, appleId, 222) -> %s", byOwner.getMessage()));
        assertFalse(byManager.isError(), String.format("bridge.setItemQuantity(managerId, fruitStoreId, tomatoId, 333) => %s", byManager.getMessage()));
        assertFalse(lemon1.isError(), String.format("bridge.getItemInformation(fruitStoreId, lemonId) => %s", lemon1.getMessage()));
        assertFalse(apple1.isError(), String.format("bridge.getItemInformation(fruitStoreId, appleId) => %s", apple1.getMessage()));
        assertFalse(tomato1.isError(), String.format("bridge.getItemInformation(fruitStoreId, tomatoId) => %s", tomato1.getMessage()));

        assertTrue(byFounder.getValue(), "bridge.setItemQuantity(storeFounderId, fruitStoreId, lemonId, 111) failed");
        assertTrue(byOwner.getValue(), "bridge.setItemQuantity(storeOwnerId, fruitStoreId, appleId, 222) failed");
        assertTrue(byManager.getValue(), "bridge.setItemQuantity(managerId, fruitStoreId, tomatoId, 333) failed");

        assertNotEquals(lemon0.getValue().getQuantity(), lemon1.getValue().getQuantity(), "lemon quantity did not changed");
        assertNotEquals(apple0.getValue().getQuantity(), apple1.getValue().getQuantity(), "apple quantity did not changed");
        assertNotEquals(tomato0.getValue().getQuantity(), tomato1.getValue().getQuantity(), "tomato quantity did not changed");

        assertEquals(111, lemon1.getValue().getQuantity(), "lemon quantity is not 111");
        assertEquals(222, apple1.getValue().getQuantity(), "apple quantity is not 222");
        assertEquals(333, tomato1.getValue().getQuantity(), "tomato quantity is not 333");
    }

    @Test
    public void setNameSuccess() {
        Response<ServiceItem> lemon0 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple0 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato0 = bridge.getItemInformation(fruitStoreId, tomatoId);

        Response<Boolean> byFounder = bridge.setItemName(storeFounderId, fruitStoreId, lemonId, "$lemon$");
        Response<Boolean> byOwner = bridge.setItemName(storeOwnerId, fruitStoreId, appleId, "$apple$");
        Response<Boolean> byManager = bridge.setItemName(managerId, fruitStoreId, tomatoId, "$tomato$");

        Response<ServiceItem> lemon1 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple1 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato1 = bridge.getItemInformation(fruitStoreId, tomatoId);

        assertFalse(lemon0.isError(), String.format("bridge.getItemInformation(fruitStoreId, lemonId) => %s", lemon0.getMessage()));
        assertFalse(apple0.isError(), String.format("bridge.getItemInformation(fruitStoreId, appleId) => %s", apple0.getMessage()));
        assertFalse(tomato0.isError(), String.format("bridge.getItemInformation(fruitStoreId, tomatoId) => %s", tomato0.getMessage()));
        assertFalse(byFounder.isError(), String.format("bridge.setItemName(storeFounderId, fruitStoreId, lemonId, \"$lemon$\") => %s", byFounder.getMessage()));
        assertFalse(byOwner.isError(), String.format("bridge.setItemName(storeOwnerId, fruitStoreId, appleId, \"$apple$\") => %s", byOwner.getMessage()));
        assertFalse(byManager.isError(), String.format("bridge.setItemName(managerId, fruitStoreId, tomatoId, \"$tomato$\") => %s", byManager.getMessage()));
        assertFalse(lemon1.isError(), String.format("bridge.getItemInformation(fruitStoreId, lemonId) => %s", lemon1.getMessage()));
        assertFalse(apple1.isError(), String.format("bridge.getItemInformation(fruitStoreId, appleId) => %s", apple1.getMessage()));
        assertFalse(tomato1.isError(), String.format("bridge.getItemInformation(fruitStoreId, tomatoId) => %s", tomato1.getMessage()));

        assertTrue(byFounder.getValue(), "bridge.setItemName(storeFounderId, fruitStoreId, lemonId, \"$lemon$\") failed");
        assertTrue(byOwner.getValue(), "bridge.setItemName(storeOwnerId, fruitStoreId, appleId, \"$apple$\") failed");
        assertTrue(byManager.getValue(), "bridge.setItemName(managerId, fruitStoreId, tomatoId, \"$tomato$\") failed");

        assertEquals("$" + lemon0.getValue().getName() + "$", lemon1.getValue().getName(), "lemon name did not changed");
        assertEquals("$" + apple0.getValue().getName() + "$", apple1.getValue().getName(), "apple name did not changed");
        assertEquals("$" + tomato0.getValue().getName() + "$", tomato1.getValue().getName(), "tomato name did not changed");
    }

    @Test
    public void setDescriptionSuccess() {
        Response<ServiceItem> lemon0 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple0 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato0 = bridge.getItemInformation(fruitStoreId, tomatoId);

        Response<Boolean> byFounder = bridge.setItemDescription(storeFounderId, fruitStoreId, lemonId, "$price per 1kg$");
        Response<Boolean> byOwner = bridge.setItemDescription(storeOwnerId, fruitStoreId, appleId, "$price per 1kg$");
        Response<Boolean> byManager = bridge.setItemDescription(managerId, fruitStoreId, tomatoId, "$price per 1kg$");

        Response<ServiceItem> lemon1 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple1 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato1 = bridge.getItemInformation(fruitStoreId, tomatoId);

        assertFalse(lemon0.isError(), String.format("bridge.getItemInformation(fruitStoreId, lemonId) => %s", lemon0.getMessage()));
        assertFalse(apple0.isError(), String.format("bridge.getItemInformation(fruitStoreId, appleId) => %s", apple0.getMessage()));
        assertFalse(tomato0.isError(), String.format("bridge.getItemInformation(fruitStoreId, tomatoId) => %s", tomato0.getMessage()));
        assertFalse(byFounder.isError(), String.format("bridge.setItemDescription(storeFounderId, fruitStoreId, lemonId, \"$price per 1kg$\") => %s", byFounder.getMessage()));
        assertFalse(byOwner.isError(), String.format("bridge.setItemDescription(storeOwnerId, fruitStoreId, appleId, \"$price per 1kg$\") => %s", byOwner.getMessage()));
        assertFalse(byManager.isError(), String.format("bridge.setItemDescription(managerId, fruitStoreId, tomatoId, \"$price per 1kg$\") => %s", byManager.getMessage()));
        assertFalse(lemon1.isError(), String.format("bridge.getItemInformation(fruitStoreId, lemonId) => %s", lemon1.getMessage()));
        assertFalse(apple1.isError(), String.format("bridge.getItemInformation(fruitStoreId, appleId) => %s", apple1.getMessage()));
        assertFalse(tomato1.isError(), String.format("bridge.getItemInformation(fruitStoreId, tomatoId) => %s", tomato1.getMessage()));

        assertTrue(byFounder.getValue(), "bridge.setItemDescription(storeFounderId, fruitStoreId, lemonId, \"$price per 1kg$\") failed");
        assertTrue(byOwner.getValue(), "bridge.setItemDescription(storeOwnerId, fruitStoreId, appleId, \"$price per 1kg$\") failed");
        assertTrue(byManager.getValue(), "bridge.setItemDescription(managerId, fruitStoreId, tomatoId, \"$price per 1kg$\") failed");

        assertEquals("$" + lemon0.getValue().getDescription() + "$", lemon1.getValue().getDescription(), "lemon description did not changed");
        assertEquals("$" + apple0.getValue().getDescription() + "$", apple1.getValue().getDescription(), "apple description did not changed");
        assertEquals("$" + tomato0.getValue().getDescription() + "$", tomato1.getValue().getDescription(), "tomato description did not changed");
    }

    @Test
    public void setPriceSuccess() {
        Response<ServiceItem> lemon0 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple0 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato0 = bridge.getItemInformation(fruitStoreId, tomatoId);

        Response<Boolean> byFounder = bridge.setItemPrice(storeFounderId, fruitStoreId, lemonId, 10);
        Response<Boolean> byOwner = bridge.setItemPrice(storeOwnerId, fruitStoreId, appleId, 15);
        Response<Boolean> byManager = bridge.setItemPrice(managerId, fruitStoreId, tomatoId, 20);

        Response<ServiceItem> lemon1 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple1 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato1 = bridge.getItemInformation(fruitStoreId, tomatoId);

        assertFalse(lemon0.isError(), String.format("bridge.getItemInformation(fruitStoreId, lemonId) => %s", lemon0.getMessage()));
        assertFalse(apple0.isError(), String.format("bridge.getItemInformation(fruitStoreId, appleId) => %s", apple0.getMessage()));
        assertFalse(tomato0.isError(), String.format("bridge.getItemInformation(fruitStoreId, tomatoId) => %s", tomato0.getMessage()));
        assertFalse(byFounder.isError(), String.format("bridge.setItemPrice(storeFounderId, fruitStoreId, lemonId, 10) => %s", byFounder.getMessage()));
        assertFalse(byOwner.isError(), String.format("bridge.setItemPrice(storeOwnerId, fruitStoreId, appleId, 15) => %s", byOwner.getMessage()));
        assertFalse(byManager.isError(), String.format("bridge.setItemPrice(managerId, fruitStoreId, tomatoId, 20) => %s", byManager.getMessage()));
        assertFalse(lemon1.isError(), String.format("bridge.getItemInformation(fruitStoreId, lemonId) => %s", lemon1.getMessage()));
        assertFalse(apple1.isError(), String.format("bridge.getItemInformation(fruitStoreId, appleId) => %s", apple1.getMessage()));
        assertFalse(tomato1.isError(), String.format("bridge.getItemInformation(fruitStoreId, tomatoId) => %s", tomato1.getMessage()));

        assertTrue(byFounder.getValue(), "bridge.setItemPrice(storeFounderId, fruitStoreId, lemonId, 10) failed");
        assertTrue(byOwner.getValue(), "bridge.setItemPrice(storeOwnerId, fruitStoreId, appleId, 15) failed");
        assertTrue(byManager.getValue(), "bridge.setItemPrice(managerId, fruitStoreId, tomatoId, 20) failed");

        assertEquals(10 * lemon0.getValue().getPrice(), lemon1.getValue().getPrice(), 0.0, "lemon price did not changed");
        assertEquals(10 * apple0.getValue().getPrice(), apple1.getValue().getPrice(), 0.0, "apple price did not changed");
        assertEquals(10 * tomato0.getValue().getPrice(), tomato1.getValue().getPrice(), 0.0, "tomato price did not changed");
    }

    @Test
    public void stockManagementUserFail() {
        Response<ServiceItem> watermelon0 = bridge.getItemInformation(fruitStoreId, watermelonId);

        Response<Boolean> quantity = bridge.setItemQuantity(userId, fruitStoreId, lemonId, 1000000);
        Response<Boolean> name = bridge.setItemName(userId, fruitStoreId, appleId, "aaaaaaaaaaaaaaaaaaa");
        Response<Boolean> description = bridge.setItemDescription(userId, fruitStoreId, tomatoId, "bbbbbbbbbbbbbbbbbbb");
        Response<Boolean> price = bridge.setItemPrice(userId, fruitStoreId, tomatoId, 10000000000.0);

        Response<ServiceItem> watermelon1 = bridge.getItemInformation(fruitStoreId, watermelonId);

        assertFalse(watermelon0.isError(), String.format("bridge.getItemInformation(fruitStoreId, watermelonId) => %s", watermelon0.getMessage()));
        assertTrue(quantity.isError(), "bridge.setItemQuantity(userId, fruitStoreId, lemonId, 1000000) should have failed");
        assertTrue(name.isError(), "bridge.setItemName(userId, fruitStoreId, appleId, \"aaaaaaaaaaaaaaaaaaa\") should have failed");
        assertTrue(description.isError(), "bridge.setItemDescription(userId, fruitStoreId, tomatoId, \"bbbbbbbbbbbbbbbbbbb\") should have failed");
        assertTrue(price.isError(), "bridge.setItemPrice(userId, fruitStoreId, tomatoId, 10000000000.0) should have failed");
        assertFalse(watermelon1.isError(), String.format("bridge.getItemInformation(fruitStoreId, watermelonId) => %s", watermelon1.getMessage()));

        assertEquals(watermelon0.getValue().getQuantity(), watermelon1.getValue().getQuantity(), "watermelon quantity changed");
        assertEquals(watermelon0.getValue().getName(), watermelon1.getValue().getName(), "watermelon name changed");
        assertEquals(watermelon0.getValue().getDescription(), watermelon1.getValue().getDescription(), "watermelon description changed");
        assertEquals(watermelon0.getValue().getPrice(), watermelon1.getValue().getPrice(), 0.0, "watermelon price changed");
    }
}
