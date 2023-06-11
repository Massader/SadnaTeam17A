package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SearchStoreItem extends ProjectTest {

    UUID storeFounderId;
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

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();

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

        bridge.logout(storeFounderId);
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
        bridge.reopenStore(storeFounderId, fruitStoreId);
        bridge.reopenStore(storeFounderId, carStoreId);
    }

    @AfterEach
    public void tearDown() {
        bridge.closeStore(storeFounderId, fruitStoreId);
        bridge.closeStore(storeFounderId, carStoreId);
        bridge.logout(storeFounderId);
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    public void searchEmptySuccess() {
        Response<List<ServiceItem>> all = bridge.searchItem(null, null, null, null, null, null, null, null, null);

        assertFalse(all.isError(), String.format("bridge.searchItem(null, null, null, null, null, null, null, null, null) => %s", all.getMessage()));

        assertNotNull(all.getValue(), "bridge.searchItem(null, null, null, null, null, null, null, null, null) failed");

        assertEquals(12, all.getValue().size(), "item list size is not 12");

        assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(lemonId)), "list does not contain lemon");
        assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(appleId)), "list does not contain apple");
        assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(tomatoId)), "list does not contain tomato");
        assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(watermelonId)), "list does not contain watermelon");
        assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(teslaId)), "list does not contain tesla");
        assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)), "list does not contain toyota corolla");
        assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaHybridId)), "list does not contain toyota corolla hybrid");
        assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(toyotaYarisId)), "list does not contain toyota yaris");
        assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)), "list does not contain pineapple");
        assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)), "list does not contain strawberry");
        assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(milkId)), "list does not contain milk");
        assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(cheeseId)), "list does not contain cheese");
    }
    @Test
    public void searchOnlyByNameSuccess() {
        Response<List<ServiceItem>> lemon = bridge.searchItem("lemon", null, null, null, null, null, null, null, null);
        Response<List<ServiceItem>> toyota = bridge.searchItem("toyota", null, null, null, null, null, null, null, null);
        Response<List<ServiceItem>> apple = bridge.searchItem("apple", null, null, null, null, null, null, null, null);

        assertFalse(lemon.isError(), String.format("bridge.searchItem(\"lemon\", null, null, null, null, null, null, null, null) => %s", lemon.getMessage()));
        assertFalse(toyota.isError(), String.format("bridge.searchItem(\"toyota\", null, null, null, null, null, null, null, null) => %s", toyota.getMessage()));
        assertFalse(apple.isError(), String.format("bridge.searchItem(\"apple\", null, null, null, null, null, null, null, null) => %s", apple.getMessage()));

        assertNotNull(lemon.getValue(), "bridge.searchItem(\"lemon\", null, null, null, null, null, null, null, null) failed");
        assertNotNull(toyota.getValue(), "bridge.searchItem(\"toyota\", null, null, null, null, null, null, null, null) failed");
        assertNotNull(apple.getValue(), "bridge.searchItem(\"apple\", null, null, null, null, null, null, null, null) failed");

        assertEquals(1, lemon.getValue().size(), "list size is not 1");
        assertEquals(3, toyota.getValue().size(), "list size is not 3");
        assertEquals(2, apple.getValue().size(), "list size is not 2");

        assertTrue(lemon.getValue().stream().anyMatch(item -> item.getId().equals(lemonId)), "the list does not contain lemon");
        assertTrue(toyota.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)), "the list does not contain toyota corolla");
        assertTrue(toyota.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaHybridId)), "the list does not contain toyota corolla hybrid");
        assertTrue(toyota.getValue().stream().anyMatch(item -> item.getId().equals(toyotaYarisId)), "the list does not contain toyota yaris");
        assertTrue(apple.getValue().stream().anyMatch(item -> item.getId().equals(appleId)), "the list does not contain apple");
        assertTrue(apple.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)), "the list does not contain pineapple");
    }

    @Test
    public void searchOnlyByCategorySuccess() {
        Response<List<ServiceItem>> fruits = bridge.searchItem(null, "fruits", null, null, null, null, null, null, null);
        Response<List<ServiceItem>> cars = bridge.searchItem(null, "cars", null, null, null, null, null, null, null);
        Response<List<ServiceItem>> dairy = bridge.searchItem(null, "dairy", null, null, null, null, null, null, null);

        assertFalse(fruits.isError(), String.format("bridge.searchItem(null, \"fruits\", null, null, null, null, null, null, null) => %s", fruits.getMessage()));
        assertFalse(cars.isError(), String.format("bridge.searchItem(null, \"cars\", null, null, null, null, null, null, null) => %s", cars.getMessage()));
        assertFalse(dairy.isError(), String.format("bridge.searchItem(null, \"dairy\", null, null, null, null, null, null, null) => %s", dairy.getMessage()));

        assertNotNull(fruits.getValue(), "bridge.searchItem(null, \"fruits\", null, null, null, null, null, null, null) failed");
        assertNotNull(cars.getValue(), "bridge.searchItem(null, \"cars\", null, null, null, null, null, null, null) failed");
        assertNotNull(dairy.getValue(), "bridge.searchItem(null, \"dairy\", null, null, null, null, null, null, null) failed");

        assertEquals(5, fruits.getValue().size(), "list size is not 5");
        assertEquals(4, cars.getValue().size(), "list size is not 4");
        assertEquals(2, dairy.getValue().size(), "list size is not 2");

        assertTrue(fruits.getValue().stream().anyMatch(item -> item.getId().equals(lemonId)), "list does not contain lemon");
        assertTrue(fruits.getValue().stream().anyMatch(item -> item.getId().equals(appleId)), "list does not contain apple");
        assertTrue(fruits.getValue().stream().anyMatch(item -> item.getId().equals(watermelonId)), "list does not contain watermelon");
        assertTrue(fruits.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)), "list does not contain pineapple");
        assertTrue(fruits.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)), "list does not contain strawberry");
        assertTrue(cars.getValue().stream().anyMatch(item -> item.getId().equals(teslaId)), "list does not contain tesla");
        assertTrue(cars.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)), "list does not contain toyota corolla");
        assertTrue(cars.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaHybridId)), "list does not contain toyota corolla hybrid");
        assertTrue(cars.getValue().stream().anyMatch(item -> item.getId().equals(toyotaYarisId)), "list does not contain toyota yaris");
        assertTrue(dairy.getValue().stream().anyMatch(item -> item.getId().equals(milkId)), "list does not contain milk");
        assertTrue(dairy.getValue().stream().anyMatch(item -> item.getId().equals(cheeseId)), "list does not contain cheese");

        assertTrue(fruits.getValue().stream().allMatch(item -> item.getCategories().contains("fruits")), "list does not contain item from category \"fruits\"");
        assertTrue(cars.getValue().stream().allMatch(item -> item.getCategories().contains("cars")), "list does not contain item from category \"cars\"");
        assertTrue(dairy.getValue().stream().allMatch(item -> item.getCategories().contains("dairy")), "list does not contain item from category \"dairy\"");
    }

    @Test
    public void searchOnlyByPriceRangeSuccess() {
        Response<List<ServiceItem>> max10 = bridge.searchItem(null, null, null, 10.0, null, null, null, null, null);
        Response<List<ServiceItem>> min10 = bridge.searchItem(null, null, 10.0, null, null, null, null, null, null);
        Response<List<ServiceItem>> min10max100 = bridge.searchItem(null, null, 10.0, 100.0, null, null, null, null, null);

        assertFalse(max10.isError(), String.format("bridge.searchItem(null, null, null, 10.0, null, null, null, null, null) => %s", max10.getMessage()));
        assertFalse(min10.isError(), String.format("bridge.searchItem(null, null, 10.0, null, null, null, null, null, null) => %s", min10.getMessage()));
        assertFalse(min10max100.isError(), String.format("bridge.searchItem(null, null, 10.0, 100.0, null, null, null, null, null) => %s", min10max100.getMessage()));

        assertNotNull(max10.getValue(), "bridge.searchItem(null, null, null, 10.0, null, null, null, null, null) failed");
        assertNotNull(min10.getValue(), "bridge.searchItem(null, null, 10.0, null, null, null, null, null, null) failed");
        assertNotNull(min10max100.getValue(), "bridge.searchItem(null, null, 10.0, 100.0, null, null, null, null, null) failed");

        assertEquals(4, max10.getValue().size(), "list size is not 4");
        assertEquals(9, min10.getValue().size(), "list size is not 9");
        assertEquals(5, min10max100.getValue().size(), "list size is not 5");

        assertTrue(max10.getValue().stream().anyMatch(item -> item.getId().equals(lemonId)), "list does not contain lemon");
        assertTrue(max10.getValue().stream().anyMatch(item -> item.getId().equals(appleId)), "list does not contain apple");
        assertTrue(max10.getValue().stream().anyMatch(item -> item.getId().equals(tomatoId)), "list does not contain tomato");
        assertTrue(max10.getValue().stream().anyMatch(item -> item.getId().equals(milkId)), "list does not contain milk");

        assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(appleId)), "list does not contain apple");
        assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(watermelonId)), "list does not contain watermelon");
        assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(teslaId)), "list does not contain tesla");
        assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)), "list does not contain toyota corolla");
        assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaHybridId)), "list does not contain toyota corolla hybrid");
        assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(toyotaYarisId)), "list does not contain toyota yaris");
        assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)), "list does not contain pineapple");
        assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)), "list does not contain strawberry");
        assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(cheeseId)), "list does not contain cheese");

        assertTrue(min10max100.getValue().stream().anyMatch(item -> item.getId().equals(appleId)), "list does not contain apple");
        assertTrue(min10max100.getValue().stream().anyMatch(item -> item.getId().equals(watermelonId)), "list does not contain watermelon");
        assertTrue(min10max100.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)), "list does not contain pineapple");
        assertTrue(min10max100.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)), "list does not contain strawberry");
        assertTrue(min10max100.getValue().stream().anyMatch(item -> item.getId().equals(cheeseId)), "list does not contain cheese");

        assertTrue(max10.getValue().stream().allMatch(item -> item.getPrice() <= 10), "list does not contain items that cost under 10");
        assertTrue(min10.getValue().stream().allMatch(item -> item.getPrice() >= 10), "list does not contain items that cost over 10");
        assertTrue(min10max100.getValue().stream().allMatch(item -> item.getPrice() >= 10 && item.getPrice() <= 100), "list does not contain items that cost over 10 and under 100");
    }

    @Test
    public void searchOnlyByItemRatingSuccess() {
        Response<List<ServiceItem>> min3 = bridge.searchItem(null, null, null, null, 3, null, null, null, null);
        Response<List<ServiceItem>> min5 = bridge.searchItem(null, null, null, null, 5, null, null, null, null);

        assertFalse(min3.isError(), String.format("bridge.searchItem(null, null, null, null, 3, null, null, null, null) => %s", min3.getMessage()));
        assertFalse(min5.isError(), String.format("bridge.searchItem(null, null, null, null, 5, null, null, null, null) => %s", min5.getMessage()));

        assertNotNull(min3.getValue(), "bridge.searchItem(null, null, null, null, 3, null, null, null, null) failed");
        assertNotNull(min5.getValue(), "bridge.searchItem(null, null, null, null, 5, null, null, null, null) failed");

        assertEquals(9, min3.getValue().size(), "list size is not 9");
        assertEquals(3, min5.getValue().size(), "list size is not 3");

        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(lemonId)), "list does not contain lemon");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(appleId)), "list does not contain apple");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(tomatoId)), "list does not contain tomato");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(teslaId)), "list does not contain tesla");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)), "list does not contain toyota corolla");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaHybridId)), "list does not contain toyota corolla hybrid");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)), "list does not contain pineapple");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)), "list does not contain strawberry");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(milkId)), "list does not contain milk");

        assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(lemonId)), "list does not contain lemon");
        assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(teslaId)), "list does not contain tesla");
        assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)), "list does not contain pineapple");

        assertTrue(min3.getValue().stream().allMatch(item -> item.getRating() >= 3), "list does not contain items with rating 3 and above");
        assertTrue(min5.getValue().stream().allMatch(item -> item.getRating() >= 5), "list does not contain items with rating 5 and above");
    }

    @Test
    public void searchOnlyByStoreRatingSuccess() {
        Response<List<ServiceItem>> min3 = bridge.searchItem(null, null, null, null, null, 3, null, null, null);
        Response<List<ServiceItem>> min5 = bridge.searchItem(null, null, null, null, null, 5, null, null, null);

        assertFalse(min3.isError(), String.format("bridge.searchItem(null, null, null, null, null, 3, null, null, null) => %s", min3.getMessage()));
        assertFalse(min5.isError(), String.format("bridge.searchItem(null, null, null, null, null, 5, null, null, null) => %s", min5.getMessage()));

        assertNotNull(min3.getValue(), "bridge.searchItem(null, null, null, null, null, 3, null, null, null) failed");
        assertNotNull(min5.getValue(), "bridge.searchItem(null, null, null, null, null, 5, null, null, null) failed");

        assertEquals(8, min3.getValue().size(), "list size is not 8");
        assertEquals(4, min5.getValue().size(), "list size is not 4");

        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(teslaId)), "list does not contain tesla");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)), "list does not contain toyota corolla");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaHybridId)), "list does not contain toyota corolla hybrid");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(toyotaYarisId)), "list does not contain toyota yaris");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)), "list does not contain pineapple");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)), "list does not contain strawberry");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(milkId)), "list does not contain milk");
        assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(cheeseId)), "list does not contain cheese");

        assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)), "list does not contain pineapple");
        assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)), "list does not contain strawberry");
        assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(milkId)), "list does not contain milk");
        assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(cheeseId)), "list does not contain cheese");
    }

    @Test
    public void searchByMultipleParametersSuccess() {
        Response<List<ServiceItem>> topRatingItems = bridge.searchItem(null, null, null, null, 5, 5, null, null, null);
        Response<List<ServiceItem>> fruits = bridge.searchItem(null, "fruits", 5.0, 25.0, 4, 3, null, null, null);
        Response<List<ServiceItem>> cheapCars = bridge.searchItem(null, "cars", null, 120000.0, null, null, null, null, carStoreId);

        assertFalse(topRatingItems.isError(), String.format("bridge.searchItem(null, null, null, null, 5, 5, null, null, null) => %s", topRatingItems.getMessage()));
        assertFalse(fruits.isError(), String.format("bridge.searchItem(null, \"fruits\", 5.0, 25.0, 4, 3, null, null, null) => %s", fruits.getMessage()));
        assertFalse(cheapCars.isError(), String.format("bridge.searchItem(null, \"cars\", null, 120000.0, null, null, null, null, carStoreId) => %s", cheapCars.getMessage()));

        assertNotNull(topRatingItems.getValue(), "bridge.searchItem(null, null, null, null, 5, 5, null, null, null) failed");
        assertNotNull(fruits.getValue(), "bridge.searchItem(null, \"fruits\", 5.0, 25.0, 4, 3, null, null, null) failed");
        assertNotNull(cheapCars.getValue(), "bridge.searchItem(null, \"cars\", null, 120000.0, null, null, null, null, carStoreId) failed");

        assertEquals(1, topRatingItems.getValue().size(), "list size is not 1");
        assertEquals(1, fruits.getValue().size(), "list size is not 1");
        assertEquals(2, cheapCars.getValue().size(), "list size is not 2");

        assertTrue(topRatingItems.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)), "list does not contain pineapple");

        assertTrue(fruits.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)), "list does not contain pineapple");

        assertTrue(cheapCars.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)), "list does not contain toyota corolla");
        assertTrue(cheapCars.getValue().stream().anyMatch(item -> item.getId().equals(toyotaYarisId)), "list does not contain toyota yaris");
    }

    @Test
    public void searchNotExistingStoreFail() {
        Response<List<ServiceItem>> fail = bridge.searchItem(null, "cars", null, 120000.0, null, null, null, null, UUID.randomUUID());

        assertTrue(fail.isError(), "bridge.searchItem(null, \"cars\", null, 120000.0, null, null, null, null, UUID.randomUUID()) should have failed");
        assertEquals("Store does not exist", fail.getMessage(), fail.getMessage());
    }

    @Test
    public void searchClosedStoreFail() {
        bridge.closeStore(storeFounderId, carStoreId);
        Response<List<ServiceItem>> fail = bridge.searchItem(null, "cars", null, 120000.0, null, null, null, null, carStoreId);

        assertTrue(fail.isError(), "bridge.searchItem(null, \"cars\", null, 120000.0, null, null, null, null, carStoreId) should have failed");
        assertEquals("Store is not open", fail.getMessage(), fail.getMessage());
    }
}