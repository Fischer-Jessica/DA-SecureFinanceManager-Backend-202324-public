package at.htlhl.securefinancemanager.model.response;

import at.htlhl.securefinancemanager.model.database.DatabaseCategory;

/**
 * The {@code ResponseCategory} class represents a category response entity used in communication with mobile applications.
 * It extends the {@link DatabaseCategory} class, inheriting properties and methods related to category information.
 *
 * <p>
 * This class includes an additional attribute, {@code mobileCategoryId}, which is specific to the mobile application representation.
 * </p>
 *
 * <p>
 * The class provides a constructor to create a new ResponseCategory object with the specified properties.
 * Additionally, getter methods are provided to retrieve the values of category attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * ResponseCategory responseCategory = new ResponseCategory(databaseCategory, 123);
 * }</pre>
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work in conjunction with other entities such as {@link DatabaseCategory}.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 1.1
 * @since 02.02.2024 (version 1.1)
 * @see DatabaseCategory
 */
public class ResponseCategory extends DatabaseCategory {
    /**
     * The unique identifier for the mobile category.
     */
    private int mobileCategoryId;

    /**
     * Constructs a new ResponseCategory object with the specified properties.
     *
     * @param databaseCategory The DatabaseCategory object.
     * @param mobileCategoryId The unique identifier for the mobile category.
     */
    public ResponseCategory(DatabaseCategory databaseCategory, int mobileCategoryId) {
        super(databaseCategory.getCategoryId(), databaseCategory.getCategoryName(), databaseCategory.getCategoryDescription(), databaseCategory.getCategoryColourId(), databaseCategory.getCategoryUserId());
        this.mobileCategoryId = mobileCategoryId;
    }
}