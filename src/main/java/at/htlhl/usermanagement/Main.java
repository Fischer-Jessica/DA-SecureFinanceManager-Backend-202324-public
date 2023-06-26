package at.htlhl.usermanagement;

import org.apache.catalina.User;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        // TODO: testing not possible because the instance in main is different from the instance in the repository and the keys and message can therefore not be decrypted

        String secretMessage = "password";

        UserRepository userRepository = new UserRepository();

        userRepository.getPublicKey();

        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, userRepository.pair.getPublic());

        byte[] secretMessageBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);

        String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);

        System.out.println(encodedMessage);

        // SERVER

        byte[] encodedMessageBytesServer = Base64.getDecoder().decode(encodedMessage.getBytes());

        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, userRepository.pair.getPrivate());

        byte[] decryptedMessageBytes = decryptCipher.doFinal(encodedMessageBytesServer);

        System.out.println(new String(decryptedMessageBytes, StandardCharsets.UTF_8));
    }
}
