package AcceptanceTests.UseCases;
import AcceptanceTests.*;
import ServiceLayer.ServiceObjects.ServiceSale;
import ServiceLayer.ServiceObjects.ServiceStore;
import org.junit.*;

import java.util.List;
import java.util.UUID;

public class GetStoreSaleHistorySystemAdmin extends ProjectTest {

        UUID founder;
        UUID client;
    UUID client2;
        ServiceStore store;
        UUID storeId;
        String userName;
        String password;
        UUID admin;

        @BeforeClass
        public void setUp() {
            bridge.setReal();
            bridge.register("founder", "pass");
            client = bridge.enterSystem();
            founder = bridge.login(client, "founder", "pass");
            store = bridge.openStore(founder, "test", "test");
            storeId = store.getStoreId();

            userName= "adminUser";
            password = "pass";


            bridge.register(userName,password);
            client2 = bridge.enterSystem();
            admin= bridge.login(client2, userName, password);


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
            bridge.logout(admin);
            bridge.exitSystem(client);
            bridge.exitSystem(client2);
        }
        @Test
        public void GetStoreSaleHistorySuccess() {
            List<ServiceSale> saleHistory = bridge.getStoreSaleHistorySystemAdmin(admin,storeId,userName,password);
            Assert.assertTrue(saleHistory.isEmpty());}
        @Test
        public void GetStoreSaleHistoryNotExistingStoreFail() {
            List<ServiceSale> saleHistory = bridge.getStoreSaleHistorySystemAdmin(founder,storeId,userName,password);
            Assert.assertNull(saleHistory);
        }



    }

