package UnitTests;

import APILayer.Main;
import DataAccessLayer.RepositoryFactory;
import DomainLayer.Market.Notification;
import DomainLayer.Market.NotificationController;
import DomainLayer.Market.UserController;
import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.StateFileRunner.StateFileRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationControllerTest {

    private NotificationController notificationController;
    private UUID recipient;
    static Service service;
    
    @BeforeAll
    static void beforeAll() {
        SpringApplication.run(Main.class);
        service = Service.getInstance();
        service.init(UserController.repositoryFactory, new StateFileRunner(new ObjectMapper(), service));
    }
    
    @AfterEach
    void tearDown() {
        try {
            RepositoryFactory repositoryFactory = UserController.repositoryFactory;
            repositoryFactory.roleRepository.deleteAll();
            repositoryFactory.itemRepository.deleteAll();
            repositoryFactory.passwordRepository.deleteAll();
            repositoryFactory.securityQuestionRepository.deleteAll();
            repositoryFactory.userRepository.deleteAll();
            repositoryFactory.storeRepository.deleteAll();
            service.resetService();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    @BeforeEach
    void setUp() {
        notificationController = NotificationController.getInstance();
//        notificationController.init();
        recipient = UUID.randomUUID();
        notificationController.sendNotification(recipient, "test notification");
    }


    @Test
    void testSendNotification() {
        assertFalse(notificationController.sendNotification(recipient, "new notification").isError());
    }

    @Test
    void testGetNotifications() {
        Response<List<Notification>> notifications = notificationController.getNotifications(recipient, recipient);
        assertNotNull(notifications);
        assertFalse(notifications.isError());
        assertEquals(1, notifications.getValue().size());
        assertEquals("test notification", notifications.getValue().get(0).getMessage());
    }

    @Test
    void testGetNotificationsInvalidCredentials() {
        UUID credentials = UUID.randomUUID();
        assertTrue(notificationController.getNotifications(credentials, recipient).isError());
    }

    @Test
    void testGetNotificationsInvalidRecipient() {
        UUID invalidRecipient = UUID.randomUUID();
        assertTrue(notificationController.getNotifications(recipient, invalidRecipient).isError());
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
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Response<List<Notification>> notifications = notificationController.getNotifications(recipient, recipient);
        assertEquals(numOfThreads * numOfNotificationsPerThread + 1, notifications.getValue().size());
    }

}
