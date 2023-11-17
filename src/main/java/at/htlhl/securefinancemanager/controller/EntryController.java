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
 * @version 3.6
 * @since 17.12.2023 (version 3.6)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager/categories/subcategories")
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
    @GetMapping(value = "/{subcategoryId}/entries", headers = "API-Version=1")
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
    @GetMapping(value = "/{subcategoryId}/entries/{entryId}", headers = "API-Version=1")
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
     * Creates new entries in specific subcategories.
     *
     * @param subcategoryIds    The IDs of the subcategories.
     * @param newApiEntries     The new entries to be added.
     * @param userDetails       The UserDetails object representing the logged-in user.
     * @return A List of the newly created entries.
     */
    @PostMapping(value = "/{subcategoryIds}/entries", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "creates new entries in subcategories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created the given entries within the given subcategories",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class)) }),
            @ApiResponse(responseCode = "400", description = "the number of given subcategoryIds and newApiEntries are not equal or a categoryColourId is less than or equal to 0 or a entryAmount or a entryTimeOfTransaction is empty",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> addEntries(@PathVariable List<Integer> subcategoryIds,
                                             @RequestBody List<ApiEntry> newApiEntries,
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
     * Updates existing entries in specific subcategories.
     *
     * @param subcategoryIds        The IDs of the subcategories in which the entries are.
     * @param entryIds              The IDs of the entries.
     * @param updatedApiEntries     The entries to be updated.
     * @param userDetails           The UserDetails object representing the logged-in user.
     * @return A List of the updated entries.
     */
    @PatchMapping(value = "/{subcategoryIds}/entries/{entryIds}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "updates existing entries in subcategories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully updated the given entries of the given subcategories",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DatabaseEntry.class)) }),
            @ApiResponse(responseCode = "400", description = "the number of given subcategoryIds, entryIds and updatedApiEntries are not equal or a subcategoryId or a entryId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "a given entry does not exist inside the given subcategory or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> updateEntries(@PathVariable List<Integer> subcategoryIds,
                                                @PathVariable List<Integer> entryIds,
                                                @RequestBody List<ApiEntry> updatedApiEntries,
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
     * Deletes entries from a specific subcategories.
     *
     * @param subcategoryIds    The IDs of the subcategories.
     * @param entryIds          The IDs of the entries to be deleted.
     * @param userDetails       The UserDetails object representing the logged-in user.
     * @return An Integer List representing the number of deleted rows.
     */
    @DeleteMapping(value = "/{subcategoryIds}/entries/{entryIds}", headers = "API-Version=2")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "deletes entries from subcategories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted the given entries of the given subcategories",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class)) }),
            @ApiResponse(responseCode = "400", description = "the number of given subcategoryIds and entryIds are not equal or a subcategoryId or a entryId is less than or equal to 0",
                    content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "404", description = "a given entry does not exist inside the given subcategory or is not found for the authenticated user",
                    content = { @Content(mediaType = "text/plain") })
    })
    public ResponseEntity<Object> deleteEntries(@PathVariable List<Integer> subcategoryIds,
                                                @PathVariable List<Integer> entryIds,
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
