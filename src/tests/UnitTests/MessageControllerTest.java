package UnitTests;

import DomainLayer.Market.MessageController;
import DomainLayer.Market.UserController;
import DomainLayer.Market.Users.Message;
import ServiceLayer.Response;
import ServiceLayer.Service;
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
        UUID sender = service.login(client, "sender", "Sender1");
        UUID recipient = service.login(client, "recipient", "Recipient1");
        Response<UUID> response = messageController.sendMessage(sender, sender, recipient, body);
        assertTrue(response.isSuccessful());
        assertNotNull(response.getValue());
        UUID messageId = response.getValue();
        List<Message> messages = messageController.getMessages(recipient, recipient).getValue();
        assertEquals(1, messages.size());
        Message message = messages.get(0);
        assertEquals(messageId, message.getId());
        assertEquals(sender, message.getSender());
        assertEquals(recipient, message.getRecipient());
        assertEquals(body, message.getBody());
        client = service.logout(sender);
        client = service.logout(recipient);
    }

    @Test
    public void testGetMessages() {
        UUID sender = service.login(client, "sender", "Sender1");
        UUID recipient = service.login(client, "recipient", "Recipient1");
        List<Message> messages = messageController.getMessages(sender, sender).getValue();
        assertNotNull(messages);
        assertEquals(0, messages.size());
        messageController.sendMessage(sender, sender, recipient, body);
        messages = messageController.getMessages(recipient, recipient).getValue();
        assertEquals(1, messages.size());
        Message message = messages.get(0);
        assertEquals(sender, message.getSender());
        assertEquals(recipient, message.getRecipient());
        assertEquals(body, message.getBody());
        client = service.logout(sender);
        client = service.logout(recipient);
    }

    @Test
    public void testGetMessage() {
        UUID sender = service.login(client, "sender", "Sender1");
        UUID recipient = service.login(client, "recipient", "Recipient1");
        UUID messageId = messageController.sendMessage(sender, sender, recipient, body).getValue();
        Response<Message> response = messageController.getMessage(recipient, recipient, messageId);
        assertTrue(response.isSuccessful());
        assertNotNull(response.getValue());
        Message message = response.getValue();
        assertEquals(sender, message.getSender());
        assertEquals(recipient, message.getRecipient());
        assertEquals(body, message.getBody());
        client = service.logout(sender);
        client = service.logout(recipient);
    }

    @Test
    public void testGetNonExistentMessage() {
        UUID recipient = service.login(client, "recipient", "Recipient1");
        UUID messageId = UUID.randomUUID();
        Response<Message> response = messageController.getMessage(recipient, recipient, messageId);
        assertFalse(response.isSuccessful());
        assertNull(response.getValue());
        assertEquals("Message not found.", response.getMessage());
        client = service.logout(recipient);
    }

    @Test
    public void testGetMessagesWithInvalidCredentials() {
        UUID recipient = service.login(client, "recipient", "Recipient1");
        UUID invalidCredentials = UUID.randomUUID();
        Response<List<Message>> response = messageController.getMessages(invalidCredentials, recipient);
        assertFalse(response.isSuccessful());
        assertNull(response.getValue());
        assertEquals("Passed recipient ID do not match logged in user or an existing store.", response.getMessage());
        client = service.logout(recipient);
    }
}
