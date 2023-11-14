package at.htlhl.securefinancemanager.model.api;

/**
 * The {@code ApiUser} class represents a user entity in the secure finance manager system.
 *
 * <p>
 * This class includes attributes such as the username, password, email address, first name, and last name.
 * It is used as a data transfer object (DTO) to facilitate communication between the frontend and backend.
 * </p>
 *
 * <p>
 * The {@code ApiUser} class is designed to be instantiated with a parameterized constructor that sets the username,
 * password, email address, first name, and last name. It also provides getter and setter methods to retrieve and modify these attributes.
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work in conjunction with other entities.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * ApiUser user = new ApiUser("john_doe", "password123", "john.doe@example.com", "John", "Doe");
 * }</pre>
 * </p>
 *
 * <p>
 * This class is not annotated with JPA annotations since it is a DTO and does not directly map to a database entity.
 * </p>
 *
 * <p>
 * Note: It is important to handle password storage securely, and the use of hashed passwords is recommended.
 * </p>
 *
 * @author Fischer
 * @version 1.7
 * @since 10.11.2023 (version 1.7)
 */
public class ApiUser {
    private String username;

    /** The password of the user. */
    private String password;

    /** The email address of the user. */
    private String eMailAddress;

    /** The first name of the user. */
    private String firstName;

    /** The last name of the user. */
    private String lastName;

    /** Default constructor. */
    public ApiUser() {
    }

    /**
     * Constructs a new User instance with the specified details.
     *
     * @param username      The username of the user.
     * @param password      The password of the user.
     * @param eMailAddress  The email address of the user.
     * @param firstName     The first name of the user.
     * @param lastName      The last name of the user.
     */
    public ApiUser(String username, String password, String eMailAddress, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.eMailAddress = eMailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the email address of the user.
     *
     * @return The email address.
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * Retrieves the first name of the user.
     *
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieves the last name of the user.
     *
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the email address of the user.
     *
     * @param eMailAddress The email address to set.
     */
    public void setEMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}