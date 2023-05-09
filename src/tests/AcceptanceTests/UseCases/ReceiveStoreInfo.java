package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.Response;
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
public class ReceiveStoreInfo extends ProjectTest {

    UUID storeFounderId;
    ServiceStore store;
    UUID storeId;
    UUID item1Id;
    UUID item2Id;
    UUID item3Id;
    UUID item4Id;

    @BeforeAll
    public void beforeClass() {
        bridge.register("founder", "1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "1234").getValue().getId();

        store = bridge.createStore(storeFounderId, "store", "test").getValue();
        storeId = store.getStoreId();

        item1Id = bridge.addItemToStore(storeFounderId, "item1", 10, storeId, 100, "test").getValue().getId();
        item2Id = bridge.addItemToStore(storeFounderId, "item2", 20, storeId, 100, "test").getValue().getId();
        item3Id = bridge.addItemToStore(storeFounderId, "item3", 30, storeId, 100, "test").getValue().getId();
        item4Id = bridge.addItemToStore(storeFounderId, "item4", 40, storeId, 100, "test").getValue().getId();

        bridge.logout(storeFounderId);
    }

    @BeforeEach
    public void setUp()  {
        bridge.login(bridge.createClient().getValue(), "founder", "1234");
    }

    @AfterEach
    public void tearDown() {
        bridge.logout(storeFounderId);
    }

    @AfterAll
    public void afterClass() {
        bridge.resetService();
    }

    @Test
    public void getStoreInfoSuccess() {
        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeFounderId, storeId);

        Assert.assertFalse(storeInfo.isError());
        Assert.assertNotNull(storeInfo.getValue());
        Assert.assertEquals(storeId, storeInfo.getValue().getStoreId());
        Assert.assertEquals("test", storeInfo.getValue().getName());
    }

    @Test
    public void getStoreInfoNotExistFail() {
        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeFounderId, UUID.randomUUID());

        Assert.assertTrue(storeInfo.isError());
        Assert.assertEquals("Store does not exist.", storeInfo.getMessage());
    }

    @Test
    public void getStoreInfoClosedStoreFail() {
        bridge.closeStore(storeFounderId, storeId);

        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeFounderId, storeId);

        Assert.assertTrue(storeInfo.isError());
        Assert.assertEquals("Store is closed.", storeInfo.getMessage());
    }
}
