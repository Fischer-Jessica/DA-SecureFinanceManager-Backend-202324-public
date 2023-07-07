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
 * @version 1
 * @since 07.07.2023 (version 1)
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
     * @param categoryId     The ID of the category.
     * @param subcategoryId  The ID of the subcategory.
     * @param loggedInUser The logged-in user.
     * @return A list of entries for the specified subcategory.
     */
    @GetMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all entries of one subcategory")
    public List<Entry> getEntries(@PathVariable int categoryId, @PathVariable int subcategoryId, @RequestBody User loggedInUser) {
        return entryRepository.getEntries(categoryId, subcategoryId, loggedInUser);
    }

    /**
     * Retrieves a specific entry for a specific subcategory.
     *
     * @param categoryId     The ID of the category.
     * @param subcategoryId  The ID of the subcategory.
     * @param entryId        The ID of the entry.
     * @param loggedInUser The logged-in user.
     * @return The requested entry.
     */
    @GetMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one entry")
    public Entry getEntry(@PathVariable int categoryId, @PathVariable int subcategoryId,
                          @RequestParam int entryId, @RequestBody User loggedInUser) {
        return entryRepository.getEntry(categoryId, subcategoryId, entryId, loggedInUser);
    }

    /**
     * Creates a new entry for a specific subcategory.
     *
     * @param categoryId              The ID of the category.
     * @param subcategoryId           The ID of the subcategory.
     * @param entryName               The name of the entry.
     * @param entryDescription        The description of the entry.
     * @param entryAmount             The amount of the entry.
     * @param entryCreationTime       The creation time of the entry.
     * @param entryTimeOfExpense      The time of expense of the entry.
     * @param entryAttachment         The attachment of the entry.
     * @param entryLabelId            The label ID of the entry.
     * @param loggedInUser         The logged-in user.
     * @return The ID of the newly created entry.
     */
    @PostMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "create a new entry")
    public int addEntry(@PathVariable int categoryId, @PathVariable int subcategoryId,
                        @RequestParam byte[] entryName, @RequestParam byte[] entryDescription,
                        @RequestParam byte[] entryAmount, @RequestParam byte[] entryCreationTime,
                        @RequestParam byte[] entryTimeOfExpense, @RequestParam byte[] entryAttachment,
                        @RequestParam int entryLabelId, @RequestBody User loggedInUser) {
        return entryRepository.addEntry(categoryId, subcategoryId, entryName, entryDescription, entryAmount,
                entryCreationTime, entryTimeOfExpense, entryAmount, entryLabelId, loggedInUser);
    }

    /**
     * Updates an existing entry for a specific subcategory.
     *
     * @param categoryId         The ID of the category.
     * @param subcategoryId      The ID of the subcategory.
     * @param updatedEntry       The updated entry.
     * @param loggedInUser     The logged-in user.
     */
    @PatchMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing entry")
    public void updateEntry(@PathVariable int categoryId, @PathVariable int subcategoryId,
                            @RequestBody Entry updatedEntry, @RequestBody User loggedInUser) {
        entryRepository.updateEntry(categoryId, subcategoryId, updatedEntry, loggedInUser);
    }

    /**
     * Changes the name of an existing entry for a specific subcategory.
     *
     * @param categoryId            The ID of the category.
     * @param subcategoryId         The ID of the subcategory.
     * @param updatedEntryName      The updated entry name.
     * @param loggedInUser        The logged-in user.
     */
    @PatchMapping("/entries/entryName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of an existing entry")
    public void updateEntryName(@PathVariable int categoryId, @PathVariable int subcategoryId,
                                @RequestParam byte[] updatedEntryName, @RequestBody User loggedInUser) {
        entryRepository.updateEntryName(categoryId, subcategoryId, updatedEntryName, loggedInUser);
    }

    /**
     * Changes the description of an existing entry for a specific subcategory.
     *
     * @param categoryId                 The ID of the category.
     * @param subcategoryId              The ID of the subcategory.
     * @param updatedEntryDescription    The updated entry description.
     * @param loggedInUser              The logged-in user.
     */
    @PatchMapping("/entries/entryDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of an existing entry")
    public void updateEntryDescription(@PathVariable int categoryId, @PathVariable int subcategoryId,
                                       @RequestParam byte[] updatedEntryDescription, @RequestBody User loggedInUser) {
        entryRepository.updateEntryDescription(categoryId, subcategoryId, updatedEntryDescription, loggedInUser);
    }

    /**
     * Changes the amount of an existing entry for a specific subcategory.
     *
     * @param categoryId                 The ID of the category.
     * @param subcategoryId              The ID of the subcategory.
     * @param updatedEntryAmount         The updated entry amount.
     * @param loggedInUser              The logged-in user.
     */
    @PatchMapping("/entries/entryAmount")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the amount of an existing entry")
    public void updateEntryAmount(@PathVariable int categoryId, @PathVariable int subcategoryId,
                            @RequestParam byte[] updatedEntryAmount, @RequestBody User loggedInUser) {
        entryRepository.updateEntryAmount(categoryId, subcategoryId, updatedEntryAmount, loggedInUser);
    }

    /**
     * Changes the time of expense of an existing entry for a specific subcategory.
     *
     * @param categoryId                    The ID of the category.
     * @param subcategoryId                 The ID of the subcategory.
     * @param entryTimeOfExpense            The updated entry time of expense.
     * @param loggedInUser                 The logged-in user.
     */
    @PatchMapping("/entries/entryTimeOfExpense")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the time of expense of an existing entry")
    public void updateEntryTimeOfExpense(@PathVariable int categoryId, @PathVariable int subcategoryId,
                                         @RequestParam byte[] entryTimeOfExpense, @RequestBody User loggedInUser) {
        entryRepository.updateEntryTimeOfExpense(categoryId, subcategoryId, entryTimeOfExpense, loggedInUser);
    }

    /**
     * Updates the attachment of an existing entry in a specific subcategory.
     *
     * @param categoryId               The ID of the category.
     * @param subcategoryId            The ID of the subcategory.
     * @param updatedEntryAttachment    The updated attachment.
     * @param loggedInUser             The logged-in user.
     */
    @PatchMapping("/entries/entryAttachment")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the attachment of an existing entry")
    public void updateEntryAttachment(@PathVariable int categoryId, @PathVariable int subcategoryId,
                                      @RequestParam byte[] updatedEntryAttachment, @RequestBody User loggedInUser) {
        entryRepository.updateEntryAttachment(categoryId, subcategoryId, updatedEntryAttachment, loggedInUser);
    }

    /**
     * Changes the label of an existing entry in a specific subcategory.
     *
     * @param categoryId               The ID of the category.
     * @param subcategoryId            The ID of the subcategory.
     * @param updatedEntryLabelId       The updated label ID.
     * @param loggedInUser             The logged-in user.
     */
    @PatchMapping("/entries/entryLabelId")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the label of an existing entry")
    public void updateEntryLabelId(@PathVariable int categoryId, @PathVariable int subcategoryId,
                                   @RequestParam int updatedEntryLabelId, @RequestBody User loggedInUser) {
        entryRepository.updateEntryLabelId(categoryId, subcategoryId, updatedEntryLabelId, loggedInUser);
    }

    /**
     * Deletes an entry from a specific subcategory.
     *
     * @param categoryId               The ID of the category.
     * @param subcategoryId            The ID of the subcategory.
     * @param entryId                  The ID of the entry to be deleted.
     * @param loggedInUser             The logged-in user.
     */
    @DeleteMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete an entry")
    public void deleteEntry(@PathVariable int categoryId, @PathVariable int subcategoryId,
                            @RequestParam int entryId, @RequestBody User loggedInUser) {
        entryRepository.deleteEntry(categoryId, subcategoryId, entryId, loggedInUser);
    }
}
