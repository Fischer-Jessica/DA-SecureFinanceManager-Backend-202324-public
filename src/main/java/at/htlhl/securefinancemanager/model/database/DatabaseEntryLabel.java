package at.htlhl.securefinancemanager.model.database;

/**
 * The {@code DatabaseEntryLabel} class represents the association between an entry and a label in the 'entry_labels' table of the 'secure_finance_manager' PostgreSQL database.
 *
 * <p>
 * This class includes attributes such as the unique identifier for the entry label, entry ID, label ID, and user ID.
 * </p>
 *
 * <p>
 * The {@code DatabaseEntryLabel} class is designed to be instantiated with a parameterized constructor that sets the entry label ID, entry ID, label ID, and user ID.
 * </p>
 *
 * <p>
 * Author: Fischer
 * Version: 1.4
 * Since: 10.11.2023 (version 1.4)
 * </p>
 */
/**
 * The {@code DatabaseEntryLabel} class represents the association between an entry and a label in the 'entry_labels' table of the 'financial_overview' PostgreSQL database.
 *
 * <p>
 * This class includes attributes such as the unique identifier for the entry label, entry ID, label ID, and user ID.
 * </p>
 *
 * <p>
 * The {@code DatabaseEntryLabel} class is designed to be instantiated with a parameterized constructor that sets the entry label ID, entry ID, label ID, and user ID.
 * </p>
 *
 * <p>
 * Author: Fischer
 * Version: 1.4
 * Since: 10.11.2023 (version 1.4)
 * </p>
 */
public class DatabaseEntryLabel {
    /**
     * The unique identifier for this EntryLabel.
     * It is generated automatically by the database.
     */
    private int entryLabelId;
    /**
     * The unique identifier for the entry.
     * It represents the association between an entry and a label.
     */
    private int entryId;
    /**
     * The unique identifier for the label.
     * It represents the association between an entry and a label.
     */
    private int labelId;
    /**
     * The unique identifier for the user.
     * It represents the association between an EntryLabel and a user.
     */
    private int userId;

    /**
     * Constructs a new DatabaseEntryLabel object with the specified entry label ID, entry ID, label ID, and user ID.
     *
     * @param entryLabelId  The unique identifier for this EntryLabel.
     * @param entryId       The ID of the associated entry.
     * @param labelId       The ID of the associated label.
     * @param userId        The ID of the associated user.
     */
    public DatabaseEntryLabel(int entryLabelId, int entryId, int labelId, int userId) {
        this.entryLabelId = entryLabelId;
        this.entryId = entryId;
        this.labelId = labelId;
        this.userId = userId;
    }
}