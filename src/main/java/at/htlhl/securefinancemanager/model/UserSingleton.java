package at.htlhl.securefinancemanager.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code UserSingleton} class is a singleton implementation for managing user information,
 * storing a mapping of usernames to user IDs.
 *
 * <p>
 * This class follows the Singleton design pattern, ensuring that only one instance is created
 * and providing a global point of access to that instance.
 * </p>
 *
 * <p>
 * Usage:
 * </p>
 *
 * <pre>{@code
 * // Retrieve the singleton instance
 * UserSingleton userSingleton = UserSingleton.getInstance();
 *
 * // Add a new user to the user map
 * userSingleton.addUser("exampleUser", 123);
 *
 * // Retrieve the user ID associated with a username
 * int userId = userSingleton.getUserId("exampleUser");
 *
 * // Update the username associated with a user ID
 * userSingleton.updateUsername(123, "newUsername");
 *
 * // Remove a user entry based on the user ID
 * userSingleton.removeUserById(123);
 * }</pre>
 *
 * <p>
 * The class provides methods to add a new user to the user map, retrieve the user ID associated
 * with a given username, update the username associated with a user ID, and remove a user entry based on the user ID.
 * If the username is not found in the map, a default value of -1 is returned.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 1.2
 * @since 02.02.2024 (version 1.3)
 */
public class UserSingleton {
    /**
     * Represents the single instance of the UserSingleton class.
     * It is marked as static to ensure only one instance exists across the entire application.
     */
    private static UserSingleton instance;

    /**
     * A map that associates usernames (as String) with user IDs (as Integer).
     * It is used to store user information, allowing quick retrieval of user IDs based on usernames.
     * Marked as private to encapsulate the internal state of the class.
     */
    private Map<String, Integer> userMap;

    /**
     * Private constructor to initialize the userMap.
     */
    private UserSingleton() {
        userMap = new HashMap<>();
    }

    /**
     * Retrieves the instance of the UserSingleton class.
     *
     * @return The singleton instance of UserSingleton.
     */
    public static synchronized UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    /**
     * Adds a new user to the user map with the specified username and user ID.
     *
     * @param username The username of the new user.
     * @param userId   The user ID associated with the username.
     */
    public void addUser(String username, int userId) {
        userMap.put(username, userId);
    }

    /**
     * Retrieves the user ID associated with the given username.
     *
     * @param username The username for which to retrieve the user ID.
     * @return The user ID associated with the username, or a default value (-1) if not found.
     */
    public int getUserId(String username) {
        return userMap.getOrDefault(username, -1);
    }

    /**
     * Updates the username associated with the given user ID.
     *
     * @param userId      The user ID for which to update the username.
     * @param newUsername The new username to associate with the user ID.
     */
    public void updateUsername(int userId, String newUsername) {
        userMap.entrySet().stream().filter(entry -> entry.getValue().equals(userId)).findFirst().ifPresent(entry -> {
            userMap.remove(entry.getKey());
            userMap.put(newUsername, userId);
        });
    }

    /**
     * Removes a user entry from the user map based on the user ID.
     *
     * @param userId The user ID for which to remove the user entry.
     */
    public void removeUserById(int userId) {
        userMap.entrySet().removeIf(entry -> entry.getValue().equals(userId));
    }
}