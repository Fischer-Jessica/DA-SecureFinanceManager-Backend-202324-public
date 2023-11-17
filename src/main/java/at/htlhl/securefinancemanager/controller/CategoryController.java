package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.api.ApiCategory;
import at.htlhl.securefinancemanager.model.database.DatabaseCategory;
import at.htlhl.securefinancemanager.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static at.htlhl.securefinancemanager.SecureFinanceManagerApplication.userSingleton;

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
 * classes to enable efficient management of categories in the secure finance manager system.
 * </p>
 *
 * @author Fischer
 * @version 3.4
 * @since 17.11.2023 (version 3.4)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager")
public class CategoryController {
    /** The CategoryRepository instance for accessing category data. */
    @Autowired
    CategoryRepository categoryRepository;

    /**
     * Returns a list of all categories of the logged-in user.
     *
     * @param userDetails The UserDetails object containing information about the logged-in user.
     * @return A list of categories.
     */
    @GetMapping(value = "/categories", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns a list of all categories of the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned all categories of the authenticated user",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = DatabaseCategory.class)) }),
            @ApiResponse(responseCode = "404", description = "no categories found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> getCategories(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(categoryRepository.getCategories(userDetails.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Returns a specific category for the logged-in user.
     *
     * @param categoryId   The ID of the category to retrieve.
     * @param userDetails  The UserDetails object containing information about the logged-in user.
     * @return The requested category.
     */
    @GetMapping(value = "/categories/{categoryId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns the requested category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned the requested category",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseCategory.class)) }),
            @ApiResponse(responseCode = "400", description = "the categoryId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the requested category does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> getCategory(@PathVariable int categoryId,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (categoryId <= 0) {
                throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(categoryRepository.getCategory(categoryId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Creates new categories for the logged-in user.
     *
     * @param newApiCategories  The Categories representing the new categories.
     * @param userDetails       The UserDetails object containing information about the logged-in user.
     * @return A List of the newly created categories.
     */
    @PostMapping(value =  "/categories", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "creates new categories for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given categories for the authenticated user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseCategory.class)) }),
            @ApiResponse(responseCode = "400", description = "A categoryName is empty or a categoryColourId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> addCategories(@RequestBody List<ApiCategory> newApiCategories,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<DatabaseCategory> createdCategories = new ArrayList<>();
            for (ApiCategory newApiCategory : newApiCategories) {
                if (newApiCategory.getCategoryName() == null || newApiCategory.getCategoryName().isBlank()) {
                    throw new MissingRequiredParameter("categoryName cannot be empty");
                } else if (newApiCategory.getCategoryColourId() <= 0) {
                    throw new MissingRequiredParameter("categoryColourId cannot be less than or equal to 0");
                }
                createdCategories.add(categoryRepository.addCategory(new DatabaseCategory(newApiCategory, userSingleton.getUserId(userDetails.getUsername()))));
            }
            return ResponseEntity.ok(createdCategories);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates existing categories for the logged-in user.
     *
     * @param categoryIds               The IDs of the categories to update.
     * @param updatedApiCategories      The Categories representing the updated categories.
     * @param userDetails               The UserDetails object containing information about the logged-in user.
     * @return A List of the updated categories.
     */
    @PatchMapping(value = "/categories/{categoryIds}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "updates existing categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully updated the given categories",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseCategory.class)) }),
            @ApiResponse(responseCode = "400", description = "the number of given categoryIds and updatedApiCategories are not equal or a categoryId is less than or equal to 0 or a categoryColourId is less than 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "a given category does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> updateCategories(@PathVariable List<Integer> categoryIds,
                                                   @RequestBody List<ApiCategory> updatedApiCategories,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (categoryIds.size() != updatedApiCategories.size()) {
                throw new MissingRequiredParameter("the number of categoryIds and updatedApiCategories must be equal");
            }
            List<DatabaseCategory> updatedCategories = new ArrayList<>();
            for(int i = 0; i < categoryIds.size(); i++) {
                if (categoryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
                } else if (updatedApiCategories.get(i).getCategoryColourId() < 0) {
                    throw new MissingRequiredParameter("categoryColourId cannot be less than 0");
                }
                updatedCategories.add(categoryRepository.updateCategory(new DatabaseCategory(categoryIds.get(i), updatedApiCategories.get(i), userSingleton.getUserId(userDetails.getUsername())), userDetails.getUsername()));
            }
            return ResponseEntity.ok(updatedCategories);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes categories for the logged-in user.
     *
     * @param categoryIds   The IDs of the categories to delete.
     * @param userDetails   The UserDetails object containing information about the logged-in user.
     * @return An Integer List representing the number of deleted rows.
     */
    @DeleteMapping(value = "/categories/{categoryIds}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted the given categories",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class)) }),
            @ApiResponse(responseCode = "400", description = "a categoryId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "a given category does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> deleteCategories(@PathVariable List<Integer> categoryIds,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<Integer> deletedRows = new ArrayList<>();
            for (int categoryId : categoryIds) {
                if (categoryId <= 0) {
                    throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
                }
                deletedRows.add(categoryRepository.deleteCategory(categoryId, userDetails.getUsername()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(deletedRows);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }
}