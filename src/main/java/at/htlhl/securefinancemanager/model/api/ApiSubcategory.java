package at.htlhl.securefinancemanager.model.api;

/**
 * The {@code ApiSubcategory} class represents a subcategory entity in the 'subcategories' table of the 'secure_finance_manager' PostgreSQL database.
 * It is a POJO (Plain Old Java Object) or entity class that maps to the database table.
 *
 * <p>
 * This class is designed to be used as a data transfer object (DTO) for subcategory information. It includes attributes such as the subcategory name,
 * description, and the ID of the associated color.
 * </p>
 *
 * <p>
 * The class provides a parameterized constructor to create a new Subcategory object with the specified properties.
 * Additionally, getter methods are provided to retrieve the values of subcategory attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * ApiSubcategory subcategory = new ApiSubcategory("Groceries", "Monthly grocery expenses", 1);
 * }</pre>
 * </p>
 *
 * <p>
 * This class can be used in conjunction with other entities in the secure finance manager system, such as {@link ApiCategory}, {@link ApiLabel}, and {@link ApiUser}.
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work in conjunction with other entities such as {@link ApiCategory}, {@link ApiLabel}, and {@link ApiUser}.
 * </p>
 *
 * <p>
 * This class can be used as a data transfer object (DTO) to facilitate communication between the frontend and backend.
 * </p>
 *
 * @author Fischer
 * @version 1.7
 * @since 10.11.2023 (version 1.7)
 */
public class ApiSubcategory {
    /** The name of the subcategory. */
    private String subcategoryName;

    /** The description of the subcategory. */
    private String subcategoryDescription;

    /** The ID of the associated colour. */
    private int subcategoryColourId;

    /**
     * Constructs a new Subcategory object with the specified properties.
     *
     * @param subcategoryName               The name of the subcategory.
     * @param subcategoryDescription        The description of the subcategory.
     * @param subcategoryColourId           The ID of the associated colour.
     */
    public ApiSubcategory(String subcategoryName, String subcategoryDescription, int subcategoryColourId) {
        this.subcategoryName = subcategoryName;
        this.subcategoryDescription = subcategoryDescription;
        this.subcategoryColourId = subcategoryColourId;
    }

    /**
     * Returns the name of the subcategory.
     *
     * @return The name of the subcategory.
     */
    public String getSubcategoryName() {
        return subcategoryName;
    }

    /**
     * Returns the description of the subcategory.
     *
     * @return The description of the subcategory.
     */
    public String getSubcategoryDescription() {
        return subcategoryDescription;
    }

    /**
     * Returns the ID of the associated colour.
     *
     * @return The ID of the associated colour.
     */
    public int getSubcategoryColourId() {
        return subcategoryColourId;
    }
}