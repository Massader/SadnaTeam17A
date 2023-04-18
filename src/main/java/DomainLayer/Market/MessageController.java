package DomainLayer.Market;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.Message;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.User;
import ServiceLayer.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;

public class MessageController {

    private static MessageController instance = null;
    private static final Object instanceLock = new Object();
    private final ConcurrentHashMap<UUID, ConcurrentHashMap<UUID, Message>> messages;
    private final ConcurrentHashMap<UUID, Complaint> complaints;

    private MessageController() {
        messages = new ConcurrentHashMap<>();
        complaints = new ConcurrentHashMap<>();
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
        if (clientCredentials != sender) {
            Store store = StoreController.getInstance().getStore(sender);
            if (store == null) return Response.getFailResponse("Message sender credentials do not match logged in user.");
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_COMMUNICATION))
                return Response.getFailResponse("Logged in user does not have the correct permissions to send a message for store " + sender);
        }
        if (!messages.containsKey(recipient)) messages.put(recipient, new ConcurrentHashMap<>());
        messages.get(recipient).put(message.getId(), message);
        return Response.getSuccessResponse(message.getId());
    }

    public Response<List<Message>> getMessages(UUID clientCredentials, UUID recipient) {
        if (clientCredentials != recipient) {
            Store store = StoreController.getInstance().getStore(recipient);
            if (store == null) return Response.getFailResponse("Passed recipient ID do not match logged in user or an existing store.");
            if (!store.checkPermission(clientCredentials, StorePermissions.STORE_COMMUNICATION))
                return Response.getFailResponse("Logged in user does not have the correct permissions to read a message to store " + recipient);
        }
        if (!messages.containsKey(clientCredentials)) {
            if (UserController.getInstance().isRegisteredUser(recipient)) {
                messages.put(recipient, new ConcurrentHashMap<>());
            }
            else return Response.getFailResponse("Messages not found.");
        }
        return Response.getSuccessResponse(new ArrayList<>(messages.get(clientCredentials).values()));
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
        return Response.getSuccessResponse(new ArrayList<>(complaints.values()));
    }

    public Response<UUID> sendComplaint(UUID clientCredentials, UUID purchaseId, String body) {
        Complaint complaint = new Complaint(body, clientCredentials, purchaseId);
        complaints.put(complaint.getId(), complaint);
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

}
