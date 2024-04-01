package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.api.ApiUser;
import at.htlhl.securefinancemanager.model.database.DatabaseCategory;
import at.htlhl.securefinancemanager.model.database.DatabaseUser;
import at.htlhl.securefinancemanager.model.response.ResponseUser;
import at.htlhl.securefinancemanager.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
 * @fullName Fischer, Jessica Christina
 * @version 3.7
 * @since 01.04.2024 (version 3.7)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager")
public class UserController {
    /**
     * The UserRepository instance for accessing user data.
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Spring JDBC template for executing SQL queries and updates.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
     * @see UserRepository#getUserObject(JdbcTemplate, String)
     */
    @GetMapping(value = "/user", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns the currently authenticated user", description = "This endpoint returns information about the currently authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned the authenticated user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseUser.class))}),
            @ApiResponse(responseCode = "404", description = "the authenticated user was not found",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "500", description = "internal server error occurred",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> getUserV1(@AuthenticationPrincipal UserDetails activeUser) {
        try {
            return ResponseEntity.ok(UserRepository.getUserObject(jdbcTemplate, activeUser.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds a new user.
     *
     * @param newApiUser The User object representing the new user to be added.
     * @return The newly created user.
     */
    @PostMapping(value = "/users", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "creates a new user", description = "This endpoint creates a new user. It does not require a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "created a new user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseUser.class))}),
            @ApiResponse(responseCode = "400", description = "the username or the password is missing",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "409", description = "the username or the email address already exists",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "500", description = "internal server error occurred",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> addUserV1(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "The new User-Object.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiUser.class)
            )
    ) @RequestBody ApiUser newApiUser) {
        try {
            if (newApiUser.getUsername() == null || newApiUser.getUsername().isBlank()) {
                throw new MissingRequiredParameter("username is required");
            } else if (newApiUser.getPassword() == null || newApiUser.getPassword().isBlank()) {
                throw new MissingRequiredParameter("password is required");
            }

            boolean isUsernameExists = userRepository.checkUsername(newApiUser.getUsername());

            boolean isEmailExists = newApiUser.getEMailAddress() != null && !newApiUser.getEMailAddress().isBlank() &&
                    userRepository.checkEMailAddress(newApiUser.getEMailAddress());

            if (isUsernameExists && isEmailExists) {
                throw new ValidationException("Both username and email address already exist.");
            } else if (isUsernameExists) {
                throw new ValidationException("Username already exists.");
            } else if (isEmailExists) {
                throw new ValidationException("Email address already exists.");
            }

            return ResponseEntity.ok(userRepository.addUser(newApiUser));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getLocalizedMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds new users.
     *
     * @param newApiUsers The users representing the new users to be added.
     * @return A List of the newly created users.
     */
    @PostMapping(value = "/users", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "creates new users", description = "This endpoint creates new users. It does not require a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "created the new users",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseUser.class))}),
            @ApiResponse(responseCode = "400", description = "a username or a password is missing",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "409", description = "the username or the email address already exists",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "500", description = "internal server error occurred",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> addUsersV2(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "List of new Users. As it is a List, the objects need to be enclosed in [].",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiUser.class)
            )
    ) @RequestBody List<ApiUser> newApiUsers) {
        try {
            List<DatabaseUser> createdUsers = new ArrayList<>();
            for (ApiUser newApiUser : newApiUsers) {
                if (newApiUser.getUsername() == null || newApiUser.getUsername().isBlank()) {
                    throw new MissingRequiredParameter("username is required");
                } else if (newApiUser.getPassword() == null || newApiUser.getPassword().isBlank()) {
                    throw new MissingRequiredParameter("password is required");
                }

                boolean isUsernameExists = userRepository.checkUsername(newApiUser.getUsername());

                boolean isEmailExists = newApiUser.getEMailAddress() != null && !newApiUser.getEMailAddress().isBlank() &&
                        userRepository.checkEMailAddress(newApiUser.getEMailAddress());

                if (isUsernameExists && isEmailExists) {
                    throw new ValidationException("Both username and email address already exist.");
                } else if (isUsernameExists) {
                    throw new ValidationException("Username already exists.");
                } else if (isEmailExists) {
                    throw new ValidationException("Email address already exists.");
                }

                createdUsers.add(userRepository.addUser(newApiUser));
            }
            return ResponseEntity.ok(createdUsers);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getLocalizedMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds new users.
     *
     * @param mobileUserId The ID of the user in the mobile application.
     * @param newApiUsers  The users representing the new users to be added.
     * @return A List of the newly created users.
     */
    @PostMapping(value = "/users", headers = "API-Version=3")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "creates new users", description = "Creates new Users. It does not requires a Basic-Auth-Header. This version for mobile applications also requires a List of mobileUserIds added to the URL."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "created the new users",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseUser.class))}),
            @ApiResponse(responseCode = "400", description = "a mobileUserId is less than or equal to 0 or a username or a password is missing",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "409", description = "the username or the email address already exists",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "500", description = "internal server error occurred",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> addUsersV3(@Parameter(description = "List of mobileUserIds from mobile applications to be added to the URL. The mobileUserIds and newApiUsers must be in the same order.") @RequestParam List<Integer> mobileUserId,
                                             @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                     description = "List of new users. As it is a List, the objects need to be enclosed in [].",
                                                     required = true,
                                                     content = @Content(
                                                             mediaType = "application/json",
                                                             schema = @Schema(implementation = ApiUser.class)
                                                     )
                                             ) @RequestBody List<ApiUser> newApiUsers) {
        try {
            List<ResponseUser> createdUsers = new ArrayList<>();
            if (mobileUserId.size() != newApiUsers.size()) {
                throw new MissingRequiredParameter("the number of mobile user IDs must match the number of new users");
            }
            for (int i = 0; i < mobileUserId.size(); i++) {
                if (newApiUsers.get(i).getUsername() == null || newApiUsers.get(i).getUsername().isBlank()) {
                    throw new MissingRequiredParameter("username is required");
                } else if (newApiUsers.get(i).getPassword() == null || newApiUsers.get(i).getPassword().isBlank()) {
                    throw new MissingRequiredParameter("password is required");
                } else if (mobileUserId.get(i) <= 0) {
                    throw new MissingRequiredParameter("mobileUserId cannot be less than or equal to 0");
                }

                boolean isUsernameExists = userRepository.checkUsername(newApiUsers.get(i).getUsername());

                boolean isEmailExists = newApiUsers.get(i).getEMailAddress() != null && !newApiUsers.get(i).getEMailAddress().isBlank() &&
                        userRepository.checkEMailAddress(newApiUsers.get(i).getEMailAddress());

                if (isUsernameExists && isEmailExists) {
                    throw new ValidationException("Both username and email address already exist.");
                } else if (isUsernameExists) {
                    throw new ValidationException("Username already exists.");
                } else if (isEmailExists) {
                    throw new ValidationException("Email address already exists.");
                }

                createdUsers.add(new ResponseUser(userRepository.addUser(newApiUsers.get(i)), mobileUserId.get(i)));
            }
            return ResponseEntity.ok(createdUsers);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getLocalizedMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates an existing user.
     *
     * @param updatedApiUser The User object containing the updated user information.
     * @param activeUser     The UserDetails object representing the currently authenticated user.
     * @return The updated User object.
     */
    @PatchMapping(value = "/users", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "updates an existing user which is authenticated at the moment", description = "Updates an existing user, which is authenticated. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully updated the authenticated user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseCategory.class))}),
            @ApiResponse(responseCode = "404", description = "the authenticated user is not found",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "500", description = "internal server error occurred",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> updateUserV1(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "The updated user-Object.",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiUser.class)
            )
    ) @RequestBody ApiUser updatedApiUser,
                                               @AuthenticationPrincipal UserDetails activeUser) {
        try {
            return ResponseEntity.ok(userRepository.updateUser(updatedApiUser, activeUser.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes a user.
     *
     * @param activeUser The UserDetails object representing the currently authenticated user.
     * @return An Integer representing the number of deleted rows.
     */
    @DeleteMapping(value = "/users", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes an user which is authenticated at the moment", description = "Deletes an existing user, which is authenticated. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted the authenticated user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "the authenticated user was not found",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "500", description = "internal server error occurred",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> deleteUserV1(@AuthenticationPrincipal UserDetails activeUser) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.deleteUser(activeUser.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getLocalizedMessage());
        }
    }
}