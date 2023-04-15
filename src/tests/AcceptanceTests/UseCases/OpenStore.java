package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.*;

import java.util.UUID;

public class OpenStore extends ProjectTest {
    UUID founder;
    UUID client;
    ServiceStore store;
    UUID storeId;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = null;
        storeId = null;
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
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
    }

    @Test
    public void openStoreSuccess() {
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        Assert.assertNotNull(store);
    }

    @Test
    public void openStoreNotLoggedInFail() {
        bridge.logout(founder);
        ServiceStore storeFail = bridge.openStore(founder, "fail", "fail");
        Assert.assertNull(storeFail);
    }
}
