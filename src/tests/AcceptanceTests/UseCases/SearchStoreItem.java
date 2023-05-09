package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.Response;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

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
        bridge.register("founder", "1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "1234").getValue().getId();

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
        bridge.login(bridge.createClient().getValue(), "founder", "1234");
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

        Assert.assertFalse(all.isError());

        Assert.assertNotNull(all.getValue());

        Assert.assertEquals(12, all.getValue().size());

        Assert.assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(lemonId)));
        Assert.assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(appleId)));
        Assert.assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(tomatoId)));
        Assert.assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(watermelonId)));
        Assert.assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(teslaId)));
        Assert.assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)));
        Assert.assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaHybridId)));
        Assert.assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(toyotaYarisId)));
        Assert.assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)));
        Assert.assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)));
        Assert.assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(milkId)));
        Assert.assertTrue(all.getValue().stream().anyMatch(item -> item.getId().equals(cheeseId)));
    }
    @Test
    public void searchOnlyByNameSuccess() {
        Response<List<ServiceItem>> lemon = bridge.searchItem("lemon", null, null, null, null, null, null, null, null);
        Response<List<ServiceItem>> toyota = bridge.searchItem("toyota", null, null, null, null, null, null, null, null);
        Response<List<ServiceItem>> apple = bridge.searchItem("apple", null, null, null, null, null, null, null, null);

        Assert.assertFalse(lemon.isError());
        Assert.assertFalse(toyota.isError());
        Assert.assertFalse(apple.isError());

        Assert.assertNotNull(lemon.getValue());
        Assert.assertNotNull(toyota.getValue());
        Assert.assertNotNull(apple.getValue());

        Assert.assertEquals(1, lemon.getValue().size());
        Assert.assertEquals(3, toyota.getValue().size());
        Assert.assertEquals(2, apple.getValue().size());

        Assert.assertTrue(lemon.getValue().stream().anyMatch(item -> item.getId().equals(lemonId)));
        Assert.assertTrue(toyota.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)));
        Assert.assertTrue(toyota.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaHybridId)));
        Assert.assertTrue(toyota.getValue().stream().anyMatch(item -> item.getId().equals(toyotaYarisId)));
        Assert.assertTrue(apple.getValue().stream().anyMatch(item -> item.getId().equals(appleId)));
        Assert.assertTrue(apple.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)));
    }

    @Test
    public void searchOnlyByCategorySuccess() {
        Response<List<ServiceItem>> fruits = bridge.searchItem(null, "fruits", null, null, null, null, null, null, null);
        Response<List<ServiceItem>> cars = bridge.searchItem(null, "cars", null, null, null, null, null, null, null);
        Response<List<ServiceItem>> dairy = bridge.searchItem(null, "dairy", null, null, null, null, null, null, null);

        Assert.assertFalse(fruits.isError());
        Assert.assertFalse(cars.isError());
        Assert.assertFalse(dairy.isError());

        Assert.assertNotNull(fruits.getValue());
        Assert.assertNotNull(cars.getValue());
        Assert.assertNotNull(dairy.getValue());

        Assert.assertEquals(5, fruits.getValue().size());
        Assert.assertEquals(4, cars.getValue().size());
        Assert.assertEquals(2, dairy.getValue().size());

        Assert.assertTrue(fruits.getValue().stream().anyMatch(item -> item.getId().equals(lemonId)));
        Assert.assertTrue(fruits.getValue().stream().anyMatch(item -> item.getId().equals(appleId)));
        Assert.assertTrue(fruits.getValue().stream().anyMatch(item -> item.getId().equals(watermelonId)));
        Assert.assertTrue(fruits.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)));
        Assert.assertTrue(fruits.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)));
        Assert.assertTrue(cars.getValue().stream().anyMatch(item -> item.getId().equals(teslaId)));
        Assert.assertTrue(cars.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)));
        Assert.assertTrue(cars.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaHybridId)));
        Assert.assertTrue(cars.getValue().stream().anyMatch(item -> item.getId().equals(toyotaYarisId)));
        Assert.assertTrue(dairy.getValue().stream().anyMatch(item -> item.getId().equals(milkId)));
        Assert.assertTrue(dairy.getValue().stream().anyMatch(item -> item.getId().equals(cheeseId)));

        Assert.assertTrue(fruits.getValue().stream().allMatch(item -> item.getCategories().contains("fruits")));
        Assert.assertTrue(cars.getValue().stream().allMatch(item -> item.getCategories().contains("cars")));
        Assert.assertTrue(dairy.getValue().stream().allMatch(item -> item.getCategories().contains("dairy")));
    }

    @Test
    public void searchOnlyByPriceRangeSuccess() {
        Response<List<ServiceItem>> max10 = bridge.searchItem(null, null, null, 10.0, null, null, null, null, null);
        Response<List<ServiceItem>> min10 = bridge.searchItem(null, null, 10.0, null, null, null, null, null, null);
        Response<List<ServiceItem>> min10max100 = bridge.searchItem(null, null, 10.0, 100.0, null, null, null, null, null);

        Assert.assertFalse(max10.isError());
        Assert.assertFalse(min10.isError());
        Assert.assertFalse(min10max100.isError());

        Assert.assertNotNull(max10.getValue());
        Assert.assertNotNull(min10.getValue());
        Assert.assertNotNull(min10max100.getValue());

        Assert.assertEquals(4, max10.getValue().size());
        Assert.assertEquals(9, min10.getValue().size());
        Assert.assertEquals(5, min10max100.getValue().size());

        Assert.assertTrue(max10.getValue().stream().anyMatch(item -> item.getId().equals(lemonId)));
        Assert.assertTrue(max10.getValue().stream().anyMatch(item -> item.getId().equals(appleId)));
        Assert.assertTrue(max10.getValue().stream().anyMatch(item -> item.getId().equals(tomatoId)));
        Assert.assertTrue(max10.getValue().stream().anyMatch(item -> item.getId().equals(milkId)));

        Assert.assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(appleId)));
        Assert.assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(watermelonId)));
        Assert.assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(teslaId)));
        Assert.assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)));
        Assert.assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaHybridId)));
        Assert.assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(toyotaYarisId)));
        Assert.assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)));
        Assert.assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)));
        Assert.assertTrue(min10.getValue().stream().anyMatch(item -> item.getId().equals(cheeseId)));

        Assert.assertTrue(min10max100.getValue().stream().anyMatch(item -> item.getId().equals(appleId)));
        Assert.assertTrue(min10max100.getValue().stream().anyMatch(item -> item.getId().equals(watermelonId)));
        Assert.assertTrue(min10max100.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)));
        Assert.assertTrue(min10max100.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)));
        Assert.assertTrue(min10max100.getValue().stream().anyMatch(item -> item.getId().equals(cheeseId)));

        Assert.assertTrue(max10.getValue().stream().allMatch(item -> item.getPrice() <= 10));
        Assert.assertTrue(min10.getValue().stream().allMatch(item -> item.getPrice() >= 10));
        Assert.assertTrue(min10max100.getValue().stream().allMatch(item -> item.getPrice() >= 10 && item.getPrice() <= 100));
    }

    @Test
    public void searchOnlyByItemRatingSuccess() {
        Response<List<ServiceItem>> min3 = bridge.searchItem(null, null, null, null, 3, null, null, null, null);
        Response<List<ServiceItem>> min5 = bridge.searchItem(null, null, null, null, 5, null, null, null, null);

        Assert.assertFalse(min3.isError());
        Assert.assertFalse(min5.isError());

        Assert.assertNotNull(min3.getValue());
        Assert.assertNotNull(min5.getValue());

        Assert.assertEquals(9, min3.getValue().size());
        Assert.assertEquals(3, min5.getValue().size());

        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(lemonId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(appleId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(tomatoId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(teslaId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaHybridId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(milkId)));

        Assert.assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(lemonId)));
        Assert.assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(teslaId)));
        Assert.assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)));

        Assert.assertTrue(min3.getValue().stream().allMatch(item -> item.getRating() >= 3));
        Assert.assertTrue(min5.getValue().stream().allMatch(item -> item.getRating() >= 5));
    }

    @Test
    public void searchOnlyByStoreRatingSuccess() {
        Response<List<ServiceItem>> min3 = bridge.searchItem(null, null, null, null, null, 3, null, null, null);
        Response<List<ServiceItem>> min5 = bridge.searchItem(null, null, null, null, null, 5, null, null, null);

        Assert.assertFalse(min3.isError());
        Assert.assertFalse(min5.isError());

        Assert.assertNotNull(min3.getValue());
        Assert.assertNotNull(min5.getValue());

        Assert.assertEquals(8, min3.getValue().size());
        Assert.assertEquals(4, min5.getValue().size());

        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(teslaId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaHybridId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(toyotaYarisId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(milkId)));
        Assert.assertTrue(min3.getValue().stream().anyMatch(item -> item.getId().equals(cheeseId)));

        Assert.assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)));
        Assert.assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(strawberryId)));
        Assert.assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(milkId)));
        Assert.assertTrue(min5.getValue().stream().anyMatch(item -> item.getId().equals(cheeseId)));
    }

    @Test
    public void searchByMultipleParametersSuccess() {
        Response<List<ServiceItem>> topRatingItems = bridge.searchItem(null, null, null, null, 5, 5, null, null, null);
        Response<List<ServiceItem>> fruits = bridge.searchItem(null, "fruits", 5.0, 25.0, 4, 3, null, null, null);
        Response<List<ServiceItem>> cheapCars = bridge.searchItem(null, "cars", null, 120000.0, null, null, null, null, carStoreId);

        Assert.assertFalse(topRatingItems.isError());
        Assert.assertFalse(fruits.isError());
        Assert.assertFalse(cheapCars.isError());

        Assert.assertNotNull(topRatingItems.getValue());
        Assert.assertNotNull(fruits.getValue());
        Assert.assertNotNull(cheapCars.getValue());

        Assert.assertEquals(1, topRatingItems.getValue().size());
        Assert.assertEquals(1, fruits.getValue().size());
        Assert.assertEquals(2, cheapCars.getValue().size());

        Assert.assertTrue(topRatingItems.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)));

        Assert.assertTrue(fruits.getValue().stream().anyMatch(item -> item.getId().equals(pineappleId)));

        Assert.assertTrue(cheapCars.getValue().stream().anyMatch(item -> item.getId().equals(toyotaCorollaId)));
        Assert.assertTrue(cheapCars.getValue().stream().anyMatch(item -> item.getId().equals(toyotaYarisId)));
    }

    @Test
    public void searchNotExistingStoreFail() {
        Response<List<ServiceItem>> fail = bridge.searchItem(null, "cars", null, 120000.0, null, null, null, null, UUID.randomUUID());

        Assert.assertTrue(fail.isError());
        Assert.assertEquals("Store does not exist", fail.getMessage());
    }

    @Test
    public void searchClosedStoreFail() {
        bridge.closeStore(storeFounderId, carStoreId);
        Response<List<ServiceItem>> fail = bridge.searchItem(null, "cars", null, 120000.0, null, null, null, null, carStoreId);

        Assert.assertTrue(fail.isError());
        Assert.assertEquals("Store is not open", fail.getMessage());
    }
}