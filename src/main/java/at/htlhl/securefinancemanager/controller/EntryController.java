package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.MissingRequiredParameter;
import at.htlhl.securefinancemanager.model.api.ApiEntry;
import at.htlhl.securefinancemanager.model.database.DatabaseEntry;
import at.htlhl.securefinancemanager.repository.EntryRepository;
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
 * efficient management of entries in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 2.9
 * @since 14.11.2023 (version 2.9)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager/categories/subcategories/{subcategoryId}")
public class EntryController {
    /** The EntryRepository instance for accessing entry data. */
    @Autowired
    EntryRepository entryRepository;

    /**
     * Retrieves a list of all entries for a specific subcategory.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return A list of entries for the specified subcategory.
     */
    @GetMapping(value = "/entries", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns all entries of one subcategory")
    public ResponseEntity<Object> getEntries(@PathVariable int subcategoryId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (subcategoryId <= 0) {
                throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
            }
            return ResponseEntity.ok(entryRepository.getEntries(subcategoryId, userDetails.getUsername()));
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Retrieves a specific entry for a specific subcategory.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param entryId      The ID of the entry.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return The requested entry.
     */
    @GetMapping(value = "/entries/{entryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns one entry")
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
        }
    }

    /**
     * Creates a new entry for a specific subcategory.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param newApiEntry   The new entry to be added.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return The ID of the newly created entry.
     */
    @PostMapping(value = "/entries", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "create a new entry")
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
     * Updates an existing entry for a specific subcategory.
     *
     * @param subcategoryId The ID of the subcategory in which the entry is.
     * @param entryId      The ID of the entry.
     * @param updatedApiEntry The updated entry data.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return The updated entry.
     */
    @PatchMapping(value = "/entries/{entryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "change an existing entry")
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
        }
    }

    /**
     * Deletes an entry from a specific subcategory.
     *
     * @param subcategoryId The ID of the subcategory.
     * @param entryId      The ID of the entry to be deleted.
     * @param userDetails  The UserDetails object representing the logged-in user.
     */
    @DeleteMapping(value = "/entries/{entryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "delete an entry")
    public ResponseEntity<Object> deleteEntry(@PathVariable int subcategoryId,
                                              @PathVariable int entryId,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (subcategoryId <= 0) {
                throw new MissingRequiredParameter("subcategoryId cannot be less than or equal to 0");
            } else if (entryId <= 0) {
                throw new MissingRequiredParameter("entryId cannot be less than or equal to 0");
            }
            entryRepository.deleteEntry(subcategoryId, entryId, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MissingRequiredParameter exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getLocalizedMessage());
        }
    }
}
