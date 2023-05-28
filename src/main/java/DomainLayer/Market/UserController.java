package DomainLayer.Market;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import DataAccessLayer.ClientRepository;
import DataAccessLayer.RepositoryFactory;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.*;
import DomainLayer.Security.SecurityController;
import ServiceLayer.Response;
import DomainLayer.Market.Users.Roles.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;

public class UserController {

    private static UserController singleton = null;
    private ConcurrentHashMap<UUID, User> users;// for registered clients only!
    private ConcurrentHashMap<String, UUID> usernames;    // for registered clients only!
    private ConcurrentHashMap<UUID, User> loggedInUsers; // for logged-in users only!
    private SecurityController securityController;
    private ConcurrentHashMap<UUID, Client> clients;  // for non-registered clients only!
    private StoreController storeController;
    private NotificationController notificationController;
    private RepositoryFactory repositoryFactory;

    private UserController() {

    }

    public void init(RepositoryFactory repositoryFactory) {
        users = new ConcurrentHashMap<>();
        usernames = new ConcurrentHashMap<>();
        loggedInUsers = new ConcurrentHashMap<>();
        securityController = SecurityController.getInstance();
        clients = new ConcurrentHashMap<>();
        storeController = StoreController.getInstance();
        notificationController = NotificationController.getInstance();
        this.repositoryFactory = repositoryFactory;
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

    public Response<ShoppingCart> getCart(UUID clientCredentials){
        try {
            Client client = getClientOrUser(clientCredentials);
            return Response.getSuccessResponse(client.getCart());
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<User> login(UUID clientCredentials, String username, String password) {
        try {
            if (loggedInUsers.contains(username))
                return Response.getFailResponse("User is already logged in, please log out first.");
            if (!usernames.containsKey(username) || !users.containsKey(usernames.get(username)))
                return Response.getFailResponse("User is not registered in the system.");
            //validate the password
            Response<Boolean> securityResponse = securityController.validatePassword(getId(username), password);
            if (securityResponse.isError()) return Response.getFailResponse(securityResponse.getMessage());
            if (securityResponse.getValue()) {
                //transfer the client to the logged in users, and delete it from the non registered clients list
                User user = users.get(getId(username));
                loggedInUsers.put(user.getId(), user);
                closeClient(clientCredentials);
                return Response.getSuccessResponse(users.get(usernames.get(username)));
            }
            return Response.getFailResponse("Wrong password.");
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> register(String username, String password) {
        try{
            if (username == null || username.length()==0)
                return Response.getFailResponse("No username input.");
            if (password == null || password.length()==0)
                return Response.getFailResponse("No password input.");
            synchronized (usernames) {
                if (usernames.containsKey(username))
                    return Response.getFailResponse("This username is already in use.");
                //add user
                return loadUser(username, password, UUID.randomUUID());
            }
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    // add a new user to all the data structured
    private Response<Boolean> loadUser(String username, String password, UUID id) {
        try {
            User user = new User(username, id);
            users.put(id, user);
            usernames.put(user.getUsername(), id);
            Response<Boolean> response = securityController.encryptAndSavePassword(id, password);
            if (response.isError()) {
                users.remove(id);
                usernames.remove(username);
            }
            return response;
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }


    public Response<UUID> createClient() {
        try {
//            UUID id = UUID.randomUUID();
            Client client = new Client();
            ClientRepository clientRepository = repositoryFactory.getClientRepository();
            clientRepository.save(client);
            clients.put(client.getId(), client);
//            System.out.println(client.getId());
            return Response.getSuccessResponse(client.getId());
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> closeClient(UUID clientCredentials) {
        try {
            if(!clients.containsKey(clientCredentials))
                Response.getFailResponse("This client does not exist in the system");
            synchronized (clients) {
                ShoppingCart shoppingCart = getClientOrUser(clientCredentials).getCart();
                deleteShoppingCart(shoppingCart);
                clients.remove(clientCredentials);
            }
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
            Response<Item> itemResponse = storeController.getItem(itemId);
            if (itemResponse.isError())
                return Response.getFailResponse(itemResponse.getMessage());
            synchronized (itemResponse.getValue()) {
                //Response<Boolean> response = storeController.removeItemQuantity(storeId, itemId, quantity);
                //if (response.isError()) return response;
                if (shoppingCart.addItemToCart(storeController.getItem(itemId).getValue(), storeId, quantity)) {
                    return Response.getSuccessResponse(true);
                } else {
                    return Response.getFailResponse("Cannot add item to cart");
                }
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
            Response<Item> itemResponse = storeController.getItem(itemId);
            if (itemResponse.isError())
                return Response.getFailResponse(itemResponse.getMessage());
            synchronized (itemResponse.getValue()) {
                //Response<Boolean> response = storeController.addItemQuantity(storeId, itemId, quantity);
                //if (response.isError()) return response;
                ShoppingCart shoppingCart = getClientOrUser(clientCredentials).getCart();
                if (shoppingCart.removeItemFromCart(itemResponse.getValue(), storeId, quantity))
                    return Response.getSuccessResponse(true);
                else
                    return Response.getFailResponse("Cannot remove item quantity from cart.");
            }
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
                return Response.getFailResponse(response2.getMessage());
            if(storeController.getStore(storeId).getRolesMap().containsKey(appointee) &&
                    storeController.getStore(storeId).checkPermission(appointee, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User already owner of the shop.");
            StoreOwner storeOwner = new StoreOwner(storeId);
            response2.getValue().addStoreRole(storeOwner);
            storeController.getStore(storeId).addRole(appointee, storeOwner);
            storeController.getStore(storeId).getOwner(clientCredentials).addAppointee(appointee);
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
            storeController.getStore(storeId).getOwner(clientCredentials).addAppointee(appointee);
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
            Store store = storeController.getStore(storeId);
            if(!store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            Response<User> response2 = this.getUser(roleToRemove);
            if(response2.isError())
                return Response.getFailResponse("User does not exist.");
            if(!store.getRolesMap().containsKey(roleToRemove))
                return Response.getFailResponse("User does not have role in the shop.");
            if(!store.getOwner(clientCredentials).getAppointees().contains(roleToRemove))
                return Response.getFailResponse("Staff member was not appointed by this owner.");
            response2.getValue().removeStoreRole(storeId);
            if (store.getOwner(roleToRemove) != null) {
                for (UUID appointee : store.getOwner(roleToRemove).getAppointees())
                    removeStoreRole(roleToRemove, appointee, storeId);
            }
            store.removeRole(roleToRemove);
            store.getOwner(clientCredentials).removeAppointee(roleToRemove);
            notificationController.sendNotification(roleToRemove, "Your role has been removed from "
                    + store.getName());
            return Response.getSuccessResponse(true);
        }
        catch(Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> setManagerPermissions(UUID clientCredentials, UUID manager,
                                                   UUID storeId, List<Integer> permissions) {
        try {
            if (permissions == null)
                return Response.getFailResponse("Passed permissions list is null.");
            if(!storeController.storeExist(storeId))
                return Response.getFailResponse("Store does not exist.");
            if(!storeController.getStore(storeId).checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User doesn't have permission.");
            if(!users.containsKey(manager))
                return Response.getFailResponse("Manager does not exist.");
            if(!storeController.getStore(storeId).getRolesMap().containsKey(manager))
                return Response.getFailResponse("User is not store manager.");
            storeController.getStore(storeId).getRolesMap().get(manager).setPermissions(permissions);
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
            if (clientCredentials.equals(userId))
                return Response.getFailResponse("Admin can't delete himself.");
            if (userId.equals(usernames.get("admin")))
                return Response.getFailResponse("Can't delete default admin.");
            User user = users.get(userId);
            if (loggedInUsers.containsKey(user.getId()))
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
            if (!loggedInUsers.containsKey(user.getId()))
                return Response.getFailResponse("this user is already logged out");
            loggedInUsers.remove(user.getId());
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

    public User getUserById(UUID id) {
        return users.get(id);
    }

    public UUID getId(String userName) {
        return usernames.get(userName);
    }

    // i made these methods to avoid confusion between clients and users.
    public boolean isRegisteredUser(UUID id){
        return users.containsKey(id);
    }
    public Response<Boolean> isLoggedInUser(UUID clientCredentials){
        try {
            User user = users.get(clientCredentials);
            if (user == null) return Response.getFailResponse("User does not exist.");
            if (!loggedInUsers.containsKey(user.getId())) return Response.getFailResponse("User is logged-out.");
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
    
    public boolean isUserLoggedIn(UUID clientCredentials) {
        return loggedInUsers.containsKey(clientCredentials);
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
                securityController.encryptAndSavePassword(adminCredentials, "Admin1");
            }
        } catch (Exception ignored) {}
    }

    public Response<Boolean> registerAsAdmin(UUID clientCredentials, String username, String password) {
        if (!users.containsKey(clientCredentials)) return Response.getFailResponse("Passed client credentials do not correspond to an existing user.");
        if (usernames.containsKey(username)) return Response.getFailResponse("A user by that username already exists.");
        if (!users.get(clientCredentials).isAdmin()) return Response.getFailResponse("Only admins can register admins.");

        User newAdmin = new Admin(username, UUID.randomUUID());
        usernames.put(username, newAdmin.getId());
        users.put(newAdmin.getId(), newAdmin);
        return securityController.encryptAndSavePassword(newAdmin.getId(), password);
    }

    public void resetController() {
        singleton = new UserController();
    }

    // On remove item from store, goes over all the users with the item in their carts and removes it
    public void removeItemFromCarts(UUID storeId, Item item) {
        for (Client client : clients.values()) {
            Map<UUID, ShoppingBasket> baskets = client.getCart().getShoppingBaskets();
            if (!baskets.containsKey(storeId)) continue;
            baskets.get(storeId).getItems().remove(item.getId());
        }
        for (Client client : users.values()) {
            Map<UUID, ShoppingBasket> baskets = client.getCart().getShoppingBaskets();
            if (!baskets.containsKey(storeId)) continue;
            baskets.get(storeId).getItems().remove(item.getId());
        }
    }


    public Response<List<User>> getAllLoggedInUsers(UUID clientCredentials) {
        try {
            if (!users.containsKey(clientCredentials))
                return Response.getFailResponse("User does not exist.");
            if(!users.get(clientCredentials).isAdmin())
                return Response.getFailResponse("Only admins can delete users.");
            return Response.getSuccessResponse(loggedInUsers.values().stream().toList());
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }

    }

    public Response<List<User>> getNotLoggedInUsers(UUID clientCredentials) {
        try {
            if (!users.containsKey(clientCredentials))
                return Response.getFailResponse("User does not exist.");
            if(!users.get(clientCredentials).isAdmin())
                return Response.getFailResponse("Only admins can delete users.");
            return Response.getSuccessResponse(users.values().stream()
                    .filter((user) -> !loggedInUsers.containsKey(user.getId())).toList());
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> CancelSubscriptionNotRole(UUID adminCredentials, UUID clientCredentials){
        if (!users.containsKey(clientCredentials))
            return Response.getFailResponse("User does not exist.");
        if (!users.containsKey(adminCredentials))
            return Response.getFailResponse("this admin does not exist.");
        if(!users.get(adminCredentials).isAdmin())
            return Response.getFailResponse("Only admins can delete users.");
        User user = users.get(clientCredentials);
        if(user.getRoles().isEmpty()){
            return  deleteUser(adminCredentials,clientCredentials);
        }
        else return Response.getFailResponse("User have role cant unsubscribe user.");

    }

    public Response<List<Role>> getUserRoles(UUID clientCredentials) {
        User user = getUserById(clientCredentials);
        if (user == null)
            return Response.getFailResponse("User does not exist.");
        return Response.getSuccessResponse(new ArrayList<>(user.getRoles()));
    }

    public Response<Integer> numOfUsers() {
        try {
            int users = this.users.size();
            return Response.getSuccessResponse(users);
        }
        catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Integer> numOfClients() {
        try {
            int clients = this.clients.size();
            return Response.getSuccessResponse(clients);
        }
        catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<UUID> getAdminCredentials() {
        UUID admin = usernames.get("admin");
        if (admin == null) return Response.getFailResponse("There is no admin");
        return Response.getSuccessResponse(admin);
    }

    public ConcurrentHashMap<String, UUID> getUsernames() {
        return usernames;
    }

    public Response<Integer> numOfLoggedInUsers() {
        try {
            int loggedInUsers = this.loggedInUsers.size();
            return Response.getSuccessResponse(loggedInUsers);
        }
        catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
    
    public void loginFromSecurityQuestion(UUID id) {    //For use from security controller only
        loggedInUsers.put(id, users.get(id));
    }
    
    public List<UUID> getAdminIds() {
        List<UUID> admins = new ArrayList<>();
        for (User user : users.values()) {
            if (user.isAdmin()) admins.add(user.getId());
        }
        return admins;
    }
    
    public boolean hasUserPurchasedItem(UUID clientCredentials, UUID itemId) throws Exception {
        if (!users.containsKey(clientCredentials))
            throw new Exception("User does not exist");
        User user = users.get(clientCredentials);
        List<Purchase> purchases = user.getPurchases().stream().toList();
        return !purchases.stream()
                .filter(purchase -> purchase.getItemId().equals(itemId))
                .toList()
                .isEmpty();
    }
    
    public Response<User> getUserByUsername(String username) {
        try {
            if (usernames.containsKey(username)) {
                User user = users.get(usernames.get(username));
                if (user != null)
                    return Response.getSuccessResponse(user);
            }
            return Response.getFailResponse("User not found.");
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
}