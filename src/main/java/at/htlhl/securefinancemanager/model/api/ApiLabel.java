package at.htlhl.securefinancemanager.model.api;

/**
 * The {@code ApiLabel} class represents a label entity in the 'labels' table of the 'secure_finance_manager' PostgreSQL database.
 * It is a POJO (Plain Old Java Object) or entity class that maps to the database table.
 *
 * <p>
 * This class includes attributes such as the label name, description, and the ID of the associated color.
 * It is used as a data transfer object (DTO) to facilitate communication between the frontend and backend.
 * </p>
 *
 * <p>
 * The {@code ApiLabel} class is designed to be instantiated with a parameterized constructor that sets the label name,
 * description, and color ID. It also provides getter methods to retrieve these attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * ApiLabel label = new ApiLabel("Work", "Work-related expenses", 1);
 * }</pre>
 * </p>
 *
 * @author Fischer
 * @version 2.0
 * @since 14.11.2023 (version 2.0)
 */
public class ApiLabel {

    /** The name of the label. */
    private String labelName;

    /** The description of the label. */
    private String labelDescription;

    /** The ID of the associated colour. */
    private int labelColourId;

    /**
     * Constructs a new Label object with the specified properties.
     *
     * @param labelName             The name of the label.
     * @param labelDescription      The description of the label.
     * @param labelColourId         The ID of the associated colour.
     */
    public ApiLabel(String labelName, String labelDescription, int labelColourId) {
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelColourId = labelColourId;
    }

    /**
     * Returns the name of the label.
     *
     * @return The name of the label.
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * Returns the description of the label.
     *
     * @return The description of the label.
     */
    public String getLabelDescription() {
        return labelDescription;
    }

    /**
     * Returns the ID of the associated colour.
     *
     * @return The ID of the associated colour.
     */
    public int getLabelColourId() {
        return labelColourId;
    }
}