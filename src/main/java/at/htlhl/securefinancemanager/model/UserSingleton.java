package at.htlhl.securefinancemanager.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class for managing user information, storing a mapping of usernames to user IDs.
 *
 * @author Fischer
 * @version 1.0
 * @since 14.11.2023 (version 1.0)
 */
public class UserSingleton {
    private static UserSingleton instance;
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
}