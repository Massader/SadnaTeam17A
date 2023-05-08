package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StockManagement extends ProjectTest {
    UUID founder;
    UUID client;
    UUID client2;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void beforeClass() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        client2 = bridge.createClient().getValue();
    }

    @BeforeEach
    public void setUp()  {
        client = bridge.createClient().getValue();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(client);
        bridge.closeClient(client2);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
        bridge.closeClient(client);
        bridge.closeClient(client2);
    }

    @Test
    // Test successful stock management operations including adding, changing, and removing item from a store.
    public void StockManagementSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        client2 = bridge.createClient().getValue();
        String name ="bannana";
        int quantity = 100;
        int price =5;
        String description = "yellow fruit";
        ServiceItem item = bridge.addItemToStore(founder, name,price,storeId,quantity,description).getValue();
        Assert.assertEquals(item.getName(),name);
        Assert.assertEquals(item.getDescription(),description);
        Assert.assertEquals(item.getQuantity(), quantity);
        UUID itemId = item.getId();
        String newName = "apple";
        String newDescription = "green fruit";
        Boolean changeItemInfo = bridge.stockManagementChangeItemInfo(founder,storeId,itemId,newName,newDescription).getValue();
        Assert.assertTrue(changeItemInfo);
        ServiceItem updateItem = bridge.getItemInformation(storeId,item.getId()).getValue();
        Assert.assertEquals(updateItem.getName(),newName);
        Boolean removeItem = bridge.setItemQuantity(founder,storeId,itemId).getValue();
        Assert.assertTrue(removeItem);
        ServiceItem updateItem1 = bridge.getItemInformation(storeId,item.getId()).getValue();
        Assert.assertEquals(0, updateItem1.getQuantity());
    }

    @Test
    // Test unsuccessful stock management operations including adding an item to a store by a non-founder client.
    public void StockManagementFail() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        client2 = bridge.createClient().getValue();

        String name ="pineapple";
        int quantity = 10;
        int price =5;
        String description = "yellow fruit";
        ServiceItem item = bridge.addItemToStore(client2, name,price,storeId,quantity,description).getValue();
        Assert.assertNull(item);

    }
}
