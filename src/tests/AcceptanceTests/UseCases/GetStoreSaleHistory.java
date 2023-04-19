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
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        String userName= "user1";
        String password = "pass";
        bridge.register(userName,password);
        client2 = bridge.enterSystem();
        userId= bridge.login(client2, userName, password);
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
        bridge.logout(userId);
        bridge.exitSystem(client);
        bridge.exitSystem(client2);
    }
    @Test
    public void GetStoreSaleHistorySuccess() {
        List<ServiceSale> saleHistory = bridge.getStoreSaleHistory(founder,storeId);
        Assert.assertTrue(saleHistory.isEmpty());}
    @Test
    public void GetStoreSaleHistoryNotExistingStoreFail() {
        List<ServiceSale> saleHistory = bridge.getStoreSaleHistory(client2,storeId);
        Assert.assertNull(saleHistory);
    }
}
