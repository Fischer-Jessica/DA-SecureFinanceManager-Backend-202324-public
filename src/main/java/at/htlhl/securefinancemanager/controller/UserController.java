package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.User;
import at.htlhl.securefinancemanager.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
 * @version 2.1
 * @since 15.10.2023 (version 2.1)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager")
public class UserController {
    /** The UserRepository instance for accessing user data. */
    @Autowired
    UserRepository userRepository;

    /**
     * Returns a list of all users.
     *
     * @return A list of all users.
     */
    // TODO: This will be restricted or removed in the final product.
    @GetMapping(value = "/users", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all users")
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    /**
     * Adds a new user.
     *
     * @param newUser The user object representing the new user to be added.
     * @return The ID of the newly created user.
     */
    @PostMapping(value = "/users", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new user")
    public User addUser(@RequestBody User newUser) {
        return userRepository.addUser(newUser);
    }

    /**
     * Updates an existing user.
     *
     * @param updatedUser The user object containing the updated user information.
     * @param activeUser The UserDetails object representing the currently authenticated user.
     * @return ResponseEntity with the updated user if successful, or an error response if validation fails.
     */
    @PatchMapping(value = "/users/{userId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "change an existing user")
    public ResponseEntity<Object> updateUser(@PathVariable int userId,
                                             @RequestBody(required = false) User updatedUser,
                                             @AuthenticationPrincipal UserDetails activeUser) {
        try {
            return ResponseEntity.ok(userRepository.updateUser(userId, updatedUser, activeUser.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes a user.
     *
     * @param activeUser The UserDetails object representing the currently authenticated user.
     * @return ResponseEntity indicating the success of the deletion operation or an error response if validation fails.
     */
    @DeleteMapping(value = "/users", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes an user")
    public ResponseEntity<Object> deleteUser(@AuthenticationPrincipal UserDetails activeUser) {
        try {
            userRepository.deleteUser(activeUser.getUsername());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }
}