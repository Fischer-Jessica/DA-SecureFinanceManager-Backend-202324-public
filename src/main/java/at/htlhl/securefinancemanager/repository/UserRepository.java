package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.User;
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
 * The CategoryRepository class handles the persistence operations for category data.
 * It provides methods to access and manipulate the 'categories' table in the 'financial_overview' PostgreSQL database.
 *
 * <p>
 * This class interacts with the Category entity class, which represents a POJO (Plain Old Java Object) or entity class that maps to the 'categories' table in the database.
 * It uses Spring Data JPA to provide generic CRUD (Create, Read, Update, Delete) operations for the Category entity, reducing boilerplate code.
 * </p>
 *
 * <p>
 * The CategoryRepository serves as an abstraction layer between the CategoryController and the underlying data storage, enabling seamless access and manipulation of Category entities.
 * </p>
 *
 * @author Fischer
 * @version 1.7
 * @since 05.10.2023 (version 1.7)
 */
@Repository
public class UserRepository {
    /** JdbcTemplate which is used for executing SQL statements, in the other repositories too, but implementing the template at each usage would be unnecessary */
    @Autowired
    protected static JdbcTemplate jdbcTemplate = new JdbcTemplate();

    /** SQL query to retrieve a user based on the user ID. */
    private static final String SELECT_USER = "SELECT pk_user_id, username, password, email_address, first_name, last_name " +
            "FROM users " +
            "WHERE pk_user_id = ?;";

    /** SQL query to select user data from the database based on the provided username and password. */
    private static final String SELECT_USER_BY_USERNAME_AND_PASSWORD = "SELECT pk_user_id, username, password, email_address, first_name, last_name " +
            "FROM users " +
            "WHERE username = ? AND password = ?;";


    /** SQL query to retrieve all users. */
    private static final String SELECT_USERS = "SELECT pk_user_id, username, password, email_address, first_name, last_name " +
            "FROM users;";

    /** SQL query to insert a new user into the database. */
    private static final String INSERT_USER = "INSERT INTO users " +
            "(username, password, email_address, first_name, last_name) " +
            "VALUES (?, ?, ?, ?, ?);";

    /** SQL query to update an existing user. */
    private static final String UPDATE_USER = "UPDATE users " +
            "SET username = ?, password = ?, email_address = ?, first_name = ?, last_name = ? " +
            "WHERE pk_user_id = ?;";

    /** SQL query to delete a user from the repository. */
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
     * Validates the credentials of a user.
     *
     * @param loggedInUser The User object containing the user credentials to be validated.
     * @return True if the credentials are valid, false otherwise.
     */
    public static boolean validateUserCredentials(User loggedInUser) throws ValidationException {
        User databaseUser = null;
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_USER);
            ps.setInt(1, loggedInUser.getUserId());
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
            throw new ValidationException("Invalid credentials.");
        } else {
            return databaseUser.equals(loggedInUser);
        }
    }

    /**
     * Authenticates the user with the provided username and password.
     *
     * This method checks if the user with the given username exists in the database and
     * if the provided password matches the stored password for that user. If the
     * authentication is successful, it returns the User object representing the
     * authenticated user; otherwise, it returns null.
     *
     * @param usernameToValidate The username of the user to be authenticated.
     * @param passwordToValidate The password of the user to be authenticated.
     * @return The User object representing the authenticated user if successful, or null if authentication fails.
     * @throws RuntimeException If an error occurs while accessing the database.
     */
    public User authenticateUser(String usernameToValidate, String passwordToValidate) {
        User databaseUser = null;
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD);
            ps.setString(1, usernameToValidate);
            ps.setBytes(2, Base64.getDecoder().decode(passwordToValidate));
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
        return databaseUser;
    }

    /**
     * Retrieves a list of all users.
     * This will be restricted or removed in the final product.
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
            return new User(userId, username, Base64.getEncoder().encodeToString(password), eMailAddress, firstName,lastName);
        });
    }

    /**
     * Adds a new user to the database.
     *
     * @param username        The username of the new user.
     * @param password        The password of the new user.
     * @param eMailAddress    The email address of the new user.
     * @param firstName       The first name of the new user.
     * @param lastName        The last name of the new user.
     * @return The ID of the newly created user.
     */
    public User addUser(String username, String password, String eMailAddress, String firstName, String lastName) {
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_USER, new String[]{"pk_user_id"});
                ps.setString(1, username);
                ps.setBytes(2, Base64.getDecoder().decode(password));
                ps.setString(3, eMailAddress);
                ps.setString(4, firstName);
                ps.setString(5, lastName);
                return ps;
            }, keyHolder);

            return new User(Objects.requireNonNull(keyHolder.getKey()).intValue(), username, password, eMailAddress, firstName, lastName);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Updates an existing user with new data.
     *
     * @param updatedUser       The updated User object.
     * @param loggedInUser      The original User object to be updated.
     */
    public void updateUser(User updatedUser, User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_USER);

                if (updatedUser.getUsername() != null) {
                    ps.setString(1, updatedUser.getUsername());
                } else {
                    ps.setString(1, loggedInUser.getUsername());
                }

                if (updatedUser.getPassword() != null) {
                    ps.setBytes(2, Base64.getDecoder().decode(updatedUser.getPassword()));
                } else {
                    ps.setBytes(2, Base64.getDecoder().decode(loggedInUser.getPassword()));
                }

                if (updatedUser.geteMailAddress() != null) {
                    ps.setString(3, updatedUser.geteMailAddress());
                } else {
                    ps.setString(3, loggedInUser.geteMailAddress());
                }

                if (updatedUser.getFirstName() != null) {
                    ps.setString(4, updatedUser.getFirstName());
                } else {
                    ps.setString(4, loggedInUser.getFirstName());
                }

                if (updatedUser.getLastName() != null) {
                    ps.setString(5, updatedUser.getLastName());
                } else {
                    ps.setString(5, loggedInUser.getLastName());
                }

                ps.setInt(6, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Deletes a user from the repository.
     *
     * @param loggedInUser The logged-in user performing the deletion on itself.
     */
    public void deleteUser(User loggedInUser) throws ValidationException {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = jdbcTemplate.getDataSource().getConnection();

                PreparedStatement ps = conn.prepareStatement(DELETE_USER);
                ps.setInt(1, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}