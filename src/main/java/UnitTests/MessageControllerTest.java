package UnitTests;

import DomainLayer.Market.MessageController;
import DomainLayer.Market.Users.Message;
import ServiceLayer.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class MessageControllerTest {

    private MessageController messageController;
    private UUID clientCredentials;
    private UUID sender;
    private UUID recipient;
    private String body;

    @Before
    public void setUp() {
        messageController = MessageController.getInstance();
        clientCredentials = UUID.randomUUID();
        sender = UUID.randomUUID();
        recipient = UUID.randomUUID();
        body = "Hello, World!";
    }

    @Test
    public void testSendMessage() {
        Response<UUID> response = messageController.sendMessage(clientCredentials, sender, recipient, body);
        assertTrue(response.isSuccessful());
        assertNotNull(response.getValue());
        UUID messageId = response.getValue();
        List<Message> messages = messageController.getMessages(clientCredentials).getValue();
        assertEquals(1, messages.size());
        Message message = messages.get(0);
        assertEquals(messageId, message.getId());
        assertEquals(sender, message.getSender());
        assertEquals(recipient, message.getRecipient());
        assertEquals(body, message.getBody());
    }

    @Test
    public void testGetMessages() {
        List<Message> messages = messageController.getMessages(clientCredentials).getValue();
        assertNotNull(messages);
        assertEquals(0, messages.size());
        messageController.sendMessage(clientCredentials, sender, recipient, body);
        messages = messageController.getMessages(clientCredentials).getValue();
        assertEquals(1, messages.size());
        Message message = messages.get(0);
        assertEquals(sender, message.getSender());
        assertEquals(recipient, message.getRecipient());
        assertEquals(body, message.getBody());
    }

    @Test
    public void testGetMessage() {
        UUID messageId = messageController.sendMessage(clientCredentials, sender, recipient, body).getValue();
        Response<Message> response = messageController.getMessage(clientCredentials, messageId);
        assertTrue(response.isSuccessful());
        assertNotNull(response.getValue());
        Message message = response.getValue();
        assertEquals(sender, message.getSender());
        assertEquals(recipient, message.getRecipient());
        assertEquals(body, message.getBody());
    }

    @Test
    public void testGetNonExistentMessage() {
        UUID messageId = UUID.randomUUID();
        Response<Message> response = messageController.getMessage(clientCredentials, messageId);
        assertFalse(response.isSuccessful());
        assertNull(response.getValue());
        assertEquals("Message not found.", response.getMessage());
    }

    @Test
    public void testGetMessagesWithInvalidCredentials() {
        UUID invalidCredentials = UUID.randomUUID();
        Response<List<Message>> response = messageController.getMessages(invalidCredentials);
        assertFalse(response.isSuccessful());
        assertNull(response.getValue());
        assertEquals("No user with the passed client credentials.", response.getMessage());
    }

}
