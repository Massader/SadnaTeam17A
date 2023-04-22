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
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("toManager", "Pass2");
        client2 = bridge.createClient();
        storeManager = bridge.login(client2, "toManager", "Pass2");
    }

    @BeforeEach
    public void beforeEach()  {
        client = bridge.createClient();
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
    public void AppointStoreManagerSuccess() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("toManager", "Pass2");
        client2 = bridge.createClient();
        storeManager = bridge.login(client2, "toManager", "Pass2");
    Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId);
    Assert.assertTrue(AppointStoreManager);
    }
    @Test
    public void AppointStoreManagerFail() {
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("toManager", "Pass2");
        client2 = bridge.createClient();
        storeManager = bridge.login(client2, "toManager", "Pass2");
        Boolean AppointStoreManager = bridge.appointStoreManager(storeManager,founder,storeId);
        Assert.assertFalse(AppointStoreManager);
    }


    @Test
    public void AppointStoreManagerBy2ManagerParallel(){
        bridge.register("founder", "Pass1");
        client = bridge.createClient();
        founder = bridge.login(client, "founder", "Pass1");
        store = bridge.createStore(founder, "test", "test");
        storeId = store.getStoreId();
        bridge.register("toManager", "Pass2");
        client2 = bridge.createClient();
        storeManager = bridge.login(client2, "toManager", "Pass2");
        Boolean AppointStoreManager = bridge.appointStoreManager(founder,storeManager,storeId);
        Assert.assertTrue(AppointStoreManager);
        UUID client3 = bridge.createClient();
        bridge.register("toBeManager", "Pass2");
        UUID newStoreManager = bridge.login(client3, "toBeManager", "Pass2");
        List<ServiceUser> staffList = bridge.getStoreStaffList(founder, storeId);
        int stafSize= staffList.size();

        Thread thread1 = new Thread(()->{
            Boolean AppointStoreManager1 = bridge.appointStoreManager(founder,newStoreManager,storeId);
        });
        Thread thread2 = new Thread(()->{
            Boolean AppointStoreManager2 = bridge.appointStoreManager(storeManager,newStoreManager,storeId);
        });

        thread1.start();
        thread2.start();
        try{
            thread1.join();
            thread2.join();
        }catch(Exception ignored){
        }

        Assert.assertTrue(bridge.getStoreStaffList(founder, storeId).size()==stafSize+1);
    }

}

