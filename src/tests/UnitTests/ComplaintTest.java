package UnitTests;

import APILayer.Main;
import DataAccessLayer.RepositoryFactory;
import DomainLayer.Market.Complaint;
import DomainLayer.Market.UserController;
import ServiceLayer.Service;
import ServiceLayer.StateFileRunner.StateFileRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ComplaintTest {
    static Service service;
    private Complaint complaint;
    private UUID purchaseId;
    private UUID storeId;
    private UUID itemId;
    
    @BeforeAll
    static void beforeAll() {
        SpringApplication.run(Main.class);
        service = Service.getInstance();
        service.init(UserController.repositoryFactory, new StateFileRunner(new ObjectMapper(), service));
    }
    
    @BeforeEach
    void setUp() {
        purchaseId = UUID.randomUUID();
        storeId = UUID.randomUUID();
        itemId = UUID.randomUUID();
        complaint = new Complaint("Test complaint", UUID.randomUUID(), purchaseId, storeId, itemId);
    }
    
    
    @AfterAll
    static void afterAll() {
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
