package at.htlhl.securefinancemanager.config;

import at.htlhl.securefinancemanager.model.UserSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
 * @fullName Fischer, Jessica Christina
 * @version 1.9
 * @since 02.02.2024 (version 1.9)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    /**
     * Spring JDBC template for executing SQL queries and updates.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Configures the user service that dynamically loads user information from the repository based on the
     * incoming username and uses it for authentication.
     *
     * @return A user service that loads user details from the repository dynamically based on the username.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            String query = "SELECT username, password, pk_user_id FROM users WHERE username = ?";
            UserDetails userDetails = jdbcTemplate.queryForObject(query, new Object[]{username}, (resultSet, i) -> {
                String usernameFromDb = resultSet.getString("username");
                String passwordFromDb = resultSet.getString("password");
                int userIdFromDb = resultSet.getInt("pk_user_id");

                UserSingleton.getInstance().addUser(usernameFromDb, userIdFromDb);

                return User.withUsername(usernameFromDb)
                        .password(passwordFromDb)
                        .roles("USER")
                        .build();
            });

            if (userDetails == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }

            return userDetails;
        };
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
                .cors().and()
                .csrf().disable()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/secure-finance-manager/swagger-ui/**").permitAll()
                                .requestMatchers("/secure-finance-manager/colours/**").permitAll()
                                .requestMatchers("/secure-finance-manager/users/**").permitAll()
                                .requestMatchers("/secure-finance-manager/**").authenticated()
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