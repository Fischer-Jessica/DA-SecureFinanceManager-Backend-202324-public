package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.api.ApiEntry;
import at.htlhl.securefinancemanager.model.database.DatabaseEntry;
import at.htlhl.securefinancemanager.repository.EntryRepository;
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
 * @version 3.3
 * @since 16.12.2023 (version 3.3)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager/categories/subcategories/{subcategoryId}")
public class EntryController {
    /** The EntryRepository instance for accessing entry data. */
    @Autowired
    EntryRepository entryRepository;

    /**
     * Retrieves a list of all entries from a specific subcategory.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return A list of entries for the specified subcategory.
     */
    @GetMapping(value = "/entries", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns all entries of one subcategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned all entries of the given subcategory",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class)) }),
            @ApiResponse(responseCode = "400", description = "the subcategoryId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "no entries found with the given subcategoryId for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> getEntries(@PathVariable int subcategoryId,
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
     * @param subcategoryId     The ID of the subcategory.
     * @param entryId           The ID of the entry.
     * @param userDetails       The UserDetails object representing the logged-in user.
     * @return The requested entry.
     */
    @GetMapping(value = "/entries/{entryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns one entry of a subcategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully returned the requested entry of the given subcategory",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class)) }),
            @ApiResponse(responseCode = "400", description = "the subcategoryId or the entryId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the requested entry with the given subcategoryId does not exist or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> getEntry(@PathVariable int subcategoryId,
                                           @PathVariable int entryId,
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
     * @param subcategoryId     The ID of the subcategory.
     * @param newApiEntry       The new entry to be added.
     * @param userDetails       The UserDetails object representing the logged-in user.
     * @return The newly created entry.
     */
    @PostMapping(value = "/entries", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "creates a new entry in a subcategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given entry within the given subcategory",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class)) }),
            @ApiResponse(responseCode = "400", description = "the categoryColourId is less than or equal to 0 or the entryAmount or the entryTimeOfTransaction is empty",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> addEntry(@PathVariable int subcategoryId,
                                           @RequestBody ApiEntry newApiEntry,
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
     * Updates an existing entry in a specific subcategory.
     *
     * @param subcategoryId     The ID of the subcategory in which the entry is.
     * @param entryId           The ID of the entry.
     * @param updatedApiEntry   The updated entry data.
     * @param userDetails       The UserDetails object representing the logged-in user.
     * @return The updated entry.
     */
    @PatchMapping(value = "/entries/{entryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "updates an existing entry in a subcategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully updated the given entry of the given subcategory",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class)) }),
            @ApiResponse(responseCode = "400", description = "the subcategoryId or the entryId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the given entry does not exist inside the given subcategory or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> updateEntry(@PathVariable int subcategoryId,
                                              @PathVariable int entryId,
                                              @RequestBody ApiEntry updatedApiEntry,
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
     * Deletes an entry from a specific subcategory.
     *
     * @param subcategoryId     The ID of the subcategory.
     * @param entryId           The ID of the entry to be deleted.
     * @param userDetails       The UserDetails object representing the logged-in user.
     * @return An Integer representing the number of deleted rows.
     */
    @DeleteMapping(value = "/entries/{entryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes an entry from a subcategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted the given entry of the given subcategory",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class)) }),
            @ApiResponse(responseCode = "400", description = "the subcategoryId or the entryId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "the given entry does not exist inside the given subcategory or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> deleteEntry(@PathVariable int subcategoryId,
                                              @PathVariable int entryId,
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
}
