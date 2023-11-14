package at.htlhl.securefinancemanager.repository;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.api.ApiUser;
import at.htlhl.securefinancemanager.model.database.DatabaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Base64;
import java.util.List;
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
 * @version 2.6
 * @since 14.11.2023 (version 2.6)
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
     * Retrieves a user object based on the provided username.
     *
     * @param activeUsername The username of the user to be retrieved.
     * @return The User object representing the user with the given username.
     * @throws ValidationException if the user is not found.
     */
    public static DatabaseUser getUserObject(String activeUsername) throws ValidationException {
        DatabaseUser databaseUser = null;
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
                databaseUser = new DatabaseUser(userId, username, Base64.getEncoder().encodeToString(password), eMailAddress, firstName, lastName);
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
    public List<DatabaseUser> getUsers() {
        return jdbcTemplate.query(SELECT_USERS, (rs, rowNum) -> {
            int userId = rs.getInt("pk_user_id");
            String username = rs.getString("username");
            byte[] password = rs.getBytes("password");
            String eMailAddress = rs.getString("email_address");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            return new DatabaseUser(userId, username, Base64.getEncoder().encodeToString(password), eMailAddress, firstName, lastName);
        });
    }

    /**
     * Adds a new user to the database.
     *
     * @param newApiUser The User object representing the new user to be added.
     * @return The User object representing the newly created user.
     */
    public DatabaseUser addUser(ApiUser newApiUser) {
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = conn.prepareStatement(INSERT_USER, new String[]{"pk_user_id"});
                ps.setString(1, newApiUser.getUsername());
                ps.setBytes(2, Base64.getDecoder().decode(newApiUser.getPassword()));
                if (newApiUser.getEMailAddress() == null) {
                    ps.setNull(3, Types.NULL);
                } else {
                    ps.setString(3, newApiUser.getEMailAddress());
                }
                if (newApiUser.getFirstName() == null) {
                    ps.setNull(4, Types.NULL);
                } else {
                    ps.setString(4, newApiUser.getFirstName());
                }
                if(newApiUser.getLastName() == null) {
                    ps.setNull(5, Types.NULL);
                } else {
                    ps.setString(5, newApiUser.getLastName());
                }
                return ps;
            }, keyHolder);

            userSingleton.addUser(newApiUser.getUsername(), Objects.requireNonNull(keyHolder.getKey()).intValue());
            return new DatabaseUser(Objects.requireNonNull(keyHolder.getKey()).intValue(), newApiUser.getUsername(), newApiUser.getPassword(), newApiUser.getEMailAddress(), newApiUser.getFirstName(), newApiUser.getLastName());
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
    public DatabaseUser updateUser(ApiUser updatedUser, String username) throws ValidationException {
        DatabaseUser oldDatabaseUser = getUserObject(username);
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_USER);

            if (updatedUser.getUsername() != null) {
                ps.setString(1, updatedUser.getUsername());
                username = updatedUser.getUsername();
            } else {
                ps.setString(1, oldDatabaseUser.getUsername());
                username = oldDatabaseUser.getUsername();
            }

            if (updatedUser.getPassword() != null) {
                ps.setBytes(2, Base64.getDecoder().decode(updatedUser.getPassword()));
            } else {
                ps.setBytes(2, Base64.getDecoder().decode(oldDatabaseUser.getPassword()));
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
            conn.close();
            return getUserObject(username);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Deletes a user from the repository.
     *
     * @param username The username of the user performing the deletion on themselves.
     */
    public void deleteUser(String username) {
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();

            PreparedStatement ps = conn.prepareStatement(DELETE_USER);
            ps.setInt(1, userSingleton.getUserId(username));
            ps.executeUpdate();
            conn.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}