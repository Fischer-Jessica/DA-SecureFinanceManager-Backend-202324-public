package at.htlhl.securefinancemanager.model.response;

import at.htlhl.securefinancemanager.model.database.DatabaseEntry;

/**
 * The {@code ResponseEntry} class represents an entry response entity used in communication with mobile applications.
 * It extends the {@link DatabaseEntry} class, inheriting properties and methods related to entry information.
 *
 * <p>
 * This class includes an additional attribute, {@code mobileEntryId}, which is specific to the mobile application representation.
 * </p>
 *
 * <p>
 * The class provides a constructor to create a new ResponseEntry object with the specified properties.
 * Additionally, getter methods are provided to retrieve the values of entry attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * ResponseEntry responseEntry = new ResponseEntry(databaseEntry, 456);
 * }</pre>
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work in conjunction with other entities such as {@link DatabaseEntry}.
 * </p>
 *
 * @author Fischer
 * @version 1.0
 * @since 17.11.2023 (version 1.0)
 *
 * @see DatabaseEntry
 */
public class ResponseEntry extends DatabaseEntry {
    /** The unique identifier for the mobile entry. */
    private int mobileEntryId;

    /**
     * Constructs a new ResponseEntry object with the specified properties.
     *
     * @param databaseEntry       The DatabaseEntry object.
     * @param mobileEntryId       The unique identifier for the mobile entry.
     */
    public ResponseEntry(DatabaseEntry databaseEntry, int mobileEntryId) {
        super(databaseEntry.getEntryId(), databaseEntry.getEntrySubcategoryId(), databaseEntry.getEntryName(), databaseEntry.getEntryDescription(), databaseEntry.getEntryAmount(), databaseEntry.getEntryTimeOfTransaction(), databaseEntry.getEntryAttachment(), databaseEntry.getEntryCreationTime(), databaseEntry.getEntryUserId());
        this.mobileEntryId = mobileEntryId;
    }
}