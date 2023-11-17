package at.htlhl.securefinancemanager.model.response;

import at.htlhl.securefinancemanager.model.database.DatabaseUser;

/**
 * The {@code ResponseUser} class represents a user response entity used in communication with mobile applications.
 * It extends the {@link DatabaseUser} class, inheriting properties and methods related to user information.
 *
 * <p>
 * This class includes an additional attribute, {@code mobileUserId}, which is specific to the mobile application representation.
 * </p>
 *
 * <p>
 * The class provides a constructor to create a new ResponseUser object with the specified properties.
 * Additionally, getter methods are provided to retrieve the values of user attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * ResponseUser responseUser = new ResponseUser(databaseUser, 9876);
 * }</pre>
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work in conjunction with other entities such as {@link DatabaseUser}.
 * </p>
 *
 * @author Fischer
 * @version 1.0
 * @since 17.11.2023 (version 1.0)
 *
 * @see DatabaseUser
 */
public class ResponseUser extends DatabaseUser {
    /** The unique identifier for the mobile user. */
    private int mobileUserId;

    /**
     * Constructs a new ResponseUser object with the specified properties.
     *
     * @param databaseUser       The DatabaseUser object.
     * @param mobileUserId       The unique identifier for the mobile user.
     */
    public ResponseUser(DatabaseUser databaseUser, int mobileUserId) {
        super(databaseUser.getUserId(), databaseUser.getUsername(), databaseUser.getPassword(), databaseUser.getEMailAddress(), databaseUser.getFirstName(), databaseUser.getLastName());
        this.mobileUserId = mobileUserId;
    }
}