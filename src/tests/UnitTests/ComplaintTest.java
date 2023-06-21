import DomainLayer.Market.Complaint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ComplaintTest {
    private Complaint complaint;
    private UUID purchaseId;
    private UUID storeId;
    private UUID itemId;

    @BeforeEach
    void setUp() {
        purchaseId = UUID.randomUUID();
        storeId = UUID.randomUUID();
        itemId = UUID.randomUUID();
        complaint = new Complaint("Test complaint", UUID.randomUUID(), purchaseId, storeId, itemId);
    }

    @Test
    void testOpenComplaint() {
        assertTrue(complaint.isOpen());
    }

    @Test
    void testAssignedAdmin() {
        UUID adminId = UUID.randomUUID();
        complaint.setAssignedAdmin(adminId);
        assertEquals(adminId, complaint.getAssignedAdmin());
    }

    @Test
    void testPurchaseId() {
        assertEquals(purchaseId, complaint.getPurchaseId());
    }

    @Test
    void testCloseComplaint() {
        complaint.closeComplaint();
        assertFalse(complaint.isOpen());
    }

    @Test
    void testReopenComplaint() {
        complaint.closeComplaint();
        complaint.reopenComplaint();
        assertTrue(complaint.isOpen());
    }

    @Test
    void testStoreId() {
        UUID newStoreId = UUID.randomUUID();
        complaint.setStoreId(newStoreId);
        assertEquals(newStoreId, complaint.getStoreId());
    }

    @Test
    void testItemId() {
        UUID newItemId = UUID.randomUUID();
        complaint.setItemId(newItemId);
        assertEquals(newItemId, complaint.getItemId());
    }
}
