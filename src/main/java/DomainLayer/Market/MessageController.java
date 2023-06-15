package DomainLayer.Market;

import DataAccessLayer.RepositoryFactory;
import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.Roles.StorePermissions;
import DomainLayer.Market.Users.User;
import ServiceLayer.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MessageController {

    private static MessageController instance = null;
    private static final Object instanceLock = new Object();
    private NotificationController notificationController;
    private StoreController storeController;
    private UserController userController;
    private ConcurrentHashMap<UUID, ConcurrentHashMap<UUID, Message>> messages;
    private ConcurrentHashMap<UUID, Complaint> complaints;
    private RepositoryFactory repositoryFactory;

    private MessageController() { }
    
    public void init(RepositoryFactory repositoryFactory) {
        messages = new ConcurrentHashMap<>();
        complaints = new ConcurrentHashMap<>();
        notificationController = NotificationController.getInstance();
        storeController = StoreController.getInstance();
        userController = UserController.getInstance();
        this.repositoryFactory = repositoryFactory;
    }
    
    public static MessageController getInstance() {
        synchronized (instanceLock) {
            if (instance == null)
                instance = new MessageController();
        }
        return instance;
    }
    
    public Response<UUID> sendMessage(UUID clientCredentials, UUID sender, UUID recipient, String body){
        try {
            Message message = new Message(body, sender, recipient);
            if (!clientCredentials.equals(sender)) {
                Store store = storeController.getStore(sender);
                if (store == null)
                    return Response.getFailResponse("Message sender credentials do not match an existing store.");
                if (!store.checkPermission(clientCredentials, StorePermissions.STORE_COMMUNICATION)
                        && !store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                    return Response.getFailResponse("Logged in user does not have the correct permissions to send a message for store " + sender);
            } else {
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
                repositoryFactory.messageRepository.save(message);
                for (UUID roleToNotify : rolesToNotify) {
                    notificationController.sendNotification(roleToNotify,
                            "Store " + store.getName() + " has received a new message!");
                }
            } else notificationController.sendNotification(recipient, "New message received!");
            return Response.getSuccessResponse(message.getId());
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<List<Message>> getMessages(UUID clientCredentials, UUID recipient) {
        try {
            if (clientCredentials != recipient) {
                Store store = StoreController.getInstance().getStore(recipient);
                if (store == null)
                    return Response.getFailResponse("Passed recipient ID do not match logged in user or an existing store.");
                if (!store.checkPermission(clientCredentials, StorePermissions.STORE_COMMUNICATION)
                        && !store.checkPermission(clientCredentials, StorePermissions.STORE_OWNER))
                    return Response.getFailResponse("Logged in user does not have the correct permissions to read a message to store " + recipient);
            }
//            if (!messages.containsKey(recipient)) {
//                if (UserController.getInstance().isRegisteredUser(recipient)) {
//                    messages.put(recipient, new ConcurrentHashMap<>());
//                } else return Response.getFailResponse("Messages not found.");
//            }
//            List<Message> output = new ArrayList<>(messages.get(recipient).values());
            List<Message> output = repositoryFactory.messageRepository.findByRecipient( recipient);
            if (output.isEmpty())
                return Response.getFailResponse("Message not found.");
            output.sort(Comparator.comparing(Message::getTimestamp));
            return Response.getSuccessResponse(output);
//            return Response.getSuccessResponse(output);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<Message> getMessage(UUID clientCredentials, UUID recipient, UUID messageId) {
        try {
            if (clientCredentials != recipient) {
                Store store = StoreController.getInstance().getStore(recipient);
                if (store == null)
                    return Response.getFailResponse("Passed recipient ID do not match logged in user or an existing store.");
                if (!store.checkPermission(clientCredentials, StorePermissions.STORE_COMMUNICATION))
                    return Response.getFailResponse("Logged in user does not have the correct permissions to read a message to store " + recipient);
            }
//            if (!messages.containsKey(clientCredentials) || !messages.get(clientCredentials).containsKey(messageId)) {
            Optional<Message> message = repositoryFactory.messageRepository.findById(messageId);
            if (message.isEmpty())
                    return Response.getFailResponse("Message not found.");
            return Response.getSuccessResponse(messages.get(clientCredentials).get(messageId));
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<Complaint> getComplaint(UUID clientCredentials, UUID complaintId) {
        try {
            Response<User> userResponse = UserController.getInstance().getUser(clientCredentials);
            if (userResponse.isError())
                return Response.getFailResponse("Client credentials passed do not match existing user.");
            if (!userResponse.getValue().isAdmin())
                return Response.getFailResponse("Client credentials passed do not match to an admin.");
            if (!messages.containsKey(clientCredentials)) return Response.getFailResponse("Complaint not found.");
            return Response.getSuccessResponse(complaints.get(complaintId));
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<List<Complaint>> getComplaints(UUID clientCredentials) {
        try {
            Response<User> userResponse = UserController.getInstance().getUser(clientCredentials);
            if (userResponse.isError())
                return Response.getFailResponse("Client credentials passed do not match existing user.");
            if (!userResponse.getValue().isAdmin())
                return Response.getFailResponse("Client credentials passed do not match to an admin.");
            List<Complaint> output = new ArrayList<>(complaints.values());
            output.sort(Comparator.comparing(Complaint::getTimestamp));
            return Response.getSuccessResponse(output);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<UUID> sendComplaint(UUID clientCredentials, UUID purchaseId, UUID storeId, UUID itemId, String body) {
        try {
            Complaint complaint = new Complaint(body, clientCredentials, purchaseId, storeId, itemId);
            complaints.put(complaint.getId(), complaint);
            List<UUID> adminIds = UserController.getInstance().getAdminIds();
            repositoryFactory.complaintRepository.save(complaint);
            for (UUID id : adminIds) {
                notificationController.sendNotification(id, "New complaint received!");
            }
            return Response.getSuccessResponse(complaint.getId());
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public Response<Boolean> assignAdminToComplaint(UUID clientCredentials, UUID complaintId) {
        try {
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
            Complaint complaint = complaints.get(complaintId);
            complaint.assignAdmin(clientCredentials);
            repositoryFactory.complaintRepository.save(complaint);
            return Response.getSuccessResponse(true);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }

    public void resetController() {
        instance = new MessageController();
    }
    
    public Response<List<Complaint>> getAssignedComplaints(UUID clientCredentials) {
        try {
            Response<User> userResponse = UserController.getInstance().getUser(clientCredentials);
            if (userResponse.isError())
                return Response.getFailResponse("Client credentials passed do not match existing user.");
            if (!userResponse.getValue().isAdmin())
                return Response.getFailResponse("Client credentials passed do not match to an admin.");
            List<Complaint> output = new ArrayList<>(repositoryFactory.complaintRepository.findAll()
                    .stream()
                    .filter(complaint -> complaint.getAssignedAdmin().equals(clientCredentials))
                    .toList());
            output.sort(Comparator.comparing(Complaint::getTimestamp));
            return Response.getSuccessResponse(output);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<Boolean> closeComplaint(UUID clientCredentials, UUID complaintId) {
        try {
            if (userController.getUserById(clientCredentials) == null)
                return Response.getFailResponse("Client credentials passed do not match existing user.");
            if (!userController.getUserById(clientCredentials).isAdmin())
                return Response.getFailResponse("Only admins can manage complaints.");
            Optional<Complaint> complaintOptional=repositoryFactory.complaintRepository.findById(complaintId);
            if (complaintOptional.isEmpty())
                return Response.getFailResponse("Complaint does not exist.");
            Complaint complaint = complaintOptional.get();
            if (!complaint.getAssignedAdmin().equals(clientCredentials))
                return Response.getFailResponse("Only the assigned admin can close a complaint.");
            complaint.closeComplaint();
            repositoryFactory.complaintRepository.save(complaint);
            return Response.getSuccessResponse(true);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
    
    public Response<Boolean> reopenComplaint(UUID clientCredentials, UUID complaintId) {
        try {
            if (userController.getUserById(clientCredentials) == null)
                return Response.getFailResponse("Client credentials passed do not match existing user.");
            if (!userController.getUserById(clientCredentials).isAdmin())
                return Response.getFailResponse("Only admins can manage complaints.");
            if (complaints.get(complaintId) == null)
                return Response.getFailResponse("Complaint does not exist.");
            if (!complaints.get(complaintId).getAssignedAdmin().equals(clientCredentials))
                return Response.getFailResponse("Only the assigned admin can reopen a complaint.");
            complaints.get(complaintId).reopenComplaint();
            return Response.getSuccessResponse(true);
        } catch (Exception e) {
            return Response.getFailResponse(e.getMessage());
        }
    }
}
