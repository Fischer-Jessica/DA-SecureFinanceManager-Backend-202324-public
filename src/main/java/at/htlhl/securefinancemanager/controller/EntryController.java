package at.htlhl.securefinancemanager.controller;

import at.htlhl.securefinancemanager.exception.ValidationException;
import at.htlhl.securefinancemanager.model.Entry;
import at.htlhl.securefinancemanager.model.User;
import at.htlhl.securefinancemanager.repository.EntryRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * @version 2.0
 * @since 05.10.2023 (version 2.0)
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
     * @param categoryId            The ID of the category.
     * @param subcategoryId         The ID of the subcategory.
     * @param loggedInUserId        The ID of the logged-in user.
     * @param loggedInUsername      The username of the logged-in user.
     * @param loggedInPassword      The password of the logged-in user.
     * @param loggedInEMailAddress  The email address of the logged-in user.
     * @param loggedInFirstName     The first name of the logged-in user.
     * @param loggedInLastName      The last name of the logged-in user.
     * @return A list of entries for the specified subcategory.
     */
    @GetMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all entries of one subcategory")
    public ResponseEntity<List<Entry>> getEntries(@PathVariable int categoryId,
                                                  @PathVariable int subcategoryId,
                                                  @RequestParam int loggedInUserId,
                                                  @RequestParam String loggedInUsername,
                                                  @RequestParam String loggedInPassword,
                                                  @RequestParam String loggedInEMailAddress,
                                                  @RequestParam String loggedInFirstName,
                                                  @RequestParam String loggedInLastName) {
        try {
            return ResponseEntity.ok(entryRepository.getEntries(subcategoryId,
                    new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName)));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Retrieves a specific entry for a specific subcategory.
     *
     * @param categoryId            The ID of the category.
     * @param subcategoryId         The ID of the subcategory.
     * @param entryId               The ID of the entry.
     * @param loggedInUserId        The ID of the logged-in user.
     * @param loggedInUsername      The username of the logged-in user.
     * @param loggedInPassword      The password of the logged-in user.
     * @param loggedInEMailAddress  The email address of the logged-in user.
     * @param loggedInFirstName     The first name of the logged-in user.
     * @param loggedInLastName      The last name of the logged-in user.
     * @return The requested entry.
     */
    @GetMapping("/entries/{entryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one entry")
    public ResponseEntity<Entry> getEntry(@PathVariable int categoryId,
                                          @PathVariable int subcategoryId,
                                          @PathVariable int entryId,
                                          @RequestParam int loggedInUserId,
                                          @RequestParam String loggedInUsername,
                                          @RequestParam String loggedInPassword,
                                          @RequestParam String loggedInEMailAddress,
                                          @RequestParam String loggedInFirstName,
                                          @RequestParam String loggedInLastName) {
        try {
            return ResponseEntity.ok(entryRepository.getEntry(subcategoryId, entryId,
                    new User(loggedInUserId, loggedInUsername, loggedInPassword, loggedInEMailAddress, loggedInFirstName, loggedInLastName)));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Creates a new entry for a specific subcategory.
     *
     * @param categoryId                    The ID of the category.
     * @param subcategoryId                 The ID of the subcategory.
     * @param entryName                     The name of the entry.
     * @param entryDescription              The description of the entry.
     * @param entryAmount                   The amount of the entry.
     * @param entryTimeOfTransaction        The time of the transaction in the entry.
     * @param entryAttachment               The attachment of the entry.
     * @param loggedInUser                  The logged-in user.
     * @return The ID of the newly created entry.
     */
    @PostMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "create a new entry")
    public ResponseEntity<Entry> addEntry(@PathVariable int categoryId,
                                            @PathVariable int subcategoryId,
                                            @RequestParam String entryName,
                                            @RequestParam String entryDescription,
                                            @RequestParam String entryAmount,
                                            @RequestParam String entryTimeOfTransaction,
                                            @RequestParam String entryAttachment,
                                            @RequestBody User loggedInUser) {
        try {
            return ResponseEntity.ok(entryRepository.addEntry(subcategoryId, entryName, entryDescription, entryAmount, entryTimeOfTransaction, entryAttachment,
                    loggedInUser));
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Updates an existing entry for a specific subcategory.
     *
     * @param categoryId                        The ID of the category.
     * @param subcategoryId                     The ID of the subcategory in which the entry is.
     * @param entryId                           The ID of the entry.
     * @param updatedSubcategoryId              The other ID in which the entry should be transferred.
     * @param updatedEntryAmount                The updated amount of the entry.
     * @param updatedEntryAttachment            The updated attachment of the entry.
     * @param updatedEntryDescription           The updated description of the entry.
     * @param updatedEntryName                  The updated name of the entry.
     * @param updatedEntryTimeOfTransaction     The updated time the transaction in the entry.
     * @param loggedInUser                      The logged-in user.
     */
    @PatchMapping("/entries/{entryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing entry")
    public ResponseEntity updateEntry(@PathVariable int categoryId,
                                      @PathVariable int subcategoryId,
                                      @PathVariable int entryId,
                                      @RequestParam(defaultValue = "-1", required = false) int updatedSubcategoryId,
                                      @RequestParam(required = false) String updatedEntryName,
                                      @RequestParam(required = false) String updatedEntryDescription,
                                      @RequestParam(required = false) String updatedEntryAmount,
                                      @RequestParam(required = false) String updatedEntryTimeOfTransaction,
                                      @RequestParam(required = false) String updatedEntryAttachment,
                                      @RequestBody User loggedInUser) {
        try {
            entryRepository.updateEntry(subcategoryId, entryId, updatedSubcategoryId,
                    updatedEntryName, updatedEntryDescription, updatedEntryAmount, updatedEntryTimeOfTransaction, updatedEntryAttachment,
                    loggedInUser);
            System.out.println("Sache macht");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Deletes an entry from a specific subcategory.
     *
     * @param categoryId        The ID of the category.
     * @param subcategoryId     The ID of the subcategory.
     * @param entryId           The ID of the entry to be deleted.
     * @param loggedInUser      The logged-in user.
     */
    @DeleteMapping("/entries/{entryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete an entry")
    public ResponseEntity deleteEntry(@PathVariable int categoryId,
                                      @PathVariable int subcategoryId,
                                      @PathVariable int entryId,
                                      @RequestBody User loggedInUser) {
        try {
            entryRepository.deleteEntry(subcategoryId, entryId, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ValidationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
