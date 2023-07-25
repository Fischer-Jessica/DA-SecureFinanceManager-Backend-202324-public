package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.User;
import at.htlhl.financialoverview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

/**
 * The UserController class handles the HTTP requests related to user management.
 *
 * <p>
 * This class is responsible for handling CRUD operations (Create, Read, Update, Delete) on User entities.
 * It interacts with the UserRepository to access and manipulate the User entities in the 'users' table of the 'financial_overview' PostgreSQL database.
 * </p>
 *
 * <p>
 * This controller provides endpoints for managing users, including adding a new user, updating an existing user, changing user information (username, password, email address, first name, last name),
 * and deleting a user. It also provides an endpoint for retrieving a list of all users, although this functionality might be restricted or removed in the final product for security and privacy reasons.
 * </p>
 *
 * <p>
 * The UserController class is annotated with the RestController annotation, which indicates that it is a controller that handles RESTful HTTP requests.
 * The CrossOrigin annotation allows cross-origin requests to this controller, enabling it to be accessed from different domains.
 * The RequestMapping annotation specifies the base path for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * This class works in conjunction with the UserRepository and other related classes to enable efficient management of User entities in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 1.3
 * @since 25.07.2023 (version 1.3)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview")
public class UserController {
    /** The UserRepository instance for accessing user data. */
    @Autowired
    UserRepository userRepository;

    /**
     * Validates user credentials and returns the authenticated user object if successful.
     *
     * This method authenticates the user with the provided username and password. It checks if the user
     * with the given username exists in the database and if the provided password matches the stored password
     * for that user. If the authentication is successful, it returns the User object representing the
     * authenticated user; otherwise, it returns null.
     *
     * @param usernameToValidate The username of the user to be authenticated.
     * @param passwordToValidate The password of the user to be authenticated.
     * @return The User object representing the authenticated user if successful, or null if authentication fails.
     */
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Validates user credentials and returns true or false based on the validation result.")
    public User authenticateUser(@RequestParam String usernameToValidate,
                                           @RequestParam String passwordToValidate) {
        return userRepository.authenticateUser(usernameToValidate, passwordToValidate);
    }

    /**
     * Returns a list of all users.
     * This will be restricted or removed in the final product.
     *
     * @return A list of all users.
     */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all users")
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    /**
     * Adds a new user.
     *
     * @param username        The username of the new user.
     * @param password        The password of the new user.
     * @param eMailAddress    The email address of the new user.
     * @param firstName       The first name of the new user.
     * @param lastName        The last name of the new user.
     * @return The ID of the newly created user.
     */
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new user")
    public int addUser(@RequestParam String username,
                       @RequestParam String password,
                       @RequestParam String eMailAddress,
                       @RequestParam String firstName,
                       @RequestParam String lastName) {
        return userRepository.addUser(username, password, eMailAddress, firstName, lastName);
    }

    /**
     * Updates an existing user.
     *
     * @param updatedUsername           The updated username of the user.
     * @param updatedPassword           The updated password of the user.
     * @param updatedEMailAddress       The updated e-mail-address of the user.
     * @param updatedFirstName          The updated first name of the user.
     * @param updatedLastName           The updated last name of the user.
     * @param loggedInUser              The logged-in user performing the update.
     */
    @PatchMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing user")
    public void updateUser(@RequestParam String updatedUsername,
                           @RequestParam String updatedPassword,
                           @RequestParam String updatedEMailAddress,
                           @RequestParam String updatedFirstName,
                           @RequestParam String updatedLastName,
                           @RequestBody User loggedInUser) {
        userRepository.updateUser(new User(loggedInUser.getUserId(), updatedUsername, updatedPassword, updatedEMailAddress, updatedFirstName, updatedLastName),
                loggedInUser);
    }

    /**
     * Changes the username of an existing user.
     *
     * @param updatedUsername   The updated username.
     * @param loggedInUser      The logged-in user the update is performed on.
     */
    @PatchMapping("/users/username")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the username of an existing user")
    public void updateUsername(@RequestParam String updatedUsername,
                               @RequestBody User loggedInUser) {
        userRepository.updateUsername(updatedUsername, loggedInUser);
    }

    /**
     * Changes the password of an existing user.
     *
     * @param updatedPassword   The updated password.
     * @param loggedInUser      The logged-in user the update is performed on.
     */
    @PatchMapping("/users/password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the password of an existing user")
    public void updatePassword(@RequestParam String updatedPassword,
                               @RequestBody User loggedInUser) {
        userRepository.updatePassword(updatedPassword, loggedInUser);
    }

    /**
     * Changes the email address of an existing user.
     *
     * @param updatedEMailAddress   The updated email address.
     * @param loggedInUser          The logged-in user the update is performed on.
     */
    @PatchMapping("/users/eMailAddress")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the e-mail-address of an existing user")
    public void updateEMailAddress(@RequestParam String updatedEMailAddress,
                                   @RequestBody User loggedInUser) {
        userRepository.updateEMailAddress(updatedEMailAddress, loggedInUser);
    }

    /**
     * Changes the first name of an existing user.
     *
     * @param updatedFirstName  The updated first name.
     * @param loggedInUser      The logged-in user the update is performed on.
     */
    @PatchMapping("/users/firstName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the first name of an existing user")
    public void updateFirstName(@RequestParam String updatedFirstName,
                                @RequestBody User loggedInUser) {
        userRepository.updateFirstName(updatedFirstName, loggedInUser);
    }

    /**
     * Changes the last name of an existing user.
     *
     * @param updatedLastName   The updated last name.
     * @param loggedInUser      The logged-in user the update is performed on.
     */
    @PatchMapping("/users/lastName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the last name of an existing user")
    public void updateLastName(@RequestParam String updatedLastName,
                               @RequestBody User loggedInUser) {
        userRepository.updateLastName(updatedLastName, loggedInUser);
    }

    /**
     * Deletes a user.
     *
     * @param loggedInUser The logged-in user the deletion is performed on.
     */
    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "deletes an user")
    public void deleteUser(@RequestBody User loggedInUser) {
        userRepository.deleteUser(loggedInUser);
    }
}
