package at.htlhl.financialoverview.controller;

import at.htlhl.financialoverview.model.Entry;
import at.htlhl.financialoverview.model.User;
import at.htlhl.financialoverview.repository.EntryRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("financial-overview/categories/{categoryId}/subcategories/{subcategoryId}")
public class EntryController {
    @Autowired
    EntryRepository entryRepository;

    @GetMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns all entries of one subcategory")
    public List<Entry> getEntries(@PathVariable int categoryId, @PathVariable int subcategoryId, @RequestBody User loggedInUser) {
        return entryRepository.getEntries(categoryId, subcategoryId, loggedInUser);
    }

    @GetMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "returns one entry")
    public Entry getEntry(@PathVariable int categoryId, @PathVariable int subcategoryId,
                          @RequestParam int entryId, @RequestBody User loggedInUser) {
        return entryRepository.getEntry(categoryId, subcategoryId, entryId, loggedInUser);
    }

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

    @PatchMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change an existing entry")
    public void updateEntry(@PathVariable int categoryId, @PathVariable int subcategoryId,
                            @RequestBody Entry updatedEntry, @RequestBody User loggedInUser) {
        entryRepository.updateEntry(categoryId, subcategoryId, updatedEntry, loggedInUser);
    }

    @PatchMapping("/entries/entryName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the name of an existing entry")
    public void updateEntryName(@PathVariable int categoryId, @PathVariable int subcategoryId,
                                @RequestParam byte[] updatedEntryName, @RequestBody User loggedInUser) {
        entryRepository.updateEntryName(categoryId, subcategoryId, updatedEntryName, loggedInUser);
    }

    @PatchMapping("/entries/entryDescription")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the description of an existing entry")
    public void updateEntryDescription(@PathVariable int categoryId, @PathVariable int subcategoryId,
                                       @RequestParam byte[] updatedEntryDescription, @RequestBody User loggedInUser) {
        entryRepository.updateEntryDescription(categoryId, subcategoryId, updatedEntryDescription, loggedInUser);
    }

    @PatchMapping("/entries/entryAmount")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the amount of an existing entry")
    public void updateEntryAmount(@PathVariable int categoryId, @PathVariable int subcategoryId,
                            @RequestParam byte[] updatedEntryAmount, @RequestBody User loggedInUser) {
        entryRepository.updateEntryAmount(categoryId, subcategoryId, updatedEntryAmount, loggedInUser);
    }

    @PatchMapping("/entries/entryTimeOfExpense")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the time of expense of an existing entry")
    public void updateEntryTimeOfExpense(@PathVariable int categoryId, @PathVariable int subcategoryId,
                                         @RequestParam byte[] entryTimeOfExpense, @RequestBody User loggedInUser) {
        entryRepository.updateEntryTimeOfExpense(categoryId, subcategoryId, entryTimeOfExpense, loggedInUser);
    }

    @PatchMapping("/entries/entryAttachment")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the attachment of an existing entry")
    public void updateEntryAttachment(@PathVariable int categoryId, @PathVariable int subcategoryId,
                                      @RequestParam byte[] updatedEntryAttachment, @RequestBody User loggedInUser) {
        entryRepository.updateEntryAttachment(categoryId, subcategoryId, updatedEntryAttachment, loggedInUser);
    }

    @PatchMapping("/entries/entryLabelId")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "change the label of an existing entry")
    public void updateEntryLabelId(@PathVariable int categoryId, @PathVariable int subcategoryId,
                                   @RequestParam int updatedEntryLabelId, @RequestBody User loggedInUser) {
        entryRepository.updateEntryLabelId(categoryId, subcategoryId, updatedEntryLabelId, loggedInUser);
    }

    @DeleteMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "delete an entry")
    public void deleteEntry(@PathVariable int categoryId, @PathVariable int subcategoryId,
                            @RequestParam int entryId, @RequestBody User loggedInUser) {
        entryRepository.deleteEntry(categoryId, subcategoryId, entryId, loggedInUser);
    }
}
