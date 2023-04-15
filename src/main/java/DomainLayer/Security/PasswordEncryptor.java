package DomainLayer.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class PasswordEncryptor {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "abcede897abcd221";

    // Method to encrypt a password
    public String encrypt(String password) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Method to decrypt a password
    public String decrypt(String encryptedPassword) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // Method to generate the secret key for encryption and decryption
    private Key generateKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
    }
}
