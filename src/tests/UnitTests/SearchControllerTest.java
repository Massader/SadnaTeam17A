package UnitTests;

import APILayer.Main;
import DataAccessLayer.RepositoryFactory;
import DomainLayer.Market.Notification;
import DomainLayer.Market.SearchController;
import DomainLayer.Market.StoreController;
import DomainLayer.Market.Stores.Category;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.UserController;
import DomainLayer.Market.Users.User;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceUser;
import ServiceLayer.StateFileRunner.StateFileRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;

import java.util.*;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchControllerTest {
    
    static Service service;
    
    private SearchController searchController;
    private StoreController storeController;

    private Store store1;
    private Store store2;
    private Store store3;

    private Item item1;
    private Item item2;
    private Item item3;
    private Item item4;

    private Category category1;
    private Category category2;
    private Category category3;
    
    @BeforeAll
    static void beforeAll() {
        SpringApplication.run(Main.class);
        service = Service.getInstance();
        service.init(UserController.repositoryFactory, new StateFileRunner(new ObjectMapper(), service));
    }
    
    @BeforeEach
    public void setUp() {
        searchController = SearchController.getInstance();
        storeController = StoreController.getInstance();
        storeController.init();
        service.register("user1", "User1");
        ServiceUser user = service.login(service.createClient().getValue(), "user1", "User1", new BiConsumer<UUID, Notification>() {
            @Override
            public void accept(UUID uuid, Notification notification) {
        
            }
        }).getValue();
        
        category1 = new Category("c1");
        category2 = new Category("c2");
        category3 = new Category("c3");

        
        store1 = storeController.createStore(user.getId(), "store1", "").getValue();
        store2 = storeController.createStore(user.getId(), "store2", "").getValue();
        store3 = storeController.createStore(user.getId(), "store3", "").getValue();
        
        item1 = storeController.addItemToStore(user.getId(), "Item1", 1, store1.getStoreId(), 5, "a").getValue();
        item2 = storeController.addItemToStore(user.getId(), "Item2", 10, store2.getStoreId(), 1, "b").getValue();
        item3 = storeController.addItemToStore(user.getId(), "Item3", 100, store3.getStoreId(), 5, "c").getValue();
        item4 = storeController.addItemToStore(user.getId(), "Item4", 9000, store1.getStoreId(),1000, "d").getValue();
        storeController.addItemCategory(user.getId(), store1.getStoreId(), item1.getId(), category1.getCategoryName());
        storeController.addItemCategory(user.getId(), store1.getStoreId(), item1.getId(), category2.getCategoryName());
        storeController.addItemCategory(user.getId(), store2.getStoreId(), item2.getId(), category1.getCategoryName());
        storeController.addItemCategory(user.getId(), store2.getStoreId(), item2.getId(), category2.getCategoryName());
        storeController.addItemCategory(user.getId(), store3.getStoreId(), item3.getId(), category1.getCategoryName());
        storeController.addItemCategory(user.getId(), store1.getStoreId(), item4.getId(), category3.getCategoryName());
    }
    
    @AfterEach
    void tearDown() {
        try {
            RepositoryFactory repositoryFactory = UserController.repositoryFactory;
            repositoryFactory.roleRepository.deleteAll();
            repositoryFactory.itemRepository.deleteAll();
            repositoryFactory.passwordRepository.deleteAll();
            repositoryFactory.securityQuestionRepository.deleteAll();
            repositoryFactory.userRepository.deleteAll();
            repositoryFactory.storeRepository.deleteAll();
            service.resetService();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Test
    public void testSearch() {
        List<Item> expectedItems = new ArrayList<>();
        expectedItems.add(item1);
        
        List<Item> actualItems = searchController.searchItem("Item", category1.getCategoryName(), 0, 50, 0, 0).getValue();

        assertEquals(expectedItems.size(), actualItems.size());
        Comparator<Item> comparator = new Comparator<>() {

            @Override
            public int compare(Item o1, Item o2) {
                return o1.getId().equals(o2.getId()) ? 0 : -1;
            }
        };

        assertEquals(0, comparator.compare(expectedItems.get(0), (actualItems.get(0))));
    }
}
