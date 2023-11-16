package at.htlhl.securefinancemanager.model.database;

import at.htlhl.securefinancemanager.model.api.ApiEntry;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@code DatabaseEntry} class represents an entry entity in the 'entries' table of the 'secure_finance_manager' PostgreSQL database.
 *
 * <p>
 * This class extends the {@link ApiEntry} class, inheriting attributes such as entry name, description, amount, time of transaction, and attachment.
 * It also includes additional attributes like entry ID, creation time, subcategory ID, and user ID.
 * </p>
 *
 * <p>
 * The class provides multiple constructors to create a new DatabaseEntry object with different combinations of parameters.
 * Additionally, getter methods are provided to retrieve the values of entry attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * DatabaseEntry entry = new DatabaseEntry(1, 1, "Grocery Shopping", "Monthly grocery expenses", "-$200", "2023-11-10T15:30:00", "receipt.jpg", "2023-11-10T15:30:00", 1);
 * }</pre>
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work with database interactions related to entries.
 * </p>
 *
 * @author Fischer
 * @version 1.9
 * @since 16.11.2023 (version 1.9)
 *
 * @see ApiEntry
 */
public class DatabaseEntry extends ApiEntry {
    /**
     * The unique identifier for this Entry.
     * It is generated automatically by the database.
     */
    @NotBlank
    private int entryId;

    /**
     * The timestamp representing the creation time of the entry.
     * It indicates when the entry was originally created.
     * This value is typically set at the time of entry creation and remains unchanged.
     */
    @NotBlank
    private String entryCreationTime;

    /** The ID of the subcategory associated with the Entry. */
    @NotBlank
    private int entrySubcategoryId;

    /** The ID of the user associated with the Entry. */
    @NotBlank
    private int entryUserId;

    /**
     * Constructs a new DatabaseEntry object with the specified ID, subcategory ID, entry name, entry description, entry amount, time of transaction, attachment, creation time, and user ID.
     *
     * @param entryId               The ID of the entry.
     * @param entrySubcategoryId    The ID of the associated subcategory.
     * @param entryName             The name of the entry.
     * @param entryDescription      The description of the entry.
     * @param entryAmount           The amount of the entry.
     * @param entryTimeOfTransaction The time of the transaction in the entry.
     * @param entryAttachment       The attachment of the entry.
     * @param entryCreationTime     The creation time of the entry.
     * @param entryUserId           The ID of the associated user.
     */
    public DatabaseEntry(int entryId, int entrySubcategoryId, String entryName, String entryDescription, String entryAmount, String entryTimeOfTransaction, String entryAttachment, String entryCreationTime, int entryUserId) {
        super(entryName, entryDescription, entryAmount, entryTimeOfTransaction, entryAttachment);
        this.entryId = entryId;
        this.entryCreationTime = entryCreationTime;
        this.entrySubcategoryId = entrySubcategoryId;
        this.entryUserId = entryUserId;
    }

    /**
     * Constructs a new DatabaseEntry object with the specified subcategory ID, ApiEntry object, and user ID.
     *
     * @param entrySubcategoryId    The ID of the associated subcategory.
     * @param apiEntry              The ApiEntry object representing the entry.
     * @param entryUserId           The ID of the associated user.
     */
    public DatabaseEntry(int entrySubcategoryId, ApiEntry apiEntry, int entryUserId) {
        super(apiEntry.getEntryName(), apiEntry.getEntryDescription(), apiEntry.getEntryAmount(), apiEntry.getEntryTimeOfTransaction(), apiEntry.getEntryAttachment());
        this.entrySubcategoryId = entrySubcategoryId;
        this.entryUserId = entryUserId;
    }

    /**
     * Constructs a new DatabaseEntry object with the specified ID, subcategory ID, ApiEntry object, and user ID.
     *
     * @param entryId               The ID of the entry.
     * @param entrySubcategoryId    The ID of the associated subcategory.
     * @param apiEntry              The ApiEntry object representing the entry.
     * @param entryUserId           The ID of the associated user.
     */
    public DatabaseEntry(int entryId, int entrySubcategoryId, ApiEntry apiEntry, int entryUserId) {
        super(apiEntry.getEntryName(), apiEntry.getEntryDescription(), apiEntry.getEntryAmount(), apiEntry.getEntryTimeOfTransaction(), apiEntry.getEntryAttachment());
        this.entryId = entryId;
        this.entrySubcategoryId = entrySubcategoryId;
        this.entryUserId = entryUserId;
    }

    /**
     * Returns the ID of the entry.
     *
     * @return The ID of the entry.
     */
    public int getEntryId() {
        return entryId;
    }

    /**
     * Returns the creation time of the entry.
     *
     * @return The creation time of the entry.
     */
    public String getEntryCreationTime() {
        return entryCreationTime;
    }

    /**
     * Returns the ID of the associated subcategory.
     *
     * @return The ID of the associated subcategory.
     */
    public int getEntrySubcategoryId() {
        return entrySubcategoryId;
    }

    /**
     * Returns the ID of the associated user.
     *
     * @return The ID of the associated user.
     */
    public int getEntryUserId() {
        return entryUserId;
    }
}