package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.api.ApiUser;
import at.htlhl.securefinancemanager.model.database.DatabaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static at.htlhl.securefinancemanager.SecureFinanceManagerApplication.userSingleton;

/**
 * The {@code UserRepository} class handles the persistence operations for user data.
 * It provides methods to access and manipulate the 'users' table in the 'secure_finance_manager' PostgreSQL database.
 *
 * <p>
 * This class interacts with the User entity class, which represents a POJO (Plain Old Java Object) or entity class that maps to the 'users' table in the database.
 * It uses Spring Data JPA to provide generic CRUD (Create, Read, Update, Delete) operations for the User entity, reducing boilerplate code.
 * </p>
 *
 * <p>
 * The {@code UserRepository} serves as an abstraction layer between the UserController and the underlying data storage, enabling seamless access and manipulation of User entities.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 3.8
 * @since 31.03.2024 (version 3.8)
 */
@Repository
public class UserRepository {
    /**
     * Spring JDBC template for executing SQL queries and updates.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * SQL query to retrieve a user based on the username.
     */
    private static final String SELECT_USER = "SELECT pk_user_id, username, password, email_address, first_name, last_name " +
            "FROM users " +
            "WHERE username = ?;";

    /**
     * SQL query to check if a username is already in use.
     */
    private static final String SELECT_USERNAME = "SELECT username " +
            "FROM users " +
            "WHERE username = ?;";

    /**
     * SQL query to check if an email address is already in use.
     */
    private static final String SELECT_E_MAIL_ADDRESS = "SELECT email_address " +
            "FROM users " +
            "WHERE email_address = ?;";

    /**
     * SQL query to insert a new user into the database.
     */
    private static final String INSERT_USER = "INSERT INTO users " +
            "(username, password, email_address, first_name, last_name) " +
            "VALUES (?, ?, ?, ?, ?);";

    /**
     * SQL query to update an existing user.
     */
    private static final String UPDATE_USER = "UPDATE users " +
            "SET username = ?, password = ?, email_address = ?, first_name = ?, last_name = ? " +
            "WHERE pk_user_id = ?;";

    /**
     * SQL query to delete a user from the repository.
     */
    private static final String DELETE_USER = "DELETE FROM users " +
            "WHERE pk_user_id = ?;";

    /**
     * Retrieves a User object based on the provided username.
     *
     * @param jdbcTemplate   The Spring JDBC template for executing SQL queries and updates.
     * @param activeUsername The username of the user to be retrieved.
     * @return The User object representing the user with the given username.
     * @throws ValidationException If the specified user does not exist or if the provided username is invalid.
     *                             This exception may indicate that the userId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the user.
     */
    public static DatabaseUser getUserObject(JdbcTemplate jdbcTemplate, String activeUsername) throws ValidationException {
        DatabaseUser databaseUser = null;
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USER)) {
            ps.setString(1, activeUsername);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("pk_user_id");
                    String username = rs.getString("username");
                    String encodedPassword = rs.getString("password");
                    String eMailAddress = rs.getString("email_address");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    databaseUser = new DatabaseUser(userId, username, encodedPassword, eMailAddress, firstName, lastName);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (databaseUser == null) {
            throw new ValidationException("User with username " + activeUsername + " not found.");
        }
        return databaseUser;
    }

    /**
     * Checks if the provided username already exists in the database.
     *
     * @param username The username to be checked for existence.
     * @return true if the username already exists, false otherwise.
     */
    public boolean checkUsername(String username) {
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USERNAME)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the provided email address already exists in the database.
     *
     * @param eMailAddress The email address to be checked for existence.
     * @return true if the email address already exists, false otherwise.
     */
    public boolean checkEMailAddress(String eMailAddress) {
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_E_MAIL_ADDRESS)) {
            ps.setString(1, eMailAddress);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new user to the database.
     *
     * @param newApiUser The User object representing the new user to be added.
     * @return The newly created user.
     */
    public DatabaseUser addUser(ApiUser newApiUser) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection()) {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_USER, new String[]{"pk_user_id"});
                ps.setString(1, newApiUser.getUsername());
                ps.setString(2, passwordEncoder.encode(newApiUser.getPassword()));
                ps.setString(3, newApiUser.getEMailAddress());
                ps.setString(4, newApiUser.getFirstName());
                ps.setString(5, newApiUser.getLastName());
                return ps;
            }, keyHolder);
            userSingleton.addUser(newApiUser.getUsername(), Objects.requireNonNull(keyHolder.getKey()).intValue());
            return new DatabaseUser(Objects.requireNonNull(keyHolder.getKey()).intValue(), newApiUser.getUsername(),
                    newApiUser.getPassword(), newApiUser.getEMailAddress(), newApiUser.getFirstName(), newApiUser.getLastName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an existing user with new data.
     *
     * @param updatedUser The updated User object.
     * @param username    The username of the user performing the update.
     * @return The updated user.
     * @throws ValidationException If the specified user does not exist or if the provided username is invalid.
     *                             This exception may indicate that the userId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the user.
     */
    public DatabaseUser updateUser(ApiUser updatedUser, String username) throws ValidationException {
        DatabaseUser oldDatabaseUser = getUserObject(jdbcTemplate, username);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_USER)) {
            if (updatedUser.getUsername() != null) {
                ps.setString(1, updatedUser.getUsername());
                username = updatedUser.getUsername();
                userSingleton.updateUsername(oldDatabaseUser.getUserId(), updatedUser.getUsername());
            } else {
                ps.setString(1, oldDatabaseUser.getUsername());
                username = oldDatabaseUser.getUsername();
            }
            if (updatedUser.getPassword() != null) {
                ps.setString(2, passwordEncoder.encode(updatedUser.getPassword()));
            } else {
                ps.setString(2, oldDatabaseUser.getPassword());
            }
            if (updatedUser.getEMailAddress() != null) {
                ps.setString(3, updatedUser.getEMailAddress());
            } else {
                ps.setString(3, oldDatabaseUser.getEMailAddress());
            }
            if (updatedUser.getFirstName() != null) {
                ps.setString(4, updatedUser.getFirstName());
            } else {
                ps.setString(4, oldDatabaseUser.getFirstName());
            }
            if (updatedUser.getLastName() != null) {
                ps.setString(5, updatedUser.getLastName());
            } else {
                ps.setString(5, oldDatabaseUser.getLastName());
            }
            ps.setInt(6, oldDatabaseUser.getUserId());
            ps.executeUpdate();
            return getUserObject(jdbcTemplate, username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a user from the repository.
     *
     * @param username The username of the user performing the deletion on themselves.
     * @return An Integer representing the number of deleted rows.
     * @throws ValidationException If the specified user does not exist or if the provided username is invalid.
     *                             This exception may indicate that the userId is not found or that the userId associated
     *                             with the provided username does not match the expected owner of the user.
     */
    public int deleteUser(String username) throws ValidationException {
        try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource(), "DataSource must not be null").getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_USER)) {
            ps.setInt(1, userSingleton.getUserId(username));
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new ValidationException("User with username " + username + " not found.");
            }
            userSingleton.removeUserById(userSingleton.getUserId(username));
            return rowsAffected;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}