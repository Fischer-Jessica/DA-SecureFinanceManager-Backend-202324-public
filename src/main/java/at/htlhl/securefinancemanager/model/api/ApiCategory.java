package at.htlhl.securefinancemanager.model.api;

/**
 * The `ApiCategory` class represents the data structure for category information in the secure finance manager system.
 *
 * <p>
 * This class includes attributes such as the category name, description, and the ID of the associated color.
 * It is used as a data transfer object (DTO) to facilitate communication between the frontend and backend.
 * </p>
 *
 * <p>
 * The `ApiCategory` class is designed to be instantiated with a parameterized constructor that sets the category name,
 * description, and color ID. It also provides getter methods to retrieve these attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * ApiCategory category = new ApiCategory("Groceries", "Monthly grocery expenses", 1);
 * }</pre>
 * </p>
 *
 * @author Fischer
 * @version 2.0
 * @since 14.11.2023 (version 2.0)
 */
public class ApiCategory {
    /** The name of the category. */
    private String categoryName;

    /** The description of the category. */
    private String categoryDescription;

    /** The ID of the associated color. */
    private int categoryColourId;

    /**
     * Parameterized constructor.
     *
     * @param categoryName          The name of the category.
     * @param categoryDescription   The description of the category.
     * @param categoryColourId      The ID of the associated color.
     */
    public ApiCategory(String categoryName, String categoryDescription, int categoryColourId) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryColourId = categoryColourId;
    }

    /**
     * Returns the name of this category.
     *
     * @return The category name.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Returns the description of this category.
     *
     * @return The category description.
     */
    public String getCategoryDescription() {
        return categoryDescription;
    }

    /**
     * Returns the ID of the associated color.
     *
     * @return The category color ID.
     */
    public int getCategoryColourId() {
        return categoryColourId;
    }
}