package at.htlhl.securefinancemanager.model.database;

import at.htlhl.securefinancemanager.model.api.ApiCategory;
import jakarta.validation.constraints.NotBlank;

/**
 * The {@code DatabaseCategory} class represents a category entity in the 'categories' table of the 'secure_finance_manager' PostgreSQL database.
 * It extends the {@link ApiCategory} class and includes additional attributes such as category ID and user ID.
 *
 * <p>
 * This class provides constructors to create instances with specified details and getter methods to retrieve the values of category attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * ApiCategory apiCategory = new ApiCategory("Groceries", "Expenses for groceries", 1);
 * DatabaseCategory databaseCategory = new DatabaseCategory(apiCategory, 123);
 * }</pre>
 * </p>
 *
 * <p>
 * The class includes the following constructors:
 * <ul>
 *     <li>A parameterized constructor with category ID, category name, category description, category color ID, and user ID.</li>
 *     <li>A constructor that takes an {@link ApiCategory} instance and a user ID.</li>
 *     <li>A constructor with category ID, an {@link ApiCategory} instance, and a user ID.</li>
 * </ul>
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work with database interactions related to categories.
 * </p>
 *
 * <p>
 * Note: It is assumed that the category color ID is represented by an integer value.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 2.1
 * @since 02.02.2024 (version 2.1)
 * @see ApiCategory
 */
public class DatabaseCategory extends ApiCategory {
    /**
     * The ID of the category.
     */
    @NotBlank
    private int categoryId;

    /**
     * The ID of the user associated with the category.
     */
    @NotBlank
    private int categoryUserId;

    /**
     * Constructs a new DatabaseCategory object with the specified properties.
     *
     * @param categoryId          The ID of the category.
     * @param categoryName        The name of the category.
     * @param categoryDescription The description of the category.
     * @param categoryColourId    The ID of the associated color.
     * @param categoryUserId      The ID of the user associated with the category.
     */
    public DatabaseCategory(int categoryId, String categoryName, String categoryDescription, int categoryColourId, int categoryUserId) {
        super(categoryName, categoryDescription, categoryColourId);
        this.categoryId = categoryId;
        this.categoryUserId = categoryUserId;
    }

    /**
     * Constructs a new DatabaseCategory object based on an {@link ApiCategory} instance and a user ID.
     *
     * @param apiCategory    The ApiCategory instance.
     * @param categoryUserId The ID of the user associated with the category.
     */
    public DatabaseCategory(ApiCategory apiCategory, int categoryUserId) {
        super(apiCategory.getCategoryName(), apiCategory.getCategoryDescription(), apiCategory.getCategoryColourId());
        this.categoryUserId = categoryUserId;
    }

    /**
     * Constructs a new DatabaseCategory object with the specified category ID, an {@link ApiCategory} instance, and a user ID.
     *
     * @param categoryId     The ID of the category.
     * @param apiCategory    The ApiCategory instance.
     * @param categoryUserId The ID of the user associated with the category.
     */
    public DatabaseCategory(int categoryId, ApiCategory apiCategory, int categoryUserId) {
        super(apiCategory.getCategoryName(), apiCategory.getCategoryDescription(), apiCategory.getCategoryColourId());
        this.categoryId = categoryId;
        this.categoryUserId = categoryUserId;
    }

    /**
     * Retrieves the ID of the category.
     *
     * @return The ID of the category.
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Retrieves the ID of the user associated with the category.
     *
     * @return The ID of the user associated with the category.
     */
    public int getCategoryUserId() {
        return categoryUserId;
    }
}