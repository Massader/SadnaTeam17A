package DomainLayer.Market;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.*;
import DomainLayer.Security.SecurityController;
import ServiceLayer.Response;
import DomainLayer.Market.Users.Roles.*;

public class UserController {

    private static UserController singleton = null;
    private ConcurrentHashMap<UUID, User> users;// for registered clients only!
    private ConcurrentHashMap<String, UUID> usernames;    // for registered clients only!
    private ConcurrentLinkedQueue<String> loggedInUser; // for logged in users only!
    private SecurityController securityController;
    private ConcurrentHashMap<UUID, Client> clients;  // for non-registered clients only!
    private StoreController storeController;


    private UserController() { }


    public void init()
    {
        users = new ConcurrentHashMap<>();
        usernames = new ConcurrentHashMap<>();
        loggedInUser = new ConcurrentLinkedQueue<>();
        securityController = SecurityController.getInstance();
        clients = new ConcurrentHashMap<>();
        storeController = StoreController.getInstance();
        registerDefaultAdmin();
    }

    public static synchronized UserController getInstance() {
        if (singleton == null) {
            singleton = new UserController();
        }
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

    public Response<UUID> login(UUID clientCredentials, String username, String password) {
        try {
            if (loggedInUser.contains(username))
                return Response.getFailResponse("User is already logged in, please log out first.");
            if (!usernames.containsKey(username) || !users.containsKey(usernames.get(username)))
                return Response.getFailResponse("User is not registered in the system.");
            //validate the password
            UUID idPAss = securityController.validatePassword(getId(username), password).getValue();
            if (idPAss.equals(getId(username))) {
                //transfer the client to the logged in users, and delete it from the non registered clients list
                loggedInUser.add(username);
                closeClient(clientCredentials);
                return Response.getSuccessResponse(usernames.get(username));
            }
            return Response.getFailResponse("Wrong password.");
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public synchronized Response<Boolean> register(String username, String password) {
        try{
            if (username == null || username.length()==0)
                return Response.getFailResponse("No username input.");
            if (usernames.containsKey(username))
                return Response.getFailResponse("This username is already in use.");
            //add user
            loadUser(username, password, UUID.randomUUID());
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
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
            if(!clients.containsKey(clientCredentials))
                Response.getFailResponse("This client does not exist in the system");
            ShoppingCart shoppingCart = getClientOrUser(clientCredentials).getCart();
            deleteShoppingCart(shoppingCart);
            clients.remove(clientCredentials);
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public void deleteShoppingCart(ShoppingCart shoppingCart){
        for(ShoppingBasket shoppingBasket : shoppingCart.getShoppingBaskets().values())
            deleteShoppingBasket(shoppingBasket);
    }

    private void deleteShoppingBasket(ShoppingBasket shoppingBasket){
        UUID storeId = shoppingBasket.getStoreId();
        Store store = storeController.getStore(storeId);
        for(UUID itemId : shoppingBasket.getItems().keySet()){
            store.getItem(itemId).addQuantity(shoppingBasket.getItems().get(itemId));
        }
        shoppingBasket.getItems().clear();
    }

    public Response<Boolean> addItemToCart(UUID clientCredentials, UUID itemId, int quantity, UUID storeId) {
        try {
            if (getClientOrUser(clientCredentials) == null)
                return Response.getFailResponse("User does not exist");
            ShoppingCart shoppingCart = getClientOrUser(clientCredentials).getCart();
            Response<Boolean> response = storeController.removeItemQuantity(storeId, itemId, quantity);
            if (response.isError()) return response;
            if (shoppingCart.addItemToCart(storeController.getItem(itemId).getValue(), storeId, quantity)) {
                return Response.getSuccessResponse(true);
            } else {
                return Response.getFailResponse("Cannot add item to cart");
            }
        }
        catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> removeItemFromCart(UUID clientCredentials, UUID itemId, int quantity, UUID storeId) {
        try{
            if (getClientOrUser(clientCredentials)==null)
                return Response.getFailResponse("User does not exist");
            Response<Boolean> response = storeController.addItemQuantity(storeId, itemId, quantity);
            if (response.isError()) return response;
            ShoppingCart shoppingCart = getClientOrUser(clientCredentials).getCart();
            if(shoppingCart.removeItemFromCart(storeController.getItem(itemId).getValue(), storeId, quantity))
                return Response.getSuccessResponse(true);
            else
                return Response.getFailResponse("Cannot remove item quantity from cart.");
        }
        catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<List<Purchase>> getPurchaseHistory(UUID clientCredentials, UUID userId) {
        try {
            if (!users.containsKey(userId))
                return Response.getFailResponse("this user ID does not exist");
            if (clientCredentials!=userId)
                return Response.getFailResponse("Cannot view purchase history of other users.");
            return Response.getSuccessResponse(new ArrayList<>(getUser(userId).getValue().getPurchases()));
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> appointStoreOwner(UUID clientCredentials, UUID appointee, UUID storeId) {
        try {
            if(!storeController.storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            if(!storeController.getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            Response<User> response2 = this.getUser(appointee);
            if(response2.isError())
                return Response.getFailResponse("User does not exist.");
            if(storeController.getStore(storeId).getRolesMap().containsKey(appointee) &&
                    storeController.getStore(storeId).checkPermission(appointee, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User already owner of the shop.");
            StoreOwner storeOwner = new StoreOwner(storeId);
            response2.getValue().addStoreRole(storeOwner);
            storeController.getStore(storeId).addRole(appointee, storeOwner);
            storeController.getStore(storeId).getOwner(clientCredentials).addAppoint(appointee);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> appointStoreManager(UUID clientCredentials, UUID appointee, UUID storeId) {
        try {
            if(!storeController.storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            if(!storeController.getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            Response<User> response2 = this.getUser(appointee);
            if(response2.isError())
                return Response.getFailResponse("User does not exist.");
            if(storeController.getStore(storeId).getRolesMap().containsKey(appointee))
                return Response.getFailResponse("User already manager in the shop.");
            StoreManager storeManager = new StoreManager(storeId);
            response2.getValue().addStoreRole(storeManager);
            storeController.getStore(storeId).addRole(appointee, storeManager);
            storeController.getStore(storeId).getOwner(clientCredentials).addAppoint(appointee);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> removeStoreRole(UUID clientCredentials, UUID roleToRemove, UUID storeId) {
        try {
            if(!storeController.storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            if(!storeController.getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER)
                || !users.get(clientCredentials).isAdmin())
                return Response.getFailResponse("User doesn't have permission.");
            Response<User> response2 = this.getUser(roleToRemove);
            if(response2.isError())
                return Response.getFailResponse("User does not exist.");
            if(storeController.getStore(storeId).getRolesMap().containsKey(roleToRemove))
                return Response.getFailResponse("User does not have role in the shop.");
            if(!storeController.getStore(storeId).getOwner(clientCredentials).getAppoints().contains(roleToRemove))
                return Response.getFailResponse("Owner was not appointed by this user.");
            response2.getValue().removeStoreRole(storeId);
            for(UUID appointee : storeController.getStore(storeId).getOwner(roleToRemove).getAppoints())
                removeStoreRole(roleToRemove, appointee, storeId);
            storeController.getStore(storeId).removeRole(roleToRemove);
            storeController.getStore(storeId).getOwner(clientCredentials).removeAppoint(roleToRemove);
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setManagerPermissions(UUID clientCredentials, UUID manager,
                                                   UUID storeId, List<Integer> permissions) {
        try {
            if(!storeController.storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            if(!storeController.getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            if(!users.containsKey(manager))
                return Response.getFailResponse("Manager does not exist.");
            if(!storeController.getStore(storeId).getRolesMap().containsKey(manager))
                return Response.getFailResponse("User is not store manager.");
            for(int i : permissions)
                storeController.getStore(storeId).getRolesMap().get(manager).addPermission(StorePermissions.values()[i]);
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> deleteUser(UUID clientCredentials, UUID userId) {
        try {
            if (!users.containsKey(userId))
                return Response.getFailResponse("User does not exist.");
            if(!users.get(clientCredentials).isAdmin())
                return Response.getFailResponse("Only admins can delete users.");
            User user = users.get(userId);
            if (loggedInUser.contains(user.getUsername()))
                logout(userId);
            //remove from both HashMaps
            user = users.remove(userId);
            deleteShoppingCart(user.getCart());
            usernames.remove(user.getUsername());
            //remove roles
            for (Role role: user.getRoles())
                removeStoreRole(clientCredentials, userId, role.getStoreId());
            securityController.removePassword(userId);
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<UUID> logout(UUID userId) {
        try {
            if (!users.containsKey(userId))
                return Response.getFailResponse("this user ID does not exist");
            User user = users.get(userId);
            if (!loggedInUser.contains(user.getUsername()))
                return Response.getFailResponse("this user is already logged out");
            loggedInUser.remove(user.getUsername());
            return createClient();
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<User> getUser(UUID userId) {
        try {
            User user = users.get(userId);
            if(user==null)
                return Response.getFailResponse("User does not exist.");
            return Response.getSuccessResponse(user);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

//    public Response<Boolean> getCartTotal(UUID userId) {
//        if (getClientOrUser(userId)==null)
//            return Response.getFailResponse("this user ID does not exist");
//
//
//        return null;
//    }


    public User getUserById(UUID id) {
        return users.get(id);
    }

    public UUID getId(String userName) {
        return usernames.get(userName);
    }

    // add a new user to all the data structured
    private User loadUser(String userName, String password, UUID id){
        User user = new User(userName, id);
        users.put(id, user);
        usernames.put(user.getUsername(), id);
        securityController.encryptAndSavePassword(id, password);
        return user;
    }

    // i made these methods to avoid confusion between clients and users.
    public boolean isRegisteredUser(UUID id){
        return users.containsKey(id);
    }

    public Response<Boolean> isUser(UUID id){
        if(!users.containsKey(id)){
            return Response.getFailResponse("The client does not have user permission  ");
        }
        return Response.getSuccessResponse(true);
    }

    public boolean isNonRegisteredClient(UUID id){
        return clients.containsKey(id);
    }

    // get client in all kind!
    public Client getClientOrUser(UUID id){
        if (isRegisteredUser(id))
            return users.get(id);
        if (isNonRegisteredClient(id))
            return clients.get(id);
        return null;
    }

    public Response<Boolean> clearCart(UUID clientCredentials) {
        if (getClientOrUser(clientCredentials) == null) return Response.getFailResponse("User does not exist.");
        Client client = getClientOrUser(clientCredentials);
        client.getCart().clearCart();
        return Response.getSuccessResponse(true);
    }

    private void registerDefaultAdmin() {
        try {
            if (!usernames.containsKey("admin")) {
                UUID adminCredentials = UUID.randomUUID();
                usernames.put("admin", adminCredentials);
                users.put(adminCredentials, new Admin("admin", adminCredentials));
                securityController.addPassword(adminCredentials, "admin");
            }
        } catch (Exception ignored) {}
    }
}







