package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.*;

import java.util.List;
import java.util.UUID;
import org.junit.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetStoreSaleHistory extends ProjectTest {

    UUID founder;
    UUID client;
    UUID client2;
    ServiceStore store;
    UUID storeId;
    UUID userId;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        String userName= "user1";
        String password = "Pass2";
        bridge.register(userName,password);
        client2 = bridge.createClient();
        userId= bridge.login(client2, userName, password);
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
        bridge.logout(userId);
        bridge.closeClient(client);
        bridge.closeClient(client2);
    }
    @Test
    // tests if the system can successfully retrieve the sale history of a store
    public void GetStoreSaleHistorySuccess() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        String userName= "user1";
        String password = "Pass2";
        bridge.register(userName,password);
        client2 = bridge.createClient();
        userId= bridge.login(client2, userName, password);
        List<ServiceSale> saleHistory = bridge.getStoreSaleHistory(founder,storeId);
        Assert.assertTrue(saleHistory.isEmpty());}
    @Test
    //tests if the system fails to retrieve the sale history of a non-existing store.
    public void GetStoreSaleHistoryNotExistingStoreFail() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        String userName= "user1";
        String password = "Pass2";
        bridge.register(userName,password);
        client2 = bridge.createClient();
        userId= bridge.login(client2, userName, password);
        List<ServiceSale> saleHistory = bridge.getStoreSaleHistory(client2,storeId);
        Assert.assertNull(saleHistory);
    }
}
