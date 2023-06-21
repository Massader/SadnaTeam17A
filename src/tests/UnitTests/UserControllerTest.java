package UnitTests;

import APILayer.Main;
import DataAccessLayer.RepositoryFactory;
import DomainLayer.Market.StoreController;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.UserController;
import DomainLayer.Market.Users.ShoppingCart;
import DomainLayer.Market.Users.User;
import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.StateFileRunner.StateFileRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private static Service service;
    private UserController userController;
    private String username;
    private String password;
    private String password2;
    private String password3;
    private StoreController storeController;
    private UUID userId;
    private UUID userId2;
    private UUID storeId;
    private UUID itemId;
    private User user;
    private User user2;
    
    
    @BeforeAll
    static void beforeAll() {
        SpringApplication.run(Main.class);
        service = Service.getInstance();
        service.init(UserController.repositoryFactory, new StateFileRunner(new ObjectMapper(), service));
    }
    
    @BeforeEach
    public void setUp() {
        userController = UserController.getInstance();
        username = "testuser";
        password = "Password123";
        password2 = "Pass12345";
        password3 = "Pass67890";
    
        storeController = StoreController.getInstance();
        userController.register("u", password2);
        userController.register("uu", password3);
        userId = userController.getId("u");
        user = userController.getUserById(userId);
        userId2 = userController.getId("uu");
        user2 = userController.getUserById(userId2);
        storeId = storeController.createStore(userId, "store", "des").getValue().getStoreId();
        Item item = storeController.addItemToStore(userId, "phone", 10, storeId, 100, "aaaaa").getValue();
        itemId = item.getId();
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
    public void testRegisterSuccess() {
        Response<Boolean> response = userController.register("uuu", password);
        assertFalse(response.isError());
    }

    @Test
    public void testRegisterFailureDuplicateUsername() {
        Response<Boolean> response1 = userController.register(username, password);
        assertEquals(true, response1.getValue());

        Response<Boolean> response2 = userController.register(username, "Newpassword2134");
        assertTrue(response2.isError());
        assertEquals("This username is already in use.", response2.getMessage());
    }

    @Test
    public void testRegisterFailureEmptyUsername() {
        Response<Boolean> response = userController.register("", password);
        assertTrue(response.isError());
        assertEquals("No username input.", response.getMessage());
    }

    @Test
    public void testCloseClientSuccess() {
        // create a new client and add them to the clients map
        Response<UUID> clientResponse = userController.createClient();

        // call the closeClient function
        Response<Boolean> response = userController.closeClient(clientResponse.getValue());

        // assert that the response is successful
        assertTrue(response.isSuccessful());
        assertTrue(response.getValue());
        assertFalse(userController.isNonRegisteredClient(clientResponse.getValue()));
    }
    @Test
    public void testLoginSuccess() {
        Response<UUID> clientResponse = userController.createClient();
        userController.register("logintest", "Password5");

        // call the login function
        Response<User> response = userController.login(clientResponse.getValue(), "logintest", "Password5");

        // assert that the response is successful and the returned UUID matches the UUID of the logged in user
        assertTrue(response.isSuccessful());
    }

    @Test
    public void testAddItemToCartSuccess() {
        // Set up
        int quantity = 5;

        // Call function
        Response<Boolean> response = userController.addItemToCart(userId, itemId, quantity, storeId);

        // Assertions
        ShoppingCart cart = user.getCart();
        assertTrue(response.isSuccessful());
        assertTrue(response.getValue());
        assertEquals(quantity, cart.quantityOf(storeId, itemId));
    }

    @Test
    public void testAddItemToCartFailureInvalidClient() {
        // Set up
        UUID clientCredentials = UUID.randomUUID();
        int quantity = 5;

        // Call function
        Response<Boolean> response = userController.addItemToCart(clientCredentials, itemId, quantity, storeId);

        // Assertions
        assertFalse(response.isSuccessful());
        assertEquals("User does not exist", response.getMessage());
    }

    @Test
    public void testRemoveItemFromCartSuccess() {
        Response<Boolean> addResponse = userController.addItemToCart(userId, itemId, 5, storeId);
        assertTrue(addResponse.isSuccessful() && addResponse.getValue());
        Response<Boolean> removeResponse = userController.removeItemFromCart(userId, itemId, 1, storeId);
        assertTrue(removeResponse.isSuccessful() && removeResponse.getValue());
    }

    @Test
    public void testRemoveItemFromCartFailure() {
        Response<Boolean> addResponse = userController.addItemToCart(userId, itemId, 2, storeId);
        assertTrue(addResponse.isSuccessful() && addResponse.getValue());
        Response<Boolean> removeResponse = userController.removeItemFromCart(userId, itemId, 3, storeId);
        assertFalse(removeResponse.getValue());
        assertEquals("Cannot remove item quantity from cart.", removeResponse.getMessage());
    }


    @Test
    public void testAppointStoreOwnerSuccess() {
        userController.login(userId, "u", password2);
        Response<Boolean> response = userController.appointStoreOwner(userId, userId2, storeId);
        assertTrue(response.getValue());
    }

    @Test
    public void testAppointStoreOwnerFailure() {
        // The store doesn't exist
        userController.login(userId, "u", password2);
        UUID nonExistingStoreId = UUID.randomUUID();
        Response<Boolean> response = userController.appointStoreOwner(userId, userId, nonExistingStoreId);
        assertTrue(response.isError());

        // The user doesn't have permission
        userController.login(userId, "uu", password3);
    
        userController.register("anotheruser", "Password7");
        UUID anotherUserId = userController.getId("anotheruser");
        Response<Boolean> response2 = userController.appointStoreOwner(userId2, anotherUserId, storeId);
        assertTrue(response2.isError());

        // The user doesn't exist
        UUID nonExistingUserId = UUID.randomUUID();
        Response<Boolean> response3 = userController.appointStoreOwner(userId, nonExistingUserId, storeId);
        assertTrue(response3.isError());

        // The user is already an owner of the shop
        Response<Boolean> response4 = userController.appointStoreOwner(userId, userId, storeId);
        assertTrue(response4.isError());


    }

    @Test
    public void testDeleteUserSuccess() {
        User admin = userController.login(userId, "admin", "Admin1").getValue();
        UUID adminCredentials = admin.getId();
        Response<Boolean> response = userController.deleteUser(adminCredentials, userId);
        assertTrue(response.getValue(), response.getMessage());
        User deletedUser = userController.getUserById(userId);
        assertNull(deletedUser);
    }

    @Test
    public void testLogout() {
        // Arrange
        Response<UUID> clientResponse = userController.createClient();
        userController.login(clientResponse.getValue(), user.getUsername(), password2);

        // Act
        Response<UUID> response = userController.logout(userId);

        // Assert
        assertFalse(userController.isLoggedInUser(userId).getValue());
        assertTrue(response.isSuccessful());
    }
        @Test
    public void testLoginFailure() {
        // call the login function with a non-existing username
        Response<User> response = userController.login(UUID.randomUUID(), "fakeuser", "Password123");

        // assert that the response is not successful and the error message is as expected
        assertFalse(response.isSuccessful());
        assertEquals("User is not registered in the system.", response.getMessage());

    }

    @Test
    public void testAppointStoreManager_success() {
        userController.login(userController.createClient().getValue(), user.getUsername(), password2);
        Response<Boolean> response = userController.appointStoreManager(userId, userId2, storeId);
        assertTrue(response.getValue());
    }

    @Test
    public void testAppointStoreManager_failure() {
        userController.login(userId, "uu", password3);
    
        // Not a store owner
        Response<Boolean> response = userController.appointStoreManager(userId2, userId2, storeId);
        assertTrue(response.isError());
        
        userController.login(userId, "u", password2);
        // User doesn't exist
        UUID nonExistingUser = UUID.randomUUID();
        response = userController.appointStoreManager(userId, nonExistingUser, storeId);
        assertTrue(response.isError());
    }
    
}