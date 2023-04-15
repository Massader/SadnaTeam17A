package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.*;

import java.util.UUID;

public class ReceiveStoreInfo extends ProjectTest {

    UUID founder;
    UUID client;
    ServiceStore store;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = bridge.openStore(founder, "test", "test");
    }

    @Before
    public void beforeEach()  {
        client = bridge.enterSystem();
    }

    @After
    public void tearDown() {
        bridge.exitSystem(client);
    }

    @AfterClass
    public void afterClass() {
        bridge.closeStore(founder, store.getStoreId());
    }

    @Test
    public void receiveStoreInfoSuccess() {
        ServiceStore store2 = bridge.receiveStoreInfo(store.getStoreId());
        Assert.assertNotNull(store2);
        Assert.assertEquals(store.getStoreId(), store2.getStoreId());
        Assert.assertEquals(store.getName(), store2.getName());
        Assert.assertEquals(store.getDescription(), store2.getDescription());
    }

    @Test
    public void receiveStoreInfoNotExistingStoreFail() {
        ServiceStore store2 = bridge.receiveStoreInfo(UUID.randomUUID());
        Assert.assertNull(store2);
    }
}
