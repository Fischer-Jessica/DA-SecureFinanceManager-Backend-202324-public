package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.Category;
import at.htlhl.securefinancemanager.model.User;
import at.htlhl.securefinancemanager.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * The CategoryController class handles HTTP requests related to category management.
 * It provides endpoints for retrieving, adding, updating, and deleting categories for the logged-in user.
 *
 * <p>
 * This controller is responsible for handling CRUD operations (Create, Read, Update, Delete) on Category entities.
 * It interacts with the CategoryRepository to access and manipulate the Category entities in the 'categories' table of the 'financial_overview' PostgreSQL database.
 * </p>
 *
 * <p>
 * This class is annotated with the {@link RestController} annotation, which indicates that it is a controller that handles RESTful HTTP requests.
 * The {@link CrossOrigin} annotation allows cross-origin requests to this controller, enabling it to be accessed from different domains.
 * The {@link RequestMapping} annotation specifies the base path for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * The CategoryController class works in conjunction with the CategoryRepository and other related classes to enable efficient management of categories in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 2.2
 * @since 06.10.2023 (version 2.2)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager")
public class CategoryController {
    /** The CategoryRepository instance for accessing category data. */
    @Autowired
    CategoryRepository categoryRepository;

    /**
     * Returns a list of all categories for the logged-in user.
     *
     * @param loggedInUserId       The ID of the logged-in user.
     * @param loggedInUsername     The username of the logged-in user.
     * @param loggedInPassword     The password of the logged-in user.
     * @param loggedInEMailAddress The email address of the logged-in user.
     * @param loggedInFirstName    The first name of the logged-in user.
     * @param loggedInLastName     The last name of the logged-in user.
     * @return A list of categories.
     */
    @GetMapping(value = "/categories", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all categories")
    public ResponseEntity<Object> getCategories(@RequestParam int loggedInUserId,
                                                 @RequestParam String loggedInUsername,
                                                 @RequestParam String loggedInPassword,
                                                 @RequestParam String loggedInEMailAddress,
                                                 @RequestParam String loggedInFirstName,
                                                 @RequestParam String loggedInLastName) {
        try {
            return ResponseEntity.ok(categoryRepository.getCategories(new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName)));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Returns a specific category for the logged-in user.
     *
     * @param categoryId           The ID of the category to retrieve.
     * @param loggedInUserId       The ID of the logged-in user.
     * @param loggedInUsername     The username of the logged-in user.
     * @param loggedInPassword     The password of the logged-in user.
     * @param loggedInEMailAddress The email address of the logged-in user.
     * @param loggedInFirstName    The first name of the logged-in user.
     * @param loggedInLastName     The last name of the logged-in user.
     * @return The requested category.
     */
    @GetMapping(value = "/categories/{categoryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one category")
    public ResponseEntity<Object> getCategory(@PathVariable int categoryId,
                                                @RequestParam int loggedInUserId,
                                                @RequestParam String loggedInUsername,
                                                @RequestParam String loggedInPassword,
                                                @RequestParam String loggedInEMailAddress,
                                                @RequestParam String loggedInFirstName,
                                                @RequestParam String loggedInLastName) {
        try {
            return ResponseEntity.ok(categoryRepository.getCategory(categoryId,
                    new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName)));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds a new category for the logged-in user.
     *
     * @param categoryName        The name of the new category.
     * @param categoryDescription The description of the new category.
     * @param categoryColourId    The ID of the color for the new category.
     * @param loggedInUser        The logged-in user.
     * @return The ID of the newly created category.
     */
    @PostMapping(value =  "/categories", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new category")
    public ResponseEntity<Object> addCategory(@RequestParam String categoryName,
                                               @RequestParam String categoryDescription,
                                               @RequestParam int categoryColourId,
                                               @RequestBody User loggedInUser) {
        try {
            return ResponseEntity.ok(categoryRepository.addCategory(categoryName, categoryDescription, categoryColourId,
                    loggedInUser));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates an existing category for the logged-in user.
     *
     * @param categoryId                    The ID of the category to update.
     * @param updatedCategoryName           The updated category name.
     * @param updatedCategoryDescription    The updated category description.
     * @param updatedCategoryColourId       The updated category color ID.
     * @param loggedInUser                  The logged-in user.
     */
    @PatchMapping(value = "/categories", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing category")
    public ResponseEntity<Object> updateCategory(@RequestParam int categoryId,
                                         @RequestParam(required = false) String updatedCategoryName,
                                         @RequestParam(required = false) String updatedCategoryDescription,
                                         @RequestParam(defaultValue = "-1", required = false) int updatedCategoryColourId,
                                         @RequestBody User loggedInUser) {
        try {
            return ResponseEntity.ok(categoryRepository.updateCategory(new Category(categoryId, updatedCategoryName, updatedCategoryDescription, updatedCategoryColourId, loggedInUser.getUserId()),
                    loggedInUser));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes a category for the logged-in user.
     *
     * @param categoryId   The ID of the category to delete.
     * @param loggedInUser The logged-in user.
     */
    @DeleteMapping(value = "/categories", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete a category")
    public ResponseEntity<Object> deleteCategory(@RequestParam int categoryId,
                                         @RequestBody User loggedInUser) {
        try {
            categoryRepository.deleteCategory(categoryId, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }
}
