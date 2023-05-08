package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import DomainLayer.Market.Users.Client;
import ServiceLayer.Service;
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
public class SaveItem extends ProjectTest {

    UUID founder;
    UUID client;
    UUID client2;
    ServiceStore store;
    UUID storeId;
    UUID itemId;

    @BeforeAll
    public void beforeClass() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        ServiceItem item = bridge.addItemToStore(founder, "bannana",5,storeId,100,"yellow fruit").getValue();
        itemId = item.getId();

        client2 = bridge.createClient().getValue();

    }

    @BeforeEach
    public void setUp()  {
        client = bridge.createClient().getValue();
    }

    @AfterEach
    public void tearDown() {
        bridge.closeClient(client);
    }

    @AfterAll
    public void afterClass() {
        bridge.closeStore(founder, storeId);
        bridge.logout(founder);
        bridge.closeClient(client2);
    }

    @Test
    //Tests that a client can successfully add an item to their cart.
    public void SaveItemrSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        ServiceItem item = bridge.addItemToStore(founder, "bannana",5,storeId,100,"yellow fruit").getValue();
        itemId = item.getId();

        client2 = bridge.createClient().getValue();
        Boolean save = bridge.addItemToCart(founder,itemId,4,storeId).getValue();
        Assert.assertTrue(save);
    }

    @Test
    // Tests that a client cannot add a non-existing item to their cart.
    public void SaveItemrFail() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        ServiceItem item = bridge.addItemToStore(founder, "bannana",5,storeId,100,"yellow fruit").getValue();
        itemId = item.getId();

        client2 = bridge.createClient().getValue();
        UUID notItem = UUID.randomUUID();
        Boolean save = bridge.addItemToCart(founder,notItem,4,storeId).getValue();
        Assert.assertFalse(save);
    }

    @Test
    //Tests that two clients can simultaneously add an item to their cart without any synchronization issues.
    public void addToCartSync(){
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        ServiceItem item = bridge.addItemToStore(founder, "bannana",5,storeId,1,"yellow fruit").getValue();
        itemId = item.getId();
        ServiceItem itemcheck = bridge.getItemInformation(storeId, itemId).getValue();
        UUID clientId1 = bridge.createClient().getValue();
        UUID clientId2 = bridge.createClient().getValue();

        Thread thread1 = new Thread(()->{
            bridge.addItemToCart(clientId1, itemId, 1, storeId);
        });
        Thread thread2 = new Thread(()->{
            bridge.addItemToCart(clientId2, itemId, 1, storeId);
        });

        thread1.start();
        thread2.start();
    try{
        thread1.join();
        thread2.join();
    }catch(Exception ignored){

    }
        List<ServiceShoppingBasket> cart1 = bridge.getCart(clientId1).getValue();
        List<ServiceShoppingBasket> cart2 = bridge.getCart(clientId2).getValue();
    int i =0;
    boolean cohi1 = false;
    for (ServiceShoppingBasket s : cart1){
        if (s.getStoreId() == storeId && s.getItems().containsKey(itemId)){
            cohi1= true;
            break;
        }
    }

    boolean cohi2 = false;
    for (ServiceShoppingBasket s : cart2){
        if (s.getStoreId() == storeId && s.getItems().containsKey(itemId)){
            cohi2 = true;
        }
    }
    Assert.assertTrue(cohi1 ^ cohi2);

    }
}
