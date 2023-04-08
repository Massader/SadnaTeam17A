package DomainLayer.Market;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.PurchaseHistory;
import DomainLayer.Market.Users.ShoppingCart;
import DomainLayer.Market.Users.User;
import DomainLayer.Security.SecurityController;
import ServiceLayer.Response;

public class UserController {

    private static UserController singleton = null;
    private ConcurrentHashMap<UUID, User> userCredentials;// for registered clients only!
    private ConcurrentHashMap<String, UUID> userNames;    // for registered clients only!
    private ConcurrentLinkedQueue<String> logedInUsers; // for loged in users only!
    private SecurityController securityController;
    private ConcurrentHashMap<UUID, Client> clients;  // for non-registered clients only!
    private StoreController storeController;


    private UserController() {
        userCredentials = new ConcurrentHashMap<>();
        userNames = new ConcurrentHashMap<>();
        logedInUsers = new ConcurrentLinkedQueue<>();
        securityController = SecurityController.getInstance();
        clients = new ConcurrentHashMap<>();
        storeController = StoreController.getInstance();
    }

    public static synchronized UserController getInstance() {
        if (singleton == null)
            singleton = new UserController();
        return singleton;
    }

    public Response<User> Login(String userName, String password, Client client) {
        if (logedInUsers.contains(userName))
            return Response.getFailResponse("A user is already logged in, please log out first.");
        if (!userCredentials.contains(userName))
            return Response.getFailResponse("Email does not exist in the system.");

        UUID id = getId(userName);
        User user = getUserById(id);

        //validate the password
        if (securityController.ValidatePass(id, password)) {
            //transfer the client to the loged in users, and delete it from the non registered clients list
            logedInUsers.add(userName);
            closeClien(client.getId());
            return Response.getSuccessResponse(user);
        }
        return Response.getFailResponse("Wrong password.");
    }

    public Response<UUID> getClienCredentials(String userName) {
        UUID id;
        if(userNames.get(userName)==null){return Response.getFailResponse("user doesn't exist");}
        return Response.getSuccessResponse(userNames.get(userName));

    }


    // add a new user to the system
    public Response<User> Register(String userName, String password) {
        //verify UserName is valid and unused.
        if (userName == null) return Response.getFailResponse("No UserName input.");
        if (userNames.contains(userName))
            return Response.getFailResponse("This UserName is already in use.");


        UUID id = UUID.randomUUID();
        //add user
        User u = loadUser(userName, password, id);


        return Response.getSuccessResponse(u);
    }



    public Response<UUID> createClient() {
        UUID id = UUID.randomUUID();
        Client client = new Client(id);
        clients.put(id, client);
        return Response.getSuccessResponse(id);
    }

    public synchronized Response<Boolean> closeClien(UUID clientCredentials) {
        clients.remove(clientCredentials);
        return Response.getSuccessResponse(true);
    }

    public Response<Boolean> addItemToCart(UUID userId, UUID itemId, int quantity, UUID storeID ){
        if (getClientOrUser(userId)==null)
            return Response.getFailResponse("this user ID does not exist");
        ShoppingCart shoppingCart =getClientOrUser(userId).getCart();
        return Response.getSuccessResponse( shoppingCart.addItemToCart(itemId, storeID, quantity));
    }


    public Response<Boolean> removeItemFromCart(UUID userId, UUID itemId, int quantity, UUID storeId) {
        if (getClientOrUser(userId)==null)
            return Response.getFailResponse("this user ID does not exist");
        ShoppingCart shoppingCart =getClientOrUser(userId).getCart();
        return Response.getSuccessResponse( shoppingCart.removeItemToCart(itemId, storeId, quantity));
    }

    public Response<String> getPurchaseHistory(UUID clientCredentials, UUID userId) {
        if (!userCredentials.containsKey(userId))
            return Response.getFailResponse("this user ID does not exist");
        if(!isRegisteredUser(userId)){
            return Response.getFailResponse("this user ID not register");
        }
        return Response.getSuccessResponse(getUser(userId).getValue().getPurchases().toArray().toString());// TODO: now return as a string  when we will know how Purchase will be -> change accordingly

    }
    public Response<Boolean> appointStoreOwner(UUID clientId, UUID apointee, UUID storeId) {
        return null;
    }

    public Response<Boolean> appointStoreManager(UUID clientId, UUID apointee, UUID storeId) {
        return null;
    }

    public Response<Boolean> removeStoreOwner(UUID clientId, UUID ownerToRemove, UUID storeId) {
        return null;
    }

    public Response<Boolean> removeStoreManager(UUID clientId, UUID ownerToRemove, UUID storeId) {
        return null;
    }

    public Response<Boolean> setManagerPermissions(UUID clientId, UUID manager,
                                                   UUID storeId, List<Integer> permissions) {
        return null;
    }

    public Response<Boolean> deleteUser(UUID userId, UUID storeId) {
        if (!userCredentials.containsKey(userId))
            return Response.getFailResponse("this user ID does not exist");

        if (logedInUsers.contains(userId))
            logout(userId);

        //remove from both HashMaps
        User user = userCredentials.remove(userId);
        userNames.remove(user.getUserName());

        return Response.getSuccessResponse(true);
    }

    // delete the user from the loged in list.
    public Response<Boolean> logout(UUID userId) {
        if (!userCredentials.containsKey(userId))
            return Response.getFailResponse("this user ID does not exist");
        if (!logedInUsers.contains(userId))
            return Response.getFailResponse("this user is already logged out");
        logedInUsers.remove(userId);
        //User user = userCredentials.get(userId);
        return Response.getSuccessResponse(true);
    }

    public Response<Boolean> setAsFounder() {
        return null;
    }

    public Response<User> getUser(UUID userId) {
        return Response.getSuccessResponse(userCredentials.get(userId));
    }

    public Response<Boolean> getCartTotal(UUID userId) {
        if (getClientOrUser(userId)==null)
            return Response.getFailResponse("this user ID does not exist");


        return null;
    }


    public User getUser(String userName) {
        UUID id = userNames.get(userName);
        return userCredentials.get(id);
    }

    public User getUserById(UUID id) {
        return userCredentials.get(id);
    }

    public UUID getId(String userName) {
        return userNames.get(userName);
    }

    // add a new user to all the data structured
    private User loadUser(String userName, String password, UUID id){
        User user = new User(userName, id);
        userCredentials.put(id, user);
        userNames.put(user.getUserName(), id);

        securityController.encryptAndSavePassword(id, password);

        return user;
    }

    // i made these methods to avoid confusion between clients and users.
    public boolean isRegisteredUser(UUID id){
        return userCredentials.contains(id);
    }
    public boolean isNonRegisteredClient(UUID id){
        return clients.contains(id);
    }

    // get client in all kind!
    public Client getClientOrUser(UUID id){
        if (isRegisteredUser(id))
            return userCredentials.get(id);
        if (isNonRegisteredClient(id))
            return clients.get(id);
        return null;
    }



}







