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
 * @author Fischer
 * @version 1
 * @since 07.07.2023 (version 1)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview")
public class UserController {
    /** The UserRepository instance for accessing user data. */
    @Autowired
    UserRepository userRepository;

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
                       @RequestParam byte[] password,
                       @RequestParam String eMailAddress,
                       @RequestParam String firstName,
                       @RequestParam String lastName) {
        return userRepository.addUser(username, password, eMailAddress, firstName, lastName);
    }

    /**
     * Updates an existing user.
     *
     * @param updatedUser     The updated user.
     * @param loggedInUser    The logged-in user performing the update.
     */
    @PatchMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing user")
    public void updateUser(@RequestBody User updatedUser, @RequestBody User loggedInUser) {
        userRepository.updateUser(updatedUser, loggedInUser);
    }

    /**
     * Changes the username of an existing user.
     *
     * @param updatedUsername The updated username.
     * @param loggedInUser    The logged-in user performing the update.
     */
    @PatchMapping("/users/username")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the username of an existing user")
    public void updateUsername(@RequestParam String updatedUsername, @RequestBody User loggedInUser) {
        userRepository.updateUsername(updatedUsername, loggedInUser);
    }

    /**
     * Changes the password of an existing user.
     *
     * @param updatedPassword The updated password.
     * @param loggedInUser    The logged-in user performing the update.
     */
    @PatchMapping("/users/password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the password of an existing user")
    public void updatePassword(@RequestParam String updatedPassword, @RequestBody User loggedInUser) {
        userRepository.updatePassword(updatedPassword, loggedInUser);
    }

    /**
     * Changes the email address of an existing user.
     *
     * @param updatedEMailAddress  The updated email address.
     * @param loggedInUser         The logged-in user performing the update.
     */
    @PatchMapping("/users/eMailAddress")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the e-mail-address of an existing user")
    public void updateEMailAddress(@RequestParam String updatedEMailAddress, @RequestBody User loggedInUser) {
        userRepository.updateEMailAddress(updatedEMailAddress, loggedInUser);
    }

    /**
     * Changes the first name of an existing user.
     *
     * @param updatedFirstName The updated first name.
     * @param loggedInUser     The logged-in user performing the update.
     */
    @PatchMapping("/users/firstName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the first name of an existing user")
    public void updateFirstName(@RequestParam String updatedFirstName, @RequestBody User loggedInUser) {
        userRepository.updateFirstName(updatedFirstName, loggedInUser);
    }

    /**
     * Changes the last name of an existing user.
     *
     * @param updatedLastName The updated last name.
     * @param loggedInUser    The logged-in user performing the update.
     */
    @PatchMapping("/users/lastName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the last name of an existing user")
    public void updateLastName(@RequestParam String updatedLastName, @RequestBody User loggedInUser) {
        userRepository.updateLastName(updatedLastName, loggedInUser);
    }

    /**
     * Deletes a user.
     *
     * @param loggedInUser The logged-in user performing the deletion.
     */
    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "deletes an user")
    public void deleteUser(@RequestBody User loggedInUser) {
        userRepository.deleteUser(loggedInUser);
    }
}
