package at.htlhl.securefinancemanager.model.response;

import at.htlhl.securefinancemanager.model.database.DatabaseLabel;

/**
 * The {@code ResponseLabel} class represents a label response entity used in communication with mobile applications.
 * It extends the {@link DatabaseLabel} class, inheriting properties and methods related to label information.
 *
 * <p>
 * This class includes an additional attribute, {@code mobileLabelId}, which is specific to the mobile application representation.
 * </p>
 *
 * <p>
 * The class provides a constructor to create a new ResponseLabel object with the specified properties.
 * Additionally, getter methods are provided to retrieve the values of label attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * ResponseLabel responseLabel = new ResponseLabel(databaseLabel, 987);
 * }</pre>
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work in conjunction with other entities such as {@link DatabaseLabel}.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 1.2
 * @since 01.04.2024 (version 1.2)
 * @see DatabaseLabel
 */
public class ResponseLabel extends DatabaseLabel {
    /**
     * The unique identifier for the mobile label.
     */
    private int mobileLabelId;

    /**
     * Constructs a new ResponseLabel object with the specified properties.
     *
     * @param databaseLabel The DatabaseLabel object.
     * @param mobileLabelId The unique identifier for the mobile label.
     */
    public ResponseLabel(DatabaseLabel databaseLabel, int mobileLabelId) {
        super(databaseLabel.getLabelId(), databaseLabel.getLabelName(), databaseLabel.getLabelDescription(), databaseLabel.getLabelColourId(), databaseLabel.getLabelUserId());
        this.mobileLabelId = mobileLabelId;
    }

    /**
     * Returns the unique identifier for the mobile label.
     *
     * @return The unique identifier for the mobile label.
     */
    public int getMobileLabelId() {
        return mobileLabelId;
    }
}