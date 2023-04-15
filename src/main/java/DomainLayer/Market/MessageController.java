package DomainLayer.Market;

import DomainLayer.Market.Users.Message;
import ServiceLayer.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MessageController {

    private static MessageController instance = null;
    private static final Object instanceLock = new Object();
    private final ConcurrentHashMap<UUID, ConcurrentLinkedDeque<Message>> messages;

    private MessageController() {
        messages = new ConcurrentHashMap<>();
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
        if (!messages.containsKey(recipient)) messages.put(recipient, new ConcurrentLinkedDeque<>());
        messages.get(recipient).addFirst(message);
        return Response.getSuccessResponse(message.getId());
    }

    public Response<List<Message>> getMessages(UUID clientCredentials) {
        if (messages.containsKey(clientCredentials)) {
            return Response.getSuccessResponse(new ArrayList<>(messages.get(clientCredentials)));
        }
        else return Response.getFailResponse("No user with the passed client credentials.");
    }

    public Response<Message> getMessage(UUID clientCredentials, UUID messageId) {
        if (messages.containsKey(clientCredentials)) {
            List<Message> userMessages = new ArrayList<>(messages.get(clientCredentials));
            for (Message message : userMessages) {
                if (message.getId().equals(messageId))
                    return Response.getSuccessResponse(message);
            }
            return Response.getFailResponse("Message not found.");
        }
        return Response.getFailResponse("No user with the passed client credentials.");
    }
}
