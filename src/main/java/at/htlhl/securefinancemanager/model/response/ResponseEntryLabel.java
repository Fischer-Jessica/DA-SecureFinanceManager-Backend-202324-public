package at.htlhl.securefinancemanager.model.response;

import at.htlhl.securefinancemanager.model.database.DatabaseEntryLabel;

/**
 * The {@code ResponseEntryLabel} class represents an entry label response entity used in communication with mobile applications.
 * It extends the {@link DatabaseEntryLabel} class, inheriting properties and methods related to entry label information.
 *
 * <p>
 * This class includes an additional attribute, {@code mobileEntryLabelId}, which is specific to the mobile application representation.
 * </p>
 *
 * <p>
 * The class provides a constructor to create a new ResponseEntryLabel object with the specified properties.
 * Additionally, getter methods are provided to retrieve the values of entry label attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * ResponseEntryLabel responseEntryLabel = new ResponseEntryLabel(databaseEntryLabel, 789);
 * }</pre>
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work in conjunction with other entities such as {@link DatabaseEntryLabel}.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 1.2
 * @since 01.04.2024 (version 1.2)
 * @see DatabaseEntryLabel
 */
public class ResponseEntryLabel extends DatabaseEntryLabel {
    /**
     * The unique identifier for the mobile entry label.
     */
    private int mobileEntryLabelId;

    /**
     * Constructs a new ResponseEntryLabel object with the specified properties.
     *
     * @param databaseEntryLabel The DatabaseEntryLabel object.
     * @param mobileEntryLabelId The unique identifier for the mobile entry label.
     */
    public ResponseEntryLabel(DatabaseEntryLabel databaseEntryLabel, int mobileEntryLabelId) {
        super(databaseEntryLabel.getEntryLabelId(), databaseEntryLabel.getEntryId(), databaseEntryLabel.getLabelId(), databaseEntryLabel.getUserId());
        this.mobileEntryLabelId = mobileEntryLabelId;
    }

    /**
     * Returns the unique identifier for the mobile entry-label.
     *
     * @return The unique identifier for the mobile entry-label.
     */
    public int getMobileEntryLabelId() {
        return mobileEntryLabelId;
    }
}