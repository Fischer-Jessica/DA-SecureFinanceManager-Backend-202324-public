package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.User;
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
 * @version 1.4
 * @since 25.07.2023 (version 1.4)
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

    /** SQL query to update the username of an existing user. */
    private static final String UPDATE_USERNAME = "UPDATE users " +
            "SET username = ? " +
            "WHERE pk_user_id = ?;";

    /** SQL query to update the password of an existing user. */
    private static final String UPDATE_PASSWORD = "UPDATE users " +
            "SET password = ? " +
            "WHERE pk_user_id = ?;";

    /** SQL query to update the email address of an existing user. */
    private static final String UPDATE_EMAIL_ADDRESS = "UPDATE users " +
            "SET email_address = ? " +
            "WHERE pk_user_id = ?;";

    /** SQL query to update the first name of an existing user. */
    private static final String UPDATE_FIRST_NAME = "UPDATE users " +
            "SET first_name = ? " +
            "WHERE pk_user_id = ?;";

    /** SQL query to update the last name of an existing user. */
    private static final String UPDATE_LAST_NAME = "UPDATE users " +
            "SET last_name = ? " +
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
    public static boolean validateUserCredentials(User loggedInUser) {
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
        return databaseUser.equals(loggedInUser);
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
            return databaseUser;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
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
    public int addUser(String username, String password, String eMailAddress, String firstName, String lastName) {
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

            return keyHolder.getKey().intValue();
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
    public void updateUser(User updatedUser, User loggedInUser) {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_USER);
                ps.setString(1, updatedUser.getUsername());
                ps.setBytes(2, Base64.getDecoder().decode(updatedUser.getPassword()));
                ps.setString(3, updatedUser.geteMailAddress());
                ps.setString(4, updatedUser.getFirstName());
                ps.setString(5, updatedUser.getLastName());
                ps.setInt(6, updatedUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the username of an existing user.
     *
     * @param updatedUsername       The updated username.
     * @param loggedInUser          The logged-in user performing the update.
     */
    public void updateUsername(String updatedUsername, User loggedInUser) {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_USERNAME);
                ps.setString(1, updatedUsername);
                ps.setInt(2, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the password of an existing user.
     *
     * @param updatedPassword       The updated password.
     * @param loggedInUser          The logged-in user performing the update.
     */
    public void updatePassword(String updatedPassword, User loggedInUser) {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_PASSWORD);
                ps.setBytes(1, Base64.getDecoder().decode(updatedPassword));
                ps.setInt(2, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the email address of an existing user.
     *
     * @param updatedEMailAddress   The updated email address.
     * @param loggedInUser          The logged-in user performing the update.
     */
    public void updateEMailAddress(String updatedEMailAddress, User loggedInUser) {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_EMAIL_ADDRESS);
                ps.setString(1, updatedEMailAddress);
                ps.setInt(2, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the first name of an existing user.
     *
     * @param updatedFirstName      The updated first name.
     * @param loggedInUser          The logged-in user performing the update.
     */
    public void updateFirstName(String updatedFirstName, User loggedInUser) {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_FIRST_NAME);
                ps.setString(1, updatedFirstName);
                ps.setInt(2, loggedInUser.getUserId());
                ps.executeUpdate();
                conn.close();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /**
     * Updates the last name of an existing user.
     *
     * @param updatedLastName       The updated last name.
     * @param loggedInUser          The logged-in user performing the update.
     */
    public void updateLastName(String updatedLastName, User loggedInUser) {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_LAST_NAME);
                ps.setString(1, updatedLastName);
                ps.setInt(2, loggedInUser.getUserId());
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
    public void deleteUser(User loggedInUser) {
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