package UnitTests;

import DomainLayer.Market.MessageController;
import DomainLayer.Market.UserController;
import DomainLayer.Market.Users.Message;
import ServiceLayer.Response;
import ServiceLayer.Service;
import ServiceLayer.ServiceObjects.ServiceUser;
import org.junit.*;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class MessageControllerTest {

    private static Service service;
    private static MessageController messageController;
    private UUID client;
    private String body = "Hello, World!";;

    @BeforeClass
    public static void beforeClass() {
        service = Service.getInstance();
        service.init();
        messageController = MessageController.getInstance();

    }

    @Before
    public void setUp() throws Exception {
        client = service.createClient();
        service.register("sender", "Sender1");
        service.register("recipient", "Recipient1");
    }

    @After
    public void tearDown() throws Exception {
        UUID sender = UserController.getInstance().getId("sender");
        UUID recipient = UserController.getInstance().getId("recipient");
        service.deleteUser(UserController.getInstance().getId("admin"), sender);
        service.deleteUser(UserController.getInstance().getId("admin"), recipient);
    }

    @Test
    public void testSendMessage() {
        ServiceUser sender = service.login(client, "sender", "Sender1");
        ServiceUser recipient = service.login(client, "recipient", "Recipient1");
        Response<UUID> response = messageController.sendMessage(sender.getId(), sender.getId(), recipient.getId(), body);
        assertTrue(response.isSuccessful());
        assertNotNull(response.getValue());
        UUID messageId = response.getValue();
        List<Message> messages = messageController.getMessages(recipient.getId(), recipient.getId()).getValue();
        assertEquals(1, messages.size());
        Message message = messages.get(0);
        assertEquals(messageId, message.getId());
        assertEquals(sender.getId(), message.getSender());
        assertEquals(recipient.getId(), message.getRecipient());
        assertEquals(body, message.getBody());
        client = service.logout(sender.getId());
        client = service.logout(recipient.getId());
    }

    @Test
    public void testGetMessages() {
        ServiceUser sender = service.login(client, "sender", "Sender1");
        ServiceUser recipient = service.login(client, "recipient", "Recipient1");
        List<Message> messages = messageController.getMessages(sender.getId(), sender.getId()).getValue();
        assertNotNull(messages);
        assertEquals(0, messages.size());
        messageController.sendMessage(sender.getId(), sender.getId(), recipient.getId(), body);
        messages = messageController.getMessages(recipient.getId(), recipient.getId()).getValue();
        assertEquals(1, messages.size());
        Message message = messages.get(0);
        assertEquals(sender.getId(), message.getSender());
        assertEquals(recipient.getId(), message.getRecipient());
        assertEquals(body, message.getBody());
        client = service.logout(sender.getId());
        client = service.logout(recipient.getId());
    }

    @Test
    public void testGetMessage() {
        ServiceUser sender = service.login(client, "sender", "Sender1");
        ServiceUser recipient = service.login(client, "recipient", "Recipient1");
        UUID messageId = messageController.sendMessage(sender.getId(), sender.getId(), recipient.getId(), body).getValue();
        Response<Message> response = messageController.getMessage(recipient.getId(), recipient.getId(), messageId);
        assertTrue(response.isSuccessful());
        assertNotNull(response.getValue());
        Message message = response.getValue();
        assertEquals(sender.getId(), message.getSender());
        assertEquals(recipient.getId(), message.getRecipient());
        assertEquals(body, message.getBody());
        client = service.logout(sender.getId());
        client = service.logout(recipient.getId());
    }

    @Test
    public void testGetNonExistentMessage() {
        ServiceUser recipient = service.login(client, "recipient", "Recipient1");
        UUID messageId = UUID.randomUUID();
        Response<Message> response = messageController.getMessage(recipient.getId(), recipient.getId(), messageId);
        assertFalse(response.isSuccessful());
        assertNull(response.getValue());
        assertEquals("Message not found.", response.getMessage());
        client = service.logout(recipient.getId());
    }

    @Test
    public void testGetMessagesWithInvalidCredentials() {
        ServiceUser recipient = service.login(client, "recipient", "Recipient1");
        UUID invalidCredentials = UUID.randomUUID();
        Response<List<Message>> response = messageController.getMessages(invalidCredentials, recipient.getId());
        assertFalse(response.isSuccessful());
        assertNull(response.getValue());
        assertEquals("Passed recipient ID do not match logged in user or an existing store.", response.getMessage());
        client = service.logout(recipient.getId());
    }
}
