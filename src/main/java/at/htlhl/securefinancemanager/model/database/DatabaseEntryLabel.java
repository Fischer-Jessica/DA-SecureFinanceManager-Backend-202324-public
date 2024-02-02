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
 * @author Fischer
 * @version 1.5
 * @since 17.11.2023 (version 1.5)
 */

import jakarta.validation.constraints.NotBlank;

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
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 1.7
 * @since 02.02.2024 (version 1.7)
 */
public class DatabaseEntryLabel {
    /**
     * The unique identifier for this EntryLabel.
     * It is generated automatically by the database.
     */
    @NotBlank
    private int entryLabelId;

    /**
     * The unique identifier for the entry.
     * It represents the association between an entry and a label.
     */
    @NotBlank
    private int entryId;

    /**
     * The unique identifier for the label.
     * It represents the association between an entry and a label.
     */
    @NotBlank
    private int labelId;

    /**
     * The unique identifier for the user.
     * It represents the association between an EntryLabel and a user.
     */
    @NotBlank
    private int userId;

    /**
     * Constructs a new DatabaseEntryLabel object with the specified entry label ID, entry ID, label ID, and user ID.
     *
     * @param entryLabelId The unique identifier for this EntryLabel.
     * @param entryId      The ID of the associated entry.
     * @param labelId      The ID of the associated label.
     * @param userId       The ID of the associated user.
     */
    public DatabaseEntryLabel(int entryLabelId, int entryId, int labelId, int userId) {
        this.entryLabelId = entryLabelId;
        this.entryId = entryId;
        this.labelId = labelId;
        this.userId = userId;
    }

    /**
     * Returns the ID of the entryLabel.
     *
     * @return The ID of the entryLabel.
     */
    public int getEntryLabelId() {
        return entryLabelId;
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
     * Returns the ID of the label.
     *
     * @return The ID of the label.
     */
    public int getLabelId() {
        return labelId;
    }

    /**
     * Returns the ID of the user.
     *
     * @return The ID of the user.
     */
    public int getUserId() {
        return userId;
    }
}