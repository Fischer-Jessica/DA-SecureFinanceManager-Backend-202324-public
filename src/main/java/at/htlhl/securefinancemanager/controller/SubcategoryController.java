package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.api.ApiSubcategory;
import at.htlhl.securefinancemanager.model.database.DatabaseSubcategory;
import at.htlhl.securefinancemanager.repository.SubcategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static at.htlhl.securefinancemanager.SecureFinanceManagerApplication.userSingleton;

/**
 * The SubcategoryController class handles HTTP requests related to subcategories.
 *
 * <p>
 * This controller is responsible for handling CRUD operations (Create, Read, Update, Delete) on Subcategory entities.
 * It interacts with the SubcategoryRepository to access and manipulate the Subcategory entities
 * in the 'subcategories' table of the 'secure_finance_manager' PostgreSQL database.
 * </p>
 *
 * <p>
 * This class is annotated with the {@link RestController} annotation, indicating that it is a controller
 * that handles RESTful HTTP requests. The {@link CrossOrigin} annotation allows cross-origin requests to this controller,
 * enabling it to be accessed from different domains. The {@link RequestMapping} annotation specifies the base path
 * for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * The SubcategoryController class works in conjunction with the SubcategoryRepository and other related classes
 * to enable efficient management of subcategories in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 2.8
 * @since 14.11.2023 (version 2.8)
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
     * @param categoryId    The ID of the category.
     * @param userDetails   The details of the logged-in user.
     * @return A list of subcategories for the specified category.
     */
    @GetMapping(value = "/subcategories", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns all subcategories of one category")
    public ResponseEntity<Object> getSubcategories(@PathVariable int categoryId,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (categoryId <= 0) {
                throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(subcategoryRepository.getSubcategories(categoryId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Returns a specific subcategory for a specific category.
     *
     * @param categoryId    The ID of the category.
     * @param subcategoryId The ID of the subcategory.
     * @param userDetails   The details of the logged-in user.
     * @return The requested subcategory.
     */
    @GetMapping(value = "/subcategories/{subcategoryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns one subcategory")
    public ResponseEntity<Object> getSubcategory(@PathVariable int categoryId,
                                                 @PathVariable int subcategoryId,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (categoryId <= 0) {
                throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
            } else if (subcategoryId <= 0) {
                throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(subcategoryRepository.getSubcategory(categoryId, subcategoryId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Adds a new subcategory to a specific category.
     *
     * @param categoryId             The ID of the category, in which the subcategory will be created.
     * @param newApiSubcategory         The new subcategory to be added.
     * @param userDetails            The details of the logged-in user.
     * @return The ID of the newly created subcategory.
     */
    @PostMapping(value = "/subcategories", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "add a new subcategory")
    public ResponseEntity<Object> addSubcategory(@PathVariable int categoryId,
                                                 @RequestBody ApiSubcategory newApiSubcategory,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (categoryId <= 0) {
                throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
            } else if (newApiSubcategory.getSubcategoryColourId() <= 0) {
                throw new MissingRequiredParameter("subcategoryColourId cannot be less than or equal to 0");
            } else if (newApiSubcategory.getSubcategoryName() == null || newApiSubcategory.getSubcategoryName().isBlank()) {
                throw new MissingRequiredParameter("subcategoryName is required");
            }
            return ResponseEntity.ok(subcategoryRepository.addSubcategory(new DatabaseSubcategory(categoryId, newApiSubcategory, userSingleton.getUserId(userDetails.getUsername()))));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates an existing subcategory for a specific category.
     *
     * @param categoryId            The ID of the category.
     * @param subcategoryId         The ID of the subcategory to update.
     * @param updatedApiSubcategory    The updated subcategory data.
     * @param userDetails           The details of the logged-in user.
     */
    @PatchMapping(value = "/subcategories/{subcategoryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "change an existing subcategory")
    public ResponseEntity<Object> updateSubcategory(@PathVariable int categoryId,
                                                    @PathVariable int subcategoryId,
                                                    @RequestBody ApiSubcategory updatedApiSubcategory,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (categoryId <= 0) {
                throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
            } else if (subcategoryId <= 0) {
                throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
            } else if (updatedApiSubcategory.getSubcategoryColourId() < 0) {
                throw new MissingRequiredParameter("subcategoryColourId cannot be less than 0, use 0 for no colour change");
            }
            return ResponseEntity.ok(subcategoryRepository.updateSubcategory(new DatabaseSubcategory(subcategoryId, categoryId, updatedApiSubcategory, userSingleton.getUserId(userDetails.getUsername())), userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes a subcategory for a specific category.
     *
     * @param categoryId       The ID of the category.
     * @param subcategoryId    The ID of the subcategory to delete.
     * @param userDetails      The details of the logged-in user.
     */
    @DeleteMapping(value = "/subcategories/{subcategoryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "delete a subcategory")
    public ResponseEntity<Object> deleteSubcategory(@PathVariable int categoryId,
                                                    @PathVariable int subcategoryId,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (categoryId <= 0) {
                throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
            } else if (subcategoryId <= 0) {
                throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
            }
            return ResponseEntity.status(HttpStatus.OK).body(subcategoryRepository.deleteSubcategory(categoryId, subcategoryId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }
}