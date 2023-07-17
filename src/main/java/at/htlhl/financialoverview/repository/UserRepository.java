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
 * The UserRepository class handles the persistence operations for user data.
 *
 * @author Fischer
 * @version 1.1
 * @since 17.07.2023 (version 1.1)
 */
@Repository
public class UserRepository {
    /** JdbcTemplate which is used in the code below but implementing the template at each usage would be unnecessary */
    @Autowired
    private static JdbcTemplate jdbcTemplate = new JdbcTemplate();

    private static final String SELECT_USER = "SELECT pk_user_id, username, password, email_address, first_name, last_name FROM users WHERE pk_user_id = ?;";

    private static final String SELECT_USERS = "SELECT pk_user_id, username, password, email_address, first_name, last_name FROM users;";

    private static final String INSERT_USER = "INSERT INTO users (username, password, email_address, first_name, last_name) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_USER = "UPDATE users " +
            "SET username = ?, password = ?, email_address = ?, first_name = ?, last_name = ? " +
            "WHERE pk_user_id = ?;";

    private static final String UPDATE_USERNAME = "UPDATE users SET username = ? WHERE pk_user_id = ?;";
    private static final String UPDATE_PASSWORD = "UPDATE users SET password = ? WHERE pk_user_id = ?;";
    private static final String UPDATE_EMAIL_ADDRESS = "UPDATE users SET email_address = ? WHERE pk_user_id = ?;";
    private static final String UPDATE_FIRST_NAME = "UPDATE users SET first_name = ? WHERE pk_user_id = ?;";
    private static final String UPDATE_LAST_NAME = "UPDATE users SET last_name = ? WHERE pk_user_id = ?;";

    private static final String DELETE_USER = "DELETE FROM users WHERE pk_user_id = ?;";

    @Autowired
    public UserRepository(DataSource dataSource) {
        UserRepository.jdbcTemplate = new JdbcTemplate(dataSource);
    }

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
     * @param updatedUser The updated User object.
     * @param loggedInUser     The original User object to be updated.
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
     * @param username     The updated username.
     * @param loggedInUser The logged-in user performing the update.
     */
    public void updateUsername(String username, User loggedInUser) {
        if (UserRepository.validateUserCredentials(loggedInUser)) {
            try {
                Connection conn = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_USERNAME);
                ps.setString(1, username);
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
     * @param updatedPassword The updated password.
     * @param loggedInUser    The logged-in user performing the update.
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
     * @param updatedEMailAddress The updated email address.
     * @param loggedInUser        The logged-in user performing the update.
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
     * @param updatedFirstName The updated first name.
     * @param loggedInUser     The logged-in user performing the update.
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
     * @param updatedLastName The updated last name.
     * @param loggedInUser    The logged-in user performing the update.
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
     * @param loggedInUser The logged-in user performing the deletion.
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
