package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.User;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 * The UserRepository class handles the persistence operations for user data.
 * It provides methods to access and manipulate the 'users' table in the 'financial_overview' PostgreSQL database.
 *
 * <p>
 * This class interacts with the User entity class, which represents a POJO (Plain Old Java Object) or entity class that maps to the 'users' table in the database.
 * It uses Spring Data JPA to provide generic CRUD (Create, Read, Update, Delete) operations for the User entity, reducing boilerplate code.
 * </p>
 *
 * <p>
 * The UserRepository serves as an abstraction layer between the UserController and the underlying data storage, enabling seamless access and manipulation of User entities.
 * </p>
 *
 * @author Fischer
 * @version 2.0
 * @since 15.10.2023 (version 2.0)
 */
@Repository
public class UserRepository {
    /**
     * JdbcTemplate which is used for executing SQL statements, in the other repositories too, but implementing the template at each usage would be unnecessary
     */
    @Autowired
    protected static JdbcTemplate jdbcTemplate = new JdbcTemplate();

    /**
     * SQL query to retrieve a user based on the username.
     */
    private static final String SELECT_USER = "SELECT pk_user_id, username, password, email_address, first_name, last_name " +
            "FROM users " +
            "WHERE username = ?;";

    /**
     * SQL query to retrieve a userId based on the username.
     */
    private static final String SELECT_USER_ID = "SELECT pk_user_id " +
            "FROM users " +
            "WHERE username = ?;";

    /**
     * SQL query to retrieve all users.
     */
    private static final String SELECT_USERS = "SELECT pk_user_id, username, password, email_address, first_name, last_name " +
            "FROM users;";

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
     * Constructs a new UserRepository object with the given DataSource.
     *
     * @param dataSource The DataSource object to be used by the repository.
     */
    @Autowired
    public UserRepository(DataSource dataSource) {
        UserRepository.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Retrieves the ID of a user based on the provided username.
     *
     * @param activeUsername The username of the user for which the ID is to be retrieved.
     * @return The ID of the user with the given username.
     * @throws ValidationException if the user is not found.
     */
    public static int getUserId(String activeUsername) throws ValidationException {
        int databaseUserId = -1;
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_USER_ID);
            ps.setString(1, activeUsername);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                databaseUserId = rs.getInt("pk_user_id");
                conn.close();
            }
            conn.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        if (databaseUserId == -1) {
            throw new ValidationException("User not found");
        }
        return databaseUserId;
    }

    /**
     * Retrieves a user object based on the provided username.
     *
     * @param activeUsername The username of the user to be retrieved.
     * @return The User object representing the user with the given username.
     * @throws ValidationException if the user is not found.
     */
    public static User getUserObject(String activeUsername) throws ValidationException {
        User databaseUser = null;
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_USER);
            ps.setString(1, activeUsername);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("pk_user_id");
                String username = rs.getString("username");
                byte[] password = rs.getBytes("password");
                String eMailAddress = rs.getString("email_address");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                conn.close();
                databaseUser = new User(userId, username, Base64.getEncoder().encodeToString(password), eMailAddress, firstName, lastName);
            }
            conn.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        if (databaseUser == null) {
            throw new ValidationException("User not found");
        }
        return databaseUser;
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list of User objects representing all users.
     */
    public List<User> getUsers() {
        return jdbcTemplate.query(SELECT_USERS, (rs, rowNum) -> {
            int userId = rs.getInt("pk_user_id");
            String username = rs.getString("username");
            byte[] password = rs.getBytes("password");
            String eMailAddress = rs.getString("email_address");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            return new User(userId, username, Base64.getEncoder().encodeToString(password), eMailAddress, firstName, lastName);
        });
    }

    /**
     * Adds a new user to the database.
     *
     * @param newUser The User object representing the new user to be added.
     * @return The User object representing the newly created user.
     */
    public User addUser(User newUser) {
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_USER, new String[]{"pk_user_id"});
                ps.setString(1, newUser.getUsername());
                ps.setBytes(2, Base64.getDecoder().decode(newUser.getPassword()));
                ps.setString(3, newUser.geteMailAddress());
                ps.setString(4, newUser.getFirstName());
                ps.setString(5, newUser.getLastName());
                return ps;
            }, keyHolder);

            return new User(Objects.requireNonNull(keyHolder.getKey()).intValue(), newUser.getUsername(), newUser.getPassword(), newUser.geteMailAddress(), newUser.getFirstName(), newUser.getLastName());
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Updates an existing user with new data.
     *
     * @param updatedUser The updated User object.
     * @param username The username of the user performing the update.
     * @return The User object representing the updated user.
     * @throws ValidationException if the user is not found.
     */
    public User updateUser(int userId, User updatedUser, String username) throws ValidationException {
        User activeUser = getUserObject(username);
        if (activeUser.getUserId() == userId) {
        try {
            User actualUser = new User();
            actualUser.setUserId(activeUser.getUserId());

            Connection conn = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_USER);

            if (updatedUser.getUsername() != null) {
                ps.setString(1, updatedUser.getUsername());
                actualUser.setUsername(updatedUser.getUsername());
            } else {
                ps.setString(1, activeUser.getUsername());
                actualUser.setUsername(activeUser.getUsername());
            }

            if (updatedUser.getPassword() != null) {
                ps.setBytes(2, Base64.getDecoder().decode(updatedUser.getPassword()));
                actualUser.setUsername(updatedUser.getPassword());
            } else {
                ps.setBytes(2, Base64.getDecoder().decode(activeUser.getPassword()));
                actualUser.setUsername(activeUser.getPassword());
            }

            if (updatedUser.geteMailAddress() != null) {
                ps.setString(3, updatedUser.geteMailAddress());
                actualUser.setUsername(updatedUser.geteMailAddress());
            } else {
                ps.setString(3, activeUser.geteMailAddress());
                actualUser.setUsername(activeUser.geteMailAddress());
            }

            if (updatedUser.getFirstName() != null) {
                ps.setString(4, updatedUser.getFirstName());
                actualUser.setUsername(updatedUser.getFirstName());
            } else {
                ps.setString(4, activeUser.getFirstName());
                actualUser.setUsername(activeUser.getFirstName());
            }

            if (updatedUser.getLastName() != null) {
                ps.setString(5, updatedUser.getLastName());
                actualUser.setUsername(updatedUser.getLastName());
            } else {
                ps.setString(5, activeUser.getLastName());
                actualUser.setUsername(activeUser.getLastName());
            }

            ps.setInt(6, activeUser.getUserId());
            ps.executeUpdate();
            conn.close();
            return actualUser;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        } else {
            // TODO: throw error because someone tries to access data from someone else
            return null;
        }
    }

    /**
     * Deletes a user from the repository.
     *
     * @param username The username of the user performing the deletion on themselves.
     * @throws ValidationException if the user is not found.
     */
    public void deleteUser(String username) throws ValidationException {
        int activeUserId = getUserId(username);

        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();

            PreparedStatement ps = conn.prepareStatement(DELETE_USER);
            ps.setInt(1, activeUserId);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
