package DomainLayer.Market;

import DomainLayer.Market.Users.Client;
import DomainLayer.Market.Users.Message;
import DomainLayer.Market.Users.User;
import DomainLayer.Security.SecurityController;
import ServiceLayer.Response;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageController {

    private static MessageController singleton = null;

    private MessageController() {
    }

    public static synchronized MessageController getInstance() {
        if (singleton == null)
            singleton = new MessageController();
        return singleton;
    }

//    public Response<UUID> sendMessage(UUID clientCredentials, UUID sender, UUID recipient, String body){
//        Message message = new Message(body, sender, recipient);
//    }
}
