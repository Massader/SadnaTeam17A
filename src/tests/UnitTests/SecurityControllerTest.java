package UnitTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import APILayer.Main;
import DataAccessLayer.RepositoryFactory;
import DomainLayer.Market.UserController;
import DomainLayer.Security.SecurityController;
import ServiceLayer.Service;
import ServiceLayer.StateFileRunner.StateFileRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ServiceLayer.Response;
import org.springframework.boot.SpringApplication;

class SecurityControllerTest {

    static Service service;
    SecurityController securityController;
    UUID id;
    String legalPassword;
    String question;
    String answer;
    String question2;
    String answer2;
    Response<Boolean> response;
    
    @BeforeAll
    static void beforeAll() {
        SpringApplication.run(Main.class);
        service = Service.getInstance();
        service.init(UserController.repositoryFactory, new StateFileRunner(new ObjectMapper(), service));
    }
    
    @BeforeEach
    void setUp() throws Exception {
        securityController = SecurityController.getInstance();
        securityController.init(UserController.repositoryFactory);
        id = UUID.randomUUID();
        legalPassword = "Pa$$w0rd";
        securityController.addPassword(id, legalPassword);
        question = "What is your favorite color?";
        answer = "blue";
        question2 = "sup?";
        answer2 = "walla";
        response = securityController.addSecurityQuestion(id, question, answer);

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
    
    @Test
    void testValidatePasswordSuccess() {
        Response<Boolean> response = securityController.validatePassword(id, legalPassword);
        assertFalse(response.isError());
    }

    @Test
    void testValidatePasswordFailure() {
        Response<Boolean> response = securityController.validatePassword(id, "incorrectPassword");
        assertTrue(response.isError());
    }

    @Test
    void testIsLegalPasswordSuccess() {
        boolean isLegal = securityController.isLegalPassword(legalPassword);
        assertTrue(isLegal);
    }

    @Test
    void testIsLegalPasswordFailure1() {
        String illegalPassword = "abc123";
        boolean isLegal = securityController.isLegalPassword(illegalPassword);
        assertFalse(isLegal);
    }

    @Test
    void testIsLegalPasswordFailure2() {
        String illegalPassword = "incorrectpassword1";
        boolean isLegal = securityController.isLegalPassword(illegalPassword);
        assertFalse(isLegal);
    }

    @Test
    void testChangePasswordSuccess() {
        String oldPass = "Pa$$w0rd";
        String newPass = "newPa$$w0rd";
        Response<Boolean> response = securityController.changePassword(id, oldPass, newPass);
        assertTrue(response.isSuccessful());
        assertTrue(securityController.validatePassword(id, newPass).isSuccessful());
        assertFalse(securityController.validatePassword(id, oldPass).isSuccessful());
    }

    @Test
    void testChangePasswordFailure() {
        String oldPass = "incorrectPassword";
        String newPass = "newPa$$w0rd";
        Response<Boolean> response = securityController.changePassword(id, oldPass, newPass);
        assertFalse(response.isSuccessful());
        assertEquals("the old password is incorrect!", response.getMessage());
    }

    @Test
    void testRemovePasswordSuccess() {
        securityController.removePassword(id);
        assertNull(securityController.validatePassword(id, legalPassword).getValue());
    }

    @Test
    void testEncryptAndSavePasswordSuccess() {
        String newPass = "newPa$$w0rd";
        Response<Boolean> response = securityController.encryptAndSavePassword(id, newPass);
        assertTrue(response.isSuccessful());
        assertTrue(securityController.validatePassword(id, newPass).isSuccessful());
    }

    @Test
    void testEncryptAndSavePasswordFailure() {
        String newPass = "123";
        Response<Boolean> response = securityController.encryptAndSavePassword(id, newPass);
        assertFalse(response.isSuccessful());
        assertEquals("Illegal Password", response.getMessage());
        assertNull(securityController.validatePassword(id, newPass).getValue());
    }

    @Test
    void testAddSecurityQuestionSuccess() {
        Response<Boolean> response = securityController.addSecurityQuestion(id, question2, answer2);
        assertTrue(response.isSuccessful());
        assertEquals(question2, securityController.getSecurityQuestion(id).getValue());
    }

    @Test
    void testAddSecurityQuestionFailure() {
        securityController.addSecurityQuestion(id, "sup?", "answer");
        Response<Boolean> response = securityController.addSecurityQuestion(id, "sup?", "new answer");
        assertFalse(response.isSuccessful());
        assertEquals("The same question already exists for the user", response.getMessage());
    }

    @Test
    void testGetSecurityQuestionSuccess() {
        Response<String> response = securityController.getSecurityQuestion(id);
        assertTrue(response.isSuccessful());
        assertEquals(question, response.getValue());
    }

    @Test
    void testGetSecurityQuestionFailure() {
        UUID nonExistingId = UUID.randomUUID();
        Response<String> response = securityController.getSecurityQuestion(nonExistingId);
        assertFalse(response.isSuccessful());
        assertEquals("There is no security question for this user", response.getMessage());
        assertNull(response.getValue());
    }

    @Test
    void testValidateSecurityQuestionSuccess() {
        Response<Boolean> response = securityController.validateSecurityQuestion(id, answer);
        assertTrue(response.isSuccessful());
        assertTrue(response.getValue());
    }

    @Test
    void testValidateSecurityQuestionFailure() {
        Response<Boolean> response = securityController.validateSecurityQuestion(id, "wrong answer");
        assertTrue(response.isSuccessful());
        assertFalse(response.getValue());
    }



}
