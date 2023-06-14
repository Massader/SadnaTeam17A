package DomainLayer.Market;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import DataAccessLayer.PurchaseRepository;
import DataAccessLayer.RepositoryFactory;
import DataAccessLayer.controllers.PurchaseDalController;
import DataAccessLayer.controllers.StoreDalController;
import DataAccessLayer.controllers.UserDalController;
import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.PurchaseTypes.Bid;
import DomainLayer.Market.Stores.PurchaseTypes.BidPurchase;
import DomainLayer.Market.Stores.PurchaseTypes.PurchaseType;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.*;
import DomainLayer.Security.SecurityController;
import ServiceLayer.Response;
import DomainLayer.Market.Users.Roles.*;


public class UserController {

    private static UserController singleton = null;
//    private ConcurrentHashMap<UUID, User> users;// for registered clients only!
//    private ConcurrentHashMap<String, UUID> usernames;    // for registered clients only!
    private ConcurrentHashMap<UUID, User> loggedInUsers; // for logged-in users only!
    private SecurityController securityController;
    private ConcurrentHashMap<UUID, Client> clients;  // for non-registered clients only!
    private StoreController storeController;
    private NotificationController notificationController;
    public static RepositoryFactory repositoryFactory;
    private UserDalController userDalController;
    private StoreDalController storeDalController;
    private PurchaseDalController purchaseDalController;
    private UserController() {
    }

    public static synchronized UserController getInstance() {
        if (singleton == null) {
            singleton = new UserController();
        }
        return singleton;
    }

    public void init(RepositoryFactory repositoryFactory) {
//        users = new ConcurrentHashMap<>();
//        usernames = new ConcurrentHashMap<>();
        this.repositoryFactory = repositoryFactory;
        userDalController = UserDalController.getInstance(repositoryFactory);
        loggedInUsers = new ConcurrentHashMap<>();
        securityController = SecurityController.getInstance();
        clients = new ConcurrentHashMap<>();
        storeController = StoreController.getInstance();
        notificationController = NotificationController.getInstance();
        storeDalController = StoreDalController.getInstance(repositoryFactory);
        purchaseDalController = PurchaseDalController.getInstance(repositoryFactory);
        registerDefaultAdmin();
    }

    public Response<Boolean> setAsFounder(User user, UUID storeId, Role role){
        try {
            user.addStoreRole(role);
            userDalController.saveUser(user);
            userDalController.saveRole(role);

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
            User user = userDalController.getUser(username);
            if(user == null)
                return Response.getFailResponse("User is not registered in the system.");
            if (loggedInUsers.containsKey(user.getId()))
                return Response.getFailResponse("User is already logged in, please log out first.");
//            if (!usernames.containsKey(username) || !users.containsKey(usernames.get(username)))
            //validate the password
            Response<Boolean> securityResponse = securityController.validatePassword(getId(username), password);
            if (securityResponse.isError()) return Response.getFailResponse(securityResponse.getMessage());
            if (securityResponse.getValue()) {
                //transfer the client to the logged in users, and delete it from the non registered clients list
//                User user = users.get(getId(username));
                loggedInUsers.put(user.getId(), user);
                closeClient(clientCredentials);
//                return Response.getSuccessResponse(users.get(usernames.get(username)));
                return Response.getSuccessResponse(user);
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
//            synchronized (usernames) {
            if (userDalController.userExists(username))
                return Response.getFailResponse("This username is already in use.");
                //add user
            return loadUser(username, password, UUID.randomUUID());
//            }
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    // add a new user to all the data structured
    private Response<Boolean> loadUser(String username, String password, UUID id) {
        try {
            User user = new User(username);
            user.getCart().setClient(user);
            userDalController.saveUser(user);
            Response<Boolean> response = securityController.encryptAndSavePassword(user.getId(), password);
            if (response.isError()) {
                userDalController.deleteUser(user);
            }
            return response;
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }


    public Response<UUID> createClient() {
        try {
            UUID id = UUID.randomUUID();
            Client client = new Client(id);
            ShoppingCart shoppingCart = new ShoppingCart(client);
            client.setCart(shoppingCart);
//            ClientRepository clientRepository = repositoryFactory.getClientRepository();
//            clientRepository.save(client);
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
        for(ShoppingBasket shoppingBasket : shoppingCart.getShoppingBaskets())
            deleteShoppingBasket(shoppingBasket);
    }

    private void deleteShoppingBasket(ShoppingBasket shoppingBasket){
        UUID storeId = shoppingBasket.getStoreId();
        Store store = storeController.getStore(storeId);
        for(CartItem cartItem : shoppingBasket.getItems()){
            Item item = cartItem.getItem();
            item.addQuantity(cartItem.getQuantity());
            storeDalController.saveItem(item);
        }
        shoppingBasket.getItems().clear();
        purchaseDalController.saveBasket(shoppingBasket);
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
                Item item = itemResponse.getValue();
                if (item.getPurchaseType().getType().equals(PurchaseType.BID_PURCHASE) &&
                        ((BidPurchase)item.getPurchaseType()).isBidAccepted(clientCredentials)) {
                    Bid bid = item.getBid(clientCredentials);
                    CartItem bidItem = new CartItem(item, bid.getQuantity(), bid.getPrice());
                    if (shoppingCart.addItemToCart(bidItem, storeId, bidItem.getQuantity())) {

                        item.removeBid(clientCredentials);
                        return Response.getSuccessResponse(true);
                    }
                    else return Response.getFailResponse("Failed to add item to cart");
                }
                else if (item.getPurchaseType().getType().equals(PurchaseType.DIRECT_PURCHASE) &&
                        shoppingCart.addItemToCart(new CartItem(item, quantity, item.getPrice()), storeId, quantity)) {
                    purchaseDalController.saveCart(shoppingCart);
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
            Client client = getClientOrUser(clientCredentials);
            if (client==null)
                return Response.getFailResponse("User does not exist");
            ShoppingCart shoppingCart = client.getCart();
            ShoppingBasket shoppingBasket = shoppingCart.getBasketByStoreId(storeId);
            CartItem cartItem = shoppingBasket.getCartItem(itemId);
//            if (itemResponse.isError())
//                return Response.getFailResponse(itemResponse.getMessage());
            synchronized (cartItem) {
//                ShoppingCart shoppingCart = getClientOrUser(clientCredentials).getCart();
                if (shoppingCart.removeItemFromCart(itemId, storeId, quantity)) {
                    purchaseDalController.saveCart(shoppingCart);
                    return Response.getSuccessResponse(true);
                }
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
//            if (!users.containsKey(userId))
            if (!userDalController.userExists(clientCredentials))
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
                return Response.getFailResponse("User doesn't have permission to appoint owners.");
            Response<User> response2 = this.getUser(appointee);
            if(response2.isError())
                return Response.getFailResponse(response2.getMessage());
            if (!loggedInUsers.containsKey(clientCredentials))
                return Response.getFailResponse("Appointing user is not logged in.");
            if(storeController.getStore(storeId).hasRole(appointee) &&
                    storeController.getStore(storeId).checkPermission(appointee, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("User already owner of the shop.");
            User user = response2.getValue();
            if (user == null)
                return Response.getFailResponse("User returned was null");
            Store store = storeController.getStore(storeId);
            List<OwnerPetition> petitions = store.getOwnerPetitions().stream()
                    .filter(element -> element.getAppointeeId().equals(user.getId()) &&
                            element.getStoreId().equals(storeId))
                    .toList();
            OwnerPetition petition;
            if (petitions.isEmpty()) {
                petition = new OwnerPetition(user.getId(), clientCredentials, storeId);
                store.getOwnerPetitions().add(petition);
                storeDalController.saveStore(store);
            }
            else {
                petition = petitions.get(0);
                petition.approveAppointment(clientCredentials);
            }
            if (petition.getOwnersList().containsAll(storeController.getStore(storeId).getStoreOwners())) {
                StoreOwner storeOwner = new StoreOwner(store);
                store.addRole(getUserById(appointee), storeOwner);
                store.getOwner(clientCredentials).addAppointee(appointee);
                notificationController.sendNotification(appointee,
                        "Your appointment as a store owner for " + store.getName() + " has been approved by all owners.");
                store.getOwnerPetitions().remove(petition);
                storeDalController.saveStore(store);
            }
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
            if (!loggedInUsers.containsKey(clientCredentials))
                return Response.getFailResponse("Appointing user is not logged in.");
            if(storeController.getStore(storeId).hasRole(appointee))
                return Response.getFailResponse("User already manager in the shop.");
            Store store = storeController.getStore(storeId);
            StoreManager storeManager = new StoreManager(store);
            User user = response2.getValue();
//            user.addStoreRole(storeManager);
            store.addRole(user, storeManager);
            store.getOwner(clientCredentials).addAppointee(appointee);
//            userDalController.saveUser(user);
//            userDalController.saveRole(storeManager);
            storeDalController.saveStore(store);
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
            if(!store.hasRole(roleToRemove))
                return Response.getFailResponse("User does not have role in the shop.");
            if(!store.getOwner(clientCredentials).getAppointees().contains(roleToRemove))
                return Response.getFailResponse("Staff member was not appointed by this owner.");
            User user = response2.getValue();
//            user.removeStoreRole(storeId);
            if (store.getOwner(roleToRemove) != null) {
                for (UUID appointee : store.getOwner(roleToRemove).getAppointees())
                    removeStoreRole(roleToRemove, appointee, storeId);
            }
            Role role = store.removeRole(roleToRemove, user);

            store.getOwner(clientCredentials).removeAppointee(roleToRemove);
            storeDalController.saveStore(store);
            repositoryFactory.roleRepository.delete(role);
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
            if (!userDalController.userExists(manager))
                return Response.getFailResponse("Manager does not exist.");
//            if(!storeController.getStore(storeId).hasRole(manager))

//            List<Role> roles = userDalController.getRoles(manager);
            Role role =  storeController.getStore(storeId).getRoleByUserId(manager);
//            boolean found = false;
//            for(Role role : roles) {
//                if (role.getStore().getStoreId().equals(storeId))
//                    found = true;
//            }
//            if(!found)
            if(role == null)
                return Response.getFailResponse("User is not store manager.");
            role.setPermissions(permissions);
            userDalController.saveRole(role);
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> deleteUser(UUID clientCredentials, UUID userId) {
        try {
            if (clientCredentials.equals(userId))
                return Response.getFailResponse("Admin can't delete himself.");
            User user1 = userDalController.getUser(clientCredentials);
            User user2 = userDalController.getUser(userId);
            if (user2 == null || user1 == null)
                return Response.getFailResponse("User does not exist.");
            if(!user1.isAdmin())
                return Response.getFailResponse("Only admins can delete users.");
            if (user2.isAdmin())
                return Response.getFailResponse("Can't delete default admin.");
            if (loggedInUsers.containsKey(user2.getId()))
                logout(userId);
            //remove from both HashMaps
//            user = users.remove(userId);
            userDalController.deleteUser(user2);
            deleteShoppingCart(user2.getCart());
//            usernames.remove(user2.getUsername());
            //remove roles
            for (Role role: user2.getRoles()) {
                removeStoreRole(clientCredentials, userId, role.getStore().getStoreId());
                if (role.getPermissions().contains(StorePermissions.STORE_FOUNDER))
                    storeController.shutdownStore(clientCredentials, role.getStore().getStoreId());
            }
            securityController.removePassword(userId);
            return Response.getSuccessResponse(true);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<UUID> logout(UUID userId) {
        try {
            User user = userDalController.getUser(userId);
            if (user == null)
                return Response.getFailResponse("this user ID does not exist");
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
            User user = userDalController.getUser(userId);
            if(user == null)
                return Response.getFailResponse("User does not exist.");
            return Response.getSuccessResponse(user);
        }
        catch (Exception exception){
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public User getUserById(UUID userId) {
        return userDalController.getUser(userId);
    }

    public UUID getId(String userName) {
        return userDalController.getUser(userName).getId();
    }

    // i made these methods to avoid confusion between clients and users.
    public boolean isRegisteredUser(UUID id){
        return userDalController.userExists(id);
    }
    public Response<Boolean> isLoggedInUser(UUID clientCredentials){
        try {
            User user = userDalController.getUser(clientCredentials);
            if(user == null)
                return Response.getFailResponse("User does not exist.");
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
        if(!userDalController.userExists(id)){
            return Response.getFailResponse("The client does not have user permission  ");
        }
        return Response.getSuccessResponse(true);
    }

    public boolean isNonRegisteredClient(UUID id){
        return clients.containsKey(id);
    }

    // get client in all kind!
    public Client getClientOrUser(UUID id){
        User user = userDalController.getUser(id);
        if (user != null)
            return user;
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
            if(!userDalController.userExists("admin")){
                Admin admin = new Admin("admin");
                userDalController.saveUser(admin);
//                users.put(adminCredentials, new Admin("admin", adminCredentials));
                securityController.encryptAndSavePassword(admin.getId(), "Admin1");
            }
        } catch (Exception ignored) {

        }
    }

    public Response<Boolean> registerAsAdmin(UUID clientCredentials, String username, String password) {
//        if (!users.containsKey(clientCredentials))
        if(userDalController.userExists(clientCredentials))
            return Response.getFailResponse("Passed client credentials do not correspond to an existing user.");
        if (userDalController.userExists(username)) return Response.getFailResponse("A user by that username already exists.");
        if (!userDalController.getUser(clientCredentials).isAdmin()) return Response.getFailResponse("Only admins can register admins.");

        User newAdmin = new Admin(username, UUID.randomUUID());
//        usernames.put(username, newAdmin.getId());
//        users.put(newAdmin.getId(), newAdmin);
        userDalController.saveUser(newAdmin);
        return securityController.encryptAndSavePassword(newAdmin.getId(), password);
    }

    public void resetController() {
        singleton = new UserController();
    }

    // On remove item from store, goes over all the users with the item in their carts and removes it
    /*
    public void removeItemFromCarts(UUID storeId, Item item) {
        for (Client client : clients.values()) {
            Map<UUID, ShoppingBasket> baskets = client.getCart().getShoppingBasketsMap();
            if (!baskets.containsKey(storeId)) continue;
            baskets.get(storeId).getItems().remove(item.getId());
        }
        for (Client client : userDalController.getAllUsers()) {
            Map<UUID, ShoppingBasket> baskets = client.getCart().getShoppingBasketsMap();
            if (!baskets.containsKey(storeId)) continue;
            baskets.get(storeId).getItems().remove(item.getId());
        }
    }
     */

    public void removeItemFromCarts(UUID storeId, Item item) {
        for (Client client : clients.values()) {
            Collection<ShoppingBasket> baskets = client.getCart().getShoppingBaskets();
            if (baskets.stream().noneMatch(basket -> basket.validateStore(storeId))) continue;
            client.getCart().getBasketByStoreId(storeId).getItems().remove(item.getId());
        }
        for (Client client : userDalController.getAllUsers()) {
            Collection<ShoppingBasket> baskets = client.getCart().getShoppingBaskets();
            if (baskets.stream().noneMatch(basket -> basket.validateStore(storeId))) continue;
            client.getCart().getBasketByStoreId(storeId).getItems().remove(item.getId());
        }
    }


    public Response<List<User>> getAllLoggedInUsers(UUID clientCredentials) {
        try {
            if (!userDalController.userExists(clientCredentials))
                return Response.getFailResponse("User does not exist.");
            if(!userDalController.getUser(clientCredentials).isAdmin())
                return Response.getFailResponse("Only admins can delete users.");
            return Response.getSuccessResponse(loggedInUsers.values().stream().toList());
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }

    }

    public Response<List<User>> getNotLoggedInUsers(UUID clientCredentials) {
        try {
            if (!userDalController.userExists(clientCredentials))
                return Response.getFailResponse("User does not exist.");
            if(!userDalController.getUser(clientCredentials).isAdmin())
                return Response.getFailResponse("Only admins can delete users.");
            return Response.getSuccessResponse(userDalController.getAllUsers().stream()
                    .filter((user) -> !loggedInUsers.containsKey(user.getId())).toList());
        }
        catch(Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }

    public Response<Boolean> CancelSubscriptionNotRole(UUID adminCredentials, UUID clientCredentials){
        if (!userDalController.userExists(clientCredentials))
            return Response.getFailResponse("User does not exist.");
        if (!userDalController.userExists(adminCredentials))
            return Response.getFailResponse("this admin does not exist.");
        if(!userDalController.getUser(adminCredentials).isAdmin())
            return Response.getFailResponse("Only admins can delete users.");
        User user = userDalController.getUser(clientCredentials);
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
            int users = (int) repositoryFactory.userRepository.count();
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
        User user = userDalController.getAdmin();
        if (user == null) return Response.getFailResponse("There is no admin");
        return Response.getSuccessResponse(user.getId());
    }


//    public ConcurrentHashMap<String, UUID> getUsernames() {
//        Map
//        for(User u : userDalController.getAllUsers())
//    }

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
        loggedInUsers.put(id, userDalController.getUser
                (id));
    }
    
    public List<UUID> getAdminIds() {
        List<UUID> admins = new ArrayList<>();
        for (User user : userDalController.getAllUsers()) {
            if (user.isAdmin()) admins.add(user.getId());
        }
        return admins;
    }
    
    public boolean hasUserPurchasedItem(UUID clientCredentials, UUID itemId) throws Exception {
        if (!userDalController.userExists(clientCredentials))
            throw new Exception("User does not exist");
        User user = userDalController.getUser(clientCredentials);
        List<Purchase> purchases = user.getPurchases().stream().toList();
        return !purchases.stream()
                .filter(purchase -> purchase.getItemId().equals(itemId))
                .toList()
                .isEmpty();
    }
    
    public Response<User> getUserByUsername(String username) {
        try {
            User user = userDalController.getUser(username);
            if (user == null) {
                return Response.getFailResponse("User not found.");
            }
           return Response.getSuccessResponse(user);
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
    
    public Response<Double> getItemDiscount(UUID clientCredentials, UUID storeId, UUID itemId) {
        try {
            if (!isRegisteredUser(clientCredentials) && !isNonRegisteredClient(clientCredentials))
                throw new Exception("User does not exist");
            Client client = getClientOrUser(clientCredentials);
            if (!storeController.storeExist(storeId))
                throw new Exception("Store does not exist.");
            if (!storeController.itemExist(itemId))
                throw new Exception("Item does not exist");
            Store store = storeController.getStore(storeId);
            ShoppingBasket shoppingBasket = client.getCart().getShoppingBaskets().get(storeId);
            if (!shoppingBasket.getItems().containsKey(itemId))
                throw new Exception("No such item in the user's shopping cart.");
            return Response.getSuccessResponse(store.calculateItemDiscount(shoppingBasket, itemId) * 100); // 0.X to X as it is presented in the client
        } catch (Exception exception) {
            return Response.getFailResponse(exception.getMessage());
        }
    }
}