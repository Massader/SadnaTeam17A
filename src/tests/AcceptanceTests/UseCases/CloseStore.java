package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.*;

import java.util.UUID;

public class CloseStore extends ProjectTest {

    UUID founder;
    UUID client;
    UUID client2;
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
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
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
    public void CloseStoreSuccess() {
        Boolean close = bridge.closeStore(founder,storeId);
        Assert.assertTrue(close);
    }

    @Test
    public void CloseStoreFail() {
        client2 = bridge.enterSystem();
        Boolean close = bridge.closeStore(client2,storeId);
        Assert.assertFalse(close);
        bridge.logout(client2);

    }

}
