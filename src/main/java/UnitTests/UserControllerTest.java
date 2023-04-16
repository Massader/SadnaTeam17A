package UnitTests;
import DomainLayer.Market.StoreController;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.UserController;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.ShoppingCart;
import DomainLayer.Market.Users.User;
import ServiceLayer.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;
    private String username;
    private String password;
    private UUID clientCredentials;
    private StoreController storeController;
    private UUID userId;
    private UUID userId2;
    private UUID storeId;
    private Item item;
    private UUID itemId;
    private User user;
    private User user2;



    @Before
    public void setUp() {
        userController = UserController.getInstance();
        userController.init();
        username = "testuser";
        password = "password123";
        clientCredentials = userController.createClient().getValue();
        userController = UserController.getInstance();
        userController.init();
        storeController = StoreController.getInstance();
        userController.register("u", "p");
        userController.register("u2", "p2");
        userId = userController.getId("u");
        user = userController.getUserById(userId);
        userId2 = userController.getId("u2");
        user2 = userController.getUserById(userId2);
        storeId = storeController.createStore(userId, "store", "des").getValue().getStoreId();
        item = storeController.addItem("phone", 10, storeId, 100, "aaaaa").getValue();
        itemId = item.getId();
    }

    @Test
    public void testRegisterSuccess() {
        Response<Boolean> response = userController.register(username, password);

        assertEquals(true, response.getMessage());
    }

    @Test
    public void testRegisterFailureDuplicateUsername() {

        Response<Boolean> response1 = userController.register(username, password);
        assertEquals(true, response1.getValue());

        Response<Boolean> response2 = userController.register(username, "newpassword");
        assertEquals(false, response2.getValue());
        assertEquals("This username is already in use.", response2.getMessage());
    }

    @Test
    public void testRegisterFailureEmptyUsername() {
        Response<Boolean> response = userController.register(username, password);
        assertEquals(false, response.getValue());
        assertEquals("No username input.", response.getMessage());
    }

    @Test
    public void testCloseClientSuccess() {
        // create a new client and add them to the clients map

        Client client = new Client(clientCredentials);

        // call the closeClient function
        Response<Boolean> response = userController.closeClient(clientCredentials);

        // assert that the response is successful
        assertTrue(response.isSuccessful());
        assertTrue(response.getValue());
        assertFalse(userController.isNonRegisteredClient(clientCredentials));
    }
    @Test
    public void testLoginSuccess() {
        userController.register("username", "password");

        // call the login function
        Response<UUID> response = userController.login(clientCredentials, "username", "password");

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

//    @Test
//    public void testRemoveItemFromCartFailure() {
//        Response<Boolean> addResponse = userController.addItemToCart(userId, itemId, 2, storeId);
//        assertTrue(addResponse.isSuccessful() && addResponse.getValue());
//        Response<Boolean> removeResponse = userController.removeItemFromCart(userId, itemId, 3, storeId);
//        assertFalse(removeResponse.getValue());
//        //assertEquals("Cannot remove item quantity from cart.", removeResponse.getMessage());
//    }


    @Test
    public void testAppointStoreOwnerSuccess() {
        userController.login(userId, "u", "p");
        Response<Boolean> response = userController.appointStoreOwner(userId, userId2, storeId);
        assertTrue(response.getValue());
    }

    @Test
    public void testAppointStoreOwnerFailure() {
        // The store doesn't exist
        UUID nonExistingStoreId = UUID.randomUUID();
        Response<Boolean> response = userController.appointStoreOwner(userId, userId, nonExistingStoreId);
        assertTrue(response.isError());

        // The user doesn't have permission
        userController.register("anotheruser", "password");
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

//    @Test
//    public void testDeleteUserSuccess() {
//        UUID adminCredentials = userController.login(userId, "u", "p").getValue();
//        Response<Boolean> response = userController.deleteUser(adminCredentials, userId);
//        assertTrue(response.getMessage(), response.getValue());
//        Response<User> deletedUserResponse = userController.getUserById(userId);
//        assertTrue(deletedUserResponse.isError());
//        assertEquals("User does not exist.", deletedUserResponse.getMessage());
//    }

    @Test
    public void testLogout() {
        // Arrange;
        userController.login(userId, username, "p");

        // Act
        Response<UUID> response = userController.logout(userId);

        // Assert
        assertFalse(userController.isLoggedInUser(userId));
        assertTrue(response.isSuccessful());
    }
        @Test
    public void testLoginFailure() {
        // call the login function with a non-existing username
        Response<UUID> response = UserController.getInstance().login(UUID.randomUUID(), "fakeuser", "password");

        // assert that the response is not successful and the error message is as expected
        assertFalse(response.isSuccessful());
        assertEquals("User is not registered in the system.", response.getMessage());

    }

    @Test
    public void testAppointStoreManager_success() {
        Response<Boolean> response = userController.appointStoreManager(userId, userId2, storeId);
        assertTrue(response.getValue());
        assertTrue(storeController.getStore(storeId).getRolesMap().containsKey(userId));
    }

    @Test
    public void testAppointStoreManager_failure() {
        // Not a store owner
        Response<Boolean> response = userController.appointStoreManager(userId2, userId2, storeId);
        assertFalse(response.getValue());
        assertFalse(storeController.getStore(storeId).getRolesMap().containsKey(userId2));

        // User doesn't exist
        UUID nonExistingUser = UUID.randomUUID();
        response = userController.appointStoreManager(userId, nonExistingUser, storeId);
        assertFalse(response.getValue());
        assertFalse(storeController.getStore(storeId).getRolesMap().containsKey(nonExistingUser));
    }

//    @Test
//    public void testDeleteUserSuccess() {
//        UUID adminCredentials = userController.login("admin", "admin").getValue();
//        Response<Boolean> response = userController.deleteUser(adminCredentials, userId);
//        assertTrue(response.getMessage(), response.getValue());
//        Response<User> deletedUserResponse = userController.getUserById(userId);
//        assertTrue(deletedUserResponse.isError());
//        assertEquals("User does not exist.", deletedUserResponse.getMessage());
//    }


}