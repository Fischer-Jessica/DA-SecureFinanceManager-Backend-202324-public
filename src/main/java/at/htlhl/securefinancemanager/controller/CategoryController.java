package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.api.ApiCategory;
import at.htlhl.securefinancemanager.model.database.DatabaseCategory;
import at.htlhl.securefinancemanager.repository.CategoryRepository;
import at.htlhl.securefinancemanager.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * The CategoryController class handles HTTP requests related to category management.
 * It provides endpoints for retrieving, adding, updating, and deleting categories for the logged-in user.
 *
 * <p>
 * This controller is responsible for handling CRUD operations (Create, Read, Update, Delete)
 * on Category entities. It interacts with the CategoryRepository to access and manipulate
 * the Category entities in the 'categories' table of the 'secure_finance_manager' PostgreSQL database.
 * </p>
 *
 * <p>
 * This class is annotated with the {@link RestController} annotation, which indicates that
 * it is a controller that handles RESTful HTTP requests. The {@link CrossOrigin} annotation allows
 * cross-origin requests to this controller, enabling it to be accessed from different domains.
 * The {@link RequestMapping} annotation specifies the base path for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * The CategoryController class works in conjunction with the CategoryRepository and other related
 * classes to enable efficient management of categories in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 2.5
 * @since 12.11.2023 (version 2.5)
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
     * @param userDetails The UserDetails object containing information about the logged-in user.
     * @return A list of categories.
     */
    @GetMapping(value = "/categories", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns all categories")
    public ResponseEntity<Object> getCategories(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(categoryRepository.getCategories(userDetails.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Returns a specific category for the logged-in user.
     *
     * @param categoryId   The ID of the category to retrieve.
     * @param userDetails  The UserDetails object containing information about the logged-in user.
     * @return The requested category.
     */
    @GetMapping(value = "/categories/{categoryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns one category")
    public ResponseEntity<Object> getCategory(@PathVariable int categoryId,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(categoryRepository.getCategory(categoryId, userDetails.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds a new category for the logged-in user.
     *
     * @param newApiCategory The Category object representing the new category.
     * @param userDetails    The UserDetails object containing information about the logged-in user.
     * @return The ID of the newly created category.
     */
    @PostMapping(value =  "/categories", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "add a new category")
    public ResponseEntity<Object> addCategory(@RequestBody ApiCategory newApiCategory,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (newApiCategory.getCategoryName() == null || newApiCategory.getCategoryName().isBlank()) {
                throw new MissingRequiredParameter("Category name cannot be empty");
            } else if (newApiCategory.getCategoryColourId() <= 0) {
                throw new MissingRequiredParameter("Category colour ID cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(
                    categoryRepository.addCategory(new DatabaseCategory(newApiCategory, UserRepository.getUserId(userDetails.getUsername()))));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates an existing category for the logged-in user.
     *
     * @param categoryId      The ID of the category to update.
     * @param updatedApiCategory The Category object representing the updated category.
     * @param userDetails     The UserDetails object containing information about the logged-in user.
     * @return The updated Category object.
     */
    @PatchMapping(value = "/categories/{categoryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "change an existing category")
    public ResponseEntity<Object> updateCategory(@PathVariable int categoryId,
                                                 @RequestBody ApiCategory updatedApiCategory,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (updatedApiCategory.getCategoryColourId() < 0) {
                throw new MissingRequiredParameter("Category colour ID cannot be less than 0");
            }

            return ResponseEntity.ok(
                    categoryRepository.updateCategory (new DatabaseCategory(categoryId, updatedApiCategory, UserRepository.getUserId(userDetails.getUsername())), userDetails.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes a category for the logged-in user.
     *
     * @param categoryId  The ID of the category to delete.
     * @param userDetails The UserDetails object containing information about the logged-in user.
     * @return An HTTP response indicating the result of the deletion.
     */
    @DeleteMapping(value = "/categories/{categoryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "delete a category")
    public ResponseEntity<Object> deleteCategory(@PathVariable int categoryId,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            categoryRepository.deleteCategory(categoryId, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }
}
