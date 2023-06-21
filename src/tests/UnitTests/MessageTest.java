package UnitTests;

import DomainLayer.Market.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    private Message message;
    private UUID sender;
    private UUID recipient;

    @BeforeEach
    void setUp() {
        sender = UUID.randomUUID();
        recipient = UUID.randomUUID();
        message = new Message("Test message", sender, recipient);
    }

    @Test
    void testGetId() {
        assertNull(message.getId());
    }

    @Test
    void testSetId() {
        UUID messageId = UUID.randomUUID();
        message.setId(messageId);
        assertEquals(messageId, message.getId());
    }

    @Test
    void testGetBody() {
        assertEquals("Test message", message.getBody());
    }

    @Test
    void testSetBody() {
        message.setBody("New message");
        assertEquals("New message", message.getBody());
    }

    @Test
    void testGetSender() {
        assertEquals(sender, message.getSender());
    }

    @Test
    void testSetSender() {
        UUID newSender = UUID.randomUUID();
        message.setSender(newSender);
        assertEquals(newSender, message.getSender());
    }

    @Test
    void testGetRecipient() {
        assertEquals(recipient, message.getRecipient());
    }

    @Test
    void testSetRecipient() {
        UUID newRecipient = UUID.randomUUID();
        message.setRecipient(newRecipient);
        assertEquals(newRecipient, message.getRecipient());
    }

    @Test
    void testGetTimestamp() {
        assertNotNull(message.getTimestamp());
    }

    @Test
    void testSetTimestamp() {
        Date newTimestamp = Date.from(Instant.now());
        message.setTimestamp(newTimestamp);
        assertEquals(newTimestamp, message.getTimestamp());
    }
}
