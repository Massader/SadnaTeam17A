package DomainLayer.Market;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class NotificationController {
    private ConcurrentHashMap<UUID, ConcurrentLinkedDeque<Notification>> notifications;
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

    public boolean sendNotification(UUID recipient, String body) {
        ConcurrentLinkedDeque<Notification> userNotifications = notifications.get(recipient);
        if (userNotifications == null) {
            throw new RuntimeException("NotificationController sendNotification - recipient does not exist.");
        }
        userNotifications.addFirst(new Notification(body));
        return true;
    }

    public List<Notification> getNotifications(UUID clientCredentials, UUID recipient) {
        if (clientCredentials != recipient) {
            throw new RuntimeException("NotificationController getNotifications - credentials error: notifications may only be accessed by the recipient user.");
        }
        ConcurrentLinkedDeque<Notification> userNotifications = notifications.get(recipient);
        if (userNotifications == null) {
            throw new RuntimeException("NotificationController getNotification - recipient does not exist.");
        }
        return new ArrayList<>(userNotifications);
    }



}
