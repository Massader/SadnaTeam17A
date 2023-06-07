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
public class SetStorePolicy extends ProjectTest {

    UUID storeFounderId;
    UUID storeOwnerId;
    UUID user1Id;
    UUID user2Id;
    UUID user3Id;
    ServiceStore fruitStore;
    ServiceStore carStore;
    ServiceStore supermarket;
    UUID fruitStoreId;
    UUID carStoreId;
    UUID supermarketId;
    UUID lemonId;
    UUID appleId;
    UUID tomatoId;
    UUID watermelonId;
    UUID teslaId;
    UUID toyotaCorollaId;
    UUID toyotaCorollaHybridId;
    UUID toyotaYarisId;
    UUID pineappleId;
    UUID strawberryId;
    UUID milkId;
    UUID cheeseId;

    /*
    -----fruit store-----
    store rating: 2
    items:
            ---lemon---
            price: 3
            rating: 5
            quantity: 1000
            desc: "price per 1kg"

            ---apple---
            price: 10
            rating: 4
            quantity: 500
            desc: "price per 1kg"

            ---tomato---
            price: 1.5
            rating: 3
            quantity: 1300
            desc: "price per 1kg"

            ---watermelon---
            price: 20
            rating: 2
            quantity: 80
            desc: "price per 1kg"

    -----car store-----
    store rating: 4
    items:
            ---tesla---
            price: 200,000
            rating: 5
            quantity: 50
            desc: "electric vehicle known for its high performance, cutting-edge technology, and sustainable design. from: USA"

            ---toyota corolla---
            price: 110,000
            rating: 4
            quantity: 1000
            desc: "a popular compact car known for its reliability, practicality, and fuel efficiency. from: Japan"

            ---toyota corolla hybrid---
            price: 135,000
            rating: 3
            quantity: 600
            desc: "a fuel-efficient and eco-friendly version of the popular compact car, known for its reliable performance, advanced features, and lower environmental impact. from: Japan"

            ---toyota yaris---
            price: 90,000
            rating: 2
            quantity: 1500
            desc: "a versatile and affordable subcompact car known for its fuel efficiency, practicality, and ease of handling. from: Japan"

    -----supermarket-----
    store rating: 5
    items:
            ---pineapple---
            price: 25
            rating: 5
            quantity: 1000
            desc: "price per piece"

            ---strawberry---
            price: 40
            rating: 4
            quantity: 1000
            desc: "price per 1kg"

            ---milk---
            price: 5.90
            rating: 3
            quantity: 1000
            desc: "1 liter of milk"

            ---cheese---
            price: 17.90
            rating: 2
            quantity: 1000
            desc: "price per 100g"

    */

