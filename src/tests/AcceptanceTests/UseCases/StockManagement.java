package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.Response;
import ServiceLayer.Service;
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
        bridge.register("founder", "1234");
        bridge.register("owner", "1234");
        bridge.register("manager", "1234");
        bridge.register("user", "1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "1234").getValue().getId();
        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "1234").getValue().getId();
        managerId = bridge.login(bridge.createClient().getValue(), "manager", "1234").getValue().getId();
        managerId = bridge.login(bridge.createClient().getValue(), "user", "1234").getValue().getId();


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
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "1234");
        bridge.login(bridge.createClient().getValue(), "owner", "1234");
        bridge.login(bridge.createClient().getValue(), "manager", "1234");
        bridge.login(bridge.createClient().getValue(), "user", "1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(managerId);
        bridge.logout(userId);
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
        Response<Boolean> byOwner = bridge.setItemQuantity(storeFounderId, fruitStoreId, appleId, 222);
        Response<Boolean> byManager = bridge.setItemQuantity(storeFounderId, fruitStoreId, tomatoId, 333);

        Response<ServiceItem> lemon1 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple1 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato1 = bridge.getItemInformation(fruitStoreId, tomatoId);

        Assert.assertFalse(lemon0.isError());
        Assert.assertFalse(apple0.isError());
        Assert.assertFalse(tomato0.isError());
        Assert.assertFalse(byFounder.isError());
        Assert.assertFalse(byOwner.isError());
        Assert.assertFalse(byManager.isError());
        Assert.assertFalse(lemon1.isError());
        Assert.assertFalse(apple1.isError());
        Assert.assertFalse(tomato1.isError());

        Assert.assertTrue(byFounder.getValue());
        Assert.assertTrue(byOwner.getValue());
        Assert.assertTrue(byManager.getValue());

        Assert.assertNotEquals(lemon0.getValue().getQuantity(), lemon1.getValue().getQuantity());
        Assert.assertNotEquals(apple0.getValue().getQuantity(), apple1.getValue().getQuantity());
        Assert.assertNotEquals(tomato0.getValue().getQuantity(), tomato1.getValue().getQuantity());

        Assert.assertEquals(111, lemon1.getValue().getQuantity());
        Assert.assertEquals(222, apple1.getValue().getQuantity());
        Assert.assertEquals(333, tomato1.getValue().getQuantity());
    }

    @Test
    public void setNameSuccess() {
        Response<ServiceItem> lemon0 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple0 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato0 = bridge.getItemInformation(fruitStoreId, tomatoId);

        Response<Boolean> byFounder = bridge.setItemName(storeFounderId, fruitStoreId, lemonId, "$lemon$");
        Response<Boolean> byOwner = bridge.setItemName(storeFounderId, fruitStoreId, appleId, "$apple$");
        Response<Boolean> byManager = bridge.setItemName(storeFounderId, fruitStoreId, tomatoId, "$tomato$");

        Response<ServiceItem> lemon1 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple1 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato1 = bridge.getItemInformation(fruitStoreId, tomatoId);

        Assert.assertFalse(lemon0.isError());
        Assert.assertFalse(apple0.isError());
        Assert.assertFalse(tomato0.isError());
        Assert.assertFalse(byFounder.isError());
        Assert.assertFalse(byOwner.isError());
        Assert.assertFalse(byManager.isError());
        Assert.assertFalse(lemon1.isError());
        Assert.assertFalse(apple1.isError());
        Assert.assertFalse(tomato1.isError());

        Assert.assertTrue(byFounder.getValue());
        Assert.assertTrue(byOwner.getValue());
        Assert.assertTrue(byManager.getValue());

        Assert.assertEquals("$" + lemon0.getValue().getName() + "$", lemon1.getValue().getName());
        Assert.assertEquals("$" + apple0.getValue().getName() + "$", apple1.getValue().getName());
        Assert.assertEquals("$" + tomato0.getValue().getName() + "$", tomato1.getValue().getName());
    }

    @Test
    public void setDescriptionSuccess() {
        Response<ServiceItem> lemon0 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple0 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato0 = bridge.getItemInformation(fruitStoreId, tomatoId);

        Response<Boolean> byFounder = bridge.setItemDescription(storeFounderId, fruitStoreId, lemonId, "$price per 1kg$");
        Response<Boolean> byOwner = bridge.setItemDescription(storeFounderId, fruitStoreId, appleId, "$price per 1kg$");
        Response<Boolean> byManager = bridge.setItemDescription(storeFounderId, fruitStoreId, tomatoId, "$price per 1kg$");

        Response<ServiceItem> lemon1 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple1 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato1 = bridge.getItemInformation(fruitStoreId, tomatoId);

        Assert.assertFalse(lemon0.isError());
        Assert.assertFalse(apple0.isError());
        Assert.assertFalse(tomato0.isError());
        Assert.assertFalse(byFounder.isError());
        Assert.assertFalse(byOwner.isError());
        Assert.assertFalse(byManager.isError());
        Assert.assertFalse(lemon1.isError());
        Assert.assertFalse(apple1.isError());
        Assert.assertFalse(tomato1.isError());

        Assert.assertTrue(byFounder.getValue());
        Assert.assertTrue(byOwner.getValue());
        Assert.assertTrue(byManager.getValue());

        Assert.assertEquals("$" + lemon0.getValue().getDescription() + "$", lemon1.getValue().getDescription());
        Assert.assertEquals("$" + apple0.getValue().getDescription() + "$", apple1.getValue().getDescription());
        Assert.assertEquals("$" + tomato0.getValue().getDescription() + "$", tomato1.getValue().getDescription());
    }

    @Test
    public void setPriceSuccess() {
        Response<ServiceItem> lemon0 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple0 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato0 = bridge.getItemInformation(fruitStoreId, tomatoId);

        Response<Boolean> byFounder = bridge.setItemPrice(storeFounderId, fruitStoreId, lemonId, 10);
        Response<Boolean> byOwner = bridge.setItemPrice(storeFounderId, fruitStoreId, appleId, 15);
        Response<Boolean> byManager = bridge.setItemPrice(storeFounderId, fruitStoreId, tomatoId, 20);

        Response<ServiceItem> lemon1 = bridge.getItemInformation(fruitStoreId, lemonId);
        Response<ServiceItem> apple1 = bridge.getItemInformation(fruitStoreId, appleId);
        Response<ServiceItem> tomato1 = bridge.getItemInformation(fruitStoreId, tomatoId);

        Assert.assertFalse(lemon0.isError());
        Assert.assertFalse(apple0.isError());
        Assert.assertFalse(tomato0.isError());
        Assert.assertFalse(byFounder.isError());
        Assert.assertFalse(byOwner.isError());
        Assert.assertFalse(byManager.isError());
        Assert.assertFalse(lemon1.isError());
        Assert.assertFalse(apple1.isError());
        Assert.assertFalse(tomato1.isError());

        Assert.assertTrue(byFounder.getValue());
        Assert.assertTrue(byOwner.getValue());
        Assert.assertTrue(byManager.getValue());

        Assert.assertEquals(10 * lemon0.getValue().getPrice(), lemon1.getValue().getPrice(), 0.0);
        Assert.assertEquals(10 * apple0.getValue().getPrice(), apple1.getValue().getPrice(), 0.0);
        Assert.assertEquals(10 * tomato0.getValue().getPrice(), tomato1.getValue().getPrice(), 0.0);
    }

    @Test
    public void stockManagementUserFail() {
        Response<ServiceItem> watermelon0 = bridge.getItemInformation(fruitStoreId, watermelonId);

        Response<Boolean> quantity = bridge.setItemQuantity(storeFounderId, fruitStoreId, lemonId, 1000000);
        Response<Boolean> name = bridge.setItemName(storeFounderId, fruitStoreId, appleId, "aaaaaaaaaaaaaaaaaaa");
        Response<Boolean> description = bridge.setItemDescription(storeFounderId, fruitStoreId, tomatoId, "bbbbbbbbbbbbbbbbbbb");
        Response<Boolean> price = bridge.setItemPrice(storeFounderId, fruitStoreId, tomatoId, 10000000000.0);

        Response<ServiceItem> watermelon1 = bridge.getItemInformation(fruitStoreId, watermelonId);

        Assert.assertFalse(watermelon0.isError());
        Assert.assertTrue(quantity.isError());
        Assert.assertTrue(name.isError());
        Assert.assertTrue(description.isError());
        Assert.assertTrue(price.isError());
        Assert.assertFalse(watermelon1.isError());

        Assert.assertEquals(watermelon0.getValue().getQuantity(), watermelon1.getValue().getQuantity());
        Assert.assertEquals(watermelon0.getValue().getName(), watermelon1.getValue().getName());
        Assert.assertEquals(watermelon0.getValue().getDescription(), watermelon1.getValue().getDescription());
        Assert.assertEquals(watermelon0.getValue().getPrice(), watermelon1.getValue().getPrice(), 0.0);
    }
}
