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
 * @version 1.5
 * @since 25.07.2023 (version 1.5)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview/categories/{categoryId}")
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
    public ResponseEntity<List<Subcategory>> getSubcategories(@PathVariable int categoryId,
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    public ResponseEntity<Subcategory> getSubcategory(@PathVariable int categoryId,
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    public ResponseEntity<Integer> addSubcategory(@PathVariable int categoryId,
                                                  @RequestParam String subcategoryName,
                                                  @RequestParam String subcategoryDescription,
                                                  @RequestParam int subcategoryColourId,
                                                  @RequestBody User loggedInUser) {
        try {
            return ResponseEntity.ok(subcategoryRepository.addSubcategory(categoryId,
                    subcategoryName, subcategoryDescription, subcategoryColourId,
                    loggedInUser));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Updates an existing subcategory for a specific category.
     *
     * @param categoryId                            The ID of the category.
     * @param subcategoryId                         The ID of the subcategory to update.
     * @param updatedSubcategoryName                The updated name of the subcategory.
     * @param updatedSubcategoryDescription         The updated description of the subcategory.
     * @param updatedSubcategoryColour              The updated colour of the subcategory.
     * @param loggedInUser                          The logged-in user.
     */
    @PatchMapping("/subcategories/{subcategoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing subcategory")
    public ResponseEntity updateSubcategory(@PathVariable int categoryId,
                                            @PathVariable int subcategoryId,
                                            @RequestParam String updatedSubcategoryName,
                                            @RequestParam String updatedSubcategoryDescription,
                                            @RequestParam int updatedSubcategoryColour,
                                            @RequestBody User loggedInUser) {
        try {
            subcategoryRepository.updateSubcategory(categoryId,
                subcategoryId, updatedSubcategoryName, updatedSubcategoryDescription, updatedSubcategoryColour,
                loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Changing which category the subcategory is subordinate.
     *
     * @param categoryId        The updated categoryId.
     * @param subcategoryId     The ID of the subcategory.
     * @param loggedInUser      The logged-in User.
     */

    @PostMapping("/subcategories/{subcategoryId}/categoryOfSubcategory")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the category of a subcategory")
    public ResponseEntity updateCategoryOfSubcategory(@PathVariable int categoryId,
                                                      @PathVariable int subcategoryId,
                                                      @RequestBody User loggedInUser) {
        try {
            subcategoryRepository.updateCategoryOfSubcategory(categoryId, subcategoryId, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Changes the name of an existing subcategory for a specific category.
     *
     * @param categoryId                The ID of the category.
     * @param subcategoryId             The ID of the subcategory to update.
     * @param updatedSubcategoryName    The updated subcategory name.
     * @param loggedInUser              The logged-in user.
     */
    @PatchMapping("/subcategories/{subcategoryId}/subcategoryName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of an existing subcategory")
    public ResponseEntity updateSubcategoryName(@PathVariable int categoryId,
                                                @PathVariable int subcategoryId,
                                                @RequestParam String updatedSubcategoryName,
                                                @RequestBody User loggedInUser) {
        try {
            subcategoryRepository.updateSubcategoryName(categoryId,
                subcategoryId, updatedSubcategoryName,
                loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Changes the description of an existing subcategory for a specific category.
     *
     * @param categoryId                        The ID of the category.
     * @param subcategoryId                     The ID of the subcategory to update.
     * @param updatedSubcategoryDescription     The updated subcategory description.
     * @param loggedInUser                      The logged-in user.
     */
    @PatchMapping("/subcategories/{subcategoryId}/subcategoryDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of an existing subcategory")
    public ResponseEntity updateSubcategoryDescription(@PathVariable int categoryId,
                                                       @PathVariable int subcategoryId,
                                                       @RequestParam String updatedSubcategoryDescription,
                                                       @RequestBody User loggedInUser) {
        try {
            subcategoryRepository.updateSubcategoryDescription(categoryId,
                subcategoryId, updatedSubcategoryDescription,
                loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Changes the colour of an existing subcategory for a specific category.
     *
     * @param categoryId                    The ID of the category.
     * @param subcategoryId                 The ID of the subcategory to update.
     * @param updatedSubcategoryColour      The updated subcategory colour ID.
     * @param loggedInUser                  The logged-in user.
     */
    @PatchMapping("/subcategories/{subcategoryId}/subcategoryColour")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the colour of an existing subcategory")
    public ResponseEntity updateSubcategoryColour(@PathVariable int categoryId,
                                                  @PathVariable int subcategoryId,
                                                  @RequestParam int updatedSubcategoryColour,
                                                  @RequestBody User loggedInUser) {
        try {
            subcategoryRepository.updateSubcategoryColour(categoryId,
                subcategoryId, updatedSubcategoryColour,
                loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    public ResponseEntity deleteSubcategory(@PathVariable int categoryId,
                                  @PathVariable int subcategoryId,
                                  @RequestBody User loggedInUser) {
        try {
            subcategoryRepository.deleteSubcategory(categoryId, subcategoryId, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
