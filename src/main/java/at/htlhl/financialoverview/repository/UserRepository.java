package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The UserRepository class handles the persistence operations for user data.
 *
 * @author Fischer
 * @version 1
 * @since 07.07.2023 (version 1)
 */
@Repository
public class UserRepository {
    /** JdbcTemplate which is used in the code below but implementing the template at each usage would be unnecessary */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves a list of all users.
     * This will be restricted or removed in the final product.
     *
     * @return A list of User objects representing all users.
     */
    public List<User> getUsers() {
        return null;
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
    public int addUser(String username, byte[] password, String eMailAddress, String firstName, String lastName) {
        return 0;
    }

    /**
     * Updates an existing user with new data.
     *
     * @param updatedUser The updated User object.
     * @param loggedInUser     The original User object to be updated.
     */
    public void updateUser(User updatedUser, User loggedInUser) {

    }

    /**
     * Updates the username of an existing user.
     *
     * @param username     The updated username.
     * @param loggedInUser The logged-in user performing the update.
     */
    public void updateUsername(String username, User loggedInUser) {
    }

    /**
     * Updates the password of an existing user.
     *
     * @param updatedPassword The updated password.
     * @param loggedInUser    The logged-in user performing the update.
     */
    public void updatePassword(String updatedPassword, User loggedInUser) {
    }

    /**
     * Updates the email address of an existing user.
     *
     * @param updatedEMailAddress The updated email address.
     * @param loggedInUser        The logged-in user performing the update.
     */
    public void updateEMailAddress(String updatedEMailAddress, User loggedInUser) {
    }

    /**
     * Updates the first name of an existing user.
     *
     * @param updatedFirstName The updated first name.
     * @param loggedInUser     The logged-in user performing the update.
     */
    public void updateFirstName(String updatedFirstName, User loggedInUser) {
    }

    /**
     * Updates the last name of an existing user.
     *
     * @param updatedLastName The updated last name.
     * @param loggedInUser    The logged-in user performing the update.
     */
    public void updateLastName(String updatedLastName, User loggedInUser) {
    }

    /**
     * Deletes a user from the repository.
     *
     * @param loggedInUser The logged-in user performing the deletion.
     */
    public void deleteUser(User loggedInUser) {

    }
}
