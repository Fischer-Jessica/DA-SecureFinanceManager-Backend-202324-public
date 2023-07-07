package at.htlhl.financialoverview.repository;

import at.htlhl.financialoverview.model.Entry;
import at.htlhl.financialoverview.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The EntryRepository class handles the persistence operations for entry data.
 *
 * @author Fischer
 * @version 1
 * @since 07.07.2023 (version 1)
 */
@Repository
public class EntryRepository {
    /** JdbcTemplate which is used in the code below but implementing the template at each usage would be unnecessary */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves all entries from a specific subcategory.
     *
     * @param categoryId       The ID of the category.
     * @param subcategoryId    The ID of the subcategory.
     * @param loggedInUser     The logged-in user.
     * @return                  A list of entries.
     */
    public List<Entry> getEntries(int categoryId, int subcategoryId, User loggedInUser) {
        return null;
    }

    /**
     * Retrieves a specific entry from a subcategory.
     *
     * @param categoryId       The ID of the category.
     * @param subcategoryId    The ID of the subcategory.
     * @param entryId          The ID of the entry.
     * @param loggedInUser     The logged-in user.
     * @return                  The entry object.
     */
    public Entry getEntry(int categoryId, int subcategoryId, int entryId, User loggedInUser) {
        return null;
    }

    /**
     * Adds a new entry to a specific subcategory.
     *
     * @param categoryId               The ID of the category.
     * @param subcategoryId            The ID of the subcategory.
     * @param entryName                The name of the entry.
     * @param entryDescription         The description of the entry.
     * @param entryAmount              The amount of the entry.
     * @param entryCreationTime        The creation time of the entry.
     * @param entryTimeOfExpense       The time of expense of the entry.
     * @param entryAmount1             The amount of the entry (duplicate).
     * @param entryLabelId             The ID of the label associated with the entry.
     * @param loggedInUser             The logged-in user.
     * @return                         The ID of the added entry.
     */
    public int addEntry(int categoryId, int subcategoryId, byte[] entryName, byte[] entryDescription, byte[] entryAmount, byte[] entryCreationTime, byte[] entryTimeOfExpense, byte[] entryAmount1, int entryLabelId, User loggedInUser) {
        return 0;
    }

    /**
     * Updates an existing entry in a specific subcategory.
     *
     * @param categoryId               The ID of the category.
     * @param subcategoryId            The ID of the subcategory.
     * @param updatedEntry             The updated entry object.
     * @param loggedInUser             The logged-in user.
     */
    public void updateEntry(int categoryId, int subcategoryId, Entry updatedEntry, User loggedInUser) {
    }

    /**
     * Updates the name of an existing entry in a specific subcategory.
     *
     * @param categoryId               The ID of the category.
     * @param subcategoryId            The ID of the subcategory.
     * @param updatedEntryName         The updated name of the entry.
     * @param loggedInUser             The logged-in user.
     */
    public void updateEntryName(int categoryId, int subcategoryId, byte[] updatedEntryName, User loggedInUser) {
    }

    /**
     * Updates the description of an existing entry in a specific subcategory.
     *
     * @param categoryId               The ID of the category.
     * @param subcategoryId            The ID of the subcategory.
     * @param updatedEntryDescription  The updated description of the entry.
     * @param loggedInUser             The logged-in user.
     */
    public void updateEntryDescription(int categoryId, int subcategoryId, byte[] updatedEntryDescription, User loggedInUser) {
    }

    /**
     * Updates the amount of an existing entry in a specific subcategory.
     *
     * @param categoryId               The ID of the category.
     * @param subcategoryId            The ID of the subcategory.
     * @param updatedEntryAmount       The updated amount of the entry.
     * @param loggedInUser             The logged-in user.
     */
    public void updateEntryAmount(int categoryId, int subcategoryId, byte[] updatedEntryAmount, User loggedInUser) {
    }

    /**
     * Updates the time of expense of an existing entry in a specific subcategory.
     *
     * @param categoryId               The ID of the category.
     * @param subcategoryId            The ID of the subcategory.
     * @param entryTimeOfExpense       The updated time of expense of the entry.
     * @param loggedInUser             The logged-in user.
     */
    public void updateEntryTimeOfExpense(int categoryId, int subcategoryId, byte[] entryTimeOfExpense, User loggedInUser) {
    }

    /**
     * Updates the attachment of an existing entry in a specific subcategory.
     *
     * @param categoryId               The ID of the category.
     * @param subcategoryId            The ID of the subcategory.
     * @param updatedEntryAttachment    The updated attachment of the entry.
     * @param loggedInUser             The logged-in user.
     */
    public void updateEntryAttachment(int categoryId, int subcategoryId, byte[] updatedEntryAttachment, User loggedInUser) {
    }

    /**
     * Updates the label of an existing entry in a specific subcategory.
     *
     * @param categoryId               The ID of the category.
     * @param subcategoryId            The ID of the subcategory.
     * @param updatedEntryLabelId       The updated label ID of the entry.
     * @param loggedInUser             The logged-in user.
     */
    public void updateEntryLabelId(int categoryId, int subcategoryId, int updatedEntryLabelId, User loggedInUser) {
    }

    /**
     * Deletes an entry from a specific subcategory.
     *
     * @param categoryId               The ID of the category.
     * @param subcategoryId            The ID of the subcategory.
     * @param entryId                  The ID of the entry to be deleted.
     * @param loggedInUser             The logged-in user.
     */
    public void deleteEntry(int categoryId, int subcategoryId, int entryId, User loggedInUser) {
    }
}
