package DomainLayer.Market;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.User;
import DomainLayer.Security.SecurityController;
import ServiceLayer.Response;

public class UserController {

    private static UserController singleton = null;
    private ConcurrentHashMap<UUID, User> userCredentials;
    private ConcurrentHashMap<String, UUID> userNames;
    private ConcurrentLinkedQueue<String> logedInUsers;
    private SecurityController securityController;
    private ConcurrentHashMap<UUID, Client> clients;


    private UserController() {
        userCredentials = new ConcurrentHashMap<>();
        userNames = new ConcurrentHashMap<>();
        logedInUsers = new ConcurrentLinkedQueue<>();
        securityController = SecurityController.getInstance();
        clients = new ConcurrentHashMap<>();
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

        if (securityController.ValidatePass(id, password)) {
            logedInUsers.add(userName);
            closeClien(client.getId());
            return Response.getSuccessResponse(user);
        }
        return Response.getFailResponse("Wrong password.");

    }

//    public Response<UUID> getClienCredentials(String userNAme) {
//        return null;
//    }


    public Response<User> Register(String userName, String password) {
        //verify UserName is valid and unused.
        if (userName == null) return Response.getFailResponse("No UserName input.");
        if (userNames.contains(userName))
            return Response.getFailResponse("This UserName is already in use.");

        //add user
        UUID id = UUID.randomUUID();
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

    public Response<Boolean> addItemToCart() {
        return null;
    }

    public Response<Boolean> removeItemFromCart() {
        return null;
    }

    public Response<Boolean> getPurchaseHistory() {
        return null;
    }

    public Response<Boolean> appointStoreOwner() {
        return null;
    }

    public Response<Boolean> appointStoreManager() {
        return null;
    }

    public Response<Boolean> removeStoreOwner() {
        return null;
    }

    public Response<Boolean> removeStoreManager() {
        return null;
    }

    public Response<Boolean> setManagerPermissions() {
        return null;
    }

    public Response<Boolean> deleteUser() {
        return null;
    }

    public Response<Boolean> setAsFounder() {
        return null;
    }

    public Response<Boolean> getUser(UUID userId) {
        return null;
    }

    public Response<Boolean> getCartTotal() {
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
    private User loadUser(String userName, String password, UUID id){
        User user = new User(userName, id);
        userCredentials.put(id, user);
        userNames.put(user.getUserName(), id);

        securityController.encryptAndSavePassword(id, password);

        return user;
    }
}







