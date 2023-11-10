package at.htlhl.securefinancemanager.model.database;

import at.htlhl.securefinancemanager.model.api.ApiUser;

/**
 * The {@code DatabaseUser} class represents a user entity in the 'users' table of the 'secure_finance_manager' PostgreSQL database.
 * It extends the {@link ApiUser} class, inheriting properties and methods related to user information.
 *
 * <p>
 * This class includes an additional attribute, the user ID, which is specific to the database representation.
 * </p>
 *
 * <p>
 * The class provides multiple constructors to create a new DatabaseUser object with the specified properties.
 * Additionally, getter and setter methods are provided to retrieve and update the value of the user ID.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * DatabaseUser user = new DatabaseUser(123, "john_doe", "password123", "john.doe@example.com", "John", "Doe");
 * }</pre>
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work in conjunction with other entities such as {@link ApiUser}.
 * </p>
 *
 * @author Fischer
 * @version 1.7
 * @since 10.11.2023 (version 1.7)
 *
 * @see ApiUser this class (User) for the explanations of the annotations
 */
public class DatabaseUser extends ApiUser {
    /** The unique identifier for this User. It is generated automatically by the database. */
    private int userId;

    /** Default constructor. */
    public DatabaseUser() {}

    /**
     * Constructs a new DatabaseUser object with the specified properties.
     *
     * @param userId        The unique identifier for the user.
     * @param username      The username of the user.
     * @param password      The password of the user.
     * @param eMailAddress  The email address of the user.
     * @param firstName     The first name of the user.
     * @param lastName      The last name of the user.
     */
    public DatabaseUser(int userId, String username, String password, String eMailAddress, String firstName, String lastName) {
        super(username, password, eMailAddress, firstName, lastName);
        this.userId = userId;
    }

    /**
     * Returns the unique identifier for the user.
     *
     * @return The unique identifier for the user.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param userId The unique identifier for the user.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}