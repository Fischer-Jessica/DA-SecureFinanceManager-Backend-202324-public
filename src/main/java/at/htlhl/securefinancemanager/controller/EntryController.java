package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.Entry;
import at.htlhl.securefinancemanager.model.User;
import at.htlhl.securefinancemanager.repository.EntryRepository;
import at.htlhl.securefinancemanager.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The EntryController class handles HTTP requests related to entries.
 * It provides endpoints for retrieving, adding, updating, and deleting entries for a specific subcategory.
 *
 * <p>
 * This controller is responsible for handling CRUD operations (Create, Read, Update, Delete) on Entry entities.
 * It interacts with the EntryRepository to access and manipulate the Entry entities in the 'entries' table of the 'financial_overview' PostgreSQL database.
 * </p>
 *
 * <p>
 * This class is annotated with the {@link RestController} annotation, which indicates that it is a controller that handles RESTful HTTP requests.
 * The {@link CrossOrigin} annotation allows cross-origin requests to this controller, enabling it to be accessed from different domains.
 * The {@link RequestMapping} annotation specifies the base path for all the endpoints provided by this controller.
 * </p>
 *
 * <p>
 * The EntryController class works in conjunction with the EntryRepository and other related classes to enable efficient management of entries in the financial overview system.
 * </p>
 *
 * @author Fischer
 * @version 2.4
 * @since 15.10.2023 (version 2.4)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("secure-finance-manager/categories/{categoryId}/subcategories/{subcategoryId}")
public class EntryController {
    /** The EntryRepository instance for accessing entry data. */
    @Autowired
    EntryRepository entryRepository;

    /**
     * Retrieves a list of all entries for a specific subcategory.
     *
     * @param categoryId   The ID of the category.
     * @param subcategoryId The ID of the subcategory.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return A list of entries for the specified subcategory.
     */
    @GetMapping(value = "/entries", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns all entries of one subcategory")
    public ResponseEntity<Object> getEntries(@PathVariable int categoryId,
                                             @PathVariable int subcategoryId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(entryRepository.getEntries(subcategoryId, userDetails.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Retrieves a specific entry for a specific subcategory.
     *
     * @param categoryId   The ID of the category.
     * @param subcategoryId The ID of the subcategory.
     * @param entryId      The ID of the entry.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return The requested entry.
     */
    @GetMapping(value = "/entries/{entryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "returns one entry")
    public ResponseEntity<Object> getEntry(@PathVariable int categoryId,
                                           @PathVariable int subcategoryId,
                                           @PathVariable int entryId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(entryRepository.getEntry(subcategoryId, entryId, userDetails.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Creates a new entry for a specific subcategory.
     *
     * @param categoryId   The ID of the category.
     * @param subcategoryId The ID of the subcategory.
     * @param newEntry     The new entry to be added.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return The ID of the newly created entry.
     */
    @PostMapping(value = "/entries", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "create a new entry")
    public ResponseEntity<Object> addEntry(@PathVariable int categoryId,
                                           @PathVariable int subcategoryId,
                                           @RequestBody Entry newEntry,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            newEntry.setEntrySubcategoryId(subcategoryId);
            newEntry.setEntryUserId(UserRepository.getUserId(userDetails.getUsername()));
            return ResponseEntity.ok(entryRepository.addEntry(newEntry));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Updates an existing entry for a specific subcategory.
     *
     * @param categoryId   The ID of the category.
     * @param subcategoryId The ID of the subcategory in which the entry is.
     * @param entryId      The ID of the entry.
     * @param updatedEntry The updated entry data.
     * @param userDetails  The UserDetails object representing the logged-in user.
     * @return The updated entry.
     */
    @PatchMapping(value = "/entries/{entryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "change an existing entry")
    public ResponseEntity<Object> updateEntry(@PathVariable int categoryId,
                                              @PathVariable int subcategoryId,
                                              @PathVariable int entryId,
                                              @RequestBody Entry updatedEntry,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            updatedEntry.setEntryId(entryId);
            if (updatedEntry.getEntrySubcategoryId() == -1) {
                updatedEntry.setEntrySubcategoryId(subcategoryId);
            }
            updatedEntry.setEntryUserId(UserRepository.getUserId(userDetails.getUsername()));
            return ResponseEntity.ok(entryRepository.updateEntry(updatedEntry, userDetails.getUsername()));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }

    /**
     * Deletes an entry from a specific subcategory.
     *
     * @param categoryId   The ID of the category.
     * @param subcategoryId The ID of the subcategory.
     * @param entryId      The ID of the entry to be deleted.
     * @param userDetails  The UserDetails object representing the logged-in user.
     */
    @DeleteMapping(value = "/entries/{entryId}", headers = "API-Version=0")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "delete an entry")
    public ResponseEntity<Object> deleteEntry(@PathVariable int categoryId,
                                              @PathVariable int subcategoryId,
                                              @PathVariable int entryId,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            entryRepository.deleteEntry(subcategoryId, entryId, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getLocalizedMessage());
        }
    }
}
