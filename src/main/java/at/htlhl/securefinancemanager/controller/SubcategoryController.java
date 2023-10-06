package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.Subcategory;
import at.htlhl.securefinancemanager.model.User;
import at.htlhl.securefinancemanager.repository.SubcategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * The SubcategoryController class handles the HTTP requests related to subcategories.
 *
 * <p>
 * This controller is responsible for handling CRUD operations (Create, Read, Update, Delete) on Subcategory entities.
 * It interacts with the SubcategoryRepository to access and manipulate the Subcategory entities in the 'subcategories' table of the 'financial_overview' PostgreSQL database.
 * </p>
 *
 * <p>
 * This class is annotated with the {@link RestController} annotation, which indicates that it is a controller that handles RESTful HTTP requests.
 * The {@link CrossOrigin} annotation allows cross-origin requests to this controller, enabling it to be accessed from different domains.
 * The {@link RequestMapping} annotation specifies the base path for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * The SubcategoryController class works in conjunction with the SubcategoryRepository and other related classes to enable efficient management of subcategories in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 1.9
 * @since 06.10.2023 (version 1.9)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager/categories/{categoryId}")
public class SubcategoryController {
    /** The SubcategoryRepository instance for accessing subcategory data. */
    @Autowired
    SubcategoryRepository subcategoryRepository;

    /**
     * Returns a list of all subcategories for a specific category.
     *
     * @param categoryId            The ID of the category.
     * @param loggedInUserId        The ID of the logged-in user.
     * @param loggedInUsername      The username of the logged-in user.
     * @param loggedInPassword      The password of the logged-in user.
     * @param loggedInEMailAddress  The email address of the logged-in user.
     * @param loggedInFirstName     The first name of the logged-in user.
     * @param loggedInLastName      The last name of the logged-in user.
     * @return A list of subcategories for the specified category.
     */
    @GetMapping("/subcategories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all subcategories of one category")
    public ResponseEntity<Object> getSubcategories(@PathVariable int categoryId,
                                                    @RequestParam int loggedInUserId,
                                                    @RequestParam String loggedInUsername,
                                                    @RequestParam String loggedInPassword,
                                                    @RequestParam String loggedInEMailAddress,
                                                    @RequestParam String loggedInFirstName,
                                                    @RequestParam String loggedInLastName) {
        try {
            return ResponseEntity.ok(subcategoryRepository.getSubcategories(categoryId,
                    new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName)));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Returns a specific subcategory for a specific category.
     *
     * @param categoryId            The ID of the category.
     * @param subcategoryId         The ID of the subcategory.
     * @param loggedInUserId        The ID of the logged-in user.
     * @param loggedInUsername      The username of the logged-in user.
     * @param loggedInPassword      The password of the logged-in user.
     * @param loggedInEMailAddress  The email address of the logged-in user.
     * @param loggedInFirstName     The first name of the logged-in user.
     * @param loggedInLastName      The last name of the logged-in user.
     * @return The requested subcategory.
     */
    @GetMapping("/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one subcategory")
    public ResponseEntity<Object> getSubcategory(@PathVariable int categoryId,
                                                      @PathVariable int subcategoryId,
                                                      @RequestParam int loggedInUserId,
                                                      @RequestParam String loggedInUsername,
                                                      @RequestParam String loggedInPassword,
                                                      @RequestParam String loggedInEMailAddress,
                                                      @RequestParam String loggedInFirstName,
                                                      @RequestParam String loggedInLastName) {
        try {
            return ResponseEntity.ok(subcategoryRepository.getSubcategory(categoryId, subcategoryId,
                    new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName)));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds a new subcategory to a specific category.
     *
     * @param categoryId                The ID of the category, in which the subcategory will be created.
     * @param subcategoryName           The name of the new subcategory.
     * @param subcategoryDescription    The description of the new subcategory.
     * @param subcategoryColourId       The ID of the color for the new subcategory.
     * @param loggedInUser              The logged-in user.
     * @return The ID of the newly created subcategory.
     */
    @PostMapping("/subcategories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new subcategory")
    public ResponseEntity<Object> addSubcategory(@PathVariable int categoryId,
                                                  @RequestParam String subcategoryName,
                                                  @RequestParam String subcategoryDescription,
                                                  @RequestParam int subcategoryColourId,
                                                  @RequestBody User loggedInUser) {
        try {
            return ResponseEntity.ok(subcategoryRepository.addSubcategory(categoryId,
                    subcategoryName, subcategoryDescription, subcategoryColourId,
                    loggedInUser));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates an existing subcategory for a specific category.
     *
     * @param categoryId                            The ID of the category.
     * @param subcategoryId                         The ID of the subcategory to update.
     * @param updatedCategoryId                     The updated ID of the superior category.
     * @param updatedSubcategoryName                The updated name of the subcategory.
     * @param updatedSubcategoryDescription         The updated description of the subcategory.
     * @param updatedSubcategoryColourId            The updated colour of the subcategory.
     * @param loggedInUser                          The logged-in user.
     */
    @PatchMapping("/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing subcategory")
    public ResponseEntity<Object> updateSubcategory(@PathVariable int categoryId,
                                            @PathVariable int subcategoryId,
                                            @RequestParam(defaultValue = "-1", required = false) int updatedCategoryId,
                                            @RequestParam(required = false) String updatedSubcategoryName,
                                            @RequestParam(required = false) String updatedSubcategoryDescription,
                                            @RequestParam(defaultValue = "-1", required = false) int updatedSubcategoryColourId,
                                            @RequestBody User loggedInUser) {
        try {
            subcategoryRepository.updateSubcategory(categoryId, subcategoryId, updatedCategoryId,
                    updatedSubcategoryName, updatedSubcategoryDescription, updatedSubcategoryColourId,
                loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes a subcategory for a specific category.
     *
     * @param categoryId       The ID of the category.
     * @param subcategoryId    The ID of the subcategory to delete.
     * @param loggedInUser The logged-in user.
     */
    @DeleteMapping("/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete a subcategory")
    public ResponseEntity<Object> deleteSubcategory(@PathVariable int categoryId,
                                  @PathVariable int subcategoryId,
                                  @RequestBody User loggedInUser) {
        try {
            subcategoryRepository.deleteSubcategory(categoryId, subcategoryId, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }
}
