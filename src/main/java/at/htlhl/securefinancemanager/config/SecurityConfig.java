package at.htlhl.securefinancemanager.config;

import at.htlhl.securefinancemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    // User Creation
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        List<at.htlhl.securefinancemanager.model.User> users = userRepository.getUsers();
        List<UserDetails> userDetailsList = new ArrayList<>();

        for (at.htlhl.securefinancemanager.model.User user : users) {
            UserDetails userDetails = User.withUsername(user.getUsername())
                    .password(encoder.encode(user.getPassword()))
                    .roles("USER")
                    .build();
            userDetailsList.add(userDetails);
        }

        return new InMemoryUserDetailsManager(userDetailsList);
    }


    // Configuring HttpSecurity
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/secure-finance-manager/swagger-ui/**").permitAll() // Erlaubt den Zugriff auf Swagger-UI ohne Authentifizierung
                                .requestMatchers("/secure-finance-manager/colours/**").permitAll() // Erlaubt den Zugriff auf den Colour-Endpunkt ohne Authentifizierung
                                .requestMatchers("/secure-finance-manager/**").authenticated() // Authentifizierung erforderlich für andere Pfade
                )
                .httpBasic();
        /*
        http
                .csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .httpBasic();
         */

        return http.build();
    }


    // Password Encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}