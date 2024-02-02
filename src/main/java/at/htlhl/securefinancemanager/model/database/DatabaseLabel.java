package at.htlhl.securefinancemanager.model.database;

import at.htlhl.securefinancemanager.model.api.ApiLabel;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@code DatabaseLabel} class represents a label entity in the 'labels' table of the 'secure_finance_manager' PostgreSQL database.
 * It extends the {@link ApiLabel} class, inheriting properties and methods related to label information.
 *
 * <p>
 * This class includes additional attributes such as label ID and label user ID, which are specific to the database representation.
 * </p>
 *
 * <p>
 * The class provides multiple constructors to create a new DatabaseLabel object with the specified properties.
 * Additionally, getter methods are provided to retrieve the values of label attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * DatabaseLabel label = new DatabaseLabel("Groceries", "Monthly grocery expenses", 1, 123);
 * }</pre>
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work in conjunction with other entities such as {@link ApiLabel} and {@link ApiUser}.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 2.1
 * @since 02.02.2024 (version 2.1)
 * @see ApiLabel
 */
public class DatabaseLabel extends ApiLabel {
    /**
     * The unique identifier for this Label. It is generated automatically by the database.
     */
    @NotBlank
    private int labelId;

    /**
     * The ID of the user associated with the Label.
     */
    @NotBlank
    private int labelUserId;

    /**
     * Constructs a new DatabaseLabel object with the specified properties.
     *
     * @param labelId          The unique identifier for the label.
     * @param labelName        The name of the label.
     * @param labelDescription The description of the label.
     * @param labelColourId    The ID of the associated colour.
     * @param labelUserId      The ID of the associated user.
     */
    public DatabaseLabel(int labelId, String labelName, String labelDescription, int labelColourId, int labelUserId) {
        super(labelName, labelDescription, labelColourId);
        this.labelId = labelId;
        this.labelUserId = labelUserId;
    }

    /**
     * Constructs a new DatabaseLabel object from an ApiLabel object and user ID.
     *
     * @param apiLabel    The ApiLabel object.
     * @param labelUserId The ID of the associated user.
     */
    public DatabaseLabel(ApiLabel apiLabel, int labelUserId) {
        super(apiLabel.getLabelName(), apiLabel.getLabelDescription(), apiLabel.getLabelColourId());
        this.labelUserId = labelUserId;
    }

    /**
     * Constructs a new DatabaseLabel object from an ApiLabel object, label ID, and user ID.
     *
     * @param labelId     The unique identifier for the label.
     * @param apiLabel    The ApiLabel object.
     * @param labelUserId The ID of the associated user.
     */
    public DatabaseLabel(int labelId, ApiLabel apiLabel, int labelUserId) {
        super(apiLabel.getLabelName(), apiLabel.getLabelDescription(), apiLabel.getLabelColourId());
        this.labelId = labelId;
        this.labelUserId = labelUserId;
    }

    /**
     * Returns the unique identifier for the label.
     *
     * @return The unique identifier for the label.
     */
    public int getLabelId() {
        return labelId;
    }

    /**
     * Returns the ID of the user associated with the label.
     *
     * @return The ID of the user associated with the label.
     */
    public int getLabelUserId() {
        return labelUserId;
    }
}