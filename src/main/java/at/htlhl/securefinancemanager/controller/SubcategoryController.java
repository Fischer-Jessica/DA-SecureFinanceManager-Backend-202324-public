package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.api.ApiSubcategory;
import at.htlhl.securefinancemanager.model.database.DatabaseSubcategory;
import at.htlhl.securefinancemanager.model.response.ResponseSubcategory;
import at.htlhl.securefinancemanager.repository.SubcategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
 * to enable efficient management of subcategories in the secure finance manager system.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 3.8
 * @since 24.02.2024 (version 3.8)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager/categories")
public class SubcategoryController {
    /**
     * The SubcategoryRepository instance for accessing subcategory data.
     */
    @Autowired
    SubcategoryRepository subcategoryRepository;

    /**
     * Returns a list of all subcategories from a specific category.
     *
     * @param categoryId  The ID of the category.
     * @param userDetails The details of the logged-in user.
     * @return A list of subcategories for the specified category.
     */
    @GetMapping(value = "/{categoryId}/subcategories", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns all subcategories of one category", description = "Returns a list of all subcategories with the specified categoryId from the URL for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned all subcategories of the given category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseSubcategory.class))}),
            @ApiResponse(responseCode = "400", description = "the categoryId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "the requested category does not exist or is not found for the authenticated user or there are no subcategories in this category",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> getSubcategoriesV1(@Parameter(description = "The categoryId added to the URL to retrieve the subordinated subcategories.") @PathVariable int categoryId,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (categoryId <= 0) {
                throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(subcategoryRepository.getSubcategories(categoryId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Returns a specific subcategory from a specific category.
     *
     * @param categoryId    The ID of the category.
     * @param subcategoryId The ID of the subcategory.
     * @param userDetails   The details of the logged-in user.
     * @return The requested subcategory.
     */
    @GetMapping(value = "/{categoryId}/subcategories/{subcategoryId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns one subcategory of one category", description = "Returns the subcategory with the specified subcategoryId from the URL and the specified categoryId from the URL for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned the requested subcategory from the given category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseSubcategory.class))}),
            @ApiResponse(responseCode = "400", description = "the categoryId or the subcategoryId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "the requested subcategory of the given category does not exist or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> getSubcategoryV1(@Parameter(description = "The categoryId added to the URL to retrieve the associated subcategory.") @PathVariable int categoryId,
                                                   @Parameter(description = "The subcategoryId added to the URL to retrieve the subordinated subcategory.") @PathVariable int subcategoryId,
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
     * @param categoryId        The ID of the category, in which the subcategory will be created.
     * @param newApiSubcategory The new subcategory to be added.
     * @param userDetails       The details of the logged-in user.
     * @return The newly created subcategory.
     */
    @PostMapping(value = "/{categoryId}/subcategories", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "creating a new subcategory inside a given category",
            description = "Creates a new subcategory within the specified categoryId from the URL for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given subcategory within the given category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseSubcategory.class))}),
            @ApiResponse(responseCode = "400", description = "the given categoryId or the subcategoryColourId is less than or equal to 0 or the categoryName is empty",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> addSubcategoryV1(@Parameter(description = "The categoryId added to the URL to create the new subcategory within it.") @PathVariable int categoryId,
                                                   @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                           description = "The new subcategory-Object.",
                                                           required = true,
                                                           content = @Content(
                                                                   mediaType = "application/json",
                                                                   schema = @Schema(implementation = ApiSubcategory.class)
                                                           )
                                                   ) @RequestBody ApiSubcategory newApiSubcategory,
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
     * Adds a new subcategories to a specific categories.
     *
     * @param categoryIds         The IDs of the categories, in which the subcategories will be created.
     * @param newApiSubcategories The new subcategories to be added.
     * @param userDetails         The details of the logged-in user.
     * @return A List of the newly created subcategories.
     */
    @PostMapping(value = "/{categoryIds}/subcategories", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "creating new subcategories inside given categories",
            description = "Creates new subcategories within the specified categoryIds from the URL for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given subcategories within the given categories",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseSubcategory.class))}),
            @ApiResponse(responseCode = "400", description = "the number of given categoryIds and newApiSubcategories are not equal or a given categoryId or a subcategoryColourId is less than or equal to 0 or a categoryName is empty",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> addSubcategoriesV2(@Parameter(description = "The categoryId added to the URL to create the new entries within them. The categoryId and the newApiSubcategories need to be added in the same order.") @PathVariable List<Integer> categoryIds,
                                                     @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                             description = "List of new subcategories. As it is a List, the objects need to be enclosed in [].",
                                                             required = true,
                                                             content = @Content(
                                                                     mediaType = "application/json",
                                                                     schema = @Schema(implementation = ApiSubcategory.class)
                                                             )
                                                     ) @RequestBody List<ApiSubcategory> newApiSubcategories,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (categoryIds.size() != newApiSubcategories.size()) {
                throw new MissingRequiredParameter("the number of categoryIds and newApiSubcategories must be equal");
            }
            List<DatabaseSubcategory> createdSubcategories = new ArrayList<>();
            for (int i = 0; i < categoryIds.size(); i++) {
                if (categoryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
                } else if (newApiSubcategories.get(i).getSubcategoryColourId() <= 0) {
                    throw new MissingRequiredParameter("subcategoryColourId cannot be less than or equal to 0");
                } else if (newApiSubcategories.get(i).getSubcategoryName() == null || newApiSubcategories.get(i).getSubcategoryName().isBlank()) {
                    throw new MissingRequiredParameter("subcategoryName is required");
                }
                createdSubcategories.add(subcategoryRepository.addSubcategory(new DatabaseSubcategory(categoryIds.get(i), newApiSubcategories.get(i), userSingleton.getUserId(userDetails.getUsername()))));
            }
            return ResponseEntity.ok(createdSubcategories);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }


    /**
     * Adds a new subcategories to a specific categories.
     *
     * @param categoryIds         The IDs of the categories, in which the subcategories will be created.
     * @param newApiSubcategories The new subcategories to be added.
     * @param userDetails         The details of the logged-in user.
     * @return A List of the newly created subcategories.
     */
    @PostMapping(value = "/{categoryIds}/subcategories", headers = "API-Version=3")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "creating new subcategories inside given categories",
            description = "Creates new subcategories within the specified categoryIds from the URL for the authenticated user. It requires a Basic-Auth-Header. This version for mobile applications also requires a List of mobileSubcategoryIds added to the URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given subcategories within the given categories",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseSubcategory.class))}),
            @ApiResponse(responseCode = "400", description = "the number of given categoryIds, mobileSubcategoryIds and newApiSubcategories are not equal or a given categoryId or a mobileSubcategoryId or a subcategoryColourId is less than or equal to 0 or a categoryName is empty",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> addSubcategoriesV3(@Parameter(description = "The categoryIds added to the URL to create the new subcategories within them. The categoryId and the newApiSubcategories need to be added in the same order.") @PathVariable List<Integer> categoryIds,
                                                     @Parameter(description = "List of mobileSubcategoryIds from mobile applications to be added to the URL. The mobileSubcategoryIds and newApiSubcategories must be in the same order.") @RequestParam List<Integer> mobileSubcategoryIds,
                                                     @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                             description = "List of new subcategories. As it is a List, the objects need to be enclosed in [].",
                                                             required = true,
                                                             content = @Content(
                                                                     mediaType = "application/json",
                                                                     schema = @Schema(implementation = ApiSubcategory.class)
                                                             )
                                                     ) @RequestBody List<ApiSubcategory> newApiSubcategories,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (categoryIds.size() != newApiSubcategories.size() || categoryIds.size() != mobileSubcategoryIds.size()) {
                throw new MissingRequiredParameter("the number of categoryIds, mobileSubcategoryIds and newApiSubcategories must be equal");
            }
            List<ResponseSubcategory> createdSubcategories = new ArrayList<>();
            for (int i = 0; i < categoryIds.size(); i++) {
                if (categoryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
                } else if (newApiSubcategories.get(i).getSubcategoryColourId() <= 0) {
                    throw new MissingRequiredParameter("subcategoryColourId cannot be less than or equal to 0");
                } else if (newApiSubcategories.get(i).getSubcategoryName() == null || newApiSubcategories.get(i).getSubcategoryName().isBlank()) {
                    throw new MissingRequiredParameter("subcategoryName is required");
                } else if (mobileSubcategoryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("mobileSubcategoryId cannot be less than or equal to 0");
                }
                createdSubcategories.add(new ResponseSubcategory(subcategoryRepository.addSubcategory(new DatabaseSubcategory(categoryIds.get(i), newApiSubcategories.get(i), userSingleton.getUserId(userDetails.getUsername()))), mobileSubcategoryIds.get(i)));
            }
            return ResponseEntity.ok(createdSubcategories);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates an existing subcategory from a specific category.
     *
     * @param categoryId            The ID of the category.
     * @param subcategoryId         The ID of the subcategory to update.
     * @param updatedApiSubcategory The updated subcategory data.
     * @param userDetails           The details of the logged-in user.
     * @return The updated subcategory.
     */
    @PatchMapping(value = "/{categoryId}/subcategories/{subcategoryId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "updates an existing subcategory from a given category",
            description = "Updates an existing subcategory, whose id is given in the URL as well as its superior categoryId, for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully updated the given subcategory from the given category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseSubcategory.class))}),
            @ApiResponse(responseCode = "400", description = "the categoryId or the subcategoryId is less than or equal to 0 or the categoryColourId is less than 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "the given subcategory with the given categoryId does not exist or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> updateSubcategoryV1(@Parameter(description = "The id of the superior category, added to the URL") @PathVariable int categoryId,
                                                      @Parameter(description = "The id of the subcategory to be updated, added to the URL.") @PathVariable int subcategoryId,
                                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                              description = "The updated subcategory-Object.",
                                                              required = true,
                                                              content = @Content(
                                                                      mediaType = "application/json",
                                                                      schema = @Schema(implementation = ApiSubcategory.class)
                                                              )
                                                      ) @RequestBody ApiSubcategory updatedApiSubcategory,
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
     * Updates existing subcategories from a specific categories.
     *
     * @param categoryIds             The IDs of the categories.
     * @param subcategoryIds          The IDs of the subcategories to update.
     * @param updatedApiSubcategories The updated subcategories.
     * @param userDetails             The details of the logged-in user.
     * @return A List of the updated subcategories.
     */
    @PatchMapping(value = "/{categoryIds}/subcategories/{subcategoryIds}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "updates existing subcategories from a given categories",
            description = "Updates existing subcategories, whose ids are given in the URL as well as their superior categoryIds, for the authenticated user. It requires a Basic-Auth-Header.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully updated the given subcategories from the given categories",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseSubcategory.class))}),
            @ApiResponse(responseCode = "400", description = "the number of given categoryIds, subcategoryIds and updatedApiSubcategories are not equal or a categoryId or a subcategoryId is less than or equal to 0 or a categoryColourId is less than 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "a given subcategory with the given categoryId does not exist or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> updateSubcategoriesV2(@Parameter(description = "List of the superior categoryIds. They need to be added to the URL in the same order as the updatedApiSubcategories") @PathVariable List<Integer> categoryIds,
                                                        @Parameter(description = "List of subcategoryIds to be updated. They need to be added to the URL in the same order as the updatedApiSubcategories.") @PathVariable List<Integer> subcategoryIds,
                                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                description = "List of the updated subcategories. As it is a List, the objects need to be enclosed in [].",
                                                                required = true,
                                                                content = @Content(
                                                                        mediaType = "application/json",
                                                                        schema = @Schema(implementation = ApiSubcategory.class)
                                                                )
                                                        ) @RequestBody List<ApiSubcategory> updatedApiSubcategories,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (categoryIds.size() != subcategoryIds.size() || categoryIds.size() != updatedApiSubcategories.size()) {
                throw new MissingRequiredParameter("the number of categoryIds, subcategoryIds and updatedApiSubcategories must be equal");
            }
            List<DatabaseSubcategory> updatedSubcategories = new ArrayList<>();
            for (int i = 0; i < categoryIds.size(); i++) {
                if (categoryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
                } else if (subcategoryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
                } else if (updatedApiSubcategories.get(i).getSubcategoryColourId() < 0) {
                    throw new MissingRequiredParameter("subcategoryColourId cannot be less than 0, use 0 for no colour change");
                }
                updatedSubcategories.add(subcategoryRepository.updateSubcategory(new DatabaseSubcategory(subcategoryIds.get(i), categoryIds.get(i), updatedApiSubcategories.get(i), userSingleton.getUserId(userDetails.getUsername())), userDetails.getUsername()));
            }
            return ResponseEntity.ok(updatedSubcategories);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes a subcategory from a specific category.
     *
     * @param categoryId    The ID of the category.
     * @param subcategoryId The ID of the subcategory to delete.
     * @param userDetails   The details of the logged-in user.
     * @return An Integer representing the number of deleted rows.
     */
    @DeleteMapping(value = "/{categoryId}/subcategories/{subcategoryId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes a subcategory from a given category",
            description = "Deletes a subcategory, whose id is given in the URL as well as its superior categoryId, for the authenticated user. It requires a Basic-Auth-Header.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted the given subcategory from the given category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "400", description = "the categoryId or the subcategoryId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "the given subcategory with the given categoryId does not exist or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> deleteSubcategoryV1(@Parameter(description = "The id of the superior category, added to the URL.") @PathVariable int categoryId,
                                                      @Parameter(description = "The id of the subcategory to be deleted, added to the URL.") @PathVariable int subcategoryId,
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

    /**
     * Deletes subcategories from specific categories.
     *
     * @param categoryIds    The IDs of the categories.
     * @param subcategoryIds The IDs of the subcategories to delete.
     * @param userDetails    The details of the logged-in user.
     * @return An Integer List representing the number of deleted rows.
     */
    @DeleteMapping(value = "/{categoryIds}/subcategories/{subcategoryIds}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes subcategories from given categories",
            description = "Deletes subcategories, whose ids are given in the URL as well as their superior categoryIds, for the authenticated user. It requires a Basic-Auth-Header.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted the given subcategories from the given categories",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "400", description = "the number of given categoryIds and subcategoryIds are not equal or a categoryId or a subcategoryId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "a given subcategory with the given categoryId does not exist or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> deleteSubcategoriesV2(@Parameter(description = "List of superior categoryIds. They need to be added to the URL in the same order as the subcategoryIds.") @PathVariable List<Integer> categoryIds,
                                                        @Parameter(description = "List of subcategoryIds to be deleted. They need to be added to the URL.") @PathVariable List<Integer> subcategoryIds,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (categoryIds.size() != subcategoryIds.size()) {
                throw new MissingRequiredParameter("the number of categoryIds and subcategoryIds must be equal");
            }
            List<Integer> deletedRows = new ArrayList<>();
            for (int i = 0; i < categoryIds.size(); i++) {
                if (categoryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("categoryId cannot be less than or equal to 0");
                } else if (subcategoryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
                }
                deletedRows.add(subcategoryRepository.deleteSubcategory(categoryIds.get(i), subcategoryIds.get(i), userDetails.getUsername()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(deletedRows);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }
}