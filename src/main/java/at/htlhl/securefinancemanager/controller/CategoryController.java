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
 * @version 1.6
 * @since 25.07.2023 (version 1.6)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview")
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
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all categories")
    public ResponseEntity<List<Category>> getCategories(@RequestParam int loggedInUserId,
                                                        @RequestParam String loggedInUsername,
                                                        @RequestParam String loggedInPassword,
                                                        @RequestParam String loggedInEMailAddress,
                                                        @RequestParam String loggedInFirstName,
                                                        @RequestParam String loggedInLastName) {
        try {
            return ResponseEntity.ok(categoryRepository.getCategories(new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName)));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    @GetMapping("/categories/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one category")
    public ResponseEntity<Category> getCategory(@PathVariable int categoryId,
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a new category")
    public ResponseEntity<Integer> addCategory(@RequestParam String categoryName,
                                               @RequestParam String categoryDescription,
                                               @RequestParam int categoryColourId,
                                               @RequestBody User loggedInUser) {
        try {
            return ResponseEntity.ok(categoryRepository.addCategory(categoryName, categoryDescription, categoryColourId,
                    loggedInUser));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    @PatchMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing category")
    public ResponseEntity updateCategory(@RequestParam int categoryId,
                                         @RequestParam String updatedCategoryName,
                                         @RequestParam String updatedCategoryDescription,
                                         @RequestParam int updatedCategoryColourId,
                                         @RequestBody User loggedInUser) {
        try {
            categoryRepository.updateCategory(new Category(categoryId, updatedCategoryName, updatedCategoryDescription, updatedCategoryColourId, loggedInUser.getUserId()),
                    loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Changes the name of an existing category for the logged-in user.
     *
     * @param categoryId          The ID of the category to update.
     * @param updatedCategoryName The updated category name.
     * @param loggedInUser        The logged-in user.
     */
    @PatchMapping("/categories/categoryName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of an existing category")
    public ResponseEntity updateCategoryName(@RequestParam int categoryId,
                                             @RequestParam String updatedCategoryName,
                                             @RequestBody User loggedInUser) {
        try {
            categoryRepository.updateCategoryName(categoryId, updatedCategoryName, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Changes the description of an existing category for the logged-in user.
     *
     * @param categoryId                 The ID of the category to update.
     * @param updatedCategoryDescription The updated category description.
     * @param loggedInUser               The logged-in user.
     */
    @PatchMapping("/categories/categoryDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of an existing category")
    public ResponseEntity updateCategoryDescription(@RequestParam int categoryId,
                                                    @RequestParam String updatedCategoryDescription,
                                                    @RequestBody User loggedInUser) {
        try {
            categoryRepository.updateCategoryDescription(categoryId, updatedCategoryDescription, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Changes the color of an existing category for the logged-in user.
     *
     * @param categoryId              The ID of the category to update.
     * @param updatedCategoryColourId The updated category color ID.
     * @param loggedInUser            The logged-in user.
     */
    @PatchMapping("/categories/categoryColourId")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the colour of an existing category")
    public ResponseEntity updateCategoryColourId(@RequestParam int categoryId,
                                                 @RequestParam int updatedCategoryColourId,
                                                 @RequestBody User loggedInUser) {
        try {
            categoryRepository.updateCategoryColourId(categoryId, updatedCategoryColourId, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Deletes a category for the logged-in user.
     *
     * @param categoryId   The ID of the category to delete.
     * @param loggedInUser The logged-in user.
     */
    @DeleteMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete a category")
    public ResponseEntity deleteCategory(@RequestParam int categoryId,
                                         @RequestBody User loggedInUser) {
        try {
            categoryRepository.deleteCategory(categoryId, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}