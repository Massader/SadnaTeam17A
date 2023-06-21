package DomainLayer.Market;

import DataAccessLayer.RepositoryFactory;
import ServiceLayer.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class NotificationController {
    private ConcurrentHashMap<UUID, ConcurrentHashMap<UUID, Notification>> notifications;
    //map from recipientId to map from notificationId to Notification
    private static NotificationController instance = null;
    private static final Object instanceLock = new Object();
    private ConcurrentHashMap<UUID, BiConsumer<UUID, Notification>> notifiers;
    private RepositoryFactory repositoryFactory;

    public static NotificationController getInstance() {
        synchronized (instanceLock) {
            if (instance == null)
                instance = new NotificationController();
        }
        return instance;
    }

    private NotificationController() {
    }

    public void init(RepositoryFactory repositoryFactory){
        this.repositoryFactory = repositoryFactory;
        notifications = new ConcurrentHashMap<>();
        notifiers = new ConcurrentHashMap<>();
        initNotifications();
    }

    private void initNotifications() {
        List<Notification> allNotifications = repositoryFactory.notificationRepository.findAll();
        Map<UUID, List<Notification>> groupedByRecipient = allNotifications.stream().collect(Collectors.groupingBy(Notification::getRecipient));

        for (UUID recipient : groupedByRecipient.keySet()) {
            ConcurrentHashMap<UUID, Notification> recipientNotifications = new ConcurrentHashMap<>();
            for (Notification notification : groupedByRecipient.get(recipient)) {
                recipientNotifications.put(notification.getId(), notification);
            }
            notifications.put(recipient, recipientNotifications);
        }
    }

    public Response<Boolean> sendNotification(UUID recipient, String body) {
        if (!notifications.containsKey(recipient)) {
            notifications.put(recipient, new ConcurrentHashMap<>());
        }
        ConcurrentHashMap<UUID, Notification> userNotifications = notifications.get(recipient);
        Notification notification = new Notification(body, recipient);
        //
        repositoryFactory.notificationRepository.save(notification);
        //
        if (notifiers.containsKey(recipient)) {
            notifiers.get(recipient).accept(recipient, notification);   //Send real time event
            //
            repositoryFactory.notificationRepository.deleteById(notification.getId());
            //
        }
        else
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
        List<Notification> outputList = new ArrayList<>(userNotifications.values());
        outputList.sort(Comparator.comparing(Notification::getTimestamp));
        notifications.get(recipient).clear();
        //
        repositoryFactory.notificationRepository.deleteAllById(outputList.stream().map(Notification::getId).toList());
        //
        return Response.getSuccessResponse(outputList);
    }

    public void resetController() {
        init(repositoryFactory);
    }

    public void addNotifier(UUID clientCredentials, BiConsumer<UUID, Notification> notificationSender) {
        if (notificationSender != null) {
            notifiers.put(clientCredentials, notificationSender);
        }
    }

    public void removeNotifier(UUID clientCredentials) {
        notifiers.remove(clientCredentials);
    }
}
