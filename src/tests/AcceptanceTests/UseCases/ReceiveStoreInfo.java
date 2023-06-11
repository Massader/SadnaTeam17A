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
        bridge.register("founder", "Aa1234");

        storeFounderId = bridge.login(bridge.createClient().getValue(), "founder", "Aa1234").getValue().getId();

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
        bridge.login(bridge.createClient().getValue(), "founder", "Aa1234");
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

        assertFalse(storeInfo.isError(), String.format("bridge.getStoreInformation(storeFounderId, storeId) => %s", storeInfo.getMessage()));
        assertNotNull(storeInfo.getValue(), "bridge.getStoreInformation(storeFounderId, storeId) failed");
        assertEquals(storeId, storeInfo.getValue().getStoreId(), "bridge.getStoreInformation(storeFounderId, storeId) returned wrong UUID");
    }

    @Test
    public void getStoreInfoNotExistFail() {
        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeFounderId, UUID.randomUUID());

        assertTrue(storeInfo.isError(), "bridge.getStoreInformation(storeFounderId, UUID.randomUUID()) should have failed");
        assertEquals("Store does not exist.", storeInfo.getMessage(), storeInfo.getMessage());
    }

    @Test
    public void getStoreInfoClosedStoreFail() {
        bridge.closeStore(storeFounderId, storeId);

        Response<ServiceStore> storeInfo = bridge.getStoreInformation(storeFounderId, storeId);

        assertTrue(storeInfo.isError(), "bridge.getStoreInformation(storeFounderId, storeId) should have failed");
        assertEquals("Store is closed.", storeInfo.getMessage(), storeInfo.getMessage());
    }
}