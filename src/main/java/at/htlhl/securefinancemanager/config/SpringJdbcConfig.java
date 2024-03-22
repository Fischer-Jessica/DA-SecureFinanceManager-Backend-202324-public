package at.htlhl.securefinancemanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * The configuration class for setting up the Spring JDBC DataSource.
 *
 * <p>
 * This class is annotated with {@code @Configuration} to indicate that it contains configuration settings for the application context.
 * It uses {@code @ComponentScan} to specify the base package for Spring component scanning.
 * </p>
 *
 * <p>
 * The class defines a method {@code postgresqlDataSource()} annotated with {@code @Bean} to provide a configured instance of the {@code DataSource}.
 * The method sets up a {@code DriverManagerDataSource} for PostgreSQL, specifying the driver class, database URL, username, and password.
 * </p>
 *
 * <p>
 * The configured {@code DataSource} bean is used for establishing a connection to the PostgreSQL database, enabling Spring JDBC functionality.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 1
 * @since 21.03.2024 (version 1)
 */
@Configuration
@ComponentScan("at.htlhl.securefinancemanager")
public class SpringJdbcConfig {
    /**
     * Provides a configured {@code DataSource} bean for PostgreSQL database access.
     *
     * @return A configured instance of the {@code DataSource} interface.
     * @see org.springframework.context.annotation.Bean
     */
    @Bean
    public DataSource postgresqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://TheIpAddressOfYourDatabase:5432/secure_finance_manager?characterEncoding=UTF-8");
        dataSource.setUsername("TheUsernameOfYourDatabaseUser");
        dataSource.setPassword("ThePasswordOfYourDatabaseUser");

        return dataSource;
    }
}