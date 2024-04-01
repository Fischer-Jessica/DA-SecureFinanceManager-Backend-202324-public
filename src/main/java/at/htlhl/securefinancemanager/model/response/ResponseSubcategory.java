package at.htlhl.securefinancemanager.model.response;

import at.htlhl.securefinancemanager.model.database.DatabaseSubcategory;

/**
 * The {@code ResponseSubcategory} class represents a subcategory response entity used in communication with mobile applications.
 * It extends the {@link DatabaseSubcategory} class, inheriting properties and methods related to subcategory information.
 *
 * <p>
 * This class includes an additional attribute, {@code mobileSubcategoryId}, which is specific to the mobile application representation.
 * </p>
 *
 * <p>
 * The class provides a constructor to create a new ResponseSubcategory object with the specified properties.
 * Additionally, getter methods are provided to retrieve the values of subcategory attributes.
 * </p>
 *
 * <p>
 * Example Usage:
 * <pre>{@code
 * ResponseSubcategory responseSubcategory = new ResponseSubcategory(databaseSubcategory, 654);
 * }</pre>
 * </p>
 *
 * <p>
 * This class is part of the secure finance manager system and is designed to work in conjunction with other entities such as {@link DatabaseSubcategory}.
 * </p>
 *
 * @author Fischer
 * @fullName Fischer, Jessica Christina
 * @version 1.2
 * @since 01.04.2024 (version 1.2)
 * @see DatabaseSubcategory
 */
public class ResponseSubcategory extends DatabaseSubcategory {
    /**
     * The unique identifier for the mobile subcategory.
     */
    private int mobileSubcategoryId;

    /**
     * Constructs a new ResponseSubcategory object with the specified properties.
     *
     * @param databaseSubcategory The DatabaseSubcategory object.
     * @param mobileSubcategoryId The unique identifier for the mobile subcategory.
     */
    public ResponseSubcategory(DatabaseSubcategory databaseSubcategory, int mobileSubcategoryId) {
        super(databaseSubcategory.getSubcategoryId(), databaseSubcategory.getSubcategoryCategoryId(), databaseSubcategory.getSubcategoryName(), databaseSubcategory.getSubcategoryDescription(), databaseSubcategory.getSubcategoryColourId(), databaseSubcategory.getSubcategoryUserId());
        this.mobileSubcategoryId = mobileSubcategoryId;
    }

    /**
     * Returns the unique identifier for the mobile subcategory.
     *
     * @return The unique identifier for the mobile subcategory.
     */
    public int getMobileSubcategoryId() {
        return mobileSubcategoryId;
    }
}