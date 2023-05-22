package DomainLayer.Market;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.User;
import ServiceLayer.Response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MessageController {

    private static MessageController instance = null;
    private static final Object instanceLock = new Object();
    private NotificationController notificationController;
    private StoreController storeController;
    private UserController userController;
    private ConcurrentHashMap<UUID, ConcurrentHashMap<UUID, Message>> messages;
    private ConcurrentHashMap<UUID, Complaint> complaints;

    private MessageController() { }
    
    public void init() {
        messages = new ConcurrentHashMap<>();
        complaints = new ConcurrentHashMap<>();
        notificationController = NotificationController.getInstance();
        storeController = StoreController.getInstance();
        userController = UserController.getInstance();
    }
    
    public static MessageController getInstance() {
        synchronized (instanceLock) {
            if (instance == null)
                instance = new MessageController();
        }
        return instance;
    }
    
    public Response<UUID> sendMessage(UUID clientCredentials, UUID sender, UUID recipient, String body){
        Message message = new Message(body, sender, recipient);
        if (!clientCredentials.equals(sender)) {
            Store store = storeController.getStore(sender);
            if (store == null) return Response.getFailResponse("Message sender credentials do not match an existing store.");
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_COMMUNICATION)
                    && !store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("Logged in user does not have the correct permissions to send a message for store " + sender);
        }
        else {
            if (!userController.isRegisteredUser(sender))
                return Response.getFailResponse("Message sender is not a registered user.");
            if (!userController.isRegisteredUser(recipient) && !storeController.storeExist(recipient))
                return Response.getFailResponse("Message recipient is not a registered user or existing store.");
            if (!userController.isUserLoggedIn(sender))
                return Response.getFailResponse("Only logged in users can send messages.");
        }
        if (!messages.containsKey(recipient)) messages.put(recipient, new ConcurrentHashMap<>());
        messages.get(recipient).put(message.getId(), message);
        if (storeController.storeExist(recipient)) {
            Store store = storeController.getStore(recipient);
            List<UUID> rolesToNotify = store.getStoreOwners();
            for (UUID manager : store.getStoreManagers()) {
                if (store.checkPermission(manager, StorePermissions.STORE_COMMUNICATION))
                    rolesToNotify.add(manager);
            }
            for (UUID roleToNotify : rolesToNotify) {
                notificationController.sendNotification(roleToNotify,
                        "Store " + store.getName() + " has received a new message!");
            }
        }
        else notificationController.sendNotification(recipient, "New message received!");
        return Response.getSuccessResponse(message.getId());
    }

    public Response<List<Message>> getMessages(UUID clientCredentials, UUID recipient) {
        if (clientCredentials != recipient) {
            Store store = StoreController.getInstance().getStore(recipient);
            if (store == null) return Response.getFailResponse("Passed recipient ID do not match logged in user or an existing store.");
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_COMMUNICATION)
                    && !store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                return Response.getFailResponse("Logged in user does not have the correct permissions to read a message to store " + recipient);
        }
        if (!messages.containsKey(clientCredentials)) {
            if (UserController.getInstance().isRegisteredUser(recipient)) {
                messages.put(recipient, new ConcurrentHashMap<>());
            }
            else return Response.getFailResponse("Messages not found.");
        }
        List<Message> output = new ArrayList<>(messages.get(clientCredentials).values());
        output.sort(Comparator.comparing(Message::getTimestamp));
        return Response.getSuccessResponse(output);
    }

    public Response<Message> getMessage(UUID clientCredentials, UUID recipient, UUID messageId) {
        if (clientCredentials != recipient) {
            Store store = StoreController.getInstance().getStore(recipient);
            if (store == null) return Response.getFailResponse("Passed recipient ID do not match logged in user or an existing store.");
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_COMMUNICATION))
                return Response.getFailResponse("Logged in user does not have the correct permissions to read a message to store " + recipient);
        }
        if (!messages.containsKey(clientCredentials) || !messages.get(clientCredentials).containsKey(messageId))
            return Response.getFailResponse("Message not found.");
        return Response.getSuccessResponse(messages.get(clientCredentials).get(messageId));
    }

    public Response<Complaint> getComplaint(UUID clientCredentials, UUID complaintId) {
        Response<User> userResponse = UserController.getInstance().getUser(clientCredentials);
        if (userResponse.isError())
            return Response.getFailResponse("Client credentials passed do not match existing user.");
        if (!userResponse.getValue().isAdmin())
            return Response.getFailResponse("Client credentials passed do not match to an admin.");
        if (!messages.containsKey(clientCredentials)) return Response.getFailResponse("Complaint not found.");
        return Response.getSuccessResponse(complaints.get(complaintId));
    }

    public Response<List<Complaint>> getComplaints(UUID clientCredentials) {
        Response<User> userResponse = UserController.getInstance().getUser(clientCredentials);
        if (userResponse.isError())
            return Response.getFailResponse("Client credentials passed do not match existing user.");
        if (!userResponse.getValue().isAdmin())
            return Response.getFailResponse("Client credentials passed do not match to an admin.");
        List<Complaint> output = new ArrayList<>(complaints.values());
        output.sort(Comparator.comparing(Complaint::getTimestamp));
        return Response.getSuccessResponse(output);
    }

    public Response<UUID> sendComplaint(UUID clientCredentials, UUID purchaseId, String body) {
        Complaint complaint = new Complaint(body, clientCredentials, purchaseId);
        complaints.put(complaint.getId(), complaint);
        List<UUID> adminIds = UserController.getInstance().getAdminIds();
        for (UUID id : adminIds) {
            notificationController.sendNotification(id, "New complaint received!");
        }
        return Response.getSuccessResponse(complaint.getId());
    }

    public Response<Boolean> assignAdminToComplaint(UUID clientCredentials, UUID complaintId) {
        Response<User> userResponse = UserController.getInstance().getUser(clientCredentials);
        if (userResponse.isError()) {
            return Response.getFailResponse(userResponse.getMessage());
        }
        if (!userResponse.getValue().isAdmin()) {
            return Response.getFailResponse("Only admins may assign themselves to complaints.");
        }
        if (!complaints.containsKey(complaintId)) {
            return Response.getFailResponse("Complaint not found.");
        }
        complaints.get(complaintId).assignAdmin(clientCredentials);
        return Response.getSuccessResponse(true);
    }

    public void resetController() {
        instance = new MessageController();
    }
    
    public Response<List<Complaint>> getAssignedComplaints(UUID clientCredentials) {
        Response<User> userResponse = UserController.getInstance().getUser(clientCredentials);
        if (userResponse.isError())
            return Response.getFailResponse("Client credentials passed do not match existing user.");
        if (!userResponse.getValue().isAdmin())
            return Response.getFailResponse("Client credentials passed do not match to an admin.");
        List<Complaint> output = new ArrayList<>(complaints.values()
                .stream()
                .filter(complaint -> complaint.getAssignedAdmin().equals(clientCredentials))
                .toList());
        output.sort(Comparator.comparing(Complaint::getTimestamp));
        return Response.getSuccessResponse(output);
    }
}
