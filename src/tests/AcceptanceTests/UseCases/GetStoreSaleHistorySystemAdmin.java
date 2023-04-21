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
        client = bridge.enterSystem();
    }

    @AfterEach
    public void tearDown() {
        bridge.exitSystem(client);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
        bridge.logout(admin);
        bridge.exitSystem(client);
        bridge.exitSystem(client2);
    }
    @Test
    public void GetStoreSaleHistorySuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        userName= "adminUser123456456";
        password = "Pass2";
        bridge.register(userName,password);
        client2 = bridge.enterSystem();
        admin= bridge.login(client2, userName, password);
        List<ServiceSale> saleHistory = bridge.getStoreSaleHistorySystemAdmin(admin,storeId,userName,password);
        Assert.assertTrue(saleHistory.isEmpty());}
    @Test
    public void GetStoreSaleHistoryNotExistingStoreFail() {
        bridge.register("founder", "Pass1");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        userName= "adminUser123456456";
        password = "Pass2";
        bridge.register(userName,password);
        client2 = bridge.enterSystem();
        admin= bridge.login(client2, userName, password);
        List<ServiceSale> saleHistory = bridge.getStoreSaleHistorySystemAdmin(founder,storeId,userName,password);
        Assert.assertNull(saleHistory);
    }
}

