package UnitTests;

import DomainLayer.Security.PasswordEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class PasswordEncryptorTest {

    @Test
    public void testEncryptionAndDecryption() {
        String password = "myPassword123";

        try {
            PasswordEncryptor encryptor = new PasswordEncryptor();

            // Encrypt the password
            String encryptedPassword = encryptor.encrypt(password);

            // Ensure the encrypted password is not equal to the original password
            Assertions.assertNotEquals(password, encryptedPassword);

            // Decrypt the password
            String decryptedPassword = encryptor.decrypt(encryptedPassword);

            // Ensure the decrypted password is equal to the original password
            Assertions.assertEquals(password, decryptedPassword);
        } catch (Exception e) {
            Assertions.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyPasswordEncryption() {
        String password = "";

        try {
            PasswordEncryptor encryptor = new PasswordEncryptor();

            // Encrypt the empty password
            String encryptedPassword = encryptor.encrypt(password);

            // Ensure the encrypted password is not empty
            Assertions.assertNotEquals(password, encryptedPassword);
            Assertions.assertFalse(encryptedPassword.isEmpty());
        } catch (Exception e) {
            Assertions.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidPasswordDecryption() {
        String encryptedPassword = "InvalidPassword123";

        try {
            PasswordEncryptor encryptor = new PasswordEncryptor();
            // Attempt to decrypt the invalid password
            Assertions.assertThrows(Exception.class, () -> encryptor.decrypt(encryptedPassword));
            
        } catch (Exception e) {
        
        }
    }
}
