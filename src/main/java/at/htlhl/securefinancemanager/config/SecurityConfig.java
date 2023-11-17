package at.htlhl.securefinancemanager.config;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.database.DatabaseUser;
import at.htlhl.securefinancemanager.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

import static at.htlhl.securefinancemanager.SecureFinanceManagerApplication.userSingleton;

/**
 * This configuration class defines security settings for the Secure Finance Manager application.
 * It includes configuration for user details retrieval from the repository, password encryption,
 * and HTTP security rules.
 *
 * <p>
 * The {@code userDetailsService} method configures a user service that loads user information from
 * the repository and uses it for authentication. User details are retrieved from the repository,
 * encoded with the specified password encoder, and added to an in-memory user details manager.
 * </p>
 *
 * <p>
 * The {@code securityFilterChain} method configures HTTP security settings for the application.
 * It disables Cross-Site Request Forgery (CSRF) protection, permits access to Swagger-UI and
 * certain endpoints without authentication, and requires authentication for other paths.
 * </p>
 *
 * <p>
 * The {@code passwordEncoder} method configures the password encoder for encrypting user passwords.
 * It uses the BCrypt encryption method.
 * </p>
 *
 * <p>
 * This class is annotated with {@code @Configuration}, {@code @EnableWebSecurity}, and
 * {@code @EnableMethodSecurity}, indicating its role in configuring Spring Security for the application.
 * </p>
 *
 * @author Fischer
 * @version 1.4
 * @since 17.11.2023 (version 1.4)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    /**
     * Configures the user service that loads user information from the repository and uses it for authentication.
     *
     * @param encoder           The password encoder for encrypting user passwords.
     * @param userRepository    The repository that stores user data.
     * @return A user service that loads user details from the repository.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder, UserRepository userRepository) throws ValidationException {
        List<DatabaseUser> users = userRepository.getUsers();
        List<UserDetails> userDetailsList = new ArrayList<>();

        for (DatabaseUser apiUser : users) {
            userSingleton.getInstance().addUser(apiUser.getUsername(), apiUser.getUserId());
            UserDetails userDetails = User.withUsername(apiUser.getUsername())
                    .password(encoder.encode(apiUser.getPassword()))
                    .roles("USER")
                    .build();
            userDetailsList.add(userDetails);
        }

        return new InMemoryUserDetailsManager(userDetailsList);
    }

    /**
     * Configures HTTP security settings for the application.
     *
     * @param http The HttpSecurity object to configure security rules.
     * @return A SecurityFilterChain object representing the security rules.
     * @throws Exception If an error occurs while configuring security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/secure-finance-manager/swagger-ui/**").permitAll() // Erlaubt den Zugriff auf Swagger-UI ohne Authentifizierung
                                .requestMatchers("/secure-finance-manager/colours/**").permitAll() // Erlaubt den Zugriff auf den Colour-Endpunkt ohne Authentifizierung
                                .requestMatchers("/secure-finance-manager/users/**").permitAll()
                                .requestMatchers("/secure-finance-manager/**").authenticated() // Authentifizierung erforderlich für andere Pfade
                )
                .httpBasic();
        return http.build();
    }

    /**
     * Configures the password encoder for encrypting user passwords.
     *
     * @return A password encoder that uses the BCrypt encryption method.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}