package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.Entry;
import at.htlhl.financialoverview.model.User;
import at.htlhl.financialoverview.repository.EntryRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The EntryController class handles the HTTP requests related to entries.
 *
 * @author Fischer
 * @version 1.3
 * @since 21.07.2023 (version 1.3)
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview/categories/{categoryId}/subcategories/{subcategoryId}")
public class EntryController {
    /** The CategoryRepository instance for accessing user data. */
    @Autowired
    EntryRepository entryRepository;

    /**
     * Retrieves a list of all entries for a specific subcategory.
     *
     * @param subcategoryId  The ID of the subcategory.
     * @param loggedInUser The logged-in user.
     * @return A list of entries for the specified subcategory.
     */
    @GetMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all entries of one subcategory")
    public List<Entry> getEntries(@PathVariable int subcategoryId,
                                  @RequestParam int userId, @RequestParam String username, @RequestParam String password, @RequestParam String eMailAddress, @RequestParam String firstName, @RequestParam String lastName) {
        return entryRepository.getEntries(subcategoryId, new User(userId, username, password, eMailAddress, firstName, lastName));
    }

    /**
     * Retrieves a specific entry for a specific subcategory.
     *
     * @param subcategoryId  The ID of the subcategory.
     * @param entryId        The ID of the entry.
     * @param loggedInUser The logged-in user.
     * @return The requested entry.
     */
    @GetMapping("/entries/{entryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one entry")
    public Entry getEntry(@PathVariable int subcategoryId, @PathVariable int entryId,
                          @RequestParam int userId, @RequestParam String username, @RequestParam String password, @RequestParam String eMailAddress, @RequestParam String firstName, @RequestParam String lastName) {
        return entryRepository.getEntry(subcategoryId, entryId, new User(userId, username, password, eMailAddress, firstName, lastName));
    }

