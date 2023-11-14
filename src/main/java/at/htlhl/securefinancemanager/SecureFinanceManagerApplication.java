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
 * @version 1.2
 * @since 14.11.2023 (version 1.2)
 */
@SpringBootApplication
public class SecureFinanceManagerApplication {
	/** Creating a shared instance of the UserSingleton for easy access to user information throughout the application. */
	public static UserSingleton userSingleton = UserSingleton.getInstance();

	/**
	 * The main method that starts the Secure Finance Manager application.
	 *
	 * @param args Command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SecureFinanceManagerApplication.class, args);
	}
}
