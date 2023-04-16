package UnitTests;

import DomainLayer.Market.Notification;
import DomainLayer.Market.NotificationController;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationControllerTest {

    private NotificationController notificationController;
    private UUID recipient;

    @BeforeEach
    void setUp() {
        notificationController = NotificationController.getInstance();
        notificationController.init();
        recipient = UUID.randomUUID();
        notificationController.sendNotification(recipient, "test notification");
    }

    @AfterEach
    void tearDown() {
        notificationController = null;
        recipient = null;
    }


    @Test
    void testSendNotification() {
        assertTrue(notificationController.sendNotification(recipient, "new notification"));
    }

    @Test
    void testGetNotifications() {
        List<Notification> notifications = notificationController.getNotifications(recipient, recipient);
        assertNotNull(notifications);
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
        assertEquals("test notification", notifications.get(0).getMessage());
    }

    @Test
    void testGetNotificationsInvalidCredentials() {
        UUID credentials = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> notificationController.getNotifications(credentials, recipient));
    }

    @Test
    void testGetNotificationsInvalidRecipient() {
        UUID invalidRecipient = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> notificationController.getNotifications(recipient, invalidRecipient));
    }

    @Test
    void testConcurrentSendAndGetNotifications() throws InterruptedException {
        int numOfThreads = 10;
        int numOfNotificationsPerThread = 1000;

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numOfThreads; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < numOfNotificationsPerThread; j++) {
                    notificationController.sendNotification(recipient, "test notification");
                    notificationController.getNotifications(recipient, recipient);
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        List<Notification> notifications = notificationController.getNotifications(recipient, recipient);
        assertEquals(numOfThreads * numOfNotificationsPerThread + 1, notifications.size());
    }

}
