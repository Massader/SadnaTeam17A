package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.*;

import java.util.UUID;

public class AppointStoreManager extends ProjectTest {

    UUID founder;
    UUID client;
    UUID storeManager;
    UUID client2;
    ServiceStore store;
    UUID storeId;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("lior", "pass");
        client2 = bridge.enterSystem();
        storeManager = bridge.login(client2, "toreManager", "pass");

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
        bridge.logout(storeManager);
    }

    @Test
    public void AppointStoreManagerSuccess() {
    Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId);
    Assert.assertTrue(AppointStoreManager);
    }
    @Test
    public void AppointStoreManagerFail() {
        Boolean AppointStoreManager = bridge.appointStoreManager(storeManager,founder,storeId);
        Assert.assertFalse(AppointStoreManager);
    }
    }