    /**
     * Creates a new entry for a specific subcategory.
     *
     * @param subcategoryId           The ID of the subcategory.
     * @param entryName               The name of the entry.
     * @param entryDescription        The description of the entry.
     * @param entryAmount             The amount of the entry.
     * @param entryCreationTime       The creation time of the entry.
     * @param entryTimeOfExpense      The time of expense of the entry.
     * @param entryAttachment         The attachment of the entry.
     * @param loggedInUser         The logged-in user.
     * @return The ID of the newly created entry.
     */
    @PostMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "create a new entry")
    public int addEntry(@PathVariable int subcategoryId,
                        @RequestParam String entryName, @RequestParam String entryDescription,
                        @RequestParam String entryAmount,
                        @RequestParam String entryTimeOfExpense, @RequestParam String entryAttachment,
                        @RequestBody User loggedInUser) {
        return entryRepository.addEntry(subcategoryId, entryName, entryDescription, entryAmount,
                entryTimeOfExpense, entryAttachment, loggedInUser);
    }

    /**
     * Updates an existing entry for a specific subcategory.
     *
     * @param subcategoryId      The ID of the subcategory.
     * @param loggedInUser     The logged-in user.
     */
    @PatchMapping("/entries/{entryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing entry")
    public void updateEntry(@PathVariable int subcategoryId, @PathVariable int entryId,
                            @RequestParam String entryName, @RequestParam String entryDescription,
                            @RequestParam String entryAmount, @RequestParam String entryTimeOfExpense, @RequestParam String entryAttachment,
                            @RequestBody User loggedInUser) {
        entryRepository.updateEntry(subcategoryId, entryId, entryName, entryDescription, entryAmount, entryTimeOfExpense, entryAttachment, loggedInUser);
    }

    @PatchMapping("/entries/{entryId}/subcategoryOfEntry")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the subcategory of an existing entry")
    public void updateSubcategoryOfEntry(@PathVariable int subcategoryId, @PathVariable int entryId, @RequestBody User loggedInUser) {
        entryRepository.updateSubcategoryOfEntry(subcategoryId, entryId, loggedInUser);
    }

    /**
     * Changes the name of an existing entry for a specific subcategory.
     *
     * @param subcategoryId         The ID of the subcategory.
     * @param updatedEntryName      The updated entry name.
     * @param loggedInUser        The logged-in user.
     */
    @PatchMapping("/entries/{entryId}/entryName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of an existing entry")
    public void updateEntryName(@PathVariable int subcategoryId, @PathVariable int entryId,
                                @RequestParam String updatedEntryName, @RequestBody User loggedInUser) {
        entryRepository.updateEntryName(subcategoryId, entryId, updatedEntryName, loggedInUser);
    }

    /**
     * Changes the description of an existing entry for a specific subcategory.
     *
     * @param subcategoryId              The ID of the subcategory.
     * @param updatedEntryDescription    The updated entry description.
     * @param loggedInUser              The logged-in user.
     */
    @PatchMapping("/entries/{entryId}/entryDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of an existing entry")
    public void updateEntryDescription(@PathVariable int subcategoryId, @PathVariable int entryId,
                                       @RequestParam String updatedEntryDescription, @RequestBody User loggedInUser) {
        entryRepository.updateEntryDescription(subcategoryId, entryId, updatedEntryDescription, loggedInUser);
    }

    /**
     * Changes the amount of an existing entry for a specific subcategory.
     *
     * @param subcategoryId              The ID of the subcategory.
     * @param updatedEntryAmount         The updated entry amount.
     * @param loggedInUser              The logged-in user.
     */
    @PatchMapping("/entries/{entryId}/entryAmount")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the amount of an existing entry")
    public void updateEntryAmount(@PathVariable int subcategoryId, @PathVariable int entryId,
                            @RequestParam String updatedEntryAmount, @RequestBody User loggedInUser) {
        entryRepository.updateEntryAmount(subcategoryId, entryId, updatedEntryAmount, loggedInUser);
    }

    /**
     * Changes the time of expense of an existing entry for a specific subcategory.
     *
     * @param subcategoryId                 The ID of the subcategory.
     * @param entryTimeOfExpense            The updated entry time of expense.
     * @param loggedInUser                 The logged-in user.
     */
    @PatchMapping("/entries/{entryId}/entryTimeOfExpense")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the time of expense of an existing entry")
    public void updateEntryTimeOfExpense(@PathVariable int subcategoryId, @PathVariable int entryId,
                                         @RequestParam String entryTimeOfExpense, @RequestBody User loggedInUser) {
        entryRepository.updateEntryTimeOfExpense(subcategoryId, entryId, entryTimeOfExpense, loggedInUser);
    }

    /**
     * Updates the attachment of an existing entry in a specific subcategory.
     *
     * @param subcategoryId            The ID of the subcategory.
     * @param updatedEntryAttachment    The updated attachment.
     * @param loggedInUser             The logged-in user.
     */
    @PatchMapping("/entries/{entryId}/entryAttachment")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the attachment of an existing entry")
    public void updateEntryAttachment(@PathVariable int subcategoryId, @PathVariable int entryId,
                                      @RequestParam String updatedEntryAttachment, @RequestBody User loggedInUser) {
        entryRepository.updateEntryAttachment(subcategoryId, entryId, updatedEntryAttachment, loggedInUser);
    }

    // TODO: write a method to update the to the Entry attached Labels

    /**
     * Deletes an entry from a specific subcategory.
     *
     * @param subcategoryId            The ID of the subcategory.
     * @param entryId                  The ID of the entry to be deleted.
     * @param loggedInUser             The logged-in user.
     */
    @DeleteMapping("/entries/{entryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete an entry")
    public void deleteEntry(@PathVariable int subcategoryId,
                            @PathVariable int entryId, @RequestBody User loggedInUser) {
        entryRepository.deleteEntry(subcategoryId, entryId, loggedInUser);
    }
}
