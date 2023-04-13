package DomainLayer.Market;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.*;
import DomainLayer.Security.SecurityController;
import ServiceLayer.Response;
import DomainLayer.Market.Users.Roles.*;
import ServiceLayer.ServiceUser;

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

    public Response<Boolean> setAsFounder(UUID clientCredentials, UUID storeId){
        try {
            getUser(clientCredentials).getValue().addStoreRole(new StoreFounder(storeId));
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<ShoppingCart> viewCart(UUID clientCredentials){
        try {
            Client client = getClientOrUser(clientCredentials);
            return Response.getSuccessResponse(client.getCart());
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<User> Login(String userName, String password, Client client) {
        if (logedInUsers.contains(userName))
            return Response.getFailResponse("A user is already logged in, please log out first.");
        if (!userCredentials.contains(userName))
            return Response.getFailResponse("Email does not exist in the system.");

        UUID id = getId(userName);
        User user = getUserById(id);

        //validate the password
        if (securityController.ValidatePass(id, password).getValue().equals(id)) {
            //transfer the client to the logged in users, and delete it from the non registered clients list
            logedInUsers.add(userName);
            closeClient(client.getId());
            return Response.getSuccessResponse(user);
        }
        return Response.getFailResponse("Wrong password.");
    }

    public Response<UUID> getClientCredentials(String userName) {
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
        try {
            UUID id = UUID.randomUUID();
            Client client = new Client(id);
            clients.put(id, client);
            return Response.getSuccessResponse(id);
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public synchronized Response<Boolean> closeClient(UUID clientCredentials) {
        try {
            clients.remove(clientCredentials);
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> addItemToCart(UUID userId, UUID itemId, int quantity, UUID storeID ) {
        try {
            if (getClientOrUser(userId) == null)
                return Response.getFailResponse("this user ID does not exist");
            ShoppingCart shoppingCart = getClientOrUser(userId).getCart();
            if (shoppingCart.addItemToCart(itemId, storeID, quantity)) {
                return Response.getSuccessResponse(true);
            } else {
                return Response.getFailResponse("can't add item to cart");
            }
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }



    public Response<Boolean> removeItemFromCart(UUID userId, UUID itemId, int quantity, UUID storeId) {
        try{
            if (getClientOrUser(userId)==null)
                return Response.getFailResponse("this client ID does not exist");
            ShoppingCart shoppingCart =getClientOrUser(userId).getCart();
            if(shoppingCart.removeItemToCart(itemId, storeId, quantity)){
                return Response.getSuccessResponse(true);}
            else {  return Response.getFailResponse("can't remove "+ quantity +" item from cart");}}
        catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }

    }



    public Response<List<Purchase>> getPurchaseHistory(UUID clientCredentials, UUID userId) {
        try {
            if (!userCredentials.containsKey(userId))
                return Response.getFailResponse("this user ID does not exist");
            if (clientCredentials==userId&&!isRegisteredUser(userId)) {
                return Response.getFailResponse("this user ID not register");//check the user is register
            }

            return Response.getSuccessResponse(getUser(userId).getValue().getPurchases().stream().toList());
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }}

    public Response<Boolean> appointStoreOwner(UUID clientCredentials, UUID appointee, UUID storeId) {
        try {
            Response<Store> response = storeController.getStore(storeId);
            if(response.isError())
                return Response.getFailResponse("Store does not exist.");
            if(!response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            Response<User> response2 = this.getUser(appointee);
            if(response2.isError())
                return Response.getFailResponse("User does not exist.");
            if(response.getValue().getRolesMap().containsKey(appointee) &&
                response.getValue().checkPermission(appointee, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User already owner of the shop.");
            StoreOwner storeOwner = new StoreOwner(storeId);
            response2.getValue().addStoreRole(storeOwner);
            response.getValue().addRole(appointee, storeOwner);
            response.getValue().getOwner(clientCredentials).addAppoint(appointee);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> appointStoreManager(UUID clientCredentials, UUID appointee, UUID storeId) {
        try {
            Response<Store> response = storeController.getStore(storeId);
            if(response.isError())
                return Response.getFailResponse("Store does not exist.");
            if(!response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            Response<User> response2 = this.getUser(appointee);
            if(response2.isError())
                return Response.getFailResponse("User does not exist.");
            if(response.getValue().getRolesMap().containsKey(appointee))
                return Response.getFailResponse("User already manager in the shop.");
            StoreManager storeManager = new StoreManager(storeId);
            response2.getValue().addStoreRole(storeManager);
            response.getValue().addRole(appointee, storeManager);
            response.getValue().getOwner(clientCredentials).addAppoint(appointee);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> removeStoreOwner(UUID clientCredentials, UUID ownerToRemove, UUID storeId) {
        try {
            Response<Store> response = storeController.getStore(storeId);
            if(response.isError())
                return Response.getFailResponse("Store does not exist.");
            if(!response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            Response<User> response2 = this.getUser(ownerToRemove);
            if(response2.isError())
                return Response.getFailResponse("User does not exist.");
            if(response.getValue().getOwner(ownerToRemove)==null)
                return Response.getFailResponse("User is not owner of the shop.");
            if(!response.getValue().getOwner(clientCredentials).getAppoints().contains(ownerToRemove))
                return Response.getFailResponse("Owner did not appointed by this user.");
            response2.getValue().removeStoreRole(storeId);
            response.getValue().removeRole(ownerToRemove);
            response.getValue().getOwner(clientCredentials).removeAppoint(ownerToRemove);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> removeStoreManager(UUID clientCredentials, UUID ManagerToRemove, UUID storeId) {
        try {
            Response<Store> response = storeController.getStore(storeId);
            if(response.isError())
                return Response.getFailResponse("Store does not exist.");
            if(!response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            Response<User> response2 = this.getUser(ManagerToRemove);
            if(response2.isError())
                return Response.getFailResponse("User does not exist.");
            if(!response.getValue().getRolesMap().containsKey(ManagerToRemove))
                return Response.getFailResponse("User is not Manager of the shop.");
            if(!response.getValue().getOwner(clientCredentials).getAppoints().contains(ManagerToRemove))
                return Response.getFailResponse("Manager did not appointed by this user.");
            response2.getValue().removeStoreRole(storeId);
            response.getValue().removeRole(ManagerToRemove);
            response.getValue().getOwner(clientCredentials).removeAppoint(ManagerToRemove);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setManagerPermissions(UUID clientCredentials, UUID manager,
                                                   UUID storeId, List<Integer> permissions) {
        try {
            Response<Store> response = storeController.getStore(storeId);
            if(response.isError())
                return Response.getFailResponse("Store does not exist.");
            if(!response.getValue().checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            Response<User> response2 = this.getUser(manager);
            if(response2.isError())
                return Response.getFailResponse("User does not exist.");
            if(!response.getValue().getRolesMap().containsKey(manager))
                return Response.getFailResponse("User is not store manager.");
            for(int i : permissions)
                response.getValue().getRolesMap().get(manager).addPermission(StorePermissions.values()[i]);
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> deleteUser(UUID userId, UUID storeId) {
        try {
            if (!userCredentials.containsKey(userId))
                return Response.getFailResponse("this user ID does not exist");

            if (logedInUsers.contains(userId))
                logout(userId);

            //remove from both HashMaps
            User user = userCredentials.remove(userId);
            userNames.remove(user.getUserName());
            //remove roles
            for (Role role: user.getRoles())
                removeRole(role);

            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public void removeRole(Role role){}

    // delete the user from the loged in list.
    public Response<UUID> logout(UUID userId) {
        try {
            if (!userCredentials.containsKey(userId))
                return Response.getFailResponse("this user ID does not exist");
            if (!logedInUsers.contains(userId))
                return Response.getFailResponse("this user is already logged out");
            logedInUsers.remove(userId);
            //User user = userCredentials.get(userId);
            return Response.getSuccessResponse(userId);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setAsFounder() {
        return null;
    }

    public Response<User> getUser(UUID userId) {
        try {
            User user = userCredentials.get(userId);
            if(user==null)
                return Response.getFailResponse("User does not exist.");
            return Response.getSuccessResponse(user);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
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







