package at.htlhl.securefinancemanager.model.database;

import at.htlhl.securefinancemanager.model.api.ApiSubcategory;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@code DatabaseSubcategory} class represents a subcategory entity in the 'subcategories' table of the 'secure_finance_manager' PostgreSQL database.
 * It extends the {@link ApiSubcategory} class, inheriting properties and methods related to subcategory information.
 *
 * <p>
 * This class includes additional attributes such as subcategory ID, category ID, and subcategory user ID, which are specific to the database representation.
 * </p>
 *
 * <p>
 * The class provides multiple constructors to create a new DatabaseSubcategory object with the specified properties.
 * Additionally, getter methods are provided to retrieve the values of subcategory attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * DatabaseSubcategory subcategory = new DatabaseSubcategory("Groceries", "Monthly grocery expenses", 1, 123, 456);
 * }</pre>
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work in conjunction with other entities such as {@link ApiSubcategory} and {@link DatabaseCategory}.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 1.9
 * @since 02.02.2024 (version 1.9)
 * @see ApiSubcategory
 */
public class DatabaseSubcategory extends ApiSubcategory {
    /**
     * The unique identifier for this Subcategory. It is generated automatically by the database.
     */
    @NotBlank
    private int subcategoryId;

    /**
     * The ID of the category associated with the Subcategory.
     */
    @NotBlank
    private int subcategoryCategoryId;

    /**
     * The ID of the user associated with the Subcategory.
     */
    @NotBlank
    private int subcategoryUserId;

    /**
     * Constructs a new DatabaseSubcategory object with the specified properties.
     *
     * @param subcategoryId          The unique identifier for the subcategory.
     * @param categoryId             The ID of the category associated with the subcategory.
     * @param subcategoryName        The name of the subcategory.
     * @param subcategoryDescription The description of the subcategory.
     * @param subcategoryColourId    The ID of the associated colour.
     * @param subcategoryUserId      The ID of the associated user.
     */
    public DatabaseSubcategory(int subcategoryId, int categoryId, String subcategoryName, String subcategoryDescription, int subcategoryColourId, int subcategoryUserId) {
        super(subcategoryName, subcategoryDescription, subcategoryColourId);
        this.subcategoryId = subcategoryId;
        this.subcategoryCategoryId = categoryId;
        this.subcategoryUserId = subcategoryUserId;
    }

    /**
     * Constructs a new DatabaseSubcategory object from an ApiSubcategory object and user ID.
     *
     * @param categoryId        The ID of the category associated with the subcategory.
     * @param apiSubcategory    The ApiSubcategory object.
     * @param subcategoryUserId The ID of the associated user.
     */
    public DatabaseSubcategory(int categoryId, ApiSubcategory apiSubcategory, int subcategoryUserId) {
        super(apiSubcategory.getSubcategoryName(), apiSubcategory.getSubcategoryDescription(), apiSubcategory.getSubcategoryColourId());
        this.subcategoryCategoryId = categoryId;
        this.subcategoryUserId = subcategoryUserId;
    }

    /**
     * Constructs a new DatabaseSubcategory object from an ApiSubcategory object, subcategory ID, category ID, and user ID.
     *
     * @param subcategoryId     The unique identifier for the subcategory.
     * @param categoryId        The ID of the category associated with the subcategory.
     * @param apiSubcategory    The ApiSubcategory object.
     * @param subcategoryUserId The ID of the associated user.
     */
    public DatabaseSubcategory(int subcategoryId, int categoryId, ApiSubcategory apiSubcategory, int subcategoryUserId) {
        super(apiSubcategory.getSubcategoryName(), apiSubcategory.getSubcategoryDescription(), apiSubcategory.getSubcategoryColourId());
        this.subcategoryId = subcategoryId;
        this.subcategoryCategoryId = categoryId;
        this.subcategoryUserId = subcategoryUserId;
    }

    /**
     * Returns the unique identifier for the subcategory.
     *
     * @return The unique identifier for the subcategory.
     */
    public int getSubcategoryId() {
        return subcategoryId;
    }

    /**
     * Returns the ID of the category associated with the subcategory.
     *
     * @return The ID of the category associated with the subcategory.
     */
    public int getSubcategoryCategoryId() {
        return subcategoryCategoryId;
    }

    /**
     * Returns the ID of the user associated with the subcategory.
     *
     * @return The ID of the user associated with the subcategory.
     */
    public int getSubcategoryUserId() {
        return subcategoryUserId;
    }
}