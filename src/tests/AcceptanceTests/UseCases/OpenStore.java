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
public class OpenStore extends ProjectTest {

    UUID founder;
    UUID client;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = null;
        storeId = null;
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
