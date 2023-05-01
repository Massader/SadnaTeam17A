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
public class GetStoreSaleHistorySystemAdmin extends ProjectTest {

    UUID founder;
    UUID client;
    UUID client2;
    ServiceStore store;
    UUID storeId;
    String userName;
    String password;
    UUID admin;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
//        bridge.register("founder", "Pass1");
//        client = bridge.enterSystem();
//        founder = bridge.login(client, "founder", "Pass1");
//        store = bridge.openStore(founder, "test", "test");
//        storeId = store.getStoreId();
//        userName= "adminUser";
//        password = "Pass2";
//        bridge.register(userName,password);
//        client2 = bridge.enterSystem();
//        admin= bridge.login(client2, userName, password);
    }

    @BeforeEach
    public void beforeEach()  {
        client = bridge.createClient().getValue();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(client);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
        bridge.logout(admin);
        bridge.closeClient(client);
        bridge.closeClient(client2);
    }
    @Test
    public void GetStoreSaleHistorySuccess() {
        //Tests whether the system admin can successfully retrieve the sale history of a store.
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        UUID clientCredentials = bridge.createClient().getValue();
        UUID adminCredentials = bridge.login(clientCredentials, "admin", "Admin1").getValue().getId();
        List<ServiceSale> saleHistory = bridge.getStoreSaleHistorySystemAdmin(adminCredentials,storeId).getValue();
        Assert.assertTrue(saleHistory.isEmpty());
    }
    @Test
    public void GetStoreSaleHistoryNotExistingStoreFail() {
        // Tests whether the system admin fails to retrieve the sale history of a store that does not exist
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        UUID clientCredentials = bridge.createClient().getValue();
        UUID adminCredentials = bridge.login(clientCredentials, "admin", "Admin1").getValue().getId();
        List<ServiceSale> saleHistory = bridge.getStoreSaleHistorySystemAdmin(client,storeId).getValue();
        Assert.assertNull(saleHistory);
    }
}

