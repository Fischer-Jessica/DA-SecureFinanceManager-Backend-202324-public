package at.htlhl.securefinancemanager;

import at.htlhl.securefinancemanager.model.UserSingleton;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for the Secure Finance Manager application.
 * <p>
 * This class uses Spring Boot to initialize and run the application.
 * It serves as the entry point for the application, and the `main` method
 * is responsible for starting the Spring Boot application context.
 * </p>
 *
 * @author Fischer
 * @version 1.3
 * @since 02.02.2024 (version 1.3)
 */
@SpringBootApplication
public class SecureFinanceManagerApplication {
    /**
     * Creating a shared instance of the UserSingleton for easy access to user information throughout the application.
     */
    public static UserSingleton userSingleton = UserSingleton.getInstance();

    /**
     * This static final variable holds the encryption key used for encrypting and decrypting sensitive data.
     * It should be kept secure and not exposed to unauthorized users.
     */
    public static final String ENCRYPTION_KEY = "f8904d463a99e915887d5b00748d519f1628fc8f609fb28d8864b7ae4286c9fc";

    /**
     * The main method that starts the Secure Finance Manager application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(SecureFinanceManagerApplication.class, args);
    }
}