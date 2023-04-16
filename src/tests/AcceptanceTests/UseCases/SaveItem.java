package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.ServiceItem;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.*;

import java.util.UUID;

public class SaveItem extends ProjectTest {

    UUID founder;
    UUID client;
    UUID client2;
    ServiceStore store;
    UUID storeId;
    UUID itemId;

    @BeforeClass
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "pass");
        client = bridge.enterSystem();
        founder = bridge.login(client, "founder", "pass");
        store = bridge.openStore(founder, "test", "test");
        storeId = store.getStoreId();
        ServiceItem item = bridge.stockManagementAddNewItem(founder, "bannana",5,storeId,100,"yellow fruit");
        itemId = item.getId();

        client2 = bridge.enterSystem();

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
        bridge.exitSystem(client2);
    }

    @Test
    public void SaveItemrSuccess() {
        Boolean save = bridge.saveItemInShoppingCart(founder,itemId,4,storeId);
        Assert.assertTrue(save);
    }

    @Test
    public void SaveItemrFail() {
        UUID notItem = UUID.randomUUID();
        Boolean save = bridge.saveItemInShoppingCart(founder,notItem,4,storeId);
        Assert.assertFalse(save);
    }



}
