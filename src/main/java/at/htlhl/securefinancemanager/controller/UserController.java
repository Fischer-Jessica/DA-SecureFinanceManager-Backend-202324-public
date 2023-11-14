package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.api.ApiUser;
import at.htlhl.securefinancemanager.model.database.DatabaseUser;
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
 * The UserController class handles HTTP requests related to user management.
 *
 * <p>
 * This class is responsible for handling CRUD operations (Create, Read, Update, Delete) on User entities.
 * It interacts with the UserRepository to access and manipulate the User entities
 * in the 'users' table of the 'secure_finance_manager' PostgreSQL database.
 * </p>
 *
 * <p>
 * This controller provides endpoints for managing users, including adding a new user, updating an existing user,
 * changing user information (username, password, email address, first name, last name), and deleting a user.
 * It also provides an endpoint for retrieving a list of all users, although this functionality will be removed
 * in the final product for security and privacy reasons.
 * </p>
 *
 * <p>
 * The UserController class is annotated with the RestController annotation, which indicates that it is a controller
 * that handles RESTful HTTP requests. The CrossOrigin annotation allows cross-origin requests to this controller,
 * enabling it to be accessed from different domains. The RequestMapping annotation specifies the base path
 * for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * This class works in conjunction with the UserRepository and other related classes to enable efficient management
 * of User entities in the secure finance manager system.
 * </p>
 *
 * @author Fischer
 * @version 2.7
 * @since 14.11.2023 (version 2.7)
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
    public List<DatabaseUser> getUsers() {
        return userRepository.getUsers();
    }

    /**
     * Retrieves information about the currently authenticated user upon successful login.
     * <p>
     * This endpoint allows a user to authenticate on a device and retrieve their associated data.
     * The user must be authenticated with the API version set to 0, and have the 'ROLE_USER' authority.
     *
     * @param activeUser The UserDetails object representing the currently authenticated user.
     * @return ResponseEntity containing the user's information if authentication is successful.
     * Returns UNAUTHORIZED status with an error message if authentication fails.
     * @see UserDetails
     * @see UserRepository#getUserObject(String)
     */
    @GetMapping(value = "/user", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns the currently authenticated user")
    public ResponseEntity<Object> getUser(@AuthenticationPrincipal UserDetails activeUser) {
        try {
            return ResponseEntity.ok(UserRepository.getUserObject(activeUser.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds a new user.
     *
     * @param newApiUser    The User object representing the new user to be added.
     * @return The newly created user.
     */
    @PostMapping(value = "/users", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new user")
    public ResponseEntity<Object> addUser(@RequestBody ApiUser newApiUser) {
        try {
            if (newApiUser.getUsername() == null || newApiUser.getUsername().isBlank()) {
                throw new MissingRequiredParameter("username is required");
            } else if (newApiUser.getPassword() == null || newApiUser.getPassword().isBlank()) {
                throw new MissingRequiredParameter("password is required");
            }
            return ResponseEntity.ok(userRepository.addUser(newApiUser));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates an existing user.
     *
     * @param updatedApiUser    The User object containing the updated user information.
     * @param activeUser        The UserDetails object representing the currently authenticated user.
     * @return The updated User object.
     */
    @PatchMapping(value = "/users", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "change an existing user")
    public ResponseEntity<Object> updateUser(@RequestBody ApiUser updatedApiUser,
                                             @AuthenticationPrincipal UserDetails activeUser) {
        try {
            return ResponseEntity.ok(userRepository.updateUser(updatedApiUser, activeUser.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes a user.
     *
     * @param activeUser The UserDetails object representing the currently authenticated user.
     * @return An Integer representing the number of deleted rows.
     */
    @DeleteMapping(value = "/users", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes an user")
    public ResponseEntity<Object> deleteUser(@AuthenticationPrincipal UserDetails activeUser) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.deleteUser(activeUser.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }
}