package UnitTests;

import DomainLayer.Market.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

public class NotificationTest {

    @Test
    public void testGetMessage() {
        // Create a notification
        UUID recipient = UUID.randomUUID();
        String message = "You have a new notification.";
        Notification notification = new Notification(message, recipient);

        // Ensure the message is retrieved correctly
        Assertions.assertEquals(message, notification.getMessage());
    }

    @Test
    public void testGetId() {
        // Create a notification
        UUID recipient = UUID.randomUUID();
        String message = "You have a new notification.";
        Notification notification = new Notification(message, recipient);

        // Ensure the ID is not null
        Assertions.assertNotNull(notification.getId());
    }

    @Test
    public void testGetRecipient() {
        // Create a notification
        UUID recipient = UUID.randomUUID();
        String message = "You have a new notification.";
        Notification notification = new Notification(message, recipient);

        // Ensure the recipient is retrieved correctly
        Assertions.assertEquals(recipient, notification.getRecipient());
    }

    @Test
    public void testGetTimestamp() {
        // Create a notification
        UUID recipient = UUID.randomUUID();
        String message = "You have a new notification.";
        Notification notification = new Notification(message, recipient);

        // Ensure the timestamp is not null
        Assertions.assertNotNull(notification.getTimestamp());
    }

    @Test
    public void testSetRecipient() {
        // Create a notification
        UUID recipient = UUID.randomUUID();
        String message = "You have a new notification.";
        Notification notification = new Notification(message, recipient);

        // Update the recipient
        UUID newRecipient = UUID.randomUUID();
        notification.setRecipient(newRecipient);

        // Ensure the recipient is updated correctly
        Assertions.assertEquals(newRecipient, notification.getRecipient());
    }
}
