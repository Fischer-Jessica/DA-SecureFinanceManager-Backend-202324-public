package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.api.ApiEntry;
import at.htlhl.securefinancemanager.model.database.DatabaseEntry;
import at.htlhl.securefinancemanager.model.response.ResponseEntry;
import at.htlhl.securefinancemanager.repository.EntryRepository;
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
 * The EntryController class handles HTTP requests related to entries.
 * It provides endpoints for retrieving, adding, updating, and deleting entries for a specific subcategory.
 *
 * <p>
 * This controller is responsible for handling CRUD operations (Create, Read, Update, Delete)
 * on Entry entities. It interacts with the EntryRepository to access and manipulate
 * the Entry entities in the 'entries' table of the 'secure_finance_manager' PostgreSQL database.
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
 * The EntryController class works in conjunction with the EntryRepository and other related classes to enable
 * efficient management of entries in the secure finance manager system.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 4.1
 * @since 24.02.2024 (version 4.1)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager/categories/subcategories")
public class EntryController {
    /**
     * The EntryRepository instance for accessing entry data.
     */
    @Autowired
    EntryRepository entryRepository;

    /**
     * Retrieves a list of all entries from a specific subcategory.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param userDetails   The UserDetails object representing the logged-in user.
     * @return A list of entries for the specified subcategory.
     */
    @GetMapping(value = "/{subcategoryId}/entries", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns all entries of one subcategory",
            description = "Returns a list of all entries with the specified subcategoryId from the URL for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned all entries of the given subcategory",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class))}),
            @ApiResponse(responseCode = "400", description = "the subcategoryId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "no entries found with the given subcategoryId for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> getEntriesV1(@Parameter(description = "The subcategoryId added to the URL to retrieve the subordinated entries.") @PathVariable int subcategoryId,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (subcategoryId <= 0) {
                throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(entryRepository.getEntries(subcategoryId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Retrieves a specific entry from a specific subcategory.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param entryId       The ID of the entry.
     * @param userDetails   The UserDetails object representing the logged-in user.
     * @return The requested entry.
     */
    @GetMapping(value = "/{subcategoryId}/entries/{entryId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns one entry of a subcategory",
            description = "Returns one entry of a subcategory specified by the entryId from the URL and the subcategoryId from the URL for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned the requested entry of the given subcategory",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class))}),
            @ApiResponse(responseCode = "400", description = "the subcategoryId or the entryId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "the requested entry with the given subcategoryId does not exist or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> getEntryV1(@Parameter(description = "The subcategoryId added to the URL to retrieve the associated entry.") @PathVariable int subcategoryId,
                                             @Parameter(description = "The entryId added to the URL to retrieve the subordinated entry.") @PathVariable int entryId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (subcategoryId <= 0) {
                throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
            } else if (entryId <= 0) {
                throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(entryRepository.getEntry(subcategoryId, entryId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Creates a new entry in a specific subcategory.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param newApiEntry   The new entry to be added.
     * @param userDetails   The UserDetails object representing the logged-in user.
     * @return The newly created entry.
     */
    @PostMapping(value = "/{subcategoryId}/entries", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "creates a new entry in a subcategory",
            description = "Creates a new entry within the specified subcategoryId from the URL for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given entry within the given subcategory",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class))}),
            @ApiResponse(responseCode = "400", description = "the categoryColourId is less than or equal to 0 or the entryAmount or the entryTimeOfTransaction is empty",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> addEntryV1(@Parameter(description = "The subcategoryId added to the URL to create the new entry within it.") @PathVariable int subcategoryId,
                                             @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                     description = "The new entry-Object.",
                                                     required = true,
                                                     content = @Content(
                                                             mediaType = "application/json",
                                                             schema = @Schema(implementation = ApiEntry.class)
                                                     )
                                             ) @RequestBody ApiEntry newApiEntry,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (subcategoryId <= 0) {
                throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
            } else if (newApiEntry.getEntryAmount() == null || newApiEntry.getEntryAmount().isBlank()) {
                throw new MissingRequiredParameter("entryAmount is required");
            } else if (newApiEntry.getEntryTimeOfTransaction() == null || newApiEntry.getEntryTimeOfTransaction().isBlank()) {
                throw new MissingRequiredParameter("entryTimeOfTransaction is required");
            }
            return ResponseEntity.ok(entryRepository.addEntry(new DatabaseEntry(subcategoryId, newApiEntry, userSingleton.getUserId(userDetails.getUsername()))));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Creates new entries in specific subcategories.
     *
     * @param subcategoryIds The IDs of the subcategories.
     * @param newApiEntries  The new entries to be added.
     * @param userDetails    The UserDetails object representing the logged-in user.
     * @return A List of the newly created entries.
     */
    @PostMapping(value = "/{subcategoryIds}/entries", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "creates new entries in subcategories",
            description = "Creates new entries within the specified subcategoryIds from the URL for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given entries within the given subcategories",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class))}),
            @ApiResponse(responseCode = "400", description = "the number of given subcategoryIds and newApiEntries are not equal or a categoryColourId is less than or equal to 0 or a entryAmount or a entryTimeOfTransaction is empty",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> addEntriesV2(@Parameter(description = "The subcategoryIds added to the URL to create the new entries within them. The subcategoryId and the newApiEntries need to be added in the same order.") @PathVariable List<Integer> subcategoryIds,
                                               @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                       description = "List of new entries. As it is a List, the objects need to be enclosed in [].",
                                                       required = true,
                                                       content = @Content(
                                                               mediaType = "application/json",
                                                               schema = @Schema(implementation = ApiEntry.class)
                                                       )
                                               ) @RequestBody List<ApiEntry> newApiEntries,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (subcategoryIds.size() != newApiEntries.size()) {
                throw new MissingRequiredParameter("the number of subcategoryIds and newApiEntries must be equal");
            }
            List<DatabaseEntry> createdEntries = new ArrayList<>();
            for (int i = 0; i < newApiEntries.size(); i++) {
                if (subcategoryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
                } else if (newApiEntries.get(i).getEntryAmount() == null || newApiEntries.get(i).getEntryAmount().isBlank()) {
                    throw new MissingRequiredParameter("entryAmount is required");
                } else if (newApiEntries.get(i).getEntryTimeOfTransaction() == null || newApiEntries.get(i).getEntryTimeOfTransaction().isBlank()) {
                    throw new MissingRequiredParameter("entryTimeOfTransaction is required");
                }
                createdEntries.add(entryRepository.addEntry(new DatabaseEntry(subcategoryIds.get(i), newApiEntries.get(i), userSingleton.getUserId(userDetails.getUsername()))));
            }
            return ResponseEntity.ok(createdEntries);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Creates new entries in specific subcategories.
     *
     * @param subcategoryIds The IDs of the subcategories.
     * @param mobileEntryIds The IDs of the entries in the mobile applications.
     * @param newApiEntries  The new entries to be added.
     * @param userDetails    The UserDetails object representing the logged-in user.
     * @return A List of the newly created entries.
     */
    @PostMapping(value = "/{subcategoryIds}/entries", headers = "API-Version=3")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "creates new entries in subcategories",
            description = "Creates new entries within the specified subcategoryIds from the URL for the authenticated user. It requires a Basic-Auth-Header. This version for mobile applications also requires a List of mobileEntryIds added to the URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given entries within the given subcategories",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class))}),
            @ApiResponse(responseCode = "400", description = "the number of given subcategoryIds, mobileEntryIds and newApiEntries are not equal or a categoryColourId or a mobileEntryId is less than or equal to 0 or a entryAmount or a entryTimeOfTransaction is empty",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> addEntriesV3(@Parameter(description = "The subcategoryIds added to the URL to create the new entries within them. The subcategoryId and the newApiEntries need to be added in the same order.") @PathVariable List<Integer> subcategoryIds,
                                               @Parameter(description = "List of mobileEntryIds from mobile applications to be added to the URL. The mobileEntryIds and newApiEntries must be in the same order.") @RequestParam List<Integer> mobileEntryIds,
                                               @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                       description = "List of new entries. As it is a List, the objects need to be enclosed in [].",
                                                       required = true,
                                                       content = @Content(
                                                               mediaType = "application/json",
                                                               schema = @Schema(implementation = ApiEntry.class)
                                                       )
                                               ) @RequestBody List<ApiEntry> newApiEntries,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (subcategoryIds.size() != newApiEntries.size() || subcategoryIds.size() != mobileEntryIds.size()) {
                throw new MissingRequiredParameter("the number of subcategoryIds, mobileEntryIds and newApiEntries must be equal");
            }
            List<ResponseEntry> createdEntries = new ArrayList<>();
            for (int i = 0; i < newApiEntries.size(); i++) {
                if (subcategoryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
                } else if (newApiEntries.get(i).getEntryAmount() == null || newApiEntries.get(i).getEntryAmount().isBlank()) {
                    throw new MissingRequiredParameter("entryAmount is required");
                } else if (newApiEntries.get(i).getEntryTimeOfTransaction() == null || newApiEntries.get(i).getEntryTimeOfTransaction().isBlank()) {
                    throw new MissingRequiredParameter("entryTimeOfTransaction is required");
                } else if (mobileEntryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("mobileEntryId cannot be less than or equal to 0");
                }
                createdEntries.add(new ResponseEntry(entryRepository.addEntry(new DatabaseEntry(subcategoryIds.get(i), newApiEntries.get(i), userSingleton.getUserId(userDetails.getUsername()))), mobileEntryIds.get(i)));
            }
            return ResponseEntity.ok(createdEntries);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates an existing entry in a specific subcategory.
     *
     * @param subcategoryId   The ID of the subcategory in which the entry is.
     * @param entryId         The ID of the entry.
     * @param updatedApiEntry The updated entry data.
     * @param userDetails     The UserDetails object representing the logged-in user.
     * @return The updated entry.
     */
    @PatchMapping(value = "/{subcategoryId}/entries/{entryId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "updates an existing entry in a subcategory",
            description = "Updates an existing entry, whose id is given in the URL as well as its superior subcategoryId, for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully updated the given entry of the given subcategory",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class))}),
            @ApiResponse(responseCode = "400", description = "the subcategoryId or the entryId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "the given entry does not exist inside the given subcategory or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> updateEntryV1(@Parameter(description = "The id of the superior subcategory, added to the URL") @PathVariable int subcategoryId,
                                                @Parameter(description = "The id of the entry to be updated, added to the URL.") @PathVariable int entryId,
                                                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                        description = "The updated entry-Object.",
                                                        required = true,
                                                        content = @Content(
                                                                mediaType = "application/json",
                                                                schema = @Schema(implementation = ApiEntry.class)
                                                        )
                                                ) @RequestBody ApiEntry updatedApiEntry,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (subcategoryId <= 0) {
                throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
            } else if (entryId <= 0) {
                throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(entryRepository.updateEntry(new DatabaseEntry(entryId, subcategoryId, updatedApiEntry, userSingleton.getUserId(userDetails.getUsername())), userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates existing entries in specific subcategories.
     *
     * @param subcategoryIds    The IDs of the subcategories in which the entries are.
     * @param entryIds          The IDs of the entries.
     * @param updatedApiEntries The entries to be updated.
     * @param userDetails       The UserDetails object representing the logged-in user.
     * @return A List of the updated entries.
     */
    @PatchMapping(value = "/{subcategoryIds}/entries/{entryIds}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "updates existing entries in subcategories",
            description = "Updates existing entries, whose ids are given in the URL as well as their superior subcategoryIds, for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully updated the given entries of the given subcategories",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class))}),
            @ApiResponse(responseCode = "400", description = "the number of given subcategoryIds, entryIds and updatedApiEntries are not equal or a subcategoryId or a entryId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "a given entry does not exist inside the given subcategory or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> updateEntriesV2(@Parameter(description = "List of the superior subcategoryIds. They need to be added to the URL in the same order as the updatedApiEntries") @PathVariable List<Integer> subcategoryIds,
                                                  @Parameter(description = "List of entryIds to be updated. They need to be added to the URL in the same order as the updatedApiEntries.") @PathVariable List<Integer> entryIds,
                                                  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                          description = "List of the updated entries. As it is a List, the objects need to be enclosed in [].",
                                                          required = true,
                                                          content = @Content(
                                                                  mediaType = "application/json",
                                                                  schema = @Schema(implementation = ApiEntry.class)
                                                          )
                                                  ) @RequestBody List<ApiEntry> updatedApiEntries,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (subcategoryIds.size() != entryIds.size() || subcategoryIds.size() != updatedApiEntries.size()) {
                throw new MissingRequiredParameter("the number of subcategoryIds, entryIds and updatedApiEntries must be equal");
            }
            List<DatabaseEntry> updatedEntries = new ArrayList<>();
            for (int i = 0; i < updatedApiEntries.size(); i++) {
                if (subcategoryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
                } else if (entryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
                }
                updatedEntries.add(entryRepository.updateEntry(new DatabaseEntry(entryIds.get(i), subcategoryIds.get(i), updatedApiEntries.get(i), userSingleton.getUserId(userDetails.getUsername())), userDetails.getUsername()));
            }
            return ResponseEntity.ok(updatedEntries);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes an entry from a specific subcategory.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param entryId       The ID of the entry to be deleted.
     * @param userDetails   The UserDetails object representing the logged-in user.
     * @return An Integer representing the number of deleted rows.
     */
    @DeleteMapping(value = "/{subcategoryId}/entries/{entryId}", headers = "API-Version=1")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes an entry from a subcategory",
            description = "Deletes a entry, whose id is given in the URL as well as its superior subcategoryId, for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted the given entry of the given subcategory",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "400", description = "the subcategoryId or the entryId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "the given entry does not exist inside the given subcategory or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> deleteEntryV1(@Parameter(description = "The id of the superior subcategory, added to the URL.") @PathVariable int subcategoryId,
                                                @Parameter(description = "The id of the entry to be deleted, added to the URL.") @PathVariable int entryId,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (subcategoryId <= 0) {
                throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
            } else if (entryId <= 0) {
                throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
            }
            return ResponseEntity.status(HttpStatus.OK).body(entryRepository.deleteEntry(subcategoryId, entryId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes entries from a specific subcategories.
     *
     * @param subcategoryIds The IDs of the subcategories.
     * @param entryIds       The IDs of the entries to be deleted.
     * @param userDetails    The UserDetails object representing the logged-in user.
     * @return An Integer List representing the number of deleted rows.
     */
    @DeleteMapping(value = "/{subcategoryIds}/entries/{entryIds}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes entries from subcategories",
            description = "Deletes entries, whose ids are given in the URL as well as their superior subcategoryIds, for the authenticated user. It requires a Basic-Auth-Header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted the given entries of the given subcategories",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "400", description = "the number of given subcategoryIds and entryIds are not equal or a subcategoryId or a entryId is less than or equal to 0",
                    content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "404", description = "a given entry does not exist inside the given subcategory or is not found for the authenticated user",
                    content = {@Content(mediaType = "text/plain")})
    })
    public ResponseEntity<Object> deleteEntriesV2(@Parameter(description = "List of superior subcategoryIds. They need to be added to the URL in the same order as the entryIds.") @PathVariable List<Integer> subcategoryIds,
                                                  @Parameter(description = "List of entryIds to be deleted. They need to be added to the URL.") @PathVariable List<Integer> entryIds,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (subcategoryIds.size() != entryIds.size()) {
                throw new MissingRequiredParameter("the number of subcategoryIds and entryIds must be equal");
            }
            List<Integer> deletedEntries = new ArrayList<>();
            for (int i = 0; i < entryIds.size(); i++) {
                if (subcategoryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
                } else if (entryIds.get(i) <= 0) {
                    throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
                }
                deletedEntries.add(entryRepository.deleteEntry(subcategoryIds.get(i), entryIds.get(i), userDetails.getUsername()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(deletedEntries);
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getLocalizedMessage());
        }
    }
}