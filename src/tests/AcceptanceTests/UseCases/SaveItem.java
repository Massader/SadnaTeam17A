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
public class SaveItem extends ProjectTest {

    UUID founder;
    UUID client;
    UUID client2;
    ServiceStore store;
    UUID storeId;
    UUID itemId;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        ServiceItem item = bridge.stockManagementAddNewItem(founder, "bannana",5,storeId,100,"yellow fruit");
        itemId = item.getId();

        client2 = bridge.createClient();

    }

    @BeforeEach
    public void beforeEach()  {
        client = bridge.createClient();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(client);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
        bridge.closeClient(client2);
    }

    @Test
    public void SaveItemrSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        ServiceItem item = bridge.stockManagementAddNewItem(founder, "bannana",5,storeId,100,"yellow fruit");
        itemId = item.getId();

        client2 = bridge.createClient();
        Boolean save = bridge.saveItemInShoppingCart(founder,itemId,4,storeId);
        Assert.assertTrue(save);
    }

    @Test
    public void SaveItemrFail() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        ServiceItem item = bridge.stockManagementAddNewItem(founder, "bannana",5,storeId,100,"yellow fruit");
        itemId = item.getId();

        client2 = bridge.createClient();
        UUID notItem = UUID.randomUUID();
        Boolean save = bridge.saveItemInShoppingCart(founder,notItem,4,storeId);
        Assert.assertFalse(save);
    }
}
