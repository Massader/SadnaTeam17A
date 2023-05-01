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
public class AppointStoreManager extends ProjectTest {

    UUID founder;
    UUID client;
    UUID storeManager;
    UUID client2;
    ServiceStore store;
    UUID storeId;

    @BeforeAll
    public void setUp() {
        bridge.setReal();
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        bridge.register("toManager", "Pass2");
        client2 = bridge.createClient().getValue();
        storeManager = bridge.login(client2, "toManager", "Pass2").getValue().getId();
    }

    @BeforeEach
    public void beforeEach()  {
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
        bridge.logout(storeManager);
    }

    @Test
    //  Tests that a store manager can be successfully appointed to a store by a store founder.
    public void AppointStoreManagerSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        bridge.register("toManager", "Pass2");
        client2 = bridge.createClient().getValue();
        storeManager = bridge.login(client2, "toManager", "Pass2").getValue().getId();
    Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId).getValue();
    Assert.assertTrue(AppointStoreManager);
    }
    @Test
    //Tests that a store manager cannot be appointed to a store by someone other than the store founder.
    public void AppointStoreManagerFail() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        bridge.register("toManager", "Pass2");
        client2 = bridge.createClient().getValue();
        storeManager = bridge.login(client2, "toManager", "Pass2").getValue().getId();
        Boolean AppointStoreManager = bridge.appointStoreManager(storeManager,founder,storeId).getValue();
        Assert.assertFalse(AppointStoreManager);
    }


    @Test
    //Tests that two store managers cannot simultaneously appoint a third store manager to a store, and that only one appointment is successful.
    public void AppointStoreManagerBy2ManagerParallel(){
        bridge.register("founder", "Pass1");
        client = bridge.createClient().getValue();
        founder = bridge.login(client, "founder", "Pass1").getValue().getId();
        store = bridge.createStore(founder, "test", "test").getValue();
        storeId = store.getStoreId();
        bridge.register("toManager", "Pass2");
        client2 = bridge.createClient().getValue();
        storeManager = bridge.login(client2, "toManager", "Pass2").getValue().getId();
        Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId).getValue();
        Assert.assertTrue(AppointStoreManager);
        UUID client3 = bridge.createClient().getValue();
        bridge.register("toBeManager", "Pass2");
        UUID newStoreManager = bridge.login(client3, "toBeManager", "Pass2").getValue().getId();
        List<ServiceUser> staffList = bridge.getStoreStaffList(founder, storeId).getValue();
        int stafSize= staffList.size();

        Thread thread1 = new Thread(()->{
            Boolean AppointStoreManager1 = bridge.appointStoreManager(founder,newStoreManager,storeId).getValue();
        });
        Thread thread2 = new Thread(()->{
            Boolean AppointStoreManager2 = bridge.appointStoreManager(storeManager,newStoreManager,storeId).getValue();
        });

        thread1.start();
        thread2.start();
        try{
            thread1.join();
            thread2.join();
        }catch(Exception ignored){
        }

        Assert.assertEquals(bridge.getStoreStaffList(founder, storeId).getValue().size(), stafSize + 1);
    }

}

