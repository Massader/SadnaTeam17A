package DomainLayer.Market;

import ServiceLayer.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class NotificationController {
    private ConcurrentHashMap<UUID, ConcurrentHashMap<UUID, Notification>> notifications;
    private static NotificationController instance = null;
    private static Object instanceLock = new Object();

    public static NotificationController getInstance() {
        synchronized (instanceLock) {
            if (instance == null)
                instance = new NotificationController();
        }
        return instance;
    }

    private NotificationController(){}

    public void init() {
        notifications = new ConcurrentHashMap<>();
    }

    public Response<Boolean> sendNotification(UUID recipient, String body) {
        if (!notifications.containsKey(recipient))
            notifications.put(recipient, new ConcurrentHashMap<>());
        ConcurrentHashMap<UUID, Notification> userNotifications = notifications.get(recipient);
        Notification notification = new Notification(body);
        userNotifications.put(notification.getId(), notification);
        return Response.getSuccessResponse(true);
    }

    public Response<List<Notification>> getNotifications(UUID clientCredentials, UUID recipient) {
        if (clientCredentials != recipient) {
            return Response.getFailResponse("NotificationController getNotifications - credentials error: notifications may only be accessed by the recipient user.");
        }
        ConcurrentHashMap<UUID, Notification> userNotifications = notifications.get(recipient);
        if (userNotifications == null) {
            return Response.getFailResponse("NotificationController getNotification - recipient does not exist.");
        }
        return Response.getSuccessResponse(new ArrayList<>(userNotifications.values()));
    }

    public void resetController() {
        instance = new NotificationController();
    }

}