    @BeforeAll
    public void beforeClass() {
        bridge.register("founder", "Aa1234");
        bridge.register("owner", "Aa1234");
        bridge.register("user1", "Aa1234");
        bridge.register("user2", "Aa1234");
        bridge.register("user3", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();
        storeOwnerId = bridge.login(bridge.createClient().getValue(), "owner", "Aa1234").getValue().getId();
        user1Id = bridge.login(bridge.createClient().getValue(), "user1", "Aa1234").getValue().getId();
        user2Id = bridge.login(bridge.createClient().getValue(), "user2", "Aa1234").getValue().getId();
        user3Id = bridge.login(bridge.createClient().getValue(), "user3", "Aa1234").getValue().getId();

        bridge.appointStoreOwner(storeFounderId, storeOwnerId, carStoreId);
        bridge.appointStoreOwner(storeFounderId, storeOwnerId, fruitStoreId);
        bridge.appointStoreOwner(storeFounderId, storeOwnerId, supermarketId);

        fruitStore = bridge.createStore(storeFounderId, "Fruit Store", "fruits and vegetables").getValue();
        fruitStoreId = fruitStore.getStoreId();
        bridge.addStoreRating(bridge.createClient().getValue(), fruitStoreId, 2);

        carStore = bridge.createStore(storeFounderId, "Car Store", "cars").getValue();
        carStoreId = carStore.getStoreId();
        bridge.addStoreRating(bridge.createClient().getValue(), carStoreId, 4);

        supermarket = bridge.createStore(storeFounderId, "Super Mazuz", "Supermarket in Gilat center").getValue();
        supermarketId = supermarket.getStoreId();
        bridge.addStoreRating(bridge.createClient().getValue(), supermarketId, 5);

        lemonId = bridge.addItemToStore(storeFounderId, "Lemon", 3, fruitStoreId, 1000, "price per 1kg").getValue().getId();
        appleId = bridge.addItemToStore(storeFounderId, "Apple pink lady", 10, fruitStoreId, 500, "price per 1kg").getValue().getId();
        tomatoId = bridge.addItemToStore(storeFounderId, "Cherry Tomato", 1.5, fruitStoreId, 1300, "price per 1kg").getValue().getId();
        watermelonId = bridge.addItemToStore(storeFounderId, "watermelon", 20, fruitStoreId, 80, "price per 1kg").getValue().getId();

        bridge.addItemCategory(storeFounderId, fruitStoreId, lemonId, "fruits");
        bridge.addItemCategory(storeFounderId, fruitStoreId, appleId, "fruits");
        bridge.addItemCategory(storeFounderId, fruitStoreId, tomatoId, "vegetables");
        bridge.addItemCategory(storeFounderId, fruitStoreId, watermelonId, "fruits");

        bridge.addItemRating(bridge.createClient().getValue(), lemonId, fruitStoreId, 5);
        bridge.addItemRating(bridge.createClient().getValue(), appleId, fruitStoreId, 4);
        bridge.addItemRating(bridge.createClient().getValue(), tomatoId, fruitStoreId, 3);
        bridge.addItemRating(bridge.createClient().getValue(), watermelonId, fruitStoreId, 2);


        teslaId = bridge.addItemToStore(storeFounderId, "Tesla", 200000, carStoreId, 50, "electric vehicle known for its high performance, cutting-edge technology, and sustainable design. from: USA").getValue().getId();
        toyotaCorollaId = bridge.addItemToStore(storeFounderId, "Toyota Corolla", 110000, carStoreId, 1000, "a popular compact car known for its reliability, practicality, and fuel efficiency. from: Japan").getValue().getId();
        toyotaCorollaHybridId = bridge.addItemToStore(storeFounderId, "Toyota Corolla Hybrid", 135000, carStoreId, 600, "a fuel-efficient and eco-friendly version of the popular compact car, known for its reliable performance, advanced features, and lower environmental impact. from: Japan").getValue().getId();
        toyotaYarisId = bridge.addItemToStore(storeFounderId, "Toyota Yaris", 90000, carStoreId, 1500, "a versatile and affordable subcompact car known for its fuel efficiency, practicality, and ease of handling. from: Japan").getValue().getId();

        bridge.addItemCategory(storeFounderId, carStoreId, teslaId, "cars");
        bridge.addItemCategory(storeFounderId, carStoreId, toyotaCorollaId, "cars");
        bridge.addItemCategory(storeFounderId, carStoreId, toyotaCorollaHybridId, "cars");
        bridge.addItemCategory(storeFounderId, carStoreId, toyotaYarisId, "cars");

        bridge.addItemRating(bridge.createClient().getValue(), teslaId, carStoreId, 5);
        bridge.addItemRating(bridge.createClient().getValue(), toyotaCorollaId, carStoreId, 4);
        bridge.addItemRating(bridge.createClient().getValue(), toyotaCorollaHybridId, carStoreId, 3);
        bridge.addItemRating(bridge.createClient().getValue(), toyotaYarisId, carStoreId, 2);

        pineappleId = bridge.addItemToStore(storeFounderId, "pineapple from Thailand", 25, supermarketId, 200, "price per piece").getValue().getId();
        strawberryId = bridge.addItemToStore(storeFounderId, "strawberry", 40, supermarketId, 100, "price per 1kg").getValue().getId();
        milkId = bridge.addItemToStore(storeFounderId, "milk", 5.90, supermarketId, 200, "1 liter of milk").getValue().getId();
        cheeseId = bridge.addItemToStore(storeFounderId, "gouda cheese", 17.90, supermarketId, 100, "price per 100g").getValue().getId();

        bridge.addItemCategory(storeFounderId, supermarketId, pineappleId, "fruits");
        bridge.addItemCategory(storeFounderId, supermarketId, strawberryId, "fruits");
        bridge.addItemCategory(storeFounderId, supermarketId, milkId, "dairy");
        bridge.addItemCategory(storeFounderId, supermarketId, cheeseId, "dairy");

        bridge.addItemRating(bridge.createClient().getValue(), pineappleId, supermarketId, 5);
        bridge.addItemRating(bridge.createClient().getValue(), strawberryId, supermarketId, 4);
        bridge.addItemRating(bridge.createClient().getValue(), milkId, supermarketId, 3);
        bridge.addItemRating(bridge.createClient().getValue(), cheeseId, supermarketId, 2);

        bridge.appointStoreOwner(storeFounderId, storeOwnerId, carStoreId);
        bridge.appointStoreOwner(storeFounderId, storeOwnerId, fruitStoreId);
        bridge.appointStoreOwner(storeFounderId, storeOwnerId, supermarketId);

        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
        bridge.logout(user3Id);
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "owner", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user1", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user2", "Aa1234");
        bridge.login(bridge.createClient().getValue(), "user3", "Aa1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
        bridge.logout(storeOwnerId);
        bridge.logout(user1Id);
        bridge.logout(user2Id);
        bridge.logout(user3Id);
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    public void setItemPolicySuccess() {
        Response<Boolean> addLemonToBasket0 = bridge.addItemToCart(user1Id, lemonId, 6, fruitStoreId);
        Response<Boolean> purchaseLemon0 = bridge.purchaseCart(user1Id, 18, "address", "1234000012340000");

        Response<Boolean> addAppleToBasket0 = bridge.addItemToCart(user1Id, appleId, 6, fruitStoreId);
        Response<Boolean> purchaseApple0 = bridge.purchaseCart(user1Id, 60, "address", "1234000012340000");

        Response<Boolean> lemonPolicy = bridge.addItemPolicyTerm(storeOwnerId, fruitStoreId, lemonId, 5, false);
        Response<Boolean> applePolicy = bridge.addItemPolicyTerm(storeOwnerId, fruitStoreId, appleId, 7, true);

        Response<Boolean> addLemonToBasket1 = bridge.addItemToCart(user1Id, lemonId, 6, fruitStoreId);
        Response<Boolean> purchaseLemon1 = bridge.purchaseCart(user1Id, 18, "address", "1234000012340000");

        Response<Boolean> addAppleToBasket1 = bridge.addItemToCart(user1Id, appleId, 6, fruitStoreId);
        Response<Boolean> purchaseApple1 = bridge.purchaseCart(user1Id, 60, "address", "1234000012340000");

        assertFalse(addLemonToBasket0.isError(), addLemonToBasket0.getMessage());
        assertFalse(purchaseLemon0.isError(), purchaseLemon0.getMessage());
        assertFalse(addAppleToBasket0.isError(), addAppleToBasket0.getMessage());
        assertFalse(purchaseApple0.isError(), purchaseApple0.getMessage());
        assertFalse(lemonPolicy.isError(), lemonPolicy.getMessage());
        assertFalse(applePolicy.isError(), applePolicy.getMessage());
        assertFalse(addLemonToBasket1.isError(), addLemonToBasket1.getMessage());
        assertTrue(purchaseLemon1.isError(), "bridge.purchaseCart(user1Id, 18, \"address\", \"1234000012340000\") should have failed");
        assertFalse(addAppleToBasket1.isError(), addAppleToBasket1.getMessage());
        assertTrue(purchaseApple1.isError(), "bridge.purchaseCart(user1Id, 60, \"address\", \"1234000012340000\") should have failed");

        assertEquals(purchaseLemon1.getMessage(), "The shopping Basket is not accepted by Store Policy", "purchase failed not because policy");
        assertEquals(purchaseApple1.getMessage(), "The shopping Basket is not accepted by Store Policy", "purchase failed not because policy");
    }

    @Test
    public void setCategoryPolicySuccess() {
        Response<Boolean> addMilkToBasket0 = bridge.addItemToCart(user2Id, milkId, 6, supermarketId);
        Response<Boolean> purchaseMilk0 = bridge.purchaseCart(user2Id, 6 * 5.9, "address", "1234000012340000");

        Response<Boolean> addStrawberryToBasket0 = bridge.addItemToCart(user2Id, strawberryId, 6, supermarketId);
        Response<Boolean> purchaseStrawberry0 = bridge.purchaseCart(user2Id, 6 * 40, "address", "1234000012340000");

        Response<Boolean> dairyPolicy = bridge.addCategoryPolicyTerm(storeOwnerId, supermarketId, "dairy", 5, false);
        Response<Boolean> fruitsPolicy = bridge.addCategoryPolicyTerm(storeOwnerId, supermarketId, "fruits", 7, true);

        Response<Boolean> addMilkToBasket1 = bridge.addItemToCart(user2Id, milkId, 6, supermarketId);
        Response<Boolean> purchaseMilk1 = bridge.purchaseCart(user2Id, 6 * 5.9, "address", "1234000012340000");

        Response<Boolean> addStrawberryToBasket1 = bridge.addItemToCart(user2Id, strawberryId, 6, supermarketId);
        Response<Boolean> purchaseStrawberry1 = bridge.purchaseCart(user2Id, 6 * 40, "address", "1234000012340000");

        assertFalse(addMilkToBasket0.isError(), addMilkToBasket0.getMessage());
        assertFalse(purchaseMilk0.isError(), purchaseMilk0.getMessage());
        assertFalse(addStrawberryToBasket0.isError(), addStrawberryToBasket0.getMessage());
        assertFalse(purchaseStrawberry0.isError(), purchaseStrawberry0.getMessage());
        assertFalse(dairyPolicy.isError(), dairyPolicy.getMessage());
        assertFalse(fruitsPolicy.isError(), fruitsPolicy.getMessage());
        assertFalse(addMilkToBasket1.isError(), addMilkToBasket1.getMessage());
        assertTrue(purchaseMilk1.isError(), "bridge.purchaseCart(user2Id, 6 * 5.9, \"address\", \"1234000012340000\") should have failed");
        assertFalse(addStrawberryToBasket1.isError(), addStrawberryToBasket1.getMessage());
        assertTrue(purchaseStrawberry1.isError(), "bridge.purchaseCart(user2Id, 6 * 40, \"address\", \"1234000012340000\") should have failed");

        assertEquals(purchaseMilk1.getMessage(), "The shopping Basket is not accepted by Store Policy", "purchase failed not because policy");
        assertEquals(purchaseStrawberry1.getMessage(), "The shopping Basket is not accepted by Store Policy", "purchase failed not because policy");
    }

    @Test
    public void setBasketPolicySuccess() {
        Response<Boolean> addTeslaToBasket0 = bridge.addItemToCart(user3Id, teslaId, 1, carStoreId);
        Response<Boolean> purchaseTesla0 = bridge.purchaseCart(user3Id, 200000, "address", "1234000012340000");

        Response<Boolean> basketPolicy = bridge.addBasketPolicyTerm(storeOwnerId, carStoreId, 200001, true);

        Response<Boolean> addTeslaToBasket1 = bridge.addItemToCart(user3Id, teslaId, 1, carStoreId);
        Response<Boolean> purchaseTesla1 = bridge.purchaseCart(user3Id, 200000, "address", "1234000012340000");

        assertFalse(addTeslaToBasket0.isError(), addTeslaToBasket0.getMessage());
        assertFalse(purchaseTesla0.isError(), purchaseTesla0.getMessage());
        assertFalse(basketPolicy.isError(), basketPolicy.getMessage());
        assertFalse(addTeslaToBasket1.isError(), addTeslaToBasket1.getMessage());
        assertTrue(purchaseTesla1.isError(), "bridge.purchaseCart(user3Id, 200000, \"address\", \"1234000012340000\") should have failed");

        assertEquals(purchaseTesla1.getMessage(), "The shopping Basket is not accepted by Store Policy", "purchase failed not because policy");
    }
}
