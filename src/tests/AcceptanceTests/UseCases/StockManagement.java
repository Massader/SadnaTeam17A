package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.ServiceItem;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.*;

import java.util.UUID;

public class StockManagement extends ProjectTest {
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
        bridge.exitSystem(client);
    }

    @Test
    public void SaveItemrSuccess() {
        String name ="bannana";
        int quantity = 100;
        int price =5;
        String description = "yellow fruit";
        ServiceItem item = bridge.stockManagementAddNewItem(founder, name,price,storeId,quantity,description);
        Assert.assertEquals(item.getName(),name);
        Assert.assertEquals(item.getDescription(),description);
        Assert.assertTrue(item.getQuantity()==quantity);
        UUID itemId = item.getId();
        String newName = "apple";
        String newDescription = "green fruit";
        Boolean changeItemInfo = bridge.stockManagementChangeItemInfo(founder,storeId,itemId,newName,newDescription);
        Assert.assertTrue(changeItemInfo);
        Assert.assertEquals(item.getName(),newName);
        Boolean removeItem = bridge.stockManagementRemoveItem(founder,storeId,itemId);
        Assert.assertTrue(removeItem);
        Assert.assertTrue(item.getQuantity()==0);


    }
}
//AddNewItem
//RemoveItem
//tChangeItemInfo